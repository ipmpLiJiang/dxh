package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.KqXj;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IKqXjService extends IService<KqXj> {
    List<KqXj> getOAXj();
    void updateOAXj();
}
