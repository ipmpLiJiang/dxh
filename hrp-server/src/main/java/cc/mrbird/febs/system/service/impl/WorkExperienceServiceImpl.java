package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.dao.EducationMapper;
import cc.mrbird.febs.system.dao.WorkExperienceMapper;
import cc.mrbird.febs.system.domain.Education;
import cc.mrbird.febs.system.domain.WorkExperience;
import cc.mrbird.febs.system.service.IEducationService;
import cc.mrbird.febs.system.service.IWorkExperienceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.jpa.domain.QAbstractAuditable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Service
public class WorkExperienceServiceImpl extends ServiceImpl<WorkExperienceMapper, WorkExperience> implements IWorkExperienceService {
    public List<WorkExperience> findWorkExperience(Integer employeeId){
        LambdaQueryWrapper<WorkExperience> queryWrapper = new LambdaQueryWrapper<>();
        if (employeeId!=null) {
            queryWrapper.eq(WorkExperience::getEmployeeid, employeeId);
            return baseMapper.selectList(queryWrapper);
        }else{
            return new ArrayList<>();
        }
    }

    public void createWorkExperience(WorkExperience workExperience){
        deleteByEmployeeId(workExperience.getEmployeeid());
        this.save(workExperience);
    }

    public void updateWorkExperience(WorkExperience workExperience){
        updateById(workExperience);
    }

    public void deleteWorkExperience(String[] workExperienceIds){
        baseMapper.deleteBatchIds(Arrays.asList(workExperienceIds));
    }

    public void deleteByEmployeeId(Integer employeeId){
        LambdaQueryWrapper<WorkExperience>  queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WorkExperience::getEmployeeid,employeeId);
        baseMapper.delete(queryWrapper);
    }

}
