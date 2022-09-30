package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.system.domain.Cwyd;
import cc.mrbird.febs.system.domain.EmployeeCore;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MrBird
 */
public interface EmployeeCoreMapper extends BaseMapper<EmployeeCore> {
    @DS(FebsConstant.HR_TO_OA_DATA_SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<EmployeeCore> getOAEmployeeCore();

    @DS(FebsConstant.HR_TO_OA_DATA_SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void updateOAYdxx();

    @Update("update employee set emolumentstatus=3 where employeeid=#{employeeid}")
    void updateEmployedStatus(@Param("employeeid") int employeeid);

    List<EmployeeCore> findEmployeeCoreMaxEndDateList();

}
