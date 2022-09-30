package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.AdministrativeLevel;
import cc.mrbird.febs.system.domain.AdministrativePost;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IAdministrativePostService extends IService<AdministrativePost> {
    List<AdministrativePost> findadministrativePost(Integer employeeId);

    void createAdministrativePost(AdministrativePost administrativePost);

    void updateAdministrativePost(AdministrativePost administrativePost);

    void deleteAdministrativePosts(String[] administrativePostIds);
}
