package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.*;
import cc.mrbird.febs.system.dao.KqRmxbMapper;
import cc.mrbird.febs.system.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Service
public class KqRmxbServiceImpl extends ServiceImpl<KqRmxbMapper, KqRmxb> implements IKqRmxbService {
    @Autowired
    IEmployeeService iEmployeeService;

    @Autowired
    IKqPeriodService iKqPeriodService;

    @Autowired
    IKqJkService iKqJkService;

    @Autowired
    IKqSqbService iKqSqbService;

    @Autowired
    IKqPbDetailService iKqPbDetailService;

    public IPage<KqRmxb> getRmxList(KqRmxb kqRmxb, QueryRequest request) {
        Page<KqRmxb> page = new Page<>();
        KqPeriod period=iKqPeriodService.getLatestPeriod();
        SortUtil.handlePageSort(request, page, true);
        IPage<KqRmxb> kqRmxbIPage=this.baseMapper.getRmxList(page,kqRmxb);
        List<KqRmxb> kqRmxbList=kqRmxbIPage.getRecords();
        for(KqRmxb k:kqRmxbList){
            if(k.getPbdate().compareTo(period.getPeriod())>=0&&k.getPbdate().compareTo(DateUtil.getLastDayOfMonth(period.getPeriod()))<=0&&period.getState()==1){
                k.setFlag(true);
            }
        }
        return kqRmxbIPage;
    }

    public IPage<KqRmxb> getPbReport(KqRmxb kqRmxb, QueryRequest request){
        Page<KqRmxb> page = new Page<>();
        KqPeriod period=iKqPeriodService.getLatestPeriod();
        SortUtil.handlePageSort(request, page, true);
        kqRmxb.setEnddate(DateUtil.getLastDayOfMonth(kqRmxb.getEnddate()));
        IPage<KqRmxb> kqRmxbIPage=this.baseMapper.getPbReportHan(page,kqRmxb);
        return kqRmxbIPage;
    }
    public List<KqRmxb> getPbReportExport(KqRmxb kqRmxb){
        kqRmxb.setEnddate(DateUtil.getLastDayOfMonth(kqRmxb.getEnddate()));
        return this.baseMapper.getPbReportHanExport(kqRmxb);
    }
    @Transactional
    public Map<String,Object> getByStartDate(String pbDept,Date startDate,String username,String rszfws){
        Map<String,Object>result=new HashMap<>();
        List<Date> dateList=DateUtil.getDateByWeek(startDate);
        result.put("date",dateList);
        List<Employee> employeeList=iEmployeeService.getEmployeeByKqDept(pbDept,dateList.get(0),dateList.get(6),rszfws);
        List<KqRmxb> kqRmxbList=baseMapper.getByStartDate(null,dateList.get(0),dateList.get(6));
        //把每个人的日排班汇总成个人的周排班
        for (Employee e: employeeList){
            List<KqRmxb> rmxList=new ArrayList<>();
            for(Date date:dateList){
                Optional<KqRmxb> o=kqRmxbList.stream().filter(k->k.getEmployeeid().equals(e.getEmployeeid())&&k.getPbdate().equals(date)).findFirst();
                if(o.isPresent()){
                    //如果有记录，则判断是否可修改
                    KqRmxb rmx=o.get();
                    rmx.setEditflag(false);
                    if(!rmx.getUpdateby().equals(username) && (rmx.getScheduling()==null)){
                        rmx.setEditflag(true);
                    }
                    if(rmx.getUpdateby().equals(username)&&rmx.getShstatus()!=1 && rmx.getShstatus()!=2&&rmx.getVacationflag()!=1&&rmx.getHjflag()!=1){
                        rmx.setEditflag(true);
                    }
                    rmxList.add(rmx);
                }else{
                    //没有记录就新增一条
                    rmxList.add(createRmx(e,date,username,pbDept,null));
                }
            }
            e.setRmxList(rmxList);
        }
        result.put("employeeList",employeeList.stream().sorted(Comparator.comparing(Employee::getRszfw).reversed()).collect(Collectors.toList()));
        return result;
    }

