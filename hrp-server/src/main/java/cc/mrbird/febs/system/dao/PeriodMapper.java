package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.Period;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author MrBird
 */
public interface PeriodMapper extends BaseMapper<Period> {
    Period getLatestPeriod();
}
