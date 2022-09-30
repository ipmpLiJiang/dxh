package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.SbDeyl;
import cc.mrbird.febs.system.domain.SbReport;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface ISbDeylService extends IService<SbDeyl> {
    SbReport getReport(Date thismonth);
    List<SbDeyl> getJnList(Date thismonth,Boolean bjFlag,String type);
    IPage<SbDeyl> getHistoryList(QueryRequest request, SbReport sbReport);
    List<SbDeyl> getHistoryList( SbReport sbReport);
    void deleteByDate(Date shDate);

    List<SbDeyl> getXzByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId);

    List<SbDeyl> getJsByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId);

    List<SbDeyl> getBjByShDateLists(Date bshdate);

    List<SbDeyl> getDeylByIdNumberNotEmolumentLists(Date shdate,Integer jftype);
}
