package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.KqJk;
import cc.mrbird.febs.system.domain.KqRmxb;
import cc.mrbird.febs.system.domain.KqSqb;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IKqSqbService extends IService<KqSqb> {
    IPage<KqSqb> findKqSqbs(QueryRequest request,KqSqb kqSqb);
    List<KqSqb> findKqSqbs(KqSqb kqSqb);
    KqSqb findByPbDeptAndPbDate(KqRmxb kqRmxb);
    void createSqb(KqSqb kqSqb);
    void updateSqb(KqSqb kqSqb);
    void shSqb(KqSqb kqSqb);
}
