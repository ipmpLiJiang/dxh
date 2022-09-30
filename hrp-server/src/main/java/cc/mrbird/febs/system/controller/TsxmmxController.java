package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.Tsxmmx;
import cc.mrbird.febs.system.service.FileService;
import cc.mrbird.febs.system.service.ITsxmmxService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.pojo.ExcelErrorField;
import com.wuwenze.poi.handler.ExcelReadHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("/system/tsxmymx")
public class TsxmmxController extends BaseController {
    private String message;

    @Autowired
    ITsxmmxService iTsxmmxService;

    @Autowired
    FileService fileService;
    @GetMapping
    public Map<String, Object> gzxmList(QueryRequest queryRequest, Tsxmmx tsxmmx) {
        Map<String,Object> data= getDataTable(iTsxmmxService.findGzxms(tsxmmx, queryRequest));
        data.put("sum",iTsxmmxService.getTotalData(tsxmmx));
        return data;
    }

    @Log("新增工资项目")
    @PostMapping
    public Map<String,Object> addGzxm(@Valid Tsxmmx tsxmmx) throws FebsException {
        this.iTsxmmxService.createGzxm(tsxmmx);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);

    }

    @Log("删除工资项目")
    @DeleteMapping("/{gzxmIds}")
    public Map<String,Object> deleteGzxms(@NotBlank(message = "{required}") @PathVariable String gzxmIds) throws FebsException {
        String[] ids = gzxmIds.split(StringPool.COMMA);
        this.iTsxmmxService.deleteGzxm(ids);
        return getResult(HttpStatus.OK, FebsConstant.DELETE_SUCCESS,null);
    }

    @Log("修改工资项目")
    @PutMapping
    public Map<String,Object> updateGzxm(Tsxmmx tsxmmx) throws FebsException {
        this.iTsxmmxService.updateGzxm(tsxmmx);
        return getResult(HttpStatus.OK, FebsConstant.UPDATE_SUCCESS,null);
    }

    /**
     * 导入Excel数据，并批量修改薪酬表
     */
    @PostMapping("import")
    public FebsResponse importExcels(@RequestParam("file") MultipartFile file) throws FebsException {
        try {
            fileService.validFile(file);
            // 开始导入操作
            long beginTimeMillis = System.currentTimeMillis();
            final List<Tsxmmx> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            ExcelKit.$Import(Tsxmmx.class).readXlsx(file.getInputStream(), new ExcelReadHandler<Tsxmmx>() {
                @Override
                public void onSuccess(int sheet, int row, Tsxmmx tsxmmx) {
                    // 数据校验成功时，加入集合
                    data.add(tsxmmx);
                }
                @Override
                public void onError(int sheet, int row, List<ExcelErrorField> errorFields) {
                    // 数据校验失败时，记录到 error集合
                    error.add(ImmutableMap.of("row", row, "errorFields", errorFields));
                }
            });
            if (!data.isEmpty()) {
                // 将合法的记录批量入库
                this.iTsxmmxService.batchCreateGzxm(data);
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
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }
}
