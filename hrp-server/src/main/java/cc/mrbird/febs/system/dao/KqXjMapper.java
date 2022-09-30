package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.system.domain.KqXj;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MrBird
 */
public interface KqXjMapper extends BaseMapper<KqXj> {
    @DS(FebsConstant.HR_TO_OA_DATA_SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<KqXj> getOAXj();

    @DS(FebsConstant.HR_TO_OA_DATA_SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void updateOAXj();
}
