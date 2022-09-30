package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.SbReport;
import cc.mrbird.febs.system.domain.SbSy;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface ISbSyService extends IService<SbSy> {
    SbReport getReport(Date thismonth);
    List<SbSy> getJnList(Date thismonth,Boolean bjFlag,String type);
    IPage<SbSy> getHistoryList(QueryRequest request, SbReport sbReport);
    List<SbSy> getHistoryList(SbReport sbReport);
    void deleteByDate(Date shDate);

    List<SbSy> getXzByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId);

    List<SbSy> getJsByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId);

    List<SbSy> getBjByShDateLists(Date bshdate);

    List<SbSy> getSyByIdNumberNotEmolumentLists(Date shdate,Integer jftype);
}
