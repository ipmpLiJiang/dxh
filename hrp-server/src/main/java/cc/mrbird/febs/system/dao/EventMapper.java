package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.Employee;
import cc.mrbird.febs.system.domain.Event;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author MrBird
 */
public interface EventMapper extends BaseMapper<Event> {
    IPage<Event> findEventReport(Page page, @Param("event")Event event);

    List<Event> findEventReport(@Param("event")Event event);
}
