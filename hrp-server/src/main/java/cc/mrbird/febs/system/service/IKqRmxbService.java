package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.AttandanceUser;
import cc.mrbird.febs.system.domain.Employee;
import cc.mrbird.febs.system.domain.KqJk;
import cc.mrbird.febs.system.domain.KqRmxb;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
public interface IKqRmxbService extends IService<KqRmxb> {
    Map<String,Object> getByStartDate(String pbDept, Date startDate, String username,String rszfws);
    Map<String,Object> getPbByMonth(KqRmxb kqRmxb);
    IPage<KqRmxb> getRmxList(KqRmxb kqRmxb, QueryRequest request);

    IPage<KqRmxb> getPbReport(KqRmxb kqRmxb, QueryRequest request);
    List<KqRmxb> getPbReportExport(KqRmxb kqRmxb);
    /**
    * 创建排班月明细
    */
    List<KqJk> createKqJkByMonth(KqRmxb kqRmxb);

    /**
     * 获取考勤的用户
     * @param month
     * @return
     */
    List<AttandanceUser> getAllPbByMOnth(String month);

    /**
     * 周考勤数据
     * @param startDate
     * @param endDate
     * @return
     */
    List<AttandanceUser> getAllPbByWeek(String startDate, String endDate);

    List<KqRmxb> getPbByStartAndEndDate(Date startDate,Date endDate,String code);
    /**
     * 生成月明细后更新日明细表的状态和关联id
     */
    void batchUpdateSqbInfo(KqRmxb kqRmxb);

    void batchUpdate(List<KqRmxb> kqRmxbList,String pbDept);

    KqRmxb createRmx(Employee employee,Date date,String username,String pbDept,Integer scheduling);
    /**
     * @author 按周更新排班数据
     */
    void updateByWeek(String pbdept,String username,List<Employee>employeeList);

    void batchSetschedul(KqRmxb kqRmxb);

    void updateBySqbId(KqRmxb kqRmxb);
    void updateKqRmxb(KqRmxb kqRmxb);

}
