package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.Honor;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IHonorService extends IService<Honor> {
    List<Honor> findHonor(Integer employeeId);
    void createHonor(Honor honor);

    void updateHonor(Honor honor);

    void deleteHonor(String[] honorids);
}
