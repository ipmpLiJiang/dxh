package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.EmployeeCore;
import cc.mrbird.febs.system.service.IEmployeeCoreService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("/system/employee/core")
public class EmployeeCoreController extends BaseController {
    private String message;

    @Autowired
    IEmployeeCoreService iEmployeeCoreService;
    @GetMapping
    public List<EmployeeCore> employeeCoreList(Integer employeeId) {
        return this.iEmployeeCoreService.findEmployeeCores(employeeId);
    }

    @GetMapping("/getLatestCore/{employeeId}")
    public EmployeeCore findLatestCore(@PathVariable(value = "employeeId")Integer employeeId) {
        EmployeeCore employeeCore=this.iEmployeeCoreService.findLatestCore(employeeId);
        employeeCore.setId(null);
        return employeeCore;
    }

    @Log("新增职工核心")
    @PostMapping
    public Map<String,Object> addEmplyeeCore(@Valid EmployeeCore employeeCore) throws FebsException {
        this.iEmployeeCoreService.createEmployeeCore(employeeCore);
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS,null);
    }

    @Log("修改职工核心")
    @PutMapping
    public Map<String,Object> updateEmplyeeCore(@Valid EmployeeCore employeeCore) throws Exception {
        this.iEmployeeCoreService.updateEmployeeCore(employeeCore);
        return getResult(HttpStatus.OK,FebsConstant.DELETE_SUCCESS,null);
    }
}
