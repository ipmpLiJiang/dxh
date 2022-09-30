package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.KqJk;
import cc.mrbird.febs.system.domain.KqPbDetail;
import cc.mrbird.febs.system.domain.KqRmxb;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IKqPbDetailService extends IService<KqPbDetail> {
    List<KqPbDetail> getPbList(String parentValue,Integer flag);
    List<KqRmxb> getPbName(List<KqRmxb> kqRmxbList);
    List<KqJk> getPbNameByKqjk(List<KqJk> kqJkList) throws IllegalAccessException;
}
