package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.ComFile;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IComFileService extends IService<ComFile> {
    List<ComFile> findComFile(String id);
    void createComFile(ComFile comFile);

    void updateComFile(ComFile comFile);

    void deleteComFiles(String[] ids);

    void updateComFileByInId(List<String> idList,String refId);
}
