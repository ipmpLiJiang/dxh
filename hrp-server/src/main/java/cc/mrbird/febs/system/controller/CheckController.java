package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.Checkinfo;
import cc.mrbird.febs.system.domain.CheckinfoExcel;
import cc.mrbird.febs.system.domain.Code;
import cc.mrbird.febs.system.domain.Dict;
import cc.mrbird.febs.system.service.DictService;
import cc.mrbird.febs.system.service.ICheckService;
import cc.mrbird.febs.system.service.ICodeService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.handler.ExcelReadHandler;
import com.wuwenze.poi.pojo.ExcelErrorField;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("/system/check")
public class CheckController extends BaseController {

    private String message;

    @Autowired
    ICheckService iCheckService;

    @Autowired
    DictService dictService;

    @Autowired
    ICodeService iCodeService;

    @GetMapping("/getCheckInfoReport")
    public Map<String, Object> getCheckinfoReport(QueryRequest request, Checkinfo check) throws ParseException {
        Page<Checkinfo> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formaterYYYY = new SimpleDateFormat("yyyy");
        String start = formaterYYYY.format(check.getStartdate());
        String end = formaterYYYY.format(check.getEnddate());
        check.setStartdate(formater.parse(start + "-01-01"));
        check.setEnddate(formater.parse(end + "-01-01"));
        return getDataTable(this.iCheckService.findCheckinfoReport(page, check));
    }

    @Log("新增年度考核")
    @PostMapping
    public Map<String, Object> addCheck(@Valid Checkinfo check) {
        this.iCheckService.createCheck(check);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS, null);
    }

    @Log("修改年度考核")
    @PutMapping
    public Map<String, Object> updateCheck(@Valid Checkinfo check) {
        this.iCheckService.updateCheck(check);
        return getResult(HttpStatus.OK, FebsConstant.UPDATE_SUCCESS, null);
    }

    @Log("删除年度考核")
    @DeleteMapping("/delete/{checkIds}")
    public Map<String, Object> deleteChecks(@NotBlank(message = "{required}") @PathVariable String checkIds) throws FebsException {
        String[] ids = checkIds.split(StringPool.COMMA);
        this.iCheckService.deleteChecks(ids);
        return getResult(HttpStatus.OK, FebsConstant.DELETE_SUCCESS, null);
    }

    @PostMapping("import")
    public FebsResponse importExcels(@RequestParam("file") MultipartFile file, String gparname) throws FebsException {
        try {
            if (file.isEmpty()) {
                throw new FebsException(HttpStatus.BAD_REQUEST, "导入数据为空");
            }
            String filename = file.getOriginalFilename();
            if (!StringUtils.endsWith(filename, ".xlsx")) {
                throw new FebsException(HttpStatus.BAD_REQUEST, "只支持.xlsx类型文件导入");
            }
            // 开始导入操作
            long beginTimeMillis = System.currentTimeMillis();
            final List<CheckinfoExcel> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

            ExcelKit.$Import(CheckinfoExcel.class).readXlsx(file.getInputStream(), new ExcelReadHandler<CheckinfoExcel>() {
                @SneakyThrows
                @Override
                public void onSuccess(int sheet, int row, CheckinfoExcel excel) {
                    // 数据校验成功时，加入集合
                    Date yyyy = formater.parse(excel.getYear() + "-01-01");
                    data.add(excel);
                }

                @Override
                public void onError(int sheet, int row, List<ExcelErrorField> errorFields) {
                    // 数据校验失败时，记录到 error集合
                    error.add(ImmutableMap.of("row", row, "errorFields", errorFields));
                }
            });

            if (!data.isEmpty()) {
                List<Code> codeList = iCodeService.list();
                List<Checkinfo> insert = Lists.newArrayList();
                List<String> resultList = Lists.newArrayList("1", "2", "3", "4", "5", "6", "7", "8");
                for (CheckinfoExcel item : data) {
                    List<Code> query = codeList.stream().filter(s -> s.getEmployeecode().equals(item.getCode())).collect(Collectors.toList());
                    if (query.size() > 0) {
                        Checkinfo entity = new Checkinfo();
                        entity.setId(UUID.randomUUID().toString());
                        entity.setEmployeeid(query.get(0).getEmployeeid());
                        if (StringUtils.isNotBlank(item.getResult())) {
                            if (resultList.contains(item.getResult())) {
                                entity.setResult(Long.parseLong(item.getResult()));
                            } else {
                                entity.setResult(8l);
                            }
                        } else {
                            entity.setResult(8l);
                        }
                        entity.setYear(formater.parse(item.getYear() + "-01-01"));

                        entity.setMemo(item.getMemo());
                        insert.add(entity);
                    }
                }
                // 将合法的记录批量入库
                if (insert.size() > 0)
                    this.iCheckService.saveOrUpdateBatch(insert);
            }
            long time = ((System.currentTimeMillis() - beginTimeMillis));
            ImmutableMap<String, Object> result = ImmutableMap.of(
                    "time", time,
                    "data", data,
                    "error", error
            );
            return new FebsResponse().data(result);
        } catch (Exception e) {
            message = "导入Excel数据失败," + e.getMessage();
            log.error(message);
            throw new FebsException(HttpStatus.BAD_REQUEST, message);
        }
    }

    /**
     * 导出 Excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Checkinfo check) throws FebsException {
        try {
            DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat formaterYYYY = new SimpleDateFormat("yyyy");
            String start = formaterYYYY.format(check.getStartdate());
            String end = formaterYYYY.format(check.getEnddate());
            check.setStartdate(formater.parse(start + "-01-01"));
            check.setEnddate(formater.parse(end + "-01-01"));

            List<Checkinfo> list = this.iCheckService.findCheckinfoReport2(check.getDeptids(),
                    check.getInputdate(),check.getStartdate(),check.getEnddate(),check.getEmployeeid(),check.getEmployeename(),check.getResult());
            List<CheckinfoExcel> excelList = Lists.newArrayList();
            List<Dict> dictList = dictService.getFieldDict("checkinfo", "result");
            List<Dict> query = Lists.newArrayList();
            for (Checkinfo item : list) {
                CheckinfoExcel entity = new CheckinfoExcel();
                entity.setCode(item.getCode());
                entity.setEmployeename(item.getEmployeename());
                entity.setGender(item.getGender());
                if (item.getResult() != null) {
                    query = dictList.stream().filter(s -> s.getKeyy() != null &&
                            s.getKeyy().equals(item.getResult().toString())).collect(Collectors.toList());
                    if (query.size() > 0) {
                        entity.setResult(query.get(0).getValuee());
                    }
                }
                if (item.getYear() != null) {
                    String year = formaterYYYY.format(item.getYear());
                    entity.setYear(year);
                }
                entity.setMemo(item.getMemo());
//                entityUtil.getEntity(item, entity);
                excelList.add(entity);
            }

            ExcelKit.$Export(CheckinfoExcel.class, response).downXlsx(excelList, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST, message);
        }
    }

}
