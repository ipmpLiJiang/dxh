package cc.mrbird.febs.system.service.impl;
import cc.mrbird.febs.system.domain.ContinueEducation;
import cc.mrbird.febs.system.dao.ContinueEducationMapper;
import cc.mrbird.febs.system.service.IContinueEducationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Service
public class ContinueEducationServiceImpl extends ServiceImpl<ContinueEducationMapper, ContinueEducation> implements IContinueEducationService {
    public List<ContinueEducation> findContinueEducation(Integer employeeId){
        LambdaQueryWrapper<ContinueEducation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContinueEducation::getEmployeeid, employeeId);
        return baseMapper.selectList(queryWrapper).stream().sorted(Comparator.comparing(ContinueEducation::getEnddate).reversed()).collect(Collectors.toList());
    }
    public void createContinueEducation(ContinueEducation continueEducation){
        this.save(continueEducation);
    }

    public void updateContinueEducation(ContinueEducation continueEducation){
        this.updateById(continueEducation);
    }
    public void deleteContinueEducations(String[] ContinueEducationIds){
        this.baseMapper.deleteBatchIds(Arrays.asList(ContinueEducationIds));
    }
}
