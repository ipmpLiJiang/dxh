package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.Department;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IDepartmentService extends IService<Department> {
    List<Department> findDepartmentList(Department department);
    Department findDepartmentByKey(String keyy);
    Department findDepartmentByValue(String value);
    void createDepartment(Department department);
    void createDepartmentToCw(Department department);
    String getName(List<Department>departmentList,String deptId);
    List<Department> getOADepartment();
    void deleteAll();
}
