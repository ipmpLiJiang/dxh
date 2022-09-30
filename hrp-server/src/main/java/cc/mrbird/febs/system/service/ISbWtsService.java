package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.SbWts;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface ISbWtsService extends IService<SbWts> {
    List<SbWts> getSbWtsList(SbWts sbWts);
}
