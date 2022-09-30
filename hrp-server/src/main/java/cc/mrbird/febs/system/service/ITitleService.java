package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.Title;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface ITitleService extends IService<Title> {
    List<Title> findTitle(Integer employeeId);
    void createTitle(Title title);

    void updateTitle(Title title);

    void deleteTitle(String[] titleIds);


}
