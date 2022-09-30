package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.domain.KqDept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IKqDeptService extends IService<KqDept> {
    List<KqDept> findKqDepartmentList();
    String getName(String deptId);
}
