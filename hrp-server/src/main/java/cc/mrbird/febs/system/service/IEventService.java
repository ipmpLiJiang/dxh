package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.Event;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IEventService extends IService<Event> {
     IPage<Event> getEventReport(Page page, Event event);

     Event getEvent(String coreid);

     List<Event> getEventReport(Event event);
}
