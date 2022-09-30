package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.RszfwDetail;
import cc.mrbird.febs.system.dao.RszfwDetailMapper;
import cc.mrbird.febs.system.service.IRszfwDetailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MrBird
 */
@Service
public class RszfwDetailServiceImpl extends ServiceImpl<RszfwDetailMapper, RszfwDetail> implements IRszfwDetailService {
    public List<RszfwDetail> findRszfwDetail(Integer rsfwid){
        LambdaQueryWrapper<RszfwDetail> queryWrapper = new LambdaQueryWrapper<>();
        if(rsfwid!=null){
            queryWrapper.eq(RszfwDetail::getRsfwid, rsfwid);
        }
        return baseMapper.selectList(queryWrapper);
    }

    public  List<RszfwDetail> findAllRszfwDetail(){
        LambdaQueryWrapper<RszfwDetail> queryWrapper = new LambdaQueryWrapper<>();
        return baseMapper.selectList(queryWrapper);
    }

    public String getName(List<RszfwDetail> rszfwDetails,String rsfw){
        String name="";
        for(RszfwDetail rszfwDetail:rszfwDetails){
            if(rszfwDetail.getKeyy()==Integer.parseInt(rsfw)){
                name=rszfwDetail.getValuee();
                break;
            }
        }
        return name;
    }
}
