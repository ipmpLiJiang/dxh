package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.Emolument;
import cc.mrbird.febs.system.domain.KqHz;
import cc.mrbird.febs.system.domain.KqJk;
import cc.mrbird.febs.system.domain.KqPeriod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author MrBird
 */
public interface IKqPeriodService extends IService<KqPeriod> {
    /**
     * 获取最新的期间
     */
    KqPeriod getLatestPeriod();
    /**
     * 开启期间
     */
    KqPeriod openPeriod(QueryRequest request, KqPeriod period);

    void updatePeriod(KqPeriod period);

    void createPeriod(KqPeriod period);
}
