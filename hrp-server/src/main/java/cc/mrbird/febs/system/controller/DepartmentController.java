package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.system.domain.Department;
import cc.mrbird.febs.system.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author MrBird
 */
@RestController
@RequestMapping("/system/department")
public class DepartmentController {
    @Autowired
    IDepartmentService iDepartmentService;
    @GetMapping
    public List<Department> findOrganization(String parentDeptId) {
        Department department=new Department();
        department.setParentid(parentDeptId);
        return iDepartmentService.findDepartmentList(department);
    }
}
