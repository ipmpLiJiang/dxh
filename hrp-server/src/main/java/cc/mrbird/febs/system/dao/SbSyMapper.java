package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.SbReport;
import cc.mrbird.febs.system.domain.SbSy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface SbSyMapper extends BaseMapper<SbSy> {
    SbReport getReport(Date thismonth, Date lastmonth);
    IPage<SbSy> getHistoryList(Page page,@Param("sbReport")SbReport sbReport);
    List<SbSy> getHistoryList(@Param("sbReport")SbReport sbReport);
    void deleteByDate(Date shDate);

    List<SbSy> getXzByInidnumberAndShDateList(Date bshdate,Date ushdate);

    List<SbSy> getJsByInidnumberAndShDateList(Date bshdate,Date ushdate);

    List<SbSy> getBjByShDateList(Date bshdate);

    List<SbSy> getSyByIdNumberNotEmolumentList(Date shdate,Integer jftype);
}
