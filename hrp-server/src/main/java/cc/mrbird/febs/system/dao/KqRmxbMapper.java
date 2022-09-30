package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.AttandanceUser;
import cc.mrbird.febs.system.domain.KqRmxb;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface KqRmxbMapper extends BaseMapper<KqRmxb> {
    List<KqRmxb> getByStartDate(String pbDept, Date startDate,Date endDate);
    List<KqRmxb> getPbByStartAndEndDate(Date startDate,Date endDate,String code);

    /**
     * 取出一个月的所有考勤数据的用户
     * @param date
     * @return
     */
    @Select("select employeeid,CONCAT('Z',DATE_FORMAT(pbdate,'%d')) employeecode,  kq_pb_detail.valuee ban from kq_rmxb inner join kq_pb_detail on kq_rmxb.scheduling=kq_pb_detail.keyy where DATE_FORMAT(pbdate,'%Y-%m') =#{date} ")
    List<AttandanceUser> getAllPbByMOnth(@Param("date") String date);

    @Select("select employeeid,CONCAT('Z',DATE_FORMAT(pbdate,'%w')) employeecode ,kq_pb_detail.valuee ban from kq_rmxb inner join kq_pb_detail on kq_rmxb.scheduling=kq_pb_detail.keyy where STR_TO_DATE(pbdate,'%Y-%m-%d')>=STR_TO_DATE(#{startdate},'%Y-%m-%d') and STR_TO_DATE(pbdate,'%Y-%m-%d')<= STR_TO_DATE(#{endDate},'%Y-%m-%d') ")
    List<AttandanceUser> getAllPbByWeek(@Param("startdate") String startdate,@Param("endDate") String endDate);

    IPage<KqRmxb> getRmxList(Page page, @Param("kqRmxb")KqRmxb kqRmxb);
    IPage<KqRmxb> getPbReport(Page page, @Param("kqRmxb")KqRmxb kqRmxb);
    IPage<KqRmxb> getPbReportHan(Page page, @Param("kqRmxb")KqRmxb kqRmxb);

    List<KqRmxb> getPbReportHanExport(@Param("kqRmxb")KqRmxb kqRmxb);
}
