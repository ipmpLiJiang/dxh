package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.system.domain.Cwgz;
import cc.mrbird.febs.system.domain.Department;
import cc.mrbird.febs.system.service.ICwgzService;
import cc.mrbird.febs.system.service.IDepartmentService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("/system/cwgz")
public class CwgzController extends BaseController {
    @Autowired
    ICwgzService iCwgzService;

    @Autowired
    IDepartmentService departmentService;

    private String message;

    @GetMapping
    public Map<String,Object> tsGz()  {
        this.iCwgzService.tsGz();
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    @GetMapping("/list")
    public Map<String, Object> getList()  {
        return this.iCwgzService.getList();
    }

    @GetMapping("/getSb")
    public List<Cwgz> getSb(Cwgz cwgz)  {
        return this.iCwgzService.getSb(cwgz.getPeriod());
    }

    @GetMapping("/getSbSum")
    public List<Cwgz> getSbSum(Cwgz cwgz)  {
        return this.iCwgzService.getSbSum(cwgz.getPeriod());
    }

    @PostMapping("/export")
    public void export(HttpServletResponse response, Cwgz cwgz) throws FebsException {
        try {
            List<Cwgz> list = this.iCwgzService.getSb(cwgz.getPeriod());
            List<Department> departmentList=departmentService.findDepartmentList(new Department());
            for(Cwgz c:list){
                c.setCdeptname(departmentService.getName(departmentList,c.getCDeptnum()));
            }
            ExcelKit.$Export(Cwgz.class, response).downXlsx(list, true);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }

}
