package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.JobDetail;
import cc.mrbird.febs.system.dao.JobDetailMapper;
import cc.mrbird.febs.system.service.IJobDetailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MrBird
 */
@Service
public class JobDetailServiceImpl extends ServiceImpl<JobDetailMapper, JobDetail> implements IJobDetailService {
    public List<JobDetail> findJobDetail(){
        LambdaQueryWrapper<JobDetail> queryWrapper = new LambdaQueryWrapper<>();
        return baseMapper.selectList(queryWrapper);
    }

    public void createJobDetail(JobDetail jobTypeDetail){
        this.save(jobTypeDetail);
    }
    public List<JobDetail> getOAJobDetail(){
        return this.baseMapper.getOAJobDetail();
    }

    public void deleteAll(){
        this.baseMapper.deleteAll();
    }
}
