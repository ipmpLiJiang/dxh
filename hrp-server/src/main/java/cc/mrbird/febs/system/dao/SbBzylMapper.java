package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.SbBzyl;
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
public interface SbBzylMapper extends BaseMapper<SbBzyl> {
    SbReport getReport(Date thismonth, Date lastmonth);
    IPage<SbBzyl> getHistoryList(Page page,@Param("sbReport")SbReport sbReport);
    List<SbBzyl> getHistoryList(@Param("sbReport")SbReport sbReport);
    void deleteByDate(Date shDate);

    List<SbBzyl> getXzByInidnumberAndShDateList(Date bshdate,Date ushdate);

    List<SbBzyl> getJsByInidnumberAndShDateList(Date bshdate,Date ushdate);

    List<SbBzyl> getBjByShDateList(Date bshdate);
}
