package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.Ward;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IWardService extends IService<Ward> {
    List<Ward> findWard(Integer deptId);
    String getName(String wardId);
}
