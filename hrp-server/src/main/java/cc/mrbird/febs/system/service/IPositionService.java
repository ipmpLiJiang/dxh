package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.Position;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IPositionService extends IService<Position> {
    List<Position> findPositionDetail();
    void createPosition(Position position);
    List<Position> getOAPosition();
    void deleteAll();
}
