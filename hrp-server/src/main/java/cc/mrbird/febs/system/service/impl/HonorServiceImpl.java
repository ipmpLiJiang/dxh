package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.Honor;
import cc.mrbird.febs.system.dao.HonorMapper;
import cc.mrbird.febs.system.service.IHonorService;
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
public class HonorServiceImpl extends ServiceImpl<HonorMapper, Honor> implements IHonorService {
    public List<Honor> findHonor(Integer employeeId){
        LambdaQueryWrapper<Honor> queryWrapper = new LambdaQueryWrapper<>();
        if (employeeId!=null) {
            queryWrapper.eq(Honor::getEmployeeid, employeeId);
            return baseMapper.selectList(queryWrapper);
        }else{
            return new ArrayList<>();
        }
    }
    public void createHonor(Honor honor){
        this.save(honor);
    }

    public void updateHonor(Honor honor){
        updateById(honor);
    }

    public void deleteHonor(String[] honorids){
        baseMapper.deleteBatchIds(Arrays.asList(honorids));
    }
}
