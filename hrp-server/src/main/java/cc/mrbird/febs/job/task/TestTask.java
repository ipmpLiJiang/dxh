package cc.mrbird.febs.job.task;

import cc.mrbird.febs.common.exception.RedisConnectException;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.system.domain.*;
import cc.mrbird.febs.system.service.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TestTask {
    @Autowired
    IDepartmentService iDepartmentService;

    @Autowired
    IEducationService iEducationService;

    @Autowired
    IPositionService iPositionService;

    @Autowired
    IEmployeeService iEmployeeService;

    @Autowired
    IEmployeeCoreService iEmployeeCoreService;

    @Autowired
    IFamilyService iFamilyService;

    @Autowired
    ITitleGetService iTitleGetService;

    @Autowired
    IJobDetailService iJobDetailService;

    @Autowired
    IKqJkService iKqJkService;

    @Autowired
    IKqPeriodService iKqPeriodService;

    @Autowired
    IKqXjService iKqXjService;

    @Autowired
    IKqRmxbService iKqRmxbService;

    @Autowired
    IKqSqbService iKqSqbService;

    @Autowired
    IGqTitleService iGqTitleService;

    @Autowired
    IPeriodService periodService;

    @Autowired
    ICwgzService cwgzService;

    @Autowired
    IEmolumentService emolumentService;

    public void test(String params) {
        log.info("我是带参数的test方法，正在被执行，参数为：{}" , params);
    }
    public void test1() {
        log.info("我是不带参数的test1方法，正在被执行");
    }

    @Transactional
    public void updateDept(){
        iDepartmentService.deleteAll();
        List<Department> departmentListOa=iDepartmentService.getOADepartment();
        for (Department d:departmentListOa){
            if(d.getPath().length()!=12){
                for(Department parent:departmentListOa){
                    if (d.getPath().length()==16){
                        if(parent.getPath().length()==12&&d.getPath().substring(0,12).equals(parent.getPath())){
                            d.setParentid(parent.getKeyy());
                            d.setParentvaluee(parent.getValuee());
                        }
                    }else{
                        if(parent.getPath().length()==16&&d.getPath().length()==20){
                            String s=d.getPath().substring(0,16);
                            if(s.equals(parent.getPath())){
                                d.setParentid(parent.getKeyy());
                                d.setParentvaluee(parent.getValuee());
                            }
                        }
                    }
                }
            }else{
                d.setParentid("0");
            }
            iDepartmentService.createDepartment(d);
            iDepartmentService.createDepartmentToCw(d);
        }
    }

    @Transactional
    public void updatePosition(){
        iPositionService.deleteAll();
        List<Position> positions=iPositionService.getOAPosition();
        for(Position p:positions){
            iPositionService.createPosition(p);
        }
    }

    @Transactional
    public void updateJob(){
        iJobDetailService.deleteAll();
        List<JobDetail>jobDetails=iJobDetailService.getOAJobDetail();
        for(JobDetail j:jobDetails){
            iJobDetailService.createJobDetail(j);
        }
    }

    @Transactional
    public void updateEducation(){
        List<Education>educations=iEducationService.getOAEducation();
        educations.stream().sorted(Comparator.comparing(Education::getXtUpdatedate)).collect(Collectors.toList());
        List<Integer> employeeids=new ArrayList<>();
        educations.stream().forEach(e->{
            Employee employee=iEmployeeService.findEmployeeByCode(e.getCode());
            e.setXtUpdateby("OA");
            if (employee!=null){
                e.setEmployeeid(employee.getEmployeeid());
                employeeids.add(e.getEmployeeid());
            }
        });
        if(employeeids.size()>0){
            iEducationService.deleteByEmployeeids(employeeids);
            for(Education e:educations){
                iEducationService.createEducation(e);
            }
        }
        iEducationService.updateOAEducation();
    }

    @Transactional
    public void updateFamily(){
        List<Family>families=iFamilyService.getOAFamily();
        families.stream().sorted(Comparator.comparing(Family::getXtUpdatedate)).collect(Collectors.toList());
        List<Integer> employeeids=new ArrayList<>();
        families.stream().forEach(f->{
            Employee employee=iEmployeeService.findEmployeeByCode(f.getCode());
            f.setTel(StringUtils.deleteWhitespace(f.getTel()));
            f.setXtUpdateby("OA");
            if (employee!=null) {
                f.setEmployeeid(employee.getEmployeeid());
                employeeids.add(f.getEmployeeid());
            }
        });
        if(employeeids.size()>0) {
            iFamilyService.deleteByEmployeeids(employeeids);
            for (Family f : families) {
                iFamilyService.createFamily(f);
            }
        }
        iFamilyService.updateOAFamily();
    }

    @Transactional
    public void updateTitleGet(){
        List<TitleGet>titleGets=iTitleGetService.getOATitleGet();
        titleGets.stream().sorted(Comparator.comparing(TitleGet::getXtUpdatedate)).collect(Collectors.toList());
        List<Integer> employeeids=new ArrayList<>();
        titleGets.stream().forEach(t->{
            Employee employee=iEmployeeService.findEmployeeByCode(t.getCode());
            t.setXtUpdateby("OA");
            if (employee!=null) {
                t.setEmployeeid(employee.getEmployeeid());
                employeeids.add(t.getEmployeeid());
            }
        });
        if(employeeids.size()>0) {
            iTitleGetService.deleteByEmployeeids(employeeids);
            for (TitleGet t : titleGets) {
                iTitleGetService.createTitleGet(t);
            }
        }
        iTitleGetService.updateOATitleGet();
    }

    @Transactional
    public void updateGqTitle(){
        List<GqTitle>titleGets=iGqTitleService.getOAGqTitle();
        titleGets.stream().sorted(Comparator.comparing(GqTitle::getXtUpdatedate)).collect(Collectors.toList());
        List<Integer> employeeids=new ArrayList<>();
        titleGets.stream().forEach(t->{
            Employee employee=iEmployeeService.findEmployeeByCode(t.getCode());
            t.setXtUpdateby("OA");
            if (employee!=null) {
                t.setEmployeeid(employee.getEmployeeid());
                employeeids.add(t.getEmployeeid());
            }
        });
        if(employeeids.size()>0) {
            iTitleGetService.deleteByEmployeeids(employeeids);
            for (GqTitle t : titleGets) {
                iGqTitleService.createGqTitle(t);
            }
        }
        iGqTitleService.updateOAGqTitle();
    }

    @Transactional
    public void updateCoreInfo() {
        List<EmployeeCore>employeeCores=iEmployeeCoreService.getOAEmployeeCore();
        List<Department> departmentList=iDepartmentService.findDepartmentList(new Department());
        employeeCores.stream().forEach(core->{
            Employee employee=iEmployeeService.findEmployeeByCode(core.getCode());
            if(employee!=null) {
                EmployeeCore employeeCore = iEmployeeCoreService.findLatestCore(employee.getEmployeeid());
                if(employeeCore!=null) {
                    core.setEmployeeid(employee.getEmployeeid());
                    Department department = iDepartmentService.findDepartmentByKey(core.getDept());
                    if (department != null) {
                        if (department.getPath().length() == 16) {
                            core.setDeptname(department.getValuee());
                            core.setDepttype(department.getParentid());
                        } else {
                            core.setWard(department.getKeyy());
                            core.setWardname(department.getValuee());
                            core.setDept(department.getParentid());
                            core.setDeptname(department.getParentvaluee());
                        }
                    }
                    Optional<Department> department1 = departmentList.stream().filter(d -> d.getKeyy() != null && d.getKeyy().equals(core.getDept())).findFirst();
                    String deptType = "";
                    if (department1.isPresent()) {
                        deptType = department1.get().getParentid();
                    }
                    // String deptType=departmentList.stream().filter(d->d.getKeyy().equals(core.getDept())).findFirst().get().getParentid();

                    core.setDepttype(deptType);
                    core.setRsfw(employeeCore.getRsfw());
                    core.setRszfwname(employeeCore.getRszfwname());
                    core.setRszfw(employeeCore.getRszfw());
                    core.setRsfwname(employeeCore.getRsfwname());
                    core.setJob(employeeCore.getJob());
                    core.setJobname(employeeCore.getJobname());
                    core.setPosition(employeeCore.getPosition());
                    core.setPositionname(employeeCore.getPositionname());
                    core.setEmployeetype(employeeCore.getEmployeetype());
                    core.setEmployeetypename(employeeCore.getEmployeetypename());
                    core.setStatus(1);
                    core.setStatusname("在职");
                    core.setEventtype(2);
                    core.setXtupdateby("OA");
                    if (core.getStartdate().before(employeeCore.getStartdate())) {
                        iEmployeeCoreService.deleteEmployeeCore(employeeCore.getId());
                        iEmployeeCoreService.createEmployeeCore(core);
                        iEmployeeCoreService.createEmployeeCore(employeeCore);
                    } else {
                        iEmployeeCoreService.createEmployeeCore(core);
                    }
                }
            }
        });
        iEmployeeCoreService.updateOAYdxx();
    }

    @Transactional
    public void updateKqjk(){
        List<KqJk>kqJks=iKqJkService.getOAKqJk();
        KqPeriod kqPeriod=iKqPeriodService.getLatestPeriod();
        for (KqJk k:kqJks){
            Employee e=iEmployeeService.findEmployeeByCode(k.getEmployeecode());
            if(e!=null){
                k.setIdnumber(e.getIdnumber());
            }
            k.setShdate(kqPeriod.getPeriod());
            iKqJkService.createKqjk(k);
        }
        iKqJkService.updateOAKqJk(new Date());
    }

    public void updateKqXj(){
        List<KqXj> kqXjs=iKqXjService.getOAXj();
        for(KqXj k:kqXjs){
            List<KqRmxb> kqRmxbs=iKqRmxbService.getPbByStartAndEndDate(k.getStartdate(),k.getEnddate(),k.getEmployeecode());
            List<Date> dateList= DateUtil.getDateByStartAndEndDate(k.getStartdate(),k.getEnddate());
            for(Date d:dateList){
                Optional<KqRmxb> o=kqRmxbs.stream().filter(r->r.getPbdate().equals(d)).findFirst();
                KqRmxb rmx=new KqRmxb();
                Integer scheduling=0;
                if(k.getXjtype()==0){
                    scheduling=630;
                }else if(k.getXjtype()==1){
                    scheduling=740;
                }else if(k.getXjtype()==2){
                    scheduling=680;
                }else if(k.getXjtype()==3){
                    scheduling=640;
                }else if(k.getXjtype()==4){
                    scheduling=730;
                }else if(k.getXjtype()==5){
                    scheduling=750;
                }else if(k.getXjtype()==6){
                    scheduling=710;
                }else if(k.getXjtype()==8){
                    scheduling=780;
                }else if(k.getXjtype()==10){
                    scheduling=720;
                }
                if(o.isPresent()){
                    rmx=o.get();
                    rmx.setScheduling(scheduling.toString());
                    rmx.setVacationflag(1);
                    iKqRmxbService.updateKqRmxb(rmx);
                }else{
                    Employee e=iEmployeeService.findEmployeeByCode(k.getEmployeecode());
                    rmx.setPbdept(e.getKqdept());
                    rmx.setStartdate(d);
                    rmx.setRszfw(e.getRszfw());
                    rmx.setRsfw(e.getRsfw());
                    KqSqb sqb=iKqSqbService.findByPbDeptAndPbDate(rmx);
                    if(sqb==null){
                        //如果申请表为空，则未提交且未排班，新增休假的排班
                        iKqRmxbService.createRmx(e,d,"admin",e.getKqdept(),scheduling);
                    }
                }
            }
        }
        iKqXjService.updateOAXj();
    }

    @Transactional
    public void updateEmployee(){
        List<Employee>employees=iEmployeeService.getOAEmployee();
        employees.stream().sorted(Comparator.comparing(Employee::getXtUpdatedate)).collect(Collectors.toList());
        for(Employee e:employees){
            Employee employee=iEmployeeService.findEmployeeByCode(e.getCode());
            e.setEmployeeid(employee.getEmployeeid());
            e.setPhonenumber(StringUtils.deleteWhitespace(e.getPhonenumber()));
            e.setXtUpdateby("OA");
            Calendar c=Calendar.getInstance();
            c.set(0001,01,01);
            if(e.getRddate().equals(c.getTime())){
                e.setRddate(null);
            }
            iEmployeeService.updateEmployee(e);
        }
        iEmployeeService.updateOAEmployee();
    }

    public void updateKj(){
        Period period=periodService.getLatestPeriod();
        Date lastMonth = DateUtil.getLastMonth(period.getPeriod());
        Emolument emolument=new Emolument();
        emolument.setPeriod(DateUtil.getLastMonth(period.getPeriod()));
        emolument.setEmolumentflag("true");
        List<Emolument> lastEmolumentList = emolumentService.getEmolumentList(emolument);
        List<Emolument> kjList = cwgzService.getCwKj(lastMonth);
        for(Emolument e : lastEmolumentList){
            Optional<Emolument> o = kjList.stream().filter(k->k.getCode().equals(e.getCode())).findFirst();
            if(o.isPresent()){
                Emolument kj = o.get();
                kj.setEmployeeid(e.getEmployeeid());
                String periodStr = DateUtil.getDateFormat(DateUtil.getLastMonth(period.getPeriod()),"yyyy-MM-dd");
                emolumentService.updateByEmployeeIdAndPeriod(kj,periodStr);
            }
        }


    }
}
