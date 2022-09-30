package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.KqPeriod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author MrBird
 */
public interface KqPeriodMapper extends BaseMapper<KqPeriod> {
    KqPeriod getLatestPeriod();
}
