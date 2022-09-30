package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.dao.EmolumentMapper;
import cc.mrbird.febs.system.domain.*;
import cc.mrbird.febs.system.service.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Service
public class EmolumentServiceImpl extends ServiceImpl<EmolumentMapper, Emolument> implements IEmolumentService {
    @Value("${febs.max.batch.insert.num}")
    private int batchInsertMaxNum;
    @Autowired
    IPeriodService iPeriodService;

    @Autowired
    IEmployeeService iEmployeeService;

    @Autowired
    ICwgzService iCwgzService;

    @Autowired
    ICodeService iCodeService;

    @Transactional
    public List<Emolument> insertEmolumentList(QueryRequest request,Period period){
        Emolument emolument=new Emolument();
        emolument.setPeriod(DateUtil.getLastMonth(period.getPeriod()));
        emolument.setEmolumentflag("true");
        Page<Emolument> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        List<Emolument>emolumentList=this.baseMapper.getEmolumentList_kq(emolument); //2022-01-25  han 这个方法需要重写 ，太多地放用到了


        emolumentList.stream().forEach(e->{
            e.setId(null);
            e.setIssue(e.getIssue()-e.getQt());
            e.setQt(0.00);
            e.setPeriodid(period.getId());
            e.setPeriod(period.getPeriod());
            if(e.getEmolumentstatus()==3){
               e.setQt(0.00);
                e.setGwgz(0.00);
                e.setCxjt(0.00);
                e.setXjgz(0.00);
                e.setHljt(0.00);
                e.setGcb(0.00);
                e.setTgjt(0.00);
                e.setFt(0.00);
                e.setWsf(0.00);

                e.setDzbt(0.00);
                e.setJcxjx(0.00);
                e.setGp(0.00);
                e.setJtbt(0.00);
                e.setWybt(0.00);
                e.setCebt(0.00);

                e.setGzyf(0.00);
                e.setTd2016(0.00);
                e.setTd2017(0.00);
                e.setTd2018(0.00);
                e.setTd2019(0.00);
                e.setTd2020(0.00);
                e.setTd2021(0.00);

                e.setSc(0.00);
                e.setIssue(0.00);
                e.setYlbz(0.00);
                e.setLmbz(0.00);
                e.setLmbz(0.00);

                e.setDrugcost(0.00);
                e.setHydropowercost(0.00);
                e.setRoomcharge(0.00);
                e.setMeetingcost(0.00);
                e.setAccumulationcost(0.00);
                e.setIncometax(0.00);
                e.setZgongyl(0.00);

                e.setZynj(0.00);
                e.setHs(0.00);
                e.setDeylgrbj(0.00);

                e.setHrotherreduce(0.00);
                e.setSavedown(0.00);
                e.setActual(0.00);

            }
        });
        saveBatch(emolumentList);
        /*if(!CollectionUtils.isNotEmpty(emolumentList.getRecords())) {
            List<Integer> ids= new ArrayList<>();
            emolumentList = this.baseMapper.initEmolumentList(page);
            emolumentList.getRecords().stream().forEach(e->{
                ids.add(e.getEmployeeid());
            });
            iEmployeeService.updateEmolumentStatus(ids,1);
        }*/
        return emolumentList;
    }

    public void  updateByEmployeeIdAndPeriod(Emolument kj, String periodStr){
        this.baseMapper.updateByEmployeeIdAndPeriod(kj,periodStr);
    }

    @Override
    public IPage<Emolument> getEmolumentList(Page page,Emolument emolument){
        return this.baseMapper.getEmolumentList(page,emolument);
    }
    @Override
    public IPage<Emolument> getEmolumentList_kq(Page page,Emolument emolument){
        IPage<Emolument> page1 = new Page<>();
        page1  = this.baseMapper.getEmolumentList_kq(page,emolument);
        this.setCode(page1.getRecords());
        return page1;
    }

    @Override
    public List<Emolument> getEmolumentList(Emolument emolument){
        return this.baseMapper.getEmolumentList(emolument);
    }

    private void setCode(List<Emolument> list) {
        List<Code> codeList = iCodeService.findCodeMaxEndDateLists();
        if(list.size()>0) {
            List<Code> codeQuery = new ArrayList<>();
            for (Emolument e : list) {
                codeQuery = codeList.stream().filter(s -> s.getEmployeeid().equals(e.getEmployeeid())).collect(Collectors.toList());
                if (codeQuery.size() > 0) {
                    e.setCode(codeQuery.get(0).getEmployeecode());
                }
            }
        }
    }

