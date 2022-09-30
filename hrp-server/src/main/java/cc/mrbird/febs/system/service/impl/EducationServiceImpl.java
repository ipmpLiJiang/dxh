package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.system.domain.Education;
import cc.mrbird.febs.system.dao.EducationMapper;
import cc.mrbird.febs.system.service.IComFileService;
import cc.mrbird.febs.system.service.IEducationService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Service
public class EducationServiceImpl extends ServiceImpl<EducationMapper, Education> implements IEducationService {
    @Autowired
    IComFileService comFileService;
    public List<Education> findEducation(Integer employeeId){
        LambdaQueryWrapper<Education> queryWrapper = new LambdaQueryWrapper<>();
        if (employeeId!=null) {
            queryWrapper.eq(Education::getEmployeeid, employeeId);
            return baseMapper.selectList(queryWrapper).stream().sorted(Comparator.comparing(Education::getEnddate).reversed()).collect(Collectors.toList());
        }else{
            return new ArrayList<>();
        }
    }

    public void createEducation(Education education){
        this.save(education);
        List<String> idList = new ArrayList<>();
        if(StringUtils.isNotBlank(education.getXwzsfileid())) {
            idList.add(education.getXwzsfileid());
        }
        if(StringUtils.isNotBlank(education.getXlzsfileid())) {
            idList.add(education.getXlzsfileid());
        }
        if(StringUtils.isNotBlank(education.getDzbabfileid())) {
            idList.add(education.getDzbabfileid());
        }
        if(idList.size() > 0) {
            comFileService.updateComFileByInId(idList,education.getId());
        }
    }

    public void updateEducation(Education education){
        updateById(education);
    }

    public void deleteEducations(String[] educationIds){
        baseMapper.deleteBatchIds(Arrays.asList(educationIds));
    }


    public List<Education> getOAEducation(){
        List<Education> educations=baseMapper.getOAEducation();
        return educations;
    }

    public void updateOAEducation(){
        this.baseMapper.updateOAEducation();
    }

    public void deleteByEmployeeids(List<Integer> employeeids){
        this.baseMapper.deleteByEmployeeids(employeeids);
    }

}
