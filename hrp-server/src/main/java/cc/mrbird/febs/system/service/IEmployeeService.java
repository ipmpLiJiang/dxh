package cc.mrbird.febs.system.service;


import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
public interface IEmployeeService extends IService<Employee> {
    /**
     * 模糊查询
     *
     * @param search employeeId
     */
    List<Employee> findEmployees(String search);

    Employee findEmployeeById(Integer id);

    Employee findEmployeeByCode(String code);
    IPage<Employee> deptFindEmployeeList(Page page,Employee employee);
    /**
     * 精确查询查询
     *
     * @param  employeeId
     */
    Map<String,Object> getEmployee(Integer employeeId);

    Employee createEmployee(Employee employee);

    IPage<Employee> getEmployeeReport(Page page,Employee employee);

    List<Employee> getEmployeeReport(Employee employee);

    List<Employee> getEmployeeReport3(Employee employee);

    IPage<Employee> getAttandUser(Page page,Employee employee);


    List<Employee> getEmployeeByKqDept(String kqDept, Date startDate,Date endDate,String rszfws);
    /**
     * 修改职工
     *
     * @param employee employee
     */
    void updateEmployee(Employee employee);

    void updateKqDept(Integer employeeid,String kqDept);

    void updateEmolumentStatus(List<Integer> ids,int state);

    List<Employee> getOAEmployee();

    void updateOAEmployee();

    void addOAEmployee(Employee employee);

    List<EmployeeAgeTitleReport> findEmployeeAgeTitleReport(Employee employee);

    List<EmployeeJobLevelTitleGetReport> findEmployeeJoblevelTitleGetReport(Employee employee);

    List<EmployeeBirthEducationReport> findEmployeeBirthEducationReport(Employee employee);

    void importEmployeeCoreEducationEvnt(List<EmployeeCoreEducationExcel> eceList);

}
