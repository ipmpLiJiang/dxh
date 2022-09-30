package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.Checkinfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface ICheckService extends IService<Checkinfo> {
    List<Checkinfo> findCheck(Integer employeeId);
    void createCheck(Checkinfo check);

    void updateCheck(Checkinfo check);

    void deleteChecks(String[] checkIds);

    IPage<Checkinfo> findCheckinfoReport(Page page, Checkinfo checkinfo);

    List<Checkinfo> findCheckinfoReport2(String deptids,
                                        Date inputdate,
                                        Date startdate,
                                        Date enddate,
                                         Integer employeeid,
                                        String employeename,
                                         Long result);
}
