package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.system.domain.Department;
import cc.mrbird.febs.system.dao.DepartmentMapper;
import cc.mrbird.febs.system.domain.Ward;
import cc.mrbird.febs.system.service.IDepartmentService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MrBird
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {
    @Override
    public List<Department> findDepartmentList(Department department) {
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        if(department.getParentid()!=null){
            queryWrapper.eq(Department::getParentid, department.getParentid());
        }
        if(department.getKeyy()!=null){
            queryWrapper.eq(Department::getKeyy, department.getKeyy());
        }
        return baseMapper.selectList(queryWrapper);
    }

    public Department findDepartmentByKey(String keyy){
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Department::getKeyy,keyy);
        return baseMapper.selectOne(queryWrapper);
    }


    public   Department findDepartmentByValue(String value){
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Department::getValuee,value);
        return baseMapper.selectOne(queryWrapper);
    }


    public String getName(List<Department>departmentList,String deptId){
        String name="";
        for(Department department:departmentList){
            if(department.getKeyy().equals(deptId)){
                name=department.getValuee();
                break;
            }
        }
        return name;
    }
    public void createDepartment(Department department){
        this.save(department);
    }

    public void createDepartmentToCw(Department department){
        this.baseMapper.deleteDepartment();
        if(department.getCode().length()==FebsConstant.KESHI_LENGTH){
            department.setDj(FebsConstant.DEPARTMENT_DJ_BINGQU);
        }else if(department.getCode().length()==FebsConstant.BINGQU_LENGTH){
            department.setDj(FebsConstant.DEPARTMENT_DJ_KESHI);
        }else{
            department.setDj(FebsConstant.DEPARTMENT_DJ_KESHILEIXING);
        }
        this.baseMapper.createDepartmentToCw(department);
    }

    public List<Department> getOADepartment(){
        return this.baseMapper.getOADept();
    }

    public void deleteAll(){
        this.baseMapper.deleteAll();
    }

}
