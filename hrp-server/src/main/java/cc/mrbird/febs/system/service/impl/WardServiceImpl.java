package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.Ward;
import cc.mrbird.febs.system.dao.WardMapper;
import cc.mrbird.febs.system.service.IWardService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MrBird
 */
@Service
public class WardServiceImpl extends ServiceImpl<WardMapper, Ward> implements IWardService {
    public List<Ward> findWard(Integer deptId){
        LambdaQueryWrapper<Ward> queryWrapper = new LambdaQueryWrapper<>();
        if(deptId!=null){
            queryWrapper.eq(Ward::getDeptid, deptId);
        }
        return baseMapper.selectList(queryWrapper);
    }

    public String getName(String wardId){
        List<Ward> wardList=this.findWard(null);
        String name="";
        for(Ward ward:wardList){
            if(ward.getDeptid().equals(wardId)){
                name=ward.getValuee();
                break;
            }
        }
        return name;
    }
}
