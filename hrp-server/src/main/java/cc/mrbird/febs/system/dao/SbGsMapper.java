package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.SbGs;
import cc.mrbird.febs.system.domain.SbReport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface SbGsMapper extends BaseMapper<SbGs> {
    SbReport getReport(Date thismonth, Date lastmonth);
    IPage<SbGs> getHistoryList(Page page,@Param("sbReport")SbReport sbReport);
    List<SbGs> getHistoryList(@Param("sbReport")SbReport sbReport);
    void deleteByDate(Date shDate);

    List<SbGs> getXzByInidnumberAndShDateList(Date bshdate,Date ushdate);

    List<SbGs> getJsByInidnumberAndShDateList(Date bshdate,Date ushdate);

    List<SbGs> getBjByShDateList(Date bshdate);
}
