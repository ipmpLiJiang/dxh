package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.AdministrativePost;
import cc.mrbird.febs.system.dao.AdministrativePostMapper;
import cc.mrbird.febs.system.service.IAdministrativePostService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author MrBird
 */
@Service
public class AdministrativePostServiceImpl extends ServiceImpl<AdministrativePostMapper, AdministrativePost> implements IAdministrativePostService {
    public List<AdministrativePost> findadministrativePost(Integer employeeId){
        LambdaQueryWrapper<AdministrativePost> queryWrapper = new LambdaQueryWrapper<>();
        if (employeeId!=null) {
            queryWrapper.eq(AdministrativePost::getEmployeeid, employeeId);
            return baseMapper.selectList(queryWrapper);
        }else{
            return new ArrayList<>();
        }
    }

    public void createAdministrativePost(AdministrativePost administrativePost){
        this.save(administrativePost);
    }

    public void updateAdministrativePost(AdministrativePost administrativePost){
        updateById(administrativePost);
    }

    public void deleteAdministrativePosts(String[] administrativePostIds){
        baseMapper.deleteBatchIds(Arrays.asList(administrativePostIds));
    }
}
