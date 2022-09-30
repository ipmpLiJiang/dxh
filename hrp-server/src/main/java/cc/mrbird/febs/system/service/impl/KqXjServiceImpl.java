package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.KqXj;
import cc.mrbird.febs.system.dao.KqXjMapper;
import cc.mrbird.febs.system.service.IKqXjService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MrBird
 */
@Service
public class KqXjServiceImpl extends ServiceImpl<KqXjMapper, KqXj> implements IKqXjService {
    @Override
    public List<KqXj> getOAXj(){
        return baseMapper.getOAXj();
    }

    @Override
    public void updateOAXj(){
        this.baseMapper.updateOAXj();
    }
}