    @Override
    public KqRmxb createRmx(Employee employee,Date date,String username,String pbDept,Integer scheduling){
        KqRmxb kqRmxb=new KqRmxb();
        kqRmxb.setEmployeeid(employee.getEmployeeid());
        kqRmxb.setEmployeetype(Integer.parseInt(employee.getEmployeetype()));
        kqRmxb.setEmployeecode(employee.getCode());
        kqRmxb.setIdnumber(employee.getIdnumber());
        kqRmxb.setRsfw(employee.getRsfw());
        kqRmxb.setRszfw(employee.getRszfw());
        kqRmxb.setEditflag(true);
        kqRmxb.setEmployeename(employee.getEmployeename());
        kqRmxb.setUpdateby(username);
        kqRmxb.setPbdept(pbDept);
        kqRmxb.setPbdate(date);
        kqRmxb.setScheduling(scheduling==null?null:scheduling.toString());
        if(username.equals("admin")){
            kqRmxb.setVacationflag(1);
        }
        this.save(kqRmxb);
        return kqRmxb;
    }
    @Override
    public Map<String,Object> getPbByMonth(KqRmxb kqRmxb){
        Map<String,Object>result=new HashMap<>();
        KqSqb kqSqb=iKqSqbService.findByPbDeptAndPbDate(kqRmxb);
        kqRmxb.setEnddate(DateUtil.getLastDayOfMonth(kqRmxb.getStartdate()));
        List<KqJk>kqJkList=new ArrayList<>();
        kqJkList.addAll(createKqJkByMonth(kqRmxb));
        result.put("sqb",kqSqb);
        result.put("pblist",kqJkList);
        return result;
    }

    public List<KqRmxb> getPbByStartAndEndDate(Date startDate,Date endDate,String code){
        KqPeriod period=iKqPeriodService.getLatestPeriod();
        List<KqRmxb> kqRmxbList=baseMapper.getPbByStartAndEndDate(startDate,endDate,code);
        return kqRmxbList;
    }

    /**
     * 月考勤数据
     * @param month
     * @return
     */
    public  List<AttandanceUser> getAllPbByMOnth(String month){
        return  this.baseMapper.getAllPbByMOnth(month);
    }

    public  List<AttandanceUser> getAllPbByWeek(String startDate, String endDate){
        return  this.baseMapper.getAllPbByWeek(startDate,endDate);
    }

    public List<KqJk> createKqJkByMonth(KqRmxb kqRmxb){
        List<KqJk> kqJkList=new ArrayList<>();
        List<KqRmxb>kqRmxbList=baseMapper.getByStartDate(kqRmxb.getPbdept(),kqRmxb.getStartdate(),kqRmxb.getEnddate());
        Map<Integer, List<KqRmxb>> map = kqRmxbList.stream().collect(Collectors.groupingBy(KqRmxb::getEmployeeid));
        Date finalEndDate = kqRmxb.getEnddate();
        map.values().forEach(list->{
            KqJk k=new KqJk();
            k.setEmployeeid(list.get(0).getEmployeeid().toString());
            k.setEmployeecode(list.get(0).getEmployeecode());
            k.setEmployeetype(list.get(0).getEmployeetype());
            k.setEmployeename(list.get(0).getEmployeename());
            k.setRsfw(list.get(0).getRsfw());
            k.setRszfw(list.get(0).getRszfw());
            k.setIdnumber(list.get(0).getIdnumber());
            k.setTjdept(kqRmxb.getPbdept());
            k.setStartdate(kqRmxb.getStartdate());
            k.setEnddate(finalEndDate);
            if(kqRmxb.getSqbid()!=null){
                k.setSqbid(kqRmxb.getSqbid());
            }
            createKqJk(k,list);
            kqJkList.add(k);
        });
        return kqJkList;
    }

