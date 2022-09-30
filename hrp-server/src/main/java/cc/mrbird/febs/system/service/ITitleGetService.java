package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.TitleGet;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface ITitleGetService extends IService<TitleGet> {
    List<TitleGet> findTitleGet(Integer employeeId);
    void createTitleGet(TitleGet title);

    void updateTitleGet(TitleGet title);

    void deleteTitleGet(String[] titleGetIds);

    List<TitleGet> getOATitleGet();

    void updateOATitleGet();

    void deleteByEmployeeids(List<Integer> employeeids);

    List<TitleGet> findTitleGetByMaxStartDate();
}
