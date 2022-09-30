package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.AdministrativeLevel;
import cc.mrbird.febs.system.dao.AdministrativeLevelMapper;
import cc.mrbird.febs.system.domain.Education;
import cc.mrbird.febs.system.service.IAdministrativeLevelService;
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
public class AdministrativeLevelServiceImpl extends ServiceImpl<AdministrativeLevelMapper, AdministrativeLevel> implements IAdministrativeLevelService {
    public List<AdministrativeLevel> findAdministrativeLevel(Integer employeeId){
        LambdaQueryWrapper<AdministrativeLevel> queryWrapper = new LambdaQueryWrapper<>();
        if (employeeId!=null) {
            queryWrapper.eq(AdministrativeLevel::getEmployeeid, employeeId);
            return baseMapper.selectList(queryWrapper);
        }else{
            return new ArrayList<>();
        }
    }

    public void createAdministrativeLevel(AdministrativeLevel administrativeLevel){
        this.save(administrativeLevel);
    }

    public void updateAdministrativeLevel(AdministrativeLevel administrativeLevel){
        updateById(administrativeLevel);
    }

    public void deleteAdministrativeLevels(String[] administrativeLevelIds){
        baseMapper.deleteBatchIds(Arrays.asList(administrativeLevelIds));
    }
}
