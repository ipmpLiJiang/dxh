package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.SbReport;
import cc.mrbird.febs.system.domain.SbYl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface SbYlMapper extends BaseMapper<SbYl> {
    SbReport getReport(Date thismonth,Date lastmonth);
    IPage<SbYl> getHistoryList(Page page,@Param("sbReport")SbReport sbReport);
    List<SbYl> getHistoryList(@Param("sbReport")SbReport sbReport);
    void deleteByDate(Date shDate);

    List<SbYl> getXzByInidnumberAndShDateList(Date bshdate,Date ushdate);

    List<SbYl> getJsByInidnumberAndShDateList(Date bshdate,Date ushdate);

    List<SbYl> getBjByShDateList(Date bshdate);

    List<SbYl> getYlByIdNumberNotEmolumentList(Date shdate,Integer jftype);
}
