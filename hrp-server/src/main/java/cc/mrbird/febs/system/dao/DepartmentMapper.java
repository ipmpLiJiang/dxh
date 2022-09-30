package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.system.domain.Department;
import cc.mrbird.febs.system.domain.Dept;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MrBird
 */
public interface DepartmentMapper extends BaseMapper<Department> {
    void deleteAll();

    @DS(FebsConstant.OA_TO_HR_DATA_SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<Department> getOADept();

    @DS(FebsConstant.CW_DATA_SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void createDepartmentToCw(@Param("department") Department department);

    @DS(FebsConstant.CW_DATA_SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void deleteDepartment();
}
