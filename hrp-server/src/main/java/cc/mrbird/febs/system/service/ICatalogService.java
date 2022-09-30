package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.Catalog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface ICatalogService extends IService<Catalog> {
    List<Catalog> findCatalog(Integer employeeId);

    List<Catalog> findRootCatalog();

    void createCatalog(List<Catalog> catalogList,String employeeid);

    void updateCatalog(Catalog catalog);

    void deleteCatalog(String catalogId);

}
