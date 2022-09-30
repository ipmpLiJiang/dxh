package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.KqJk;
import cc.mrbird.febs.system.domain.KqJkExcel;
import cc.mrbird.febs.system.domain.KqPeriod;
import cc.mrbird.febs.system.service.IDepartmentService;
import cc.mrbird.febs.system.service.IKqDeptService;
import cc.mrbird.febs.system.service.IKqJkService;
import cc.mrbird.febs.system.service.IKqPbDetailService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("/system/kq/jk")
public class KqJkController extends BaseController {
    private String message;
    @Autowired
    IKqJkService iKqJkService;

    @Autowired
    IKqDeptService iKqDeptService;

    @Autowired
    IKqPbDetailService iKqPbDetailService;
    @GetMapping
    public Map<String, Object> getJkList(QueryRequest request, KqJk kqJk)  {
        return getDataTable(iKqJkService.findKqjk(request,kqJk));
    }

    @GetMapping("getDuplicateList")
    public Map<String, Object> getDuplicateList(QueryRequest request, KqJk kqJk) {
        return getDataTable(iKqJkService.findDuplicateList(request,kqJk));
    }

    @PostMapping
    public Map<String, Object> createHzList(QueryRequest request, KqPeriod period) throws IllegalAccessException {
        iKqJkService.createHzList(request,period);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    /**
     * 导出 Excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, KqJk kqJk) throws FebsException {
        try {
            List<KqJk> list = this.iKqJkService.findKqjk(kqJk);
            iKqPbDetailService.getPbNameByKqjk(list);
            for(KqJk k: list){
                k.setKqdept(iKqDeptService.getName(k.getKqdept()));
                k.setTjdept(iKqDeptService.getName(k.getTjdept()));
            }
            ExcelKit.$Export(KqJkExcel.class, response).downXlsx(list, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }

    @Log("删除重复考勤")
    @DeleteMapping("/delete/{jkIds}")
    public Map<String,Object> deleteEducations(@NotBlank(message = "{required}") @PathVariable String jkIds) throws FebsException {
        String[] ids = jkIds.split(StringPool.COMMA);
        this.iKqJkService.deleteKqJk(ids);
        return getResult(HttpStatus.OK,FebsConstant.DELETE_SUCCESS,null);
    }
}
