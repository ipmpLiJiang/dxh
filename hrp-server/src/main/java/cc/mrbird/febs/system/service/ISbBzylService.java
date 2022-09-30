package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.SbBzyl;
import cc.mrbird.febs.system.domain.SbReport;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
@Component
public interface ISbBzylService extends IService<SbBzyl> {
    SbReport getReport(Date thismonth);
    List<SbBzyl> getJnList(Date thismonth,Boolean bjFlag,String type);
    IPage<SbBzyl> getHistoryList(QueryRequest request, SbReport sbReport);
    List<SbBzyl> getHistoryList(SbReport sbReport);
    void deleteByDate(Date shDate);

    List<SbBzyl> getXzByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId);

    List<SbBzyl> getJsByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId);

    List<SbBzyl> getBjByShDateLists(Date bshdate);

}
