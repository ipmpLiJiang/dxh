package cc.mrbird.febs.ylj.dao;

import cc.mrbird.febs.system.domain.SbReport;
import cc.mrbird.febs.ylj.entity.YljBRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author viki
 * @since 2021-11-25
 */
public interface YljBRecordMapper extends BaseMapper<YljBRecord> {
    void updateYljBRecord(YljBRecord yljBRecord);

    IPage<YljBRecord> findYljBRecord(Page page, @Param("yljBRecord") YljBRecord yljBRecord);

    SbReport getReportYl(Date bkssq, Date ukssq);

    SbReport getReportNj(Date bkssq, Date ukssq);

    List<YljBRecord> getXzByIdCardAndFkssqList(Date bkssq, Date ukssq);

    List<YljBRecord> getJsByIdCardAndFkssqList(Date bkssq, Date ukssq);

    List<YljBRecord> getBjByFkssqList(Date bkssq);

    List<YljBRecord>  getRecordByIdCardNotEmolumentList(Date fkssq,String jflx);
}
