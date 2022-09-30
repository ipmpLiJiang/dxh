package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.Position;
import cc.mrbird.febs.system.dao.PositionMapper;
import cc.mrbird.febs.system.service.IPositionService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MrBird
 */
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements IPositionService {
    public List<Position> findPositionDetail(){
        LambdaQueryWrapper<Position> queryWrapper = new LambdaQueryWrapper<>();
        return baseMapper.selectList(queryWrapper);
    }

    public void createPosition(Position position){
        this.save(position);
    }

    public List<Position> getOAPosition(){
        return this.baseMapper.getOAPosition();
    }

    public void deleteAll(){
        this.baseMapper.deleteAll();
    }
}
