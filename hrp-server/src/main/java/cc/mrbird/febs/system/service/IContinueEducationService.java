package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.ContinueEducation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IContinueEducationService extends IService<ContinueEducation> {
    List<ContinueEducation> findContinueEducation(Integer employeeId);
    void createContinueEducation(ContinueEducation continueEducation);
    void updateContinueEducation(ContinueEducation continueEducation);
    void deleteContinueEducations(String[] ContinueEducationIds);
}
