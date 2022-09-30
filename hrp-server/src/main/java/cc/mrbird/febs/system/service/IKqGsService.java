package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.KqGs;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author MrBird
 */
public interface IKqGsService extends IService<KqGs> {
    IPage<KqGs> getGsList(QueryRequest request, KqGs kqGs);
    void createGs(KqGs kqGs);

    void updateGs(KqGs kqGs);

    void deleteGs(String[] gsIds);
}
