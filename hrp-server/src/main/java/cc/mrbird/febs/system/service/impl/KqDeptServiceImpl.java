package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.domain.KqDept;
import cc.mrbird.febs.system.dao.KqDeptMapper;
import cc.mrbird.febs.system.service.IKqDeptService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MrBird
 */
@Service
public class KqDeptServiceImpl extends ServiceImpl<KqDeptMapper, KqDept> implements IKqDeptService {
    @Override
    public List<KqDept> findKqDepartmentList(){
        LambdaQueryWrapper<KqDept> queryWrapper = new LambdaQueryWrapper<>();
        return this.baseMapper.selectList(queryWrapper);
    }

    public String getName(String deptId){
        List<KqDept> kqDeptList=this.findKqDepartmentList();
        String name="";
        for(KqDept kqDept:kqDeptList){
            if(kqDept.getKeyy().equals(deptId)){
                name=kqDept.getValuee();
                break;
            }
        }
        return name;
    }
}
