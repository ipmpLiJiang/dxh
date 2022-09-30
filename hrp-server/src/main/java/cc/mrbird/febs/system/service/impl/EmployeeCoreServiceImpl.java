package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.system.domain.*;
import cc.mrbird.febs.system.dao.EmployeeCoreMapper;
import cc.mrbird.febs.system.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Service
public class EmployeeCoreServiceImpl extends ServiceImpl<EmployeeCoreMapper, EmployeeCore> implements IEmployeeCoreService {
    @Autowired
    IEmployeeService iEmployeeService;
    @Autowired
    IEventService iEventService;
    @Autowired
    IDepartmentService iDepartmentService;

    @Autowired
    IPeriodService iPeriodService;
    @Autowired
    ICwgzService iCwgzService;
    public List<EmployeeCore> findEmployeeCores(Integer employeeId){
        LambdaQueryWrapper<EmployeeCore> queryWrapper = new LambdaQueryWrapper<>();
        if (employeeId!=null) {
            queryWrapper.eq(EmployeeCore::getEmployeeid, employeeId);
            return baseMapper.selectList(queryWrapper).stream().sorted(Comparator.comparing(EmployeeCore::getEnddate).reversed()).collect(Collectors.toList());
        }else{
            return  new ArrayList<>();
        }

    }

    public List<EmployeeCore> getOAEmployeeCore(){
        return this.baseMapper.getOAEmployeeCore();
    }

    public EmployeeCore findLatestCore(Integer employeeId){
        //根据结束时间倒排
        List<EmployeeCore> employeeCoreList=findEmployeeCores(employeeId);
        EmployeeCore employeeCore=new EmployeeCore();
        if(CollectionUtils.isNotEmpty(employeeCoreList)){
            employeeCore=employeeCoreList.get(0);
        }
        return  employeeCore;
    }
    @Transactional
    public void createEmployeeCore(EmployeeCore employeeCore) {
        EmployeeCore latestEmployeeCore=findLatestCore(employeeCore.getEmployeeid());
        if(latestEmployeeCore.getId()!=null){
            latestEmployeeCore.setEnddate(DateUtil.getYesterday(employeeCore.getStartdate()));
            updateEmployeeCore(latestEmployeeCore);
        }
        employeeCore.setEnddate(DateUtil.getLatestDate());
        this.save(employeeCore);
        Event event=new Event();
        event.setEvent(employeeCore.getEventtype());
        event.setEventname(employeeCore.getEmployeetypename());
        event.setEmployeeid(employeeCore.getEmployeeid());
        event.setStartdate(employeeCore.getStartdate());
        event.setEventdate(employeeCore.getStartdate());
        event.setCoreid(employeeCore.getId());
        iEventService.save(event);
        if(employeeCore.getStatus().equals(4)){ //2022 1.25 邓狗 退休 人员工资状态为3 不显示工资
            this.baseMapper.updateEmployedStatus(employeeCore.getEmployeeid());
        }
        Employee e=iEmployeeService.findEmployeeById(employeeCore.getEmployeeid());
        if(employeeCore.getEventtype()!=2&&employeeCore.getEventtype()!=3&&employeeCore.getEventtype()!=14){
            e.setIdnumber(employeeCore.getId());
            e.setJob(employeeCore.getJob());
            e.setPosition(employeeCore.getPosition());
            e.setDeptname(employeeCore.getDept());
            if(employeeCore.getWard()!=null){
                e.setWardname(employeeCore.getWard());
            }
            //如果是离职，退休，外调，死亡,则传给OA上一条的信息
            if(employeeCore.getEventtype()==4||employeeCore.getEventtype()==6||employeeCore.getEventtype()==7||employeeCore.getEventtype()==8){
                e.setJob(latestEmployeeCore.getJob());
                e.setPosition(latestEmployeeCore.getPosition());
                e.setDeptname(latestEmployeeCore.getDept());
                e.setRsfwname(latestEmployeeCore.getRsfw().toString());
                e.setRszfwname(latestEmployeeCore.getRszfw().toString());
                e.setEmployeetype(latestEmployeeCore.getEmployeetypename());
                if(employeeCore.getWard()!=null){
                    e.setDeptname(latestEmployeeCore.getWard());
                }
            }
            e.setEmployeeid(employeeCore.getEmployeeid());
            e.setXtUpdatedate(new Date());
            e.setState(employeeCore.getEventtype().toString());

            //推送给OA
            iEmployeeService.addOAEmployee(e);
        }
        //推送给财务
        if(employeeCore.getEventtype() != 5 && employeeCore.getEventtype() != 14 ){
            iCwgzService.createCwyd(employeeCore,e);
        }
    }

    public void updateEmployeeCore(EmployeeCore employeeCore) {
        Event event=iEventService.getEvent(employeeCore.getId());
        if(event!=null) {
            event.setEvent(employeeCore.getEventtype());
            iEventService.updateById(event);
        }
        updateById(employeeCore);
    }

    public void updateOAYdxx(){
        this.baseMapper.updateOAYdxx();
    }

    public void deleteEmployeeCore(String id){
        this.baseMapper.deleteById(id);
    }

    public List<EmployeeCore> findEmployeeCoreMaxEndDateLists() {
        return baseMapper.findEmployeeCoreMaxEndDateList();
    }

}
