package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.SbDeyl;
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
public interface SbDeylMapper extends BaseMapper<SbDeyl> {
    SbReport getReport(Date thismonth, Date lastmonth);
    IPage<SbDeyl> getHistoryList(Page page,@Param("sbReport")SbReport sbReport);
    List<SbDeyl> getHistoryList(@Param("sbReport")SbReport sbReport);
    void deleteByDate(Date shDate);

    List<SbDeyl> getXzByInidnumberAndShDateList(Date bshdate,Date ushdate);

    List<SbDeyl> getJsByInidnumberAndShDateList(Date bshdate,Date ushdate);

    List<SbDeyl> getBjByShDateList(Date bshdate);

    List<SbDeyl> getDeylByIdNumberNotEmolumentList(Date shdate,Integer jftype);
}
