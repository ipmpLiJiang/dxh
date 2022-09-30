package cc.mrbird.febs.ylj.service;

import cc.mrbird.febs.system.domain.SbReport;
import cc.mrbird.febs.ylj.entity.YljBRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author viki
 * @since 2021-11-25
 */
public interface IYljBRecordService extends IService<YljBRecord> {

    IPage<YljBRecord> findYljBRecords(QueryRequest request, YljBRecord yljBRecord);

    IPage<YljBRecord> findYljBRecordList(QueryRequest request, YljBRecord yljBRecord);

    void createYljBRecord(YljBRecord yljBRecord);

    void updateYljBRecord(YljBRecord yljBRecord);

    void deleteYljBRecords(String[] Ids);

    List<YljBRecord> getAll(String userAccount, String dcaYear);

    void insertYljBRecord(List<YljBRecord> insertList);

    SbReport getReportYls(Date bkssq, Date ukssq);

    SbReport getReportNjs(Date bkssq, Date ukssq);

    List<YljBRecord> getXzByIdCardAndFkssqLists(Date bkssq, Date ukssq,boolean isId);

    List<YljBRecord> getJsByIdCardAndFkssqLists(Date bkssq, Date ukssq,boolean isId);

    List<YljBRecord> getBjByFkssqLists(Date bkssq);

    List<YljBRecord>  getRecordByIdCardNotEmolumentLists(Date bkssq,String jflx);
}
