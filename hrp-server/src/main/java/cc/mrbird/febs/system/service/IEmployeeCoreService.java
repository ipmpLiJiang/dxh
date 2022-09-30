package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.domain.EmployeeCore;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IEmployeeCoreService extends IService<EmployeeCore> {
    List<EmployeeCore> findEmployeeCores(Integer employeeId);

    List<EmployeeCore> getOAEmployeeCore();
     EmployeeCore findLatestCore(Integer employeeId);
    void createEmployeeCore(EmployeeCore employeeCore) throws FebsException;

    /**
     * 修改职工核心
     *
     * @param employeecore employeecore
     */
    void updateEmployeeCore(EmployeeCore employeecore) throws Exception;

    void updateOAYdxx();

    void deleteEmployeeCore(String id);

    List<EmployeeCore> findEmployeeCoreMaxEndDateLists();
}
