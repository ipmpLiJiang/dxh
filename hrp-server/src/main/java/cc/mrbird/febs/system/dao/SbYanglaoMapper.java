package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.SbReport;
import cc.mrbird.febs.system.domain.SbYanglao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface SbYanglaoMapper extends BaseMapper<SbYanglao> {
    SbReport getReport(Date thismonth, Date lastmonth);
    IPage<SbYanglao> getHistoryList(Page page,@Param("sbReport")SbReport sbReport);
    List<SbYanglao> getHistoryList(@Param("sbReport")SbReport sbReport);
    void deleteByDate(Date shDate);

    List<SbYanglao> getXzByInidnumberAndShDateList(Date bshdate,Date ushdate);

    List<SbYanglao> getJsByInidnumberAndShDateList(Date bshdate,Date ushdate);

    List<SbYanglao> getBjByShDateList(Date bshdate);

    List<SbYanglao> getYanglaoByIdNumberNotEmolumentList(Date shdate,Integer jftype);
}
