package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.Emolument;
import cc.mrbird.febs.system.domain.Period;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IPeriodService extends IService<Period> {
    /**
     * 获取最新的期间
     */
    Period getLatestPeriod();
    /**
     * 开启期间
     */
    Period openPeriod(QueryRequest request,Period period);

    void updatePeriod(Period period);

    void createPeriod(Period period);


}
