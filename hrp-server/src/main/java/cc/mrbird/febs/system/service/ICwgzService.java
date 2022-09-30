package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.Cwgz;
import cc.mrbird.febs.system.domain.Emolument;
import cc.mrbird.febs.system.domain.Employee;
import cc.mrbird.febs.system.domain.EmployeeCore;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
public interface ICwgzService extends IService<Cwgz> {
    List<Cwgz> tsGz();

    List<Cwgz> getSb(Date period);

    List<Cwgz> getSbSum(Date period);

    Map<String,Object> getList();

    void saveToCw(List<Cwgz> cwgzList);

    void createCwyd(EmployeeCore employeeCore, Employee e);

    List<Emolument> getCwKj(Date period);
}
