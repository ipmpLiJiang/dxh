package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.AdministrativeLevel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IAdministrativeLevelService extends IService<AdministrativeLevel> {
    List<AdministrativeLevel> findAdministrativeLevel(Integer employeeId);

    void createAdministrativeLevel(AdministrativeLevel administrativeLevel);

    void updateAdministrativeLevel(AdministrativeLevel administrativeLevel);

    void deleteAdministrativeLevels(String[] administrativeLevelIds);
}
