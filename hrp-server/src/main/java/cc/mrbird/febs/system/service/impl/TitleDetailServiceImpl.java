package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.JobLevelDetail;
import cc.mrbird.febs.system.domain.TitleDetail;
import cc.mrbird.febs.system.dao.TitleDetailMapper;
import cc.mrbird.febs.system.service.ITitleDetailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MrBird
 */
@Service
public class TitleDetailServiceImpl extends ServiceImpl<TitleDetailMapper, TitleDetail> implements ITitleDetailService {
    public List<TitleDetail> findTitleDetail(){
        LambdaQueryWrapper<TitleDetail> queryWrapper = new LambdaQueryWrapper<>();
        return baseMapper.selectList(queryWrapper);
    }

    public String getName(List<TitleDetail>titleDetailList,String titleId){
        String name="";
        for(TitleDetail t:titleDetailList){
            if(t.getKeyy().equals(titleId)){
                name=t.getValuee();
                break;
            }
        }
        return name;
    }
}
