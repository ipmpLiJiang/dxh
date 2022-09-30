package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.SbReport;
import cc.mrbird.febs.system.domain.SbShengyu;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface ISbShengyuService extends IService<SbShengyu> {
    SbReport getReport(Date thismonth);
    List<SbShengyu> getJnList(Date thismonth,Boolean bjFlag,String type);
    IPage<SbShengyu> getHistoryList(QueryRequest request, SbReport sbReport);
    List<SbShengyu> getHistoryList(SbReport sbReport);
    void deleteByDate(Date shDate);

    List<SbShengyu> getXzByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId);

    List<SbShengyu> getJsByInidnumberAndShDateLists(Date bshdate,Date ushdate,Boolean isId);

    List<SbShengyu> getBjByShDateLists(Date bshdate);
}
