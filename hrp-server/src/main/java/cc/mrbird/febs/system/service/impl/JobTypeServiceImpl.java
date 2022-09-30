package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.system.domain.JobType;
import cc.mrbird.febs.system.dao.JobTypeMapper;
import cc.mrbird.febs.system.service.IComFileService;
import cc.mrbird.febs.system.service.IJobTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Service
public class JobTypeServiceImpl extends ServiceImpl<JobTypeMapper, JobType> implements IJobTypeService {

    @Autowired
    IComFileService comFileService;

    public List<JobType> findJobType(Integer employeeId, Integer jobType){
        LambdaQueryWrapper<JobType> queryWrapper = new LambdaQueryWrapper<>();
        if (employeeId!=null) {
            queryWrapper.eq(JobType::getEmployeeid, employeeId);
        }
        if(jobType!=null){
            queryWrapper.eq(JobType::getJobtype, jobType);
        }
        return baseMapper.selectList(queryWrapper).stream().sorted(Comparator.comparing(JobType::getJobtype).reversed().thenComparing(JobType::getEnddate).reversed()).collect(Collectors.toList());
    }
    @Transactional
    public void createJobType(JobType jobType){
        List<JobType>jobTypeList=findJobType(jobType.getEmployeeid(),jobType.getJobtype()).stream().sorted(Comparator.comparing(JobType::getEnddate).reversed()).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(jobTypeList)){
            updateJobType(jobTypeList.get(0).setEnddate(DateUtil.getYesterday(jobType.getStartdate())));
        }
        this.save(jobType);

        List<String> idList = new ArrayList<>();
        if(StringUtils.isNotBlank(jobType.getZsfileid())) {
            idList.add(jobType.getZsfileid());
        }
        if(idList.size() > 0) {
            comFileService.updateComFileByInId(idList,jobType.getId());
        }
    }

    public void updateJobType(JobType jobType){
        updateById(jobType);
    }

    public void deleteJobType(String[] jobTypeIds){
        baseMapper.deleteBatchIds(Arrays.asList(jobTypeIds));
    }

    public List<JobType> findJobTypeByStartEndDateAndJobType(JobType jobType) {
        return baseMapper.findJobTypeByStartEndDateAndJobType(jobType);
    }
}
