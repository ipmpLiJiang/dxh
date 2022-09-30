package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.KkList;
import cc.mrbird.febs.system.domain.KqHz;
import cc.mrbird.febs.system.domain.KqPeriod;
import cc.mrbird.febs.system.service.IKqDeptService;
import cc.mrbird.febs.system.service.IKqHzService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("/system/kq/hz")
public class KqHzController extends BaseController {
    private String message;
    @Autowired
    IKqHzService iKqHzService;

    @Autowired
    IKqDeptService iKqDeptService;
    @GetMapping
    public Map<String, Object> getKkList(QueryRequest request, KqHz kqHz) {
        Map<String,Object> map=getDataTable(iKqHzService.getKkList(request,kqHz));
        map.put("sum",iKqHzService.getSum(kqHz));
        return map;
    }

    @GetMapping("getHzReport")
    public Map<String, Object> getHzReport(QueryRequest request, KqHz kqHz) {
        Map<String,Object> map=getDataTable(iKqHzService.getHzReport(request,kqHz));
        map.put("sum",iKqHzService.getHzSum(kqHz));
        return map;
    }

    @Log("修改扣款")
    @PutMapping
    public Map<String,Object> updateKk(@Valid KqHz kqHz) {
        this.iKqHzService.updateKqHz(kqHz);
        return getResult(HttpStatus.OK, FebsConstant.UPDATE_SUCCESS,null);
    }

    @Log("推送考勤")
    @GetMapping("passKqHz")
    public Map<String,Object> passKqHz(KqPeriod kqPeriod){
        iKqHzService.passKqHz(kqPeriod);
        return getResult(HttpStatus.OK,FebsConstant.CHECK_SUCCESS,null);
    }

    /**
     * 导出 Excel
     */
    @PostMapping("export")
    public void export(HttpServletResponse response, KqHz kqHz) throws FebsException {
        try {
            List<KqHz> list = this.iKqHzService.getHzReport(kqHz);
            for(KqHz k: list){
                k.setDept(iKqDeptService.getName(k.getDept()));
            }
            ExcelKit.$Export(KqHz.class, response).downXlsx(list, true);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }

    /**
     * 导出 Excel
     */
    @PostMapping("export/kk")
    public void exportKk(HttpServletResponse response, KqHz kqHz) throws FebsException {
        try {
            List<KkList> list = this.iKqHzService.getKkList(kqHz);
            for(KkList k:list){
                k.setDept(iKqDeptService.getName(k.getDept()));
            }
            ExcelKit.$Export(KkList.class, response).downXlsx(list, true);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }
}
