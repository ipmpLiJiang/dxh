package cc.mrbird.febs.system.service.impl;


import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.system.dao.CwgzMapper;
import cc.mrbird.febs.system.domain.*;
import cc.mrbird.febs.system.service.*;
import cc.mrbird.febs.ylj.entity.YljBRecord;
import cc.mrbird.febs.ylj.service.IYljBRecordService;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Service
@Transactional
public class CwgzServiceImpl extends ServiceImpl<CwgzMapper, Cwgz> implements ICwgzService {
    @Autowired
    IKqHzService iKqHzService;

    @Autowired
    ISbYanglaoService iSbYanglaoService;

    @Autowired
    IEmolumentService iEmolumentService;

    @Autowired
    IPeriodService iPeriodService;

    @Autowired
    IKqPeriodService iKqPeriodService;

    @Autowired
    ICwgzService iCwgzService;

    @Autowired
    IDepartmentService iDepartmentService;

    @Autowired
    IYljBRecordService iYljBRecordService;

    @Transactional
    public List<Cwgz>


    tsGz() {
        Date firstDate = iPeriodService.getLatestPeriod().getPeriod();
        List<Cwgz> cwgzList = baseMapper.getGz(firstDate, DateUtil.getLastMonth(firstDate));
        //hsc 2022.01.04 增加养老金的推送
        LambdaQueryWrapper<YljBRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YljBRecord::getFkssq, cn.hutool.core.date.DateUtil.format(firstDate, "yyyy-MM") + "-01");
        List<YljBRecord> yljBRecordList = iYljBRecordService.list(queryWrapper);
        for (Cwgz cw : cwgzList
        ) {
            List<YljBRecord> yljBRecord = yljBRecordList.stream().filter(p -> p.getIdCard().equals(cw.getIdnum())).collect(Collectors.toList());
           if(cw.getIdnum().equals("420104197507202412")){
           Date a=    DateUtil.getLastMonth(firstDate);
           }
            if (yljBRecord.size() > 0) {
                double zgongyl_1 = yljBRecord.stream().mapToDouble(p -> p.getYlgrjf()).sum();
                double zgongyl_2 = yljBRecord.stream().mapToDouble(p -> p.getYlgrlx()).sum();
                double zynj_1 = yljBRecord.stream().mapToDouble(p -> p.getNjgrjf()).sum();
                double zynj_2 = yljBRecord.stream().mapToDouble(p -> p.getGrlx()).sum();

                cw.setZgongyl( zgongyl_1 + zgongyl_2);
                cw.setZynj( zynj_1 + zynj_2);
            }
            else{
                cw.setZgongyl(0D);
                cw.setZynj(0D);
            }
        }

