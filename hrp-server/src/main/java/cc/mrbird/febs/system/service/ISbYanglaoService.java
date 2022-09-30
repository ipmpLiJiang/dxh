package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.SbReport;
import cc.mrbird.febs.system.domain.SbYanglao;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface ISbYanglaoService extends IService<SbYanglao> {
    SbReport getReport(Date thismonth);
    List<SbYanglao> getJnList(Date thismonth,Boolean bjFlag,String type);
    IPage<SbYanglao> getHistoryList(QueryRequest request, SbReport sbReport);
    List<SbYanglao> getHistoryList(SbReport sbReport);
    void deleteByDate(Date shDate);

    List<SbYanglao> getXzByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId);

    List<SbYanglao> getJsByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId);

    List<SbYanglao> getBjByShDateLists(Date bshdate);

    List<SbYanglao> getYanglaoByIdNumberNotEmolumentLists(Date shdate,Integer jftype);
}
