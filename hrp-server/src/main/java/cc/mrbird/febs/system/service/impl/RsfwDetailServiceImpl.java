package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.RsfwDetail;
import cc.mrbird.febs.system.dao.RsfwDetailMapper;
import cc.mrbird.febs.system.service.IRsfwDetailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MrBird
 */
@Service
public class RsfwDetailServiceImpl extends ServiceImpl<RsfwDetailMapper, RsfwDetail> implements IRsfwDetailService {
    public List<RsfwDetail> findRsfwDetail(){
        LambdaQueryWrapper<RsfwDetail> queryWrapper = new LambdaQueryWrapper<>();
        return baseMapper.selectList(queryWrapper);
    }

    public String getName(List<RsfwDetail>rsfwDetailList,String rsfw){
        String name="";
        for(RsfwDetail rsfwDetail:rsfwDetailList){
            if(rsfwDetail.getKeyy()==Integer.parseInt(rsfw)){
                name=rsfwDetail.getValuee();
                break;
            }
        }
        return name;
    }
}
