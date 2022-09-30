package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.system.domain.Employee;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface EmployeeMapper extends BaseMapper<Employee> {
    List<Employee> findEmployeeBySearch(String search);

    Employee findEmployeeById(Integer id);

    Employee findEmployeeByCode(String code);

    IPage<Employee> deptFindEmployeeList(Page page,@Param("employee")Employee employee);

    IPage<Employee> findEmployeeReport(Page page,@Param("employee")Employee employee);

    List<Employee> findEmployeeReport(@Param("employee")Employee employee);

    List<Employee> findEmployeeReport3(@Param("employee")Employee employee);

    List<Employee> findEmployeeAgeTitleReport(@Param("employee")Employee employee);

    List<Employee> findEmployeeTitleGetReport(@Param("employee")Employee employee);

    List<Employee> findEmployeeBirthEducationReport(@Param("employee")Employee employee);

    List<Employee> findEmployeeJoinReport(@Param("employee")Employee employee);

    List<Employee> getEmployeeByKqDept(String kqDept, Date startDate,Date endDate,String rszfws);
    void updateEmolumentStatus(@Param("ids") List<Integer> ids,int state);




    /**
     * 获取需要考勤用户
     * @param employee
     * @return
     */
    IPage<Employee> getAttandUser(Page page, @Param("employee")Employee employee);

    @DS(FebsConstant.HR_TO_OA_DATA_SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<Employee> getOAEmployee();

    @DS(FebsConstant.HR_TO_OA_DATA_SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void updateOAEmployee();

    @DS(FebsConstant.HR_TO_OA_DATA_SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void addOAEmployee(@Param("employee")Employee employee);
}
