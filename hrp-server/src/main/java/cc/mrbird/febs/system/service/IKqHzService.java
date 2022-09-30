package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.KkList;
import cc.mrbird.febs.system.domain.KqHz;
import cc.mrbird.febs.system.domain.KqJk;
import cc.mrbird.febs.system.domain.KqPeriod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface IKqHzService extends IService<KqHz> {
    void jkToHzList(List<KqJk> kqJks, Date period) throws IllegalAccessException;
    IPage<KkList> getKkList(QueryRequest request, KqHz kqHz);
    List<KkList> getKkList(KqHz kqHz);
    IPage<KqHz> getHzReport(QueryRequest request, KqHz kqHz);
    List<KqHz> getHzReport(KqHz kqHz);
    KqHz getSum(KqHz kqHz);
    KqHz getHzSum(KqHz kqHz);
    void updateKqHz(KqHz kqHz);

    void passKqHz(KqPeriod kqPeriod);
    /**
     * 根据身份证号和期间获取考勤汇总信息
     */
    KqHz getByIdnumberAndPeriod(String idnumber,Date period);
}