        iCwgzService.saveToCw(cwgzList);
        Emolument emolument = new Emolument();
        emolument.setPeriod(firstDate);
        List<Emolument> emolumentList = iEmolumentService.getEmolumentList(emolument);
        iEmolumentService.updateByIdnumbers(emolumentList);
        Period period = iPeriodService.getLatestPeriod();
        iEmolumentService.passEmolument(period);
        KqPeriod kqPeriod = iKqPeriodService.getLatestPeriod();
        iKqHzService.passKqHz(kqPeriod);
        return cwgzList;
    }

    public List<Cwgz> getSb(Date period) {
        Date lastPeriod = DateUtil.getLastMonth(period);
        List<Cwgz> cwgzList = this.baseMapper.getSb(period, lastPeriod);
        Emolument emolument = new Emolument();
        emolument.setPeriod(period);
        List<Emolument> emolumentList = iEmolumentService.getEmolumentList(emolument);
        cwgzList = cwgzList.stream().filter(c -> !emolumentList.stream().map(e -> e.getIdnumber().toUpperCase()).collect(Collectors.toList()).contains(c.getIdnum().toUpperCase())).collect(Collectors.toList());
        return cwgzList;
    }

    public List<Cwgz> getSbSum(Date period) {
        return this.baseMapper.getSbSum(period);
    }

    public void saveToCw(List<Cwgz> cwgzList) {
        for (Cwgz c : cwgzList) {
            this.baseMapper.saveToCW(c);
        }
    }

    public Map<String, Object> getList() {
        Period period = iPeriodService.getLatestPeriod();
        Emolument emolument = new Emolument();
        emolument.setPeriod(period.getPeriod());
        List<Emolument> emolumentList = iEmolumentService.getEmolumentList_kq(emolument);
        Date lastMonth = DateUtil.getLastMonth(period.getPeriod());
        Date lastEndDate = DateUtil.getLastDayOfMonth(lastMonth);
        SbReport sbReport = new SbReport();
        sbReport.setStartdate(period.getPeriod());
        sbReport.setEnddate(DateUtil.getLastDayOfMonth(period.getPeriod()));
        List<SbYanglao> sbYanglaoList = iSbYanglaoService.getHistoryList(sbReport);
        KqHz kqHz = new KqHz();
        kqHz.setStartdate(lastMonth);
        kqHz.setEnddate(lastEndDate);
        List<KqHz> kqHzList = iKqHzService.getHzReport(kqHz);

        //hsc 2022.01.04 增加养老金的推送
        LambdaQueryWrapper<YljBRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YljBRecord::getFkssq, cn.hutool.core.date.DateUtil.format(period.getPeriod(), "yyyy-MM") + "-01");
        List<YljBRecord> yljBRecordList = iYljBRecordService.list(queryWrapper);

        Map<String, Object> returnmap = new HashMap<>();
        returnmap.put("status", iPeriodService.getLatestPeriod().getStatus());
        returnmap.put("gz", emolumentList.size());
        returnmap.put("gzdate", period.getPeriod());
        returnmap.put("kk", kqHzList.size());
        returnmap.put("kkdate", lastMonth);
        returnmap.put("sb", sbYanglaoList.size());
        returnmap.put("yl", yljBRecordList.size());
        return returnmap;
    }

    public void createCwyd(EmployeeCore employeeCore, Employee e) {
        Period period = iPeriodService.getLatestPeriod();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(period.getPeriod());
        int month = calendar.get(Calendar.MONTH) + 1;
        Department department = iDepartmentService.findDepartmentByKey(employeeCore.getWard() != null ? employeeCore.getWard() : employeeCore.getDept());
        Cwyd cwyd = new Cwyd();
        cwyd.setCDeptnum(employeeCore.getStatus() == 7 ? "5R01" : employeeCore.getStatus() == 2 || employeeCore.getStatus() == 3 || employeeCore.getStatus() == 6 ? "5R02" : department.getCode());
        cwyd.setIsstout(0);
        cwyd.setCPsnName(e.getEmployeename());
        cwyd.setCPsnNum(e.getCode());
        //如果是在职就传人员类型
        if (employeeCore.getStatus() == 1) {
            cwyd.setRPersonType(employeeCore.getEmployeetype() == 1 ? "在编" : employeeCore.getEmployeetype() == 2 ? "合同" :
                    employeeCore.getEmployeetype() == 3 ? "代理" : null);
        } else {
            //非在职传状态
            cwyd.setRPersonType(employeeCore.getStatus() == 2 ? "退休" :
                    employeeCore.getStatus() == 5 ? "离职" : employeeCore.getStatus() == 6 ? "死亡" :
                            employeeCore.getStatus() == 7 ? "离休" : employeeCore.getStatusname());
        }
        cwyd.setREmployState(employeeCore.getStatus() == 1 ? "在职" : employeeCore.getStatus() == 2 ? "退休" :
                employeeCore.getStatus() == 3 ? "返聘" : employeeCore.getStatus() == 4 || employeeCore.getStatus() == 5 ? "离职" :
                        employeeCore.getStatus() == 6 ? "死亡" : employeeCore.getStatus() == 7 ? "离休" : employeeCore.getStatusname());
        cwyd.setRSex(e.getGender());
        cwyd.setDBirthDate(e.getBirth());
        cwyd.setDEnterDate(new Date());
        if (employeeCore.getEventtype() == 7)
            cwyd.setDLeaveDate(cwyd.getDEnterDate());
        cwyd.setSysAge(cn.hutool.core.date.DateUtil.ageOfNow(e.getBirth()));
        cwyd.setHPeriod(month);
        this.baseMapper.createCwyd(cwyd);
    }

    @Override
    public List<Emolument> getCwKj(Date period) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(period);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return this.baseMapper.getCwKj(year, month);
    }
}
