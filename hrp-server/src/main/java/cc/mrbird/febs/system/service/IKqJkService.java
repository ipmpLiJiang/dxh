package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.system.domain.KqHz;
import cc.mrbird.febs.system.domain.KqJk;
import cc.mrbird.febs.system.domain.KqPeriod;
import cc.mrbird.febs.system.domain.KqRmxb;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * @author MrBird
 */
public interface IKqJkService extends IService<KqJk> {
    IPage<KqJk> findKqjk(QueryRequest request, KqJk kqJk);
    List<KqJk> findKqjk( KqJk kqJk);
    KqJk findKqjk(Date startDate,String idnumber);
    List<KqJk> getYmxById(String id);
    List<KqJk>findByDeptAndDate(String pbDept,Date startDate);
    IPage<KqJk> findDuplicateList(QueryRequest request, KqJk kqJk);
    void createKqjk(KqJk kqJk);
    void batchCreateKqjk(KqRmxb kqRmxb);
    void updateKqjk(KqJk kqJk);
    void createHzList(QueryRequest request, KqPeriod period) throws IllegalAccessException;
    List<KqJk> getOAKqJk();
    void updateOAKqJk(Date updatetime);
    void deleteKqJk(String[] kqjkids);
    void deleteBySqbId(String sqbId);
}
