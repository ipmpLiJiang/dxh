package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.EntityUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.Code;
import cc.mrbird.febs.system.domain.Contract;
import cc.mrbird.febs.system.domain.ContractExcel;
import cc.mrbird.febs.system.domain.Dict;
import cc.mrbird.febs.system.service.DictService;
import cc.mrbird.febs.system.service.ICodeService;
import cc.mrbird.febs.system.service.IContractService;
import cc.mrbird.febs.system.service.IDutyDetailService;
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
import org.apache.http.client.utils.DateUtils;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("/system/contract")
public class ContractController extends BaseController {

    private String message;

    @Autowired
    EntityUtil entityUtil;

    @Autowired
    IContractService iContractService;

    @Autowired
    DictService dictService;

    @Autowired
    ICodeService iCodeService;

    @GetMapping("/getContractReport")
    public Map<String, Object> getContractReport(QueryRequest request, Contract contract) {
        Page<Contract> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        return getDataTable(this.iContractService.getContractReport(page,contract));
    }

    @Log("新增合同")
    @PostMapping
    public Map<String,Object> addContract(@Valid Contract contract)  {
        this.iContractService.createContract(contract);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    @Log("修改合同")
    @PutMapping
    public Map<String,Object> updateContract(@Valid Contract contract)  {
        this.iContractService.updateContract(contract);
        return getResult(HttpStatus.OK,FebsConstant.UPDATE_SUCCESS,null);
    }

    @Log("删除合同")
    @DeleteMapping("/delete/{contractIds}")
    public Map<String,Object> deleteContracts(@NotBlank(message = "{required}") @PathVariable String contractIds) throws FebsException {
        String[] ids = contractIds.split(StringPool.COMMA);
        this.iContractService.deleteContracts(ids);
        return getResult(HttpStatus.OK,FebsConstant.DELETE_SUCCESS,null);
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
            final List<ContractExcel> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

            ExcelKit.$Import(ContractExcel.class).readXlsx(file.getInputStream(), new ExcelReadHandler<ContractExcel>() {
                @SneakyThrows
                @Override
                public void onSuccess(int sheet, int row, ContractExcel excel) {
                    // 数据校验成功时，加入集合
                    Date startDate = formater.parse(excel.getStartdate());
                    Date endDate = formater.parse(excel.getEnddate());
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
                List<Contract> insert = Lists.newArrayList();
                List<String> typeList = Lists.newArrayList("1","2");
                for (ContractExcel item : data){
                    List<Code> query = codeList.stream().filter(s->s.getEmployeecode().equals(item.getCode())).collect(Collectors.toList());
                    if(query.size() > 0) {
                        Contract entity = new Contract();
                        entity.setId(UUID.randomUUID().toString());
                        entity.setEmployeeid(query.get(0).getEmployeeid());
                        if(StringUtils.isNotBlank(item.getContracttype())) {
                            if(typeList.contains(item.getContracttype())) {
                                entity.setContracttype(Integer.parseInt(item.getContracttype()));
                            } else {
                                entity.setContracttype(1);
                            }
                        } else{
                            entity.setContracttype(1);
                        }
                        entity.setStartdate(formater.parse(item.getStartdate()));
                        entity.setEnddate(formater.parse(item.getEnddate()));

                        entity.setMemo(item.getMemo());
                        insert.add(entity);
                    }
                }
                // 将合法的记录批量入库
                if (insert.size() > 0)
                    this.iContractService.saveOrUpdateBatch(insert);
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
    public void export(HttpServletResponse response, Contract contract) throws FebsException {
        try {
            List<Contract> list = this.iContractService.getContractReport(contract);
            List<ContractExcel> excelList= Lists.newArrayList();
            DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
            List<Dict> dictList = dictService.getFieldDict("contract","contract_type");
            List<Dict> query = Lists.newArrayList();
            for (Contract item : list){
                ContractExcel entity = new ContractExcel();
                entity.setCode(item.getCode());
                entity.setEmployeename(item.getEmployeename());
                entity.setGender(item.getGender());
                if(item.getContracttype() != null) {
                    query = dictList.stream().filter(s -> s.getKeyy() != null &&
                            s.getKeyy().equals(item.getContracttype().toString())).collect(Collectors.toList());
                    if (query.size() > 0) {
                        entity.setContracttype(query.get(0).getValuee());
                    }
                }
                if(item.getStartdate() != null) {
                    entity.setStartdate(formater.format(item.getStartdate()));
                }
                if(item.getEnddate() != null) {
                    entity.setEnddate(formater.format(item.getEnddate()));
                }
                entity.setMemo(item.getMemo());
//                entityUtil.getEntity(item, entity);
                excelList.add(entity);
            }

            ExcelKit.$Export(ContractExcel.class, response).downXlsx(excelList, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }

}
