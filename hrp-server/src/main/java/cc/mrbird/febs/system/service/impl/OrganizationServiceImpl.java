package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.Organization;
import cc.mrbird.febs.system.dao.OrganizationMapper;
import cc.mrbird.febs.system.domain.Test;
import cc.mrbird.febs.system.service.IOrganizationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MrBird
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {
    @Override
    public List<Organization> findOrganizations() {
        try {
            return baseMapper.selectList(new QueryWrapper<Organization>());
        } catch (Exception e) {
            log.error("获取信息失败", e);
            return new ArrayList<>();
        }
    }
}
