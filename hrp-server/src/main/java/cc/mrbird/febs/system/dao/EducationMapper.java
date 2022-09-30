package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.system.domain.Education;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MrBird
 */
public interface EducationMapper extends BaseMapper<Education> {

    @DS(FebsConstant.HR_TO_OA_DATA_SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<Education> getOAEducation();

    void deleteByEmployeeids(@Param("employeeids") List<Integer> employeeids);


    @DS(FebsConstant.HR_TO_OA_DATA_SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void updateOAEducation();
}
