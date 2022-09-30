package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.Family;
import cc.mrbird.febs.system.dao.FamilyMapper;
import cc.mrbird.febs.system.service.IFamilyService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author MrBird
 */

@Service
public class FamilyServiceImpl extends ServiceImpl<FamilyMapper, Family> implements IFamilyService {
    public List<Family> findFamily(Integer employeeId){
        LambdaQueryWrapper<Family> queryWrapper = new LambdaQueryWrapper<>();
        if (employeeId!=null) {
            queryWrapper.eq(Family::getEmployeeid, employeeId);
            return baseMapper.selectList(queryWrapper);
        }else{
            return new ArrayList<>();
        }
    }
    public void createFamily(Family family){
        this.save(family);
    }

    public void updateFamily(Family family){
        updateById(family);
    }

    public void deleteFamily(String[] familyIds){
        baseMapper.deleteBatchIds(Arrays.asList(familyIds));
    }

    public List<Family> getOAFamily(){
        return baseMapper.getOAFamily();
    }

    public void updateOAFamily(){
        this.baseMapper.updateOAFamily();
    }

    public void deleteByEmployeeids(List<Integer> employeeids){
        this.baseMapper.deleteByEmployeeids(employeeids);
    }
}
