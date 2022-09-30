package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.RsfwDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IRsfwDetailService extends IService<RsfwDetail> {
    List<RsfwDetail> findRsfwDetail();
    String getName(List<RsfwDetail>rsfwDetailList,String rsfw);
}