    @Override
    public List<Emolument> getEmolumentList_kq(Emolument emolument){
        List<Emolument> list = this.baseMapper.getEmolumentList_kq(emolument);
        this.setCode(list);
        return list;
    }
    public Emolument getTotalEmolument(Emolument emolument){
        return this.baseMapper.getTotalEmolument(emolument);
    }
    @Override
    public IPage<Emolument> getCancelEmolumentList(QueryRequest request, Emolument emolument){
        Page<Emolument> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        emolument.setStates("4,5,6");
        return this.baseMapper.getCancelEmolumentList(page,emolument);
    }
    @Override
    public IPage<Emolument> getUnEmolumentList(QueryRequest request, Emolument emolument){
        Page<Emolument> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        return this.baseMapper.getUnEmolumentList(page,emolument);
    }

    @Override
    public List<Emolument> getUnEmolumentList( Emolument emolument){ ;
        return this.baseMapper.getUnEmolumentList(emolument);
    }

    @Override
    public IPage<Emolument> getDifferentReport(QueryRequest request,Emolument emolument){
        Page<Emolument> page = new Page<>();
        emolument.setEnddate(DateUtil.getLastMonth(emolument.getStartdate()));
        SortUtil.handlePageSort(request, page, false);

        // 取出 人员相关信息
        List<Emolument> employeeList = this.baseMapper.getDifferentReport1(emolument);
        // 取出本月金额
        LambdaQueryWrapper<Emolument> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Emolument::getPeriod,emolument.getStartdate());
        // queryWrapper.eq(Emolument::getEmployeestatu,1);
        List<Emolument>  eByyfList = this.baseMapper.selectList(queryWrapper);
        // 取出上月金额
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Emolument::getPeriod,emolument.getEnddate());
        // queryWrapper.eq(Emolument::getEmployeestatu,1);
        List<Emolument>  eSyyfList = this.baseMapper.selectList(queryWrapper);
        List<Emolument> queryEmolument = new ArrayList<>();
        for (Emolument emt : employeeList) {
            queryEmolument = eByyfList.stream().filter(s->s.getEmployeeid().equals(emt.getEmployeeid())).collect(Collectors.toList());
            if(queryEmolument.size() > 0) {
                emt.setByyf(queryEmolument.get(0).getIssue());
            } else {
                emt.setByyf(0d);
            }
            queryEmolument = eSyyfList.stream().filter(s->s.getEmployeeid().equals(emt.getEmployeeid())).collect(Collectors.toList());
            if(queryEmolument.size() > 0) {
                emt.setSyyf(queryEmolument.get(0).getIssue());
            } else {
                emt.setSyyf(0d);
            }
            emt.setCe(emt.getByyf() - emt.getSyyf());
        }
        List<Emolument> emolumentList = employeeList.stream().filter(s-> s.getCe() != 0).collect(Collectors.toList());
        if(emolumentList.size() > 0) {
            page.setSearchCount(false);
            SortUtil.handlePageSort(request, page, false);//true 是属性  false是数据库字段可两个
            page.setTotal(emolumentList.size());
            long current = page.getCurrent() == 1 ? 0 : (page.getCurrent() - 1) * page.getSize();
            List<Emolument> list = emolumentList.stream().skip(current).limit(page.getSize()).collect(Collectors.toList());
            this.setCode(list);
            page.setRecords(list);
        }
        return page;
