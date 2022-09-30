package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.KkList;
import cc.mrbird.febs.system.domain.KqHz;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface KqHzMapper extends BaseMapper<KqHz> {
    IPage<KkList> getKkJkList(Page page, @Param("kqhz")KqHz kqHz);
    List<KkList> getKkJkList( @Param("kqhz")KqHz kqHz);
    void deleteByKqdate(Date kqdate);
    IPage<KqHz>getHzReport(Page page, @Param("kqhz")KqHz kqHz);
    List<KqHz> getHzReport(@Param("kqhz")KqHz kqHz);
    void setKK(Date kqdate,int day);
    KqHz getSum(@Param("kqhz")KqHz kqHz);
    KqHz getHzSum(@Param("kqhz")KqHz kqHz);
}
