package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.system.domain.JobDetail;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MrBird
 */
public interface JobDetailMapper extends BaseMapper<JobDetail> {
    @DS(FebsConstant.OA_TO_HR_DATA_SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<JobDetail> getOAJobDetail();

    void deleteAll();
}
