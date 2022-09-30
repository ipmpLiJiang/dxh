package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.annotation.KqFiled;
import cc.mrbird.febs.system.domain.KqJk;
import cc.mrbird.febs.system.domain.KqPbDetail;
import cc.mrbird.febs.system.dao.KqPbDetailMapper;
import cc.mrbird.febs.system.domain.KqRmxb;
import cc.mrbird.febs.system.service.IKqPbDetailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

/**
 * @author MrBird
 */
@Service
public class KqPbDetailServiceImpl extends ServiceImpl<KqPbDetailMapper, KqPbDetail> implements IKqPbDetailService {
    public List<KqPbDetail> getPbList(String parentValue,Integer flag){
        LambdaQueryWrapper<KqPbDetail> queryWrapper = new LambdaQueryWrapper<>();
        if(parentValue!=null){
            queryWrapper.like(KqPbDetail::getParentvaluee,parentValue);
        }
        if(parentValue!=null){
            queryWrapper.like(KqPbDetail::getFlag,flag);
        }
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<KqRmxb> getPbName(List<KqRmxb> kqRmxbList){
        List<KqPbDetail> kqPbDetailList=getPbList(null,null);
        for(KqRmxb k:kqRmxbList){
            if(k.getScheduling()!=null){
                Optional<KqPbDetail> o=kqPbDetailList.stream().filter(d->d.getKeyy().equals(Integer.parseInt(k.getScheduling()))).findFirst();
                if(o.isPresent()){
                    k.setScheduling(o.get().getValuee());
                }
            }
        }
        return kqRmxbList;
    }

    public List<KqJk> getPbNameByKqjk(List<KqJk> kqJkList) throws IllegalAccessException {
        List<KqPbDetail> kqPbDetailList=getPbList(null,null);
        for(KqJk k:kqJkList){
            Field[] fields=KqJk.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if(field.getAnnotation(KqFiled.class)!=null){
                    String s= (String) field.get(k);
                    if(s!=null){
                        Optional<KqPbDetail> o=kqPbDetailList.stream().filter(d->d.getKeyy().equals(Integer.parseInt(s))).findFirst();
                        if(o.isPresent()){
                            field.set(k,o.get().getValuee());
                        }
                    }
                }
            }
        }
        return kqJkList;
    }
}
