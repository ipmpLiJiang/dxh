package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.system.domain.Cwgz;
import cc.mrbird.febs.system.domain.Cwyd;
import cc.mrbird.febs.system.domain.Emolument;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface CwgzMapper extends BaseMapper<Cwgz> {
    List<Cwgz> getGz(Date period,Date lastPeriod);

    List<Cwgz> getSb(Date period,Date lastPeriod);

    List<Cwgz> getSbSum(Date period);

    @DS(FebsConstant.CW_DATA_SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void saveToCW(@Param("cwgz")Cwgz cwgz);

    @DS(FebsConstant.CW_DATA_SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void createCwyd(@Param("cwyd") Cwyd cwyd);

    @DS(FebsConstant.CW_DATA_SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<Emolument> getCwKj(@Param("year")int year,@Param("month")int month);

}
