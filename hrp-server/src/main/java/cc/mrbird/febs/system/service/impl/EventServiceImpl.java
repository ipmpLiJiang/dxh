package cc.mrbird.febs.system.service.impl;


import cc.mrbird.febs.system.domain.Event;
import cc.mrbird.febs.system.dao.EventMapper;

import cc.mrbird.febs.system.domain.Ward;
import cc.mrbird.febs.system.service.IEventService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MrBird
 */
@Service
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements IEventService {
    public IPage<Event> getEventReport(Page page, Event event){
        return baseMapper.findEventReport(page,event);
    }

    public Event getEvent(String coreid){
        LambdaQueryWrapper<Event> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Event::getCoreid,coreid);
        return baseMapper.selectOne(queryWrapper);
    }
    public List<Event> getEventReport(Event event){
        return baseMapper.findEventReport(event);
    }
}
