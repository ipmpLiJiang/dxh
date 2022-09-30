package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.Employee;
import cc.mrbird.febs.system.domain.Tsxmmx;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface ITsxmmxService extends IService<Tsxmmx> {
    IPage<Tsxmmx> findGzxms(Tsxmmx tsxmmx, QueryRequest request);
    void createGzxm(Tsxmmx tsxmmx);

    void updateGzxm(Tsxmmx tsxmmx);

    void deleteGzxm(String[] gzxmIds);

    void batchCreateGzxm(List<Tsxmmx> tsxmmxList);

    Employee findEmployeeByCode(String code);

    Float getTotalData(Tsxmmx tsxmmx);
}
