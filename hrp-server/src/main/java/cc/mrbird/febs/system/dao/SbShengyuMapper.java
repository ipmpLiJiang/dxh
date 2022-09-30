package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.SbReport;
import cc.mrbird.febs.system.domain.SbShengyu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface SbShengyuMapper extends BaseMapper<SbShengyu> {
    SbReport getReport(Date thismonth, Date lastmonth);
    IPage<SbShengyu> getHistoryList(Page page,@Param("sbReport")SbReport sbReport);
    List<SbShengyu> getHistoryList(@Param("sbReport")SbReport sbReport);
    void deleteByDate(Date shDate);

    List<SbShengyu> getXzByInidnumberAndShDateList(Date bshdate,Date ushdate);

    List<SbShengyu> getJsByInidnumberAndShDateList(Date bshdate,Date ushdate);

    List<SbShengyu> getBjByShDateList(Date bshdate);
}