//        return this.baseMapper.getDifferentReport(page,emolument);
    }

    @Override
    public List<EmolumentCy> getDifferentReportCyExport(Emolument emolument){
        emolument.setEnddate(DateUtil.getLastMonth(emolument.getStartdate()));
        // 取出 人员相关信息
        List<Emolument> employeeList = this.baseMapper.getDifferentReport1(emolument);
        // 取出本月金额
        LambdaQueryWrapper<Emolument> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Emolument::getPeriod,emolument.getStartdate());
        // queryWrapper.eq(Emolument::getEmployeestatu,1);
        List<Emolument>  eByyfList = this.baseMapper.selectList(queryWrapper);
        // 取出上月金额
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Emolument::getPeriod,emolument.getEnddate());
        // queryWrapper.eq(Emolument::getEmployeestatu,1);
        List<Emolument>  eSyyfList = this.baseMapper.selectList(queryWrapper);
        List<Emolument> queryEmolument = new ArrayList<>();
        for (Emolument emt : employeeList) {
            queryEmolument = eByyfList.stream().filter(s->s.getEmployeeid().equals(emt.getEmployeeid())).collect(Collectors.toList());
            if(queryEmolument.size() > 0) {
                emt.setByyf(queryEmolument.get(0).getIssue());
            } else {
                emt.setByyf(0d);
            }
            queryEmolument = eSyyfList.stream().filter(s->s.getEmployeeid().equals(emt.getEmployeeid())).collect(Collectors.toList());
            if(queryEmolument.size() > 0) {
                emt.setSyyf(queryEmolument.get(0).getIssue());
            } else {
                emt.setSyyf(0d);
            }
            emt.setCe(emt.getByyf() - emt.getSyyf());
        }
        List<Emolument> emolumentList = employeeList.stream().filter(s-> s.getCe() != 0).collect(Collectors.toList());
        this.setCode(emolumentList);
        List<EmolumentCy> cyList = new ArrayList<>();
        for (Emolument e : emolumentList) {
            EmolumentCy c =new EmolumentCy();
            c.setCode(e.getCode());
            c.setEmployeename(e.getEmployeename());
            c.setDeptname(e.getDeptname());
            c.setEmployeestatu(e.getEmployeestatu());
            c.setEmployeetype(e.getEmployeetype());
            c.setSyyf(e.getSyyf());
            c.setByyf(e.getByyf());
            c.setCe(e.getCe());
            cyList.add(c);
        }
        return cyList;
    }

    @Override
    public List<Emolument> getDifferentReportDetail(Emolument emolument){
        return this.baseMapper.getDifferentReportDetail(emolument);
    }

    public Emolument getTotalDifferentReport(Emolument emolument){
        return this.baseMapper.getTotalDifferentReport(emolument);
    }
    @Transactional
    public void createEmolument(Emolument emolument){
        Employee employee=new Employee();
        employee.setEmployeeid(emolument.getEmployeeid());
        employee.setEmolumentstatus(1);
        iEmployeeService.updateEmployee(employee);
        this.save(emolument);
    }
    @Transactional
    public void batchCreateEmolument(Emolument emolument){
        List<String> s= Arrays.asList(emolument.getEmployeeids().split(StringPool.COMMA));
        List<Integer> employeeIds=new ArrayList<>();
        List<Emolument>emolumentList=new ArrayList<>();
        s.stream().forEach(a->{
            Integer employeeid=Integer.parseInt(a);
            Employee e=iEmployeeService.findEmployeeById(employeeid);
            emolument.setIdnumber(e.getIdnumber());
            employeeIds.add(employeeid);
            Emolument neweEolument=new Emolument();
            BeanUtils.copyProperties(emolument, neweEolument);
            neweEolument.setEmployeeid(employeeid);
            emolumentList.add(neweEolument);
        });
        this.saveBatch(emolumentList);
        iEmployeeService.updateEmolumentStatus(employeeIds,1);
    }

   public void updateEmolument(Emolument emolument){
       updateById(emolument);
   }

   @Override
   public void updateByEmployeeids(Emolument emolument){
        this.baseMapper.updateByEmployeeids(emolument);
   }

   @Override
   public void batchUpdate(List<Emolument>emolumentList){
        Period period=iPeriodService.getLatestPeriod();
        emolumentList.stream().forEach(e->{
            e.setPeriodid(period.getId());
        });
        this.baseMapper.batchUpdate(emolumentList);
   }

   @Override
   public void cancelEmolumentList(List<String> ids){
        Period p=iPeriodService.getLatestPeriod();
        List<Integer>employeeIds=new ArrayList<>();
        List<String> emolumentIds=new ArrayList<>();
        ids.stream().forEach(i->{
            employeeIds.add(Integer.parseInt(i));
            if(p.getStatus()!=0){
                Emolument emolument=this.baseMapper.getEmolumentByEmployeeidAndPeriod(p.getPeriod(),Integer.parseInt(i));
                if(emolument!=null){
                    emolumentIds.add(emolument.getId());
                }
            }
        });
        if(emolumentIds.size()>0){
            this.baseMapper.deleteBatchIds(emolumentIds);
        }
        iEmployeeService.updateEmolumentStatus(employeeIds,2);
   }

   @Override
   public void passEmolument(Period period){
       this.baseMapper.passEmolument(period.getId());
       period.setStatus(0);
       iPeriodService.updatePeriod(period);
       period.setPeriod(DateUtil.getNextMonth(period.getPeriod()));
       //period.setIsfirststatus(0);
       period.setId(null);
       iPeriodService.createPeriod(period);
   }

   public Emolument getByIdnumberAndPeriod(String idnumber,Date period){
       LambdaQueryWrapper<Emolument> queryWrapper = new LambdaQueryWrapper<>();
       queryWrapper.eq(Emolument::getIdnumber, idnumber);
       queryWrapper.eq(Emolument::getPeriod,period);
       return baseMapper.selectOne(queryWrapper);
   }

   public void updateByIdnumbers(List<Emolument> emolumentList){
        this.baseMapper.updateByIdnumbers(emolumentList);
   }

}
