package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.JobType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * @author MrBird
 */
public interface JobTypeMapper extends BaseMapper<JobType> {
    List<JobType> findJobTypeByStartEndDateAndJobType(@Param("jobType")JobType jobType);
}
