package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.JobType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IJobTypeService extends IService<JobType> {
    List<JobType> findJobType(Integer employeeId, Integer jobType);
    void createJobType(JobType jobType);

    void updateJobType(JobType jobType);

    void deleteJobType(String[] jobTypeIds);

    List<JobType> findJobTypeByStartEndDateAndJobType(JobType jobType);
}
