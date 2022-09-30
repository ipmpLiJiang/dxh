package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.Education;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IEducationService extends IService<Education> {
    List<Education> findEducation(Integer employeeId);

    void createEducation(Education education);

    void updateEducation(Education education);

    void deleteEducations(String[] educationIds);

    void deleteByEmployeeids(List<Integer> employeeids);

    List<Education> getOAEducation();

    void updateOAEducation();
}
