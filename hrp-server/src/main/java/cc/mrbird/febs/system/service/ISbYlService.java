package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.SbReport;
import cc.mrbird.febs.system.domain.SbYl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface ISbYlService extends IService<SbYl> {
    SbReport getReport(Date thismonth);
    List<SbYl> getJnList(Date thismonth,Boolean bjFlag,String type);
    IPage<SbYl> getHistoryList(QueryRequest request, SbReport sbReport);
    List<SbYl> getHistoryList(SbReport sbReport);
    void deleteByDate(Date shDate);

    List<SbYl> getXzByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId);

    List<SbYl> getJsByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId);

    List<SbYl> getBjByShDateLists(Date bshdate);

    List<SbYl> getYlByIdNumberNotEmolumentLists(Date shdate,Integer jftype);
}
