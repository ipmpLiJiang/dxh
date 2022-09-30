package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.DutyDetail;
import cc.mrbird.febs.system.dao.DutyDetailMapper;
import cc.mrbird.febs.system.service.IDutyDetailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MrBird
 */
@Service
public class DutyDetailServiceImpl extends ServiceImpl<DutyDetailMapper, DutyDetail> implements IDutyDetailService {
    public List<DutyDetail> findDutyDetail(Integer titleId){
        LambdaQueryWrapper<DutyDetail> queryWrapper = new LambdaQueryWrapper<>();
        if(titleId!=null){
            queryWrapper.eq(DutyDetail::getTitleid, titleId);
        }
        return baseMapper.selectList(queryWrapper);
    }

    public String getName(List<DutyDetail>dutyDetailList,String jobLevel){
        String name="";
        for(DutyDetail d:dutyDetailList){
            if(d.getKeyy().equals(jobLevel)){
                name=d.getValuee();
                break;
            }
        }
        return name;
    }
}