    public KqJk createKqJk(KqJk kqJk,List<KqRmxb> kqRmxbList){
        Calendar calendar = Calendar.getInstance();
        for(KqRmxb rmx:kqRmxbList){
            //根据日期是当月的第几天判断对应月明细的字段
            calendar.setTime(rmx.getPbdate());
            int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
            String filedName=String.format("a%02d",monthDay);
            try {
                Field name = kqJk.getClass().getDeclaredField(filedName);
                name.setAccessible(true);
                name.set(kqJk,rmx.getScheduling()!=null?rmx.getScheduling():null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return kqJk;
    }

    @Override
    public void batchUpdateSqbInfo(KqRmxb kqRmxb){
        UpdateWrapper<KqRmxb> updateWrapper = new UpdateWrapper<KqRmxb>();
        updateWrapper.set("shstatus",kqRmxb.getShstatus());
        updateWrapper.set("sqbid",kqRmxb.getSqbid());
        updateWrapper.eq("pbdept",kqRmxb.getPbdept());
        updateWrapper.ge("pbdate",kqRmxb.getStartdate());
        updateWrapper.le("pbdate",DateUtil.getLastDayOfMonth(kqRmxb.getStartdate()));
        this.update(updateWrapper);
    }

    @Override
    public void batchUpdate(List<KqRmxb> kqRmxbList,String pbDept){
        List<KqPbDetail> pbList = new ArrayList<>();
        if(pbDept.equals("1090")||pbDept.equals("1089")||pbDept.equals("1039")||pbDept.equals("1038")){
            pbList.addAll(iKqPbDetailService.getPbList("GRK",FebsConstant.PB_STATUS_QY));
        }
        for(KqRmxb rmxb:kqRmxbList){
            if(FebsConstant.SPECIAL_DEPT_IDS.contains(pbDept)){
                pbList.addAll(iKqPbDetailService.getPbList(pbDept,FebsConstant.PB_STATUS_QY));
            }else{
                if(rmxb.getRszfw()==2004){
                    pbList.addAll(iKqPbDetailService.getPbList("HL",FebsConstant.PB_STATUS_QY));
                }else if(rmxb.getRszfw()>=2001&&rmxb.getRszfw()<2004){
                    pbList.addAll(iKqPbDetailService.getPbList("YJ",FebsConstant.PB_STATUS_QY));
                }
            }
            pbList.addAll(iKqPbDetailService.getPbList("TY",FebsConstant.PB_STATUS_QY));
            Optional<KqPbDetail> o=pbList.stream().filter(p->p.getValuee().equals(rmxb.getScheduling())).findFirst();
            if(o.isPresent()){
                rmxb.setScheduling(o.get().getKeyy().toString());
            }
            UpdateWrapper<KqRmxb> updateWrapper = new UpdateWrapper<KqRmxb>();
            updateWrapper.set("scheduling",rmxb.getScheduling());
            updateWrapper.eq("id",rmxb.getId());
            this.update(updateWrapper);
        }
    }

    @Override
    public void updateBySqbId(KqRmxb kqRmxb){
        UpdateWrapper<KqRmxb> updateWrapper = new UpdateWrapper<KqRmxb>();
        updateWrapper.set("shstatus",kqRmxb.getShstatus());
        updateWrapper.eq("sqbid",kqRmxb.getSqbid());
        this.update(updateWrapper);
    }

    @Transactional
    public void updateByWeek(String pbdept,String username,List<Employee>employeeList){
        for(Employee e:employeeList){
            for(KqRmxb k:e.getRmxList()){
                if(k.getEditflag()){
                    k.setUpdateby(username);
                    k.setPbdept(pbdept);
                    k.setShstatus(0);
                    k.setSqbid(null);
                }
                this.baseMapper.updateById(k);
            }
        }
    }

    @Override
    public void batchSetschedul(KqRmxb kqRmxb){
        UpdateWrapper<KqRmxb> updateWrapper = new UpdateWrapper<KqRmxb>();
        String [] employeeIds =kqRmxb.getEmployeeids().split(StringPool.COMMA);
        updateWrapper.set("scheduling",kqRmxb.getScheduling());
        updateWrapper.in("employeeid",employeeIds);
        if(kqRmxb.getStartdate()!=null){
            updateWrapper.ge("pbdate",kqRmxb.getStartdate());
        }
        if(kqRmxb.getEnddate()!=null){
            updateWrapper.le("pbdate",kqRmxb.getEnddate());
        }
        updateWrapper.eq("vacationflag",0);
        updateWrapper.eq("hjflag",0);
        updateWrapper.in("shstatus",0,3);
        this.update(updateWrapper);
    }

    @Override
    public void updateKqRmxb(KqRmxb kqRmxb){
        updateById(kqRmxb);
        LambdaQueryWrapper<KqRmxb> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KqRmxb::getEmployeeid, kqRmxb.getEmployeeid());
        queryWrapper.ge(KqRmxb::getPbdate,DateUtil.getFirstDayOfMonth(kqRmxb.getPbdate()));
        queryWrapper.le(KqRmxb::getPbdate,DateUtil.getLastDayOfMonth(kqRmxb.getPbdate()));
        List<KqRmxb> kqRmxbList=this.baseMapper.selectList(queryWrapper);
        KqJk kqJk=iKqJkService.findKqjk(DateUtil.getFirstDayOfMonth(kqRmxb.getPbdate()),kqRmxb.getIdnumber());
        if(kqJk!=null){
            kqJk=createKqJk(kqJk,kqRmxbList);
            iKqJkService.updateKqjk(kqJk);
        }
    }


}
