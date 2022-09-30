package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.EducationBeforeWork;
import cc.mrbird.febs.system.dao.EducationBeforeWorkMapper;
import cc.mrbird.febs.system.service.IEducationBeforeWorkService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author MrBird
 */
@Service
public class EducationBeforeWorkServiceImpl extends ServiceImpl<EducationBeforeWorkMapper, EducationBeforeWork> implements IEducationBeforeWorkService {
    public List<EducationBeforeWork> findEducationBeforeWork(Integer employeeId){
        LambdaQueryWrapper<EducationBeforeWork> queryWrapper = new LambdaQueryWrapper<>();
        if (employeeId!=null) {
            queryWrapper.eq(EducationBeforeWork::getEmployeeid, employeeId);
            return baseMapper.selectList(queryWrapper);
        }else{
            return new ArrayList<>();
        }
    }

    public void createEducationBeforeWork(EducationBeforeWork educationBeforeWork){
        this.save(educationBeforeWork);
    }

    public void updateEducationBeforeWork(EducationBeforeWork educationBeforeWork){
        updateById(educationBeforeWork);
    }

    public void deleteEducationBeforeWorks(String[] educationBeforeWorkIds){
        baseMapper.deleteBatchIds(Arrays.asList(educationBeforeWorkIds));
    }
}
