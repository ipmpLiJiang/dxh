package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.GqTitle;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IGqTitleService extends IService<GqTitle> {
    List<GqTitle> findGqTitle(Integer employeeId);

    void createGqTitle(GqTitle gqTitle);

    void updateGqTitle(GqTitle gqTitle);

    void deleteGqTitle(String[] titleIds);

    List<GqTitle> getOAGqTitle();

    void deleteByEmployeeids(List<Integer> employeeids);

    void updateOAGqTitle();
}
