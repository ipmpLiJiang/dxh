package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.Family;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IFamilyService extends IService<Family> {
    List<Family> findFamily(Integer employeeId);
    void createFamily(Family family);

    void updateFamily(Family family);

    void deleteFamily(String[] familyIds);

    List<Family> getOAFamily();

    void updateOAFamily();

    void deleteByEmployeeids(List<Integer> employeeids);


}
