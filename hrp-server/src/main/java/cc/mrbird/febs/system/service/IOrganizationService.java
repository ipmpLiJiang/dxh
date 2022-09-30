package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.Organization;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IOrganizationService extends IService<Organization> {
    List<Organization> findOrganizations();
}
