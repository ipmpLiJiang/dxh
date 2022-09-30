package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.SbGs;
import cc.mrbird.febs.system.domain.SbReport;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface ISbGsService extends IService<SbGs> {
    SbReport getReport(Date thismonth);
    List<SbGs> getJnList(Date thismonth,Boolean bjFlag,String type);
    IPage<SbGs> getHistoryList(QueryRequest request, SbReport sbReport);
    List<SbGs> getHistoryList(SbReport sbReport);
    void deleteByDate(Date shDate);

    List<SbGs> getXzByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId);

    List<SbGs> getJsByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId);

    List<SbGs> getBjByShDateLists(Date bshdate);
}
