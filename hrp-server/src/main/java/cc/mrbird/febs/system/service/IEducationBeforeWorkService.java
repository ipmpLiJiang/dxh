package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.EducationBeforeWork;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IEducationBeforeWorkService extends IService<EducationBeforeWork> {
    List<EducationBeforeWork> findEducationBeforeWork(Integer employeeId);

    void createEducationBeforeWork(EducationBeforeWork educationBeforeWork);

    void updateEducationBeforeWork(EducationBeforeWork educationBeforeWork);

    void deleteEducationBeforeWorks(String[] educationBeforeWorkIds);
}
