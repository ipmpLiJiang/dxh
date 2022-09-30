package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.Education;
import cc.mrbird.febs.system.domain.WorkExperience;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IWorkExperienceService extends IService<WorkExperience> {
    List<WorkExperience> findWorkExperience(Integer employeeId);

    void createWorkExperience(WorkExperience workExperience);

    void updateWorkExperience(WorkExperience workExperience);

    void deleteWorkExperience(String[] workExperienceIds);



}
