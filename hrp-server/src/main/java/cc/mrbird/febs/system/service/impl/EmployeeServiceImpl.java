package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.*;
import cc.mrbird.febs.system.dao.EmployeeMapper;
import cc.mrbird.febs.system.service.*;
import cn.hutool.core.date.DateUnit;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {
    @Autowired
    ICodeService iCodeService;
    @Autowired
    IEmployeeCoreService iEmployeeCoreService;

    @Autowired
    IEventService iEventService;

    @Autowired
    IFamilyService iFamilyService;

    @Autowired
    IEducationService iEducationService;

    @Autowired
    IContinueEducationService iContinueEducationService;

    @Autowired
    IEducationBeforeWorkService iEducationBeforeWorkService;

    @Autowired
    IElectronicRecordsService iElectronicRecordsService;

    @Autowired
    ITitleService iTitleService;

    @Autowired
    IJobTypeService iJobTypeService;

    @Autowired
    IJobTypeDetailService iJobTypeDetailService;

    @Autowired
    IGqTitleService iGqTitleService;

    @Autowired
    ITitleGetService iTitleGetService;

    @Autowired
    IAdministrativeLevelService iAdministrativeLevelService;

    @Autowired
    IAdministrativePostService iAdministrativePostService;

    @Autowired
    IContractService iContractService;

    @Autowired
    DictService dictService;

    @Autowired
    IHonorService iHonorService;

    @Autowired
    ICheckService iCheckService;

    @Autowired
    IWorkExperienceService iWorkExperienceService;

    @Autowired
    IRszfwDetailService iRszfwDetailService;

    @Autowired
    IJobLevelDetailService iJobLevelDetailService;

    @Autowired
    IRsfwDetailService iRsfwDetailService;

    @Autowired
    IDepartmentService iDepartmentService;

    @Autowired
    IPositionService iPositionService;

    @Autowired
    IJobDetailService iJobDetailService;

    @Override
    public List<Employee> findEmployees(String search) {
        return baseMapper.findEmployeeBySearch(search);
    }

    public Employee findEmployeeById(Integer id) {
        return baseMapper.findEmployeeById(id);
    }

    public Employee findEmployeeByCode(String code) {
        return baseMapper.findEmployeeByCode(code);
    }

    public Map<String, Object> getEmployee(Integer employeeId) {
        Map<String, Object> result = new HashMap<>();
        Employee employee = baseMapper.selectById(employeeId);
        //基础信息
        result.put("baseInfo", employee);
        List<EmployeeCore> employeeCoreList = iEmployeeCoreService.findEmployeeCores(employeeId);
        List<Code> codes = iCodeService.findCode(employeeId);
        result.put("codeInfo", codes);
        //核心信息
        result.put("coreInfo", employeeCoreList);
        List<Education> educations = iEducationService.findEducation(employeeId);
        //教育信息
        result.put("educationInfo", educations);
        List<ContinueEducation> continueEducations = iContinueEducationService.findContinueEducation(employeeId);
        //进修信息
        result.put("continueEducation", continueEducations);
        List<Family> familyList = iFamilyService.findFamily(employeeId);
        //家庭成员信息
        result.put("family", familyList);

        List<ElectronicRecords> electronicRecords = iElectronicRecordsService.findElectronicRecords(employeeId);
        //电子档案信息
        result.put("electronicRecordsInfo", electronicRecords);
        //工作学习经历
        List<WorkExperience> workExperiences = iWorkExperienceService.findWorkExperience(employeeId);
        result.put("workExperiences", workExperiences);
        List<Title> titles = iTitleService.findTitle(employeeId);
        //职称信息
        result.put("titleInfo", titles);

        List<GqTitle> gqTitles = iGqTitleService.findGqTitle(employeeId);
        //工勤等级
        result.put("gqtitle", gqTitles);

        List<JobType> jobTypes = iJobTypeService.findJobType(employeeId, null);
        //岗位等级信息
        result.put("jobTypeInfo", jobTypes);
        List<TitleGet> titleGets = iTitleGetService.findTitleGet(employeeId);
        //取得职称信息
        result.put("getTitle", titleGets);
        List<AdministrativeLevel> administrativeLevelList = iAdministrativeLevelService.findAdministrativeLevel(employeeId);
        //行政级别
        result.put("administrativeLevelInfo", administrativeLevelList);
        List<AdministrativePost> administrativePostList = iAdministrativePostService.findadministrativePost(employeeId);
        //行政职务
        result.put("administrativePostInfo", administrativePostList);
        List<Contract> contracts = iContractService.findContract(employeeId);
        //合同信息
        result.put("contract", contracts);
        List<Honor> honors = iHonorService.findHonor(employeeId);
        //荣誉
        result.put("honorInfo", honors);
        List<Checkinfo> checks = iCheckService.findCheck(employeeId);
        //考核信息
        result.put("checkInfo", checks);
        return result;
    }

    @Override
    public IPage<Employee> getEmployeeReport(Page page, Employee employee) {
        return baseMapper.findEmployeeReport(page, employee);
    }

    @Override
    public List<Employee> getEmployeeReport(Employee employee) {
        return baseMapper.findEmployeeReport(employee);
    }

    @Override
    public List<Employee> getEmployeeReport3(Employee employee) {
        return baseMapper.findEmployeeReport3(employee);
    }


    @Override
    public IPage<Employee> deptFindEmployeeList(Page page, Employee employee) {
        return baseMapper.deptFindEmployeeList(page, employee);
    }

    @Override
    public IPage<Employee> getAttandUser(Page page, Employee employee) {
        return baseMapper.getAttandUser(page, employee);
    }

    @Override
    public List<Employee> getEmployeeByKqDept(String kqDept, Date startDate, Date endDate, String rszfws) {
        return baseMapper.getEmployeeByKqDept(kqDept, startDate, endDate, rszfws);
    }

    @Override
    @Transactional
    public Employee createEmployee(Employee employee) {
        this.save(employee);
        /*Event event=new Event();
        event.setEvent((long) 1);
        event.setEmployeeid(employee.getEmployeeid());
        event.setStartdate(employee.getStartdate());
        event.setEventdate(new Date());
        iEventService.save(event);*/
        return employee;
    }

    @Override
    public void updateEmployee(Employee employee) {
        updateById(employee);
    }


    @Override
    public void updateEmolumentStatus(List<Integer> ids, int state) {
        this.baseMapper.updateEmolumentStatus(ids, state);
    }

    public List<Employee> getOAEmployee() {
        return baseMapper.getOAEmployee();
    }

    @Override
    public void updateKqDept(Integer employeeid, String kqDept) {
        UpdateWrapper<Employee> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("kqdept", kqDept);
        updateWrapper.eq("employeeid", employeeid);
        this.update(updateWrapper);
    }

    public void updateOAEmployee() {
        baseMapper.updateOAEmployee();
    }

    public void addOAEmployee(Employee employee) {
        iTitleGetService.updateOATitleGet();
        baseMapper.addOAEmployee(employee);
    }

    public List<EmployeeAgeTitleReport> findEmployeeAgeTitleReport(Employee employee) {
        Integer rsfw = 1001;
        employee.setRsfw(rsfw);
        List<EmployeeAgeTitleReport> ageTitleReportList = new ArrayList<>();
        //
        LambdaQueryWrapper<RszfwDetail> queryRszfwDetailWrapper = new LambdaQueryWrapper<>();
        queryRszfwDetailWrapper.eq(RszfwDetail::getRsfwid, rsfw);
        List<RszfwDetail> rszfwDetailList = iRszfwDetailService.list(queryRszfwDetailWrapper);

        List<Employee> employeeList = baseMapper.findEmployeeAgeTitleReport(employee);
        List<Employee> queryEmployee = new ArrayList<>();
        EmployeeAgeTitleReport entityHj = new EmployeeAgeTitleReport();
        // 2001	医疗、2002	医技、2003 医药、2004 护理、2005	档案、2006 工程、2007 编辑、2008	统计、2009 会计
        for (RszfwDetail item : rszfwDetailList) {
            EmployeeAgeTitleReport entity = new EmployeeAgeTitleReport();
            entity.setRszfw(item.getKeyy());
            entity.setRszfwname(item.getValuee());
            queryEmployee = employeeList.stream().filter(s -> s.getRszfw().equals(item.getKeyy())).collect(Collectors.toList());

            for (Employee e : queryEmployee) {
                this.setEmployeeAge(e, entity, entityHj);
            }
            ageTitleReportList.add(entity);
        }

        entityHj.setRszfwname("合计");
        ageTitleReportList.add(entityHj);

        /*
        for (EmployeeAgeTitleReport r : ageTitleReportList) {
            r.setZg25x(r.getZg25x() == 0 ? null : r.getZg25x());
            r.setZg30(r.getZg30() == 0 ? null : r.getZg30());
            r.setZg35(r.getZg35() == 0 ? null : r.getZg35());
            r.setZg40(r.getZg40() == 0 ? null : r.getZg40());
            r.setZg41s(r.getZg41s() == 0 ? null : r.getZg41s());

            r.setFg25x(r.getFg25x() == 0 ? null : r.getFg25x());
            r.setFg30(r.getFg30() == 0 ? null : r.getFg30());
            r.setFg35(r.getFg35() == 0 ? null : r.getFg35());
            r.setFg40(r.getFg40() == 0 ? null : r.getFg40());
            r.setFg41s(r.getFg41s() == 0 ? null : r.getFg41s());

            r.setZj25x(r.getZj25x() == 0 ? null : r.getZj25x());
            r.setZj30(r.getZj30() == 0 ? null : r.getZj30());
            r.setZj35(r.getZj35() == 0 ? null : r.getZj35());
            r.setZj40(r.getZj40() == 0 ? null : r.getZj40());
            r.setZj41s(r.getZj41s() == 0 ? null : r.getZj41s());

            r.setCj25x(r.getCj25x() == 0 ? null : r.getCj25x());
            r.setCj30(r.getCj30() == 0 ? null : r.getCj30());
            r.setCj35(r.getCj35() == 0 ? null : r.getCj35());
            r.setCj40(r.getCj40() == 0 ? null : r.getCj40());
            r.setCj41s(r.getCj41s() == 0 ? null : r.getCj41s());

            r.setYj25x(r.getYj25x() == 0 ? null : r.getYj25x());
            r.setYj30(r.getYj30() == 0 ? null : r.getYj30());
            r.setYj35(r.getYj35() == 0 ? null : r.getYj35());
            r.setYj40(r.getYj40() == 0 ? null : r.getYj40());
            r.setYj41s(r.getYj41s() == 0 ? null : r.getYj41s());

            r.setWd25x(r.getWd25x() == 0 ? null : r.getWd25x());
            r.setWd30(r.getWd30() == 0 ? null : r.getWd30());
            r.setWd35(r.getWd35() == 0 ? null : r.getWd35());
            r.setWd40(r.getWd40() == 0 ? null : r.getWd40());
            r.setWd41s(r.getWd41s() == 0 ? null : r.getWd41s());
        }
        */
        return ageTitleReportList;
    }

    private void setEmployeeAge(Employee e, EmployeeAgeTitleReport entity, EmployeeAgeTitleReport entityHj) {
        Integer title = e.getTitle();
        if (title != null) {
            Integer age = null;
            if (e.getBirth() != null) {
                age = DateUtil.getAge(e.getBirth());
            }
            if (age != null) {
                // region 判断 赋值
                Integer t25x = 0;
                Integer t30 = 0;
                Integer t35 = 0;
                Integer t40 = 0;
                Integer t41s = 0;
                Integer ts25x = 0;
                Integer ts30 = 0;
                Integer ts35 = 0;
                Integer ts40 = 0;
                Integer ts41s = 0;
                // 3001	正高、3002 副高、3003 中级、3004 初级、3005 员级、3006 未定
                if (title == 3001) {
                    // 3001	正高
                    if (age <= 25) {
                        t25x = entity.getZg25x() == null ? 0 : entity.getZg25x();
                        entity.setZg25x(t25x + 1);

                        ts25x = entityHj.getZg25x() == null ? 0 : entityHj.getZg25x();
                        entityHj.setZg25x(ts25x + 1);
                    } else if (age >= 26 && age <= 30) {
                        t30 = entity.getZg30() == null ? 0 : entity.getZg30();
                        entity.setZg30(t30 + 1);

                        ts30 = entityHj.getZg30() == null ? 0 : entityHj.getZg30();
                        entityHj.setZg30(ts30 + 1);
                    } else if (age >= 31 && age <= 35) {
                        t35 = entity.getZg35() == null ? 0 : entity.getZg35();
                        entity.setZg35(t35 + 1);

                        ts35 = entityHj.getZg35() == null ? 0 : entityHj.getZg35();
                        entityHj.setZg35(ts35 + 1);
                    } else if (age >= 36 && age <= 40) {
                        t40 = entity.getZg40() == null ? 0 : entity.getZg40();
                        entity.setZg40(t40 + 1);

                        ts40 = entityHj.getZg40() == null ? 0 : entityHj.getZg40();
                        entityHj.setZg40(ts40 + 1);
                    } else {
                        t41s = entity.getZg41s() == null ? 0 : entity.getZg41s();
                        entity.setZg41s(t41s + 1);

                        ts41s = entityHj.getZg41s() == null ? 0 : entityHj.getZg41s();
                        entityHj.setZg41s(ts41s + 1);
                    }
                } else if (title == 3002) {
                    // 3002 副高
                    if (age <= 25) {
                        t25x = entity.getFg25x() == null ? 0 : entity.getFg25x();
                        entity.setFg25x(t25x + 1);

                        ts25x = entityHj.getFg25x() == null ? 0 : entityHj.getFg25x();
                        entityHj.setFg25x(ts25x + 1);
                    } else if (age >= 26 && age <= 30) {
                        t30 = entity.getFg30() == null ? 0 : entity.getFg30();
                        entity.setFg30(t30 + 1);

                        ts30 = entityHj.getFg30() == null ? 0 : entityHj.getFg30();
                        entityHj.setFg30(ts30 + 1);
                    } else if (age >= 31 && age <= 35) {
                        t35 = entity.getFg35() == null ? 0 : entity.getFg35();
                        entity.setFg35(t35 + 1);

                        ts35 = entityHj.getFg35() == null ? 0 : entityHj.getFg35();
                        entityHj.setFg35(ts35 + 1);
                    } else if (age >= 36 && age <= 40) {
                        t40 = entity.getFg40() == null ? 0 : entity.getFg40();
                        entity.setFg40(t40 + 1);

                        ts40 = entityHj.getFg40() == null ? 0 : entityHj.getFg40();
                        entityHj.setFg40(ts40 + 1);
                    } else {
                        t41s = entity.getFg41s() == null ? 0 : entity.getFg41s();
                        entity.setFg41s(t41s + 1);

                        ts41s = entityHj.getFg41s() == null ? 0 : entityHj.getFg41s();
                        entityHj.setFg41s(ts41s + 1);
                    }
                } else if (title == 3003) {
                    // 3003 中级
                    if (age <= 25) {
                        t25x = entity.getZj25x() == null ? 0 : entity.getZj25x();
                        entity.setZj25x(t25x + 1);

                        ts25x = entityHj.getZj25x() == null ? 0 : entityHj.getZj25x();
                        entityHj.setZj25x(ts25x + 1);
                    } else if (age >= 26 && age <= 30) {
                        t30 = entity.getZj30() == null ? 0 : entity.getZj30();
                        entity.setZj30(t30 + 1);

                        ts30 = entityHj.getZj30() == null ? 0 : entityHj.getZj30();
                        entityHj.setZj30(ts30 + 1);
                    } else if (age >= 31 && age <= 35) {
                        t35 = entity.getZj35() == null ? 0 : entity.getZj35();
                        entity.setZj35(t35 + 1);

                        ts35 = entityHj.getZj35() == null ? 0 : entityHj.getZj35();
                        entityHj.setZj35(ts35 + 1);
                    } else if (age >= 36 && age <= 40) {
                        t40 = entity.getZj40() == null ? 0 : entity.getZj40();
                        entity.setZj40(t40 + 1);

                        ts40 = entityHj.getZj40() == null ? 0 : entityHj.getZj40();
                        entityHj.setZj40(ts40 + 1);
                    } else {
                        t41s = entity.getZj41s() == null ? 0 : entity.getZj41s();
                        entity.setZj41s(t41s + 1);

                        ts41s = entityHj.getZj41s() == null ? 0 : entityHj.getZj41s();
                        entityHj.setZj41s(ts41s + 1);
                    }
                } else if (title == 3004) {
                    // 3004 初级
                    if (age <= 25) {
                        t25x = entity.getCj25x() == null ? 0 : entity.getCj25x();
                        entity.setCj25x(t25x + 1);

                        ts25x = entityHj.getCj25x() == null ? 0 : entityHj.getCj25x();
                        entityHj.setCj25x(ts25x + 1);
                    } else if (age >= 26 && age <= 30) {
                        t30 = entity.getCj30() == null ? 0 : entity.getCj30();
                        entity.setCj30(t30 + 1);

                        ts30 = entityHj.getCj30() == null ? 0 : entityHj.getCj30();
                        entityHj.setCj30(ts30 + 1);
                    } else if (age >= 31 && age <= 35) {
                        t35 = entity.getCj35() == null ? 0 : entity.getCj35();
                        entity.setCj35(t35 + 1);

                        ts35 = entityHj.getCj35() == null ? 0 : entityHj.getCj35();
                        entityHj.setCj35(ts35 + 1);
                    } else if (age >= 36 && age <= 40) {
                        t40 = entity.getCj40() == null ? 0 : entity.getCj40();
                        entity.setCj40(t40 + 1);

                        ts40 = entityHj.getCj40() == null ? 0 : entityHj.getCj40();
                        entityHj.setCj40(ts40 + 1);
                    } else {
                        t41s = entity.getCj41s() == null ? 0 : entity.getCj41s();
                        entity.setCj41s(t41s + 1);

                        ts41s = entityHj.getCj41s() == null ? 0 : entityHj.getCj41s();
                        entityHj.setCj41s(ts41s + 1);
                    }
                } else if (title == 3005) {
                    // 3005 员级
                    if (age <= 25) {
                        t25x = entity.getYj25x() == null ? 0 : entity.getYj25x();
                        entity.setYj25x(t25x + 1);

                        ts25x = entityHj.getYj25x() == null ? 0 : entityHj.getYj25x();
                        entityHj.setYj25x(ts25x + 1);
                    } else if (age >= 26 && age <= 30) {
                        t30 = entity.getYj30() == null ? 0 : entity.getYj30();
                        entity.setYj30(t30 + 1);

                        ts30 = entityHj.getYj30() == null ? 0 : entityHj.getYj30();
                        entityHj.setYj30(ts30 + 1);
                    } else if (age >= 31 && age <= 35) {
                        t35 = entity.getYj35() == null ? 0 : entity.getYj35();
                        entity.setYj35(t35 + 1);

                        ts35 = entityHj.getYj35() == null ? 0 : entityHj.getYj35();
                        entityHj.setYj35(ts35 + 1);
                    } else if (age >= 36 && age <= 40) {
                        t40 = entity.getYj40() == null ? 0 : entity.getYj40();
                        entity.setYj40(t40 + 1);

                        ts40 = entityHj.getYj40() == null ? 0 : entityHj.getYj40();
                        entityHj.setYj40(ts40 + 1);
                    } else {
                        t41s = entity.getYj41s() == null ? 0 : entity.getYj41s();
                        entity.setYj41s(t41s + 1);

                        ts41s = entityHj.getYj41s() == null ? 0 : entityHj.getYj41s();
                        entityHj.setYj41s(ts41s + 1);
                    }
                } else if (title == 3006) {
                    // 3006 未定
                    if (age <= 25) {
                        t25x = entity.getWd25x() == null ? 0 : entity.getWd25x();
                        entity.setWd25x(t25x + 1);

                        ts25x = entityHj.getWd25x() == null ? 0 : entityHj.getWd25x();
                        entityHj.setWd25x(ts25x + 1);
                    } else if (age >= 26 && age <= 30) {
                        t30 = entity.getWd30() == null ? 0 : entity.getWd30();
                        entity.setWd30(t30 + 1);

                        ts30 = entityHj.getWd30() == null ? 0 : entityHj.getWd30();
                        entityHj.setWd30(ts30 + 1);
                    } else if (age >= 31 && age <= 35) {
                        t35 = entity.getWd35() == null ? 0 : entity.getWd35();
                        entity.setWd35(t35 + 1);

                        ts35 = entityHj.getWd35() == null ? 0 : entityHj.getWd35();
                        entityHj.setWd35(ts35 + 1);
                    } else if (age >= 36 && age <= 40) {
                        t40 = entity.getWd40() == null ? 0 : entity.getWd40();
                        entity.setWd40(t40 + 1);

                        ts40 = entityHj.getWd40() == null ? 0 : entityHj.getWd40();
                        entityHj.setWd40(ts40 + 1);
                    } else {
                        t41s = entity.getWd41s() == null ? 0 : entity.getWd41s();
                        entity.setWd41s(t41s + 1);

                        ts41s = entityHj.getWd41s() == null ? 0 : entityHj.getWd41s();
                        entityHj.setWd41s(ts41s + 1);
                    }
                }
                // endregion
            }
        }
    }

    public List<EmployeeJobLevelTitleGetReport> findEmployeeJoblevelTitleGetReport(Employee employee) {
        Integer rsfw = 1001;
        Integer jobtype = 1001;
        employee.setRsfw(rsfw);
        employee.setJobtype(jobtype);
        List<EmployeeJobLevelTitleGetReport> jobLevelTitleGetReportList = new ArrayList<>();
        //岗位等级
        LambdaQueryWrapper<JobLevelDetail> queryJobLevelDetailWrapper = new LambdaQueryWrapper<>();
        queryJobLevelDetailWrapper.eq(JobLevelDetail::getJobtypeid, jobtype);
        List<JobLevelDetail> jobLevelDetailList = iJobLevelDetailService.list(queryJobLevelDetailWrapper);
        Integer wd = 9999;
        if (jobLevelDetailList.size() > 0) {
            JobLevelDetail jld = new JobLevelDetail();
            jld.setKeyy(wd);
            jld.setValuee("未定");
            jobLevelDetailList.add(jld);
        }

        List<Employee> employeeList = baseMapper.findEmployeeTitleGetReport(employee);
        List<Employee> queryEmployee = new ArrayList<>();

        JobType jtq = new JobType();
        jtq.setStartdate(employee.getStartdate());
        jtq.setJobtype(jobtype);
        List<JobType> jobTypeList = iJobTypeService.findJobTypeByStartEndDateAndJobType(jtq);
        if (jobTypeList.size() > 0) {
            List<JobType> jobTypeQuery = new ArrayList<>();
            for (Employee e : employeeList) {
                jobTypeQuery = jobTypeList.stream().filter(s -> s.getEmployeeid().equals(e.getEmployeeid())).collect(Collectors.toList());
                if (jobTypeQuery.size() > 0) {
                    e.setJoblevel(jobTypeQuery.get(0).getJoblevel());
                } else {
                    e.setJoblevel(wd);
                }
            }
        }

        EmployeeJobLevelTitleGetReport entityHj = new EmployeeJobLevelTitleGetReport();
        // 2001	专技一级、2002 专技二级、2003 专技三级、2004 专技四级、2005 专技五级、2006 专技六级、2007 专技七级
        // 2008 专技八级、2009 专技九级、2010 专技十级、2011 专技十一级、2012 专技十二级、2013 专技十三级
        for (JobLevelDetail item : jobLevelDetailList) {
            EmployeeJobLevelTitleGetReport entity = new EmployeeJobLevelTitleGetReport();
            entity.setJoblevel(item.getKeyy());
            entity.setJoblevelname(item.getValuee());
            queryEmployee = employeeList.stream().filter(s -> s.getJoblevel().equals(item.getKeyy())).collect(Collectors.toList());

            Integer xjs = 0;
            Integer xj = 0;
            for (Employee e : queryEmployee) {
                this.setEmployeeTitleGet(e, entity, entityHj);
            }
            xjs = entityHj.getXj() == null ? 0 : entityHj.getXj();
            xj = entity.getXj() == null ? 0 : entity.getXj();
            entityHj.setXj(xjs + xj);

            jobLevelTitleGetReportList.add(entity);
        }

        entityHj.setJoblevelname("合计");
        jobLevelTitleGetReportList.add(entityHj);

        return jobLevelTitleGetReportList;
    }

    private void setEmployeeTitleGet(Employee e, EmployeeJobLevelTitleGetReport entity, EmployeeJobLevelTitleGetReport entityHj) {
        entity.setRszfw(e.getRszfw());
        entity.setTitleget(e.getTitleget());
        Integer rszfw = e.getRszfw();
        Integer titleGet = e.getTitleget();
        if (rszfw != null && titleGet != null) {
            // 2001	医疗、2002 医技、2003 医药、2004 护理、2005	档案、2006 工程、2007 编辑、2008	统计、2009 会计
            // 3001	正高、3002 副高、3003 中级、3004 初级、3005 员级、3006 未定
            Integer ys = 0;
            Integer yj = 0;
            Integer hl = 0;
            Integer qt = 0;
            Integer xj = 0;
            Integer yss = 0;
            Integer yjs = 0;
            Integer hls = 0;
            Integer qts = 0;
            if (titleGet == 3001) {
                // 正高
                if (rszfw == 2001) {
                    // 医疗
                    ys = entity.getZgys() == null ? 0 : entity.getZgys();
                    entity.setZgys(ys + 1);

                    yss = entityHj.getZgys() == null ? 0 : entityHj.getZgys();
                    entityHj.setZgys(yss + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                } else if (rszfw == 2002) {
                    // 医技
                    yj = entity.getZgyj() == null ? 0 : entity.getZgyj();
                    entity.setZgyj(yj + 1);

                    yjs = entityHj.getZgyj() == null ? 0 : entityHj.getZgyj();
                    entityHj.setZgyj(yjs + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                } else if (rszfw == 2004) {
                    // 护理
                    hl = entity.getZghl() == null ? 0 : entity.getZghl();
                    entity.setZghl(hl + 1);

                    hls = entityHj.getZghl() == null ? 0 : entityHj.getZghl();
                    entityHj.setZghl(hls + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                } else {
                    // 其他
                    qt = entity.getZgqt() == null ? 0 : entity.getZgqt();
                    entity.setZgqt(qt + 1);

                    qts = entityHj.getZgqt() == null ? 0 : entityHj.getZgqt();
                    entityHj.setZgqt(qts + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                }
            } else if (titleGet == 3002) {
                // 副高
                if (rszfw == 2001) {
                    // 医疗
                    ys = entity.getFgys() == null ? 0 : entity.getFgys();
                    entity.setFgys(ys + 1);

                    yss = entityHj.getFgys() == null ? 0 : entityHj.getFgys();
                    entityHj.setFgys(yss + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                } else if (rszfw == 2002) {
                    // 医技
                    yj = entity.getFgyj() == null ? 0 : entity.getFgyj();
                    entity.setFgyj(yj + 1);

                    yjs = entityHj.getFgyj() == null ? 0 : entityHj.getFgyj();
                    entityHj.setFgyj(yjs + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                } else if (rszfw == 2004) {
                    // 护理
                    hl = entity.getFghl() == null ? 0 : entity.getFghl();
                    entity.setFghl(hl + 1);

                    hls = entityHj.getFghl() == null ? 0 : entityHj.getFghl();
                    entityHj.setFghl(hls + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                } else {
                    // 其他
                    qt = entity.getFgqt() == null ? 0 : entity.getFgqt();
                    entity.setFgqt(qt + 1);

                    qts = entityHj.getFgqt() == null ? 0 : entityHj.getFgqt();
                    entityHj.setFgqt(qts + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                }
            } else if (titleGet == 3003) {
                // 中级
                if (rszfw == 2001) {
                    // 医疗
                    ys = entity.getZjys() == null ? 0 : entity.getZjys();
                    entity.setZjys(ys + 1);

                    yss = entityHj.getZjys() == null ? 0 : entityHj.getZjys();
                    entityHj.setZjys(yss + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                } else if (rszfw == 2002) {
                    // 医技
                    yj = entity.getZjyj() == null ? 0 : entity.getZjyj();
                    entity.setZjyj(yj + 1);

                    yjs = entityHj.getZjyj() == null ? 0 : entityHj.getZjyj();
                    entityHj.setZjyj(yjs + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                } else if (rszfw == 2004) {
                    // 护理
                    hl = entity.getZjhl() == null ? 0 : entity.getZjhl();
                    entity.setZjhl(hl + 1);

                    hls = entityHj.getZjhl() == null ? 0 : entityHj.getZjhl();
                    entityHj.setZjhl(hls + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                } else {
                    // 其他
                    qt = entity.getZjqt() == null ? 0 : entity.getZjqt();
                    entity.setZjqt(qt + 1);

                    qts = entityHj.getZjqt() == null ? 0 : entityHj.getZjqt();
                    entityHj.setZjqt(qts + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                }
            } else if (titleGet == 3004) {
                // 初级
                if (rszfw == 2001) {
                    // 医疗
                    ys = entity.getCjys() == null ? 0 : entity.getCjys();
                    entity.setCjys(ys + 1);

                    yss = entityHj.getCjys() == null ? 0 : entityHj.getCjys();
                    entityHj.setCjys(yss + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                } else if (rszfw == 2002) {
                    // 医技
                    yj = entity.getCjyj() == null ? 0 : entity.getCjyj();
                    entity.setCjyj(yj + 1);

                    yjs = entityHj.getCjyj() == null ? 0 : entityHj.getCjyj();
                    entityHj.setCjyj(yjs + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                } else if (rszfw == 2004) {
                    // 护理
                    hl = entity.getCjhl() == null ? 0 : entity.getCjhl();
                    entity.setCjhl(hl + 1);

                    hls = entityHj.getCjhl() == null ? 0 : entityHj.getCjhl();
                    entityHj.setCjhl(hls + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                } else {
                    // 其他
                    qt = entity.getCjqt() == null ? 0 : entity.getCjqt();
                    entity.setCjqt(qt + 1);

                    qts = entityHj.getCjqt() == null ? 0 : entityHj.getCjqt();
                    entityHj.setCjqt(qts + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                }
            } else if (titleGet == 3005) {
                // 员级
                if (rszfw == 2001) {
                    // 医疗
                    ys = entity.getYjys() == null ? 0 : entity.getYjys();
                    entity.setYjys(ys + 1);

                    yss = entityHj.getYjys() == null ? 0 : entityHj.getYjys();
                    entityHj.setYjys(yss + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                } else if (rszfw == 2002) {
                    // 医技
                    yj = entity.getYjyj() == null ? 0 : entity.getYjyj();
                    entity.setYjyj(yj + 1);

                    yjs = entityHj.getYjyj() == null ? 0 : entityHj.getYjyj();
                    entityHj.setYjyj(yjs + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                } else if (rszfw == 2004) {
                    // 护理
                    hl = entity.getYjhl() == null ? 0 : entity.getYjhl();
                    entity.setYjhl(hl + 1);

                    hls = entityHj.getYjhl() == null ? 0 : entityHj.getYjhl();
                    entityHj.setYjhl(hls + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                } else {
                    // 其他
                    qt = entity.getYjqt() == null ? 0 : entity.getYjqt();
                    entity.setYjqt(qt + 1);

                    qts = entityHj.getYjqt() == null ? 0 : entityHj.getYjqt();
                    entityHj.setYjqt(qts + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                }
            } else if (titleGet == 3006) {
                // 未定
                if (rszfw == 2001) {
                    // 医疗
                    ys = entity.getWdys() == null ? 0 : entity.getWdys();
                    entity.setWdys(ys + 1);

                    yss = entityHj.getWdys() == null ? 0 : entityHj.getWdys();
                    entityHj.setWdys(yss + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                } else if (rszfw == 2002) {
                    // 医技
                    yj = entity.getWdyj() == null ? 0 : entity.getWdyj();
                    entity.setWdyj(yj + 1);

                    yjs = entityHj.getWdyj() == null ? 0 : entityHj.getWdyj();
                    entityHj.setWdyj(yjs + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                } else if (rszfw == 2004) {
                    // 护理
                    hl = entity.getWdhl() == null ? 0 : entity.getWdhl();
                    entity.setWdhl(hl + 1);

                    hls = entityHj.getWdhl() == null ? 0 : entityHj.getWdhl();
                    entityHj.setWdhl(hls + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                } else {
                    // 其他
                    qt = entity.getWdqt() == null ? 0 : entity.getWdqt();
                    entity.setWdqt(qt + 1);

                    qts = entityHj.getWdqt() == null ? 0 : entityHj.getWdqt();
                    entityHj.setWdqt(qts + 1);

                    xj = entity.getXj() == null ? 0 : entity.getXj();
                    entity.setXj(xj + 1);
                }
            }
        }
//        else {
//            System.out.println("hehehe:" + e);
//        }
    }

    private List<JobLevelDetail> getJobLevelDetailList(Integer zywd, Integer glwd, Integer gqwd, List<JobLevelDetail> jldList) {
        // 专技
        List<JobLevelDetail> zyJobLevelDetailList = jldList.stream().filter(s -> s.getJobtypeid() == 1001).collect(Collectors.toList());
        // 管理
        List<JobLevelDetail> glJobLevelDetailList = jldList.stream().filter(s -> s.getJobtypeid() == 1002).collect(Collectors.toList());
        // 工勤
        List<JobLevelDetail> gqJobLevelDetailList = jldList.stream().filter(s -> s.getJobtypeid() == 1003).collect(Collectors.toList());

        List<JobLevelDetail> retjldList = new ArrayList<>();
        if (zyJobLevelDetailList.size() > 0) {
            retjldList.addAll(zyJobLevelDetailList);
            JobLevelDetail jld = new JobLevelDetail();
            jld.setKeyy(zywd);
            jld.setValuee("未定");
            retjldList.add(jld);

//            JobLevelDetail jld1 = new JobLevelDetail();
//            jld1.setKeyy(777);
//            jld1.setValuee("小计");
//            retjldList.add(jld1);
        }

        if (zyJobLevelDetailList.size() > 0) {
            retjldList.addAll(glJobLevelDetailList);
            JobLevelDetail jld = new JobLevelDetail();
            jld.setKeyy(glwd);
            jld.setValuee("未定");
            retjldList.add(jld);

//            JobLevelDetail jld1 = new JobLevelDetail();
//            jld1.setKeyy(888);
//            jld1.setValuee("小计");
//            retjldList.add(jld1);
        }

        if (zyJobLevelDetailList.size() > 0) {
            retjldList.addAll(gqJobLevelDetailList);
            JobLevelDetail jld = new JobLevelDetail();
            jld.setKeyy(gqwd);
            jld.setValuee("未定");
            retjldList.add(jld);

//            JobLevelDetail jld1 = new JobLevelDetail();
//            jld1.setKeyy(999);
//            jld1.setValuee("小计");
//            retjldList.add(jld1);
        }
        return retjldList;
    }

    public List<EmployeeBirthEducationReport> findEmployeeBirthEducationReport(Employee employee) {
        List<EmployeeBirthEducationReport> list = new ArrayList<>();
        LambdaQueryWrapper<JobLevelDetail> queryJobLevelDetailWrapper = new LambdaQueryWrapper<>();
        List<JobLevelDetail> jobLevelDetailListAll = iJobLevelDetailService.list(queryJobLevelDetailWrapper);

        Integer zywd = 7777;
        Integer glwd = 8888;
        Integer gqwd = 9999;

        List<JobLevelDetail> jobLevelDetailList = this.getJobLevelDetailList(zywd, glwd, gqwd, jobLevelDetailListAll);

        JobType jtq = new JobType();
        jtq.setStartdate(employee.getStartdate());
        List<JobType> jobTypeList = iJobTypeService.findJobTypeByStartEndDateAndJobType(jtq);

        List<Employee> employeeList = baseMapper.findEmployeeBirthEducationReport(employee);
        List<Employee> queryEmployee = new ArrayList<>();

        // 人事范围
        // 1001 专技 1002 管理 1003 工勤
//        List<RsfwDetail> rsfwDetails=iRsfwDetailService.findRsfwDetail();

        // 岗位类型
        // 1001 专技 1002 管理 1003 工勤
//        List<JobTypeDetail> jobTypeDetailList = iJobTypeDetailService.findJobTypeDetail();

        if (jobTypeList.size() > 0) {
            List<JobType> jobTypeQuery = new ArrayList<>();
            for (Employee e : employeeList) {
                // 1001 专技
                if (e.getRsfw() != null && e.getRsfw().equals(1001)) {
                    jobTypeQuery = jobTypeList.stream().filter(s ->
                            s.getEmployeeid().equals(e.getEmployeeid()) &&
                                    s.getJobtype().equals(1001)
                    ).collect(Collectors.toList());

                    if (jobTypeQuery.size() > 0) {
                        e.setJoblevel(jobTypeQuery.get(0).getJoblevel());
                    } else {
                        e.setJoblevel(zywd);
                    }

                }
                // 1002 管理
                if (e.getRsfw() != null && e.getRsfw().equals(1002)) {
                    jobTypeQuery = jobTypeList.stream().filter(s ->
                            s.getEmployeeid().equals(e.getEmployeeid()) &&
                                    s.getJobtype().equals(1002)
                    ).collect(Collectors.toList());

                    if (jobTypeQuery.size() > 0) {
                        e.setJoblevel(jobTypeQuery.get(0).getJoblevel());
                    } else {
                        e.setJoblevel(glwd);
                    }

                }
                // 1003 工勤
                if (e.getRsfw() != null && e.getRsfw().equals(1003)) {
                    jobTypeQuery = jobTypeList.stream().filter(s ->
                            s.getEmployeeid().equals(e.getEmployeeid()) &&
                                    s.getJobtype().equals(1003)
                    ).collect(Collectors.toList());

                    if (jobTypeQuery.size() > 0) {
                        e.setJoblevel(jobTypeQuery.get(0).getJoblevel());
                    } else {
                        e.setJoblevel(gqwd);
                    }

                }
            }
        }
        EmployeeBirthEducationReport entityXj = new EmployeeBirthEducationReport();
        for (JobLevelDetail item : jobLevelDetailList) {
            EmployeeBirthEducationReport entity = new EmployeeBirthEducationReport();
            entity.setId(UUID.randomUUID().toString());
            entity.setJoblevel(item.getKeyy());
            entity.setJoblevelname(item.getValuee());
            if (item.getJobtypeid() == 1001 || item.getKeyy() == 7777) {
                entity.setRylb("专业技术人员");
            }
            if (item.getJobtypeid() == 1002 || item.getKeyy() == 8888) {
                entity.setRylb("管理人员");
            }
            if (item.getJobtypeid() == 1003 || item.getKeyy() == 9999) {
                entity.setRylb("工勤人员");
            }
            queryEmployee = employeeList.stream().filter(s -> s.getJoblevel() != null && s.getJoblevel().equals(item.getKeyy())).collect(Collectors.toList());

            for (Employee e : queryEmployee) {
                this.setEmployeeBirthEducation(e, entity, entityXj);
            }
            list.add(entity);
            if (item.getValuee().equals("未定")) {
                if (item.getJobtypeid() == 1001 || item.getKeyy() == 7777) {
                    entityXj.setJoblevel(777);
                    entityXj.setJoblevelname("小计");
                    entityXj.setRylb("专业技术人员");
                }
                if (item.getJobtypeid() == 1002 || item.getKeyy() == 8888) {
                    entityXj.setJoblevel(888);
                    entityXj.setJoblevelname("小计");
                    entityXj.setRylb("管理人员");
                }
                if (item.getJobtypeid() == 1003 || item.getKeyy() == 9999) {
                    entityXj.setJoblevel(999);
                    entityXj.setJoblevelname("小计");
                    entityXj.setRylb("工勤人员");
                }
                list.add(entityXj);
                entityXj = new EmployeeBirthEducationReport();
            }
        }

        return list;
    }

    private void setEmployeeBirthEducation(Employee e, EmployeeBirthEducationReport entity, EmployeeBirthEducationReport entityXj) {
        Integer age = null;
        if (e.getBirth() != null) {
            age = DateUtil.getAge(e.getBirth());
        }
        if (age != null) {
            Integer nl35x = 0;
            Integer nl40 = 0;
            Integer nl45 = 0;
            Integer nl50 = 0;
            Integer nl54 = 0;
            Integer nl59 = 0;
            Integer nl60s = 0;
            Integer nl35xs = 0;
            Integer nl40s = 0;
            Integer nl45s = 0;
            Integer nl50s = 0;
            Integer nl54s = 0;
            Integer nl59s = 0;
            Integer nl60ss = 0;
            if (age <= 35) {
                nl35x = entity.getNl35x() == null ? 0 : entity.getNl35x();
                entity.setNl35x(nl35x + 1);

                nl35xs = entityXj.getNl35x() == null ? 0 : entityXj.getNl35x();
                entityXj.setNl35x(nl35xs + 1);
            } else if (age >= 36 && age <= 40) {
                nl40 = entity.getNl40() == null ? 0 : entity.getNl40();
                entity.setNl40(nl40 + 1);

                nl40s = entityXj.getNl40() == null ? 0 : entityXj.getNl40();
                entityXj.setNl40(nl40s + 1);
            } else if (age >= 41 && age <= 45) {
                nl45 = entity.getNl45() == null ? 0 : entity.getNl45();
                entity.setNl45(nl45 + 1);

                nl45s = entityXj.getNl45() == null ? 0 : entityXj.getNl45();
                entityXj.setNl45(nl45s + 1);
            } else if (age >= 46 && age <= 50) {
                nl50 = entity.getNl50() == null ? 0 : entity.getNl50();
                entity.setNl50(nl50 + 1);

                nl50s = entityXj.getNl50() == null ? 0 : entityXj.getNl50();
                entityXj.setNl50(nl50s + 1);
            } else if (age >= 51 && age <= 54) {
                nl54 = entity.getNl54() == null ? 0 : entity.getNl54();
                entity.setNl54(nl54 + 1);

                nl54s = entityXj.getNl54() == null ? 0 : entityXj.getNl54();
                entityXj.setNl54(nl54s + 1);
            } else if (age >= 55 && age <= 59) {
                nl59 = entity.getNl59() == null ? 0 : entity.getNl59();
                entity.setNl59(nl59 + 1);

                nl59s = entityXj.getNl59() == null ? 0 : entityXj.getNl59();
                entityXj.setNl59(nl59s + 1);
            } else if (age >= 60) {
                nl60s = entity.getNl60s() == null ? 0 : entity.getNl60s();
                entity.setNl60s(nl60s + 1);

                nl60ss = entityXj.getNl60s() == null ? 0 : entityXj.getNl60s();
                entityXj.setNl60s(nl60ss + 1);
            }
        }
        if (e.getAcademic() != null) {
            // 1 初中 2 高中 3 中专 4 大专 5 本科 6 硕士 7 博士
            Integer xlbk = 0;
            Integer xlss = 0;
            Integer xlbs = 0;
            Integer xldzys = 0;
            Integer xlxj = 0;

            Integer xlbks = 0;
            Integer xlsss = 0;
            Integer xlbss = 0;
            Integer xldzyss = 0;
            Integer xlxjs = 0;
            if (e.getAcademic() == 5) {
                // 5 本科
                xlbk = entity.getXlbk() == null ? 0 : entity.getXlbk();
                entity.setXlbk(xlbk + 1);

                xlxj = entity.getXlxj() == null ? 0 : entity.getXlxj();
                entity.setXlxj(xlxj + 1);

                xlbks = entityXj.getXlbk() == null ? 0 : entityXj.getXlbk();
                entityXj.setXlbk(xlbks + 1);

                xlxjs = entityXj.getXlxj() == null ? 0 : entityXj.getXlxj();
                entityXj.setXlxj(xlxjs + 1);
            } else if (e.getAcademic() == 6) {
                // 6 硕士
                xlss = entity.getXlss() == null ? 0 : entity.getXlss();
                entity.setXlss(xlss + 1);

                xlxj = entity.getXlxj() == null ? 0 : entity.getXlxj();
                entity.setXlxj(xlxj + 1);

                xlsss = entityXj.getXlss() == null ? 0 : entityXj.getXlss();
                entityXj.setXlss(xlsss + 1);

                xlxjs = entityXj.getXlxj() == null ? 0 : entityXj.getXlxj();
                entityXj.setXlxj(xlxjs + 1);
            } else if (e.getAcademic() == 7) {
                // 7 博士
                xlbs = entity.getXlbs() == null ? 0 : entity.getXlbs();
                entity.setXlbs(xlbs + 1);

                xlxj = entity.getXlxj() == null ? 0 : entity.getXlxj();
                entity.setXlxj(xlxj + 1);

                xlbss = entityXj.getXlbs() == null ? 0 : entityXj.getXlbs();
                entityXj.setXlbs(xlbss + 1);

                xlxjs = entityXj.getXlxj() == null ? 0 : entityXj.getXlxj();
                entityXj.setXlxj(xlxjs + 1);
            } else {
                // 大专及以下
                xldzys = entity.getXldzyx() == null ? 0 : entity.getXldzyx();
                entity.setXldzyx(xldzys + 1);

                xlxj = entity.getXlxj() == null ? 0 : entity.getXlxj();
                entity.setXlxj(xlxj + 1);

                xldzyss = entityXj.getXldzyx() == null ? 0 : entityXj.getXldzyx();
                entityXj.setXldzyx(xldzyss + 1);

                xlxjs = entityXj.getXlxj() == null ? 0 : entityXj.getXlxj();
                entityXj.setXlxj(xlxjs + 1);
            }
        }
    }

    @Override
    @Transactional
    public void importEmployeeCoreEducationEvnt(List<EmployeeCoreEducationExcel> eceList) {
        List<Code> codeList = iCodeService.findCodeMaxEndDateLists();
        List<Code> codeQuery = new ArrayList<>();
        List<Employee> employeeList = this.list();
        List<Employee> employeeQuery = new ArrayList<>();
//        List<EmployeeCore> employeeCoreList = iEmployeeCoreService.findEmployeeCoreMaxEndDateLists();
        List<Department> departmentList = iDepartmentService.list();
        List<Department> departmentQuery = new ArrayList<>();
        List<Department> departmentParentQuery = new ArrayList<>();
        List<Position> positionList = iPositionService.list();
        List<Position> positionQuery = new ArrayList<>();
        List<JobDetail> jobDetailList = iJobDetailService.list();
        List<JobDetail> jobDetailQuery = new ArrayList<>();
        Date enddate = cn.hutool.core.date.DateUtil.parse("9999-12-31");
        Date thisDate = new Date();
        for (EmployeeCoreEducationExcel ece : eceList) {
            employeeQuery = employeeList.stream().filter(s -> s.getIdnumber().equals(ece.getIdnumber())).collect(Collectors.toList());
            if (employeeQuery.size() == 0) {
                codeQuery = codeList.stream().filter(s -> s.getEmployeecode().equals(ece.getEmployeecode())).collect(Collectors.toList());
                if (codeQuery.size() == 0) {
                    Date startdate = ece.getRzdate();
                    // 人员基本信息
                    Employee employee = new Employee();
                    employee.setEmployeename(ece.getEmployeename()); // 姓名
                    employee.setGender(ece.getGender()); // 性别
                    employee.setBirth(ece.getBirth()); // 出生年月
                    employee.setIdnumber(ece.getIdnumber()); // 身份证号
                    employee.setPhonenumber(ece.getPhonenumber()); // 手机号
                    employee.setHomeaddress(ece.getHomeaddress()); // 籍贯
                    employee.setNation(ece.getNation()); // 民族
                    employee.setWorkdate(ece.getWorkdate()); // 参加工作时间
                    employee.setComedate(ece.getComedate()); // 来院时间
                    employee.setPoliticaloutlook(ece.getPoliticaloutlook()); // 政治面貌code
                    employee.setPoliticaloutlookname(ece.getPoliticaloutlookname()); // 政治面貌name
                    employee.setRddate(ece.getRddate()); // 入党时间
                    employee.setRtdate(ece.getRtdate()); // 入团时间
                    this.save(employee);
                    // 人员编号信息
                    Code code = new Code();
                    code.setEmployeeid(employee.getEmployeeid());
                    code.setStartdate(startdate);
                    code.setEnddate(enddate);
                    code.setEmployeecode(ece.getEmployeecode());
                    iCodeService.save(code);
                    // 职工核心信息
                    EmployeeCore employeeCore = new EmployeeCore();
                    employeeCore.setEmployeeid(employee.getEmployeeid());
                    employeeCore.setStartdate(startdate);
                    employeeCore.setEnddate(enddate);
                    employeeCore.setStatus(1); // 人员状态
                    employeeCore.setStatusname("在职");
                    // 病区 科室
                    departmentQuery = departmentList.stream().filter(s -> s.getCode().equals(ece.getWardname())).collect(Collectors.toList());
                    if (departmentQuery.size() > 0) {
                        String deptParent = departmentQuery.get(0).getParentid();
                        departmentParentQuery = departmentList.stream().filter(s -> s.getKeyy().equals(deptParent)).collect(Collectors.toList());
                        if (departmentParentQuery.size() > 0) {
                            // 病区
                            employeeCore.setWard(departmentQuery.get(0).getKeyy());
                            employeeCore.setWardname(departmentQuery.get(0).getValuee());
                            // 科室 父级
                            employeeCore.setDepttype(departmentParentQuery.get(0).getParentid());
                            // 科室
                            employeeCore.setDept(departmentQuery.get(0).getParentid());
                            employeeCore.setDeptname(departmentQuery.get(0).getParentvaluee());
                        }
                    }
                    // 岗位
                    jobDetailQuery = jobDetailList.stream().filter(s->s.getValuee().equals(ece.getJobname())).collect(Collectors.toList());
                    if(jobDetailQuery.size() > 0) {
                        employeeCore.setJob(jobDetailQuery.get(0).getKeyy());
                        employeeCore.setJobname(ece.getJobname());
                    }

                    // 职位
                    positionQuery = positionList.stream().filter(s->s.getValuee().equals(ece.getPositionname())).collect(Collectors.toList());
                    if(positionQuery.size() > 0) {
                        employeeCore.setPosition(positionQuery.get(0).getKeyy());
                        employeeCore.setPositionname(ece.getPositionname());
                    }
                    employeeCore.setEmployeetype(ece.getEmployeetype()); // 人员类型
                    employeeCore.setEmployeetypename(ece.getEmployeetypename());
                    employeeCore.setRsfw(ece.getRsfw()); // 人事范围
                    employeeCore.setRsfwname(ece.getRsfwname());
                    employeeCore.setRszfw(ece.getRszfw()); // // 人事子范围
                    employeeCore.setRszfwname(ece.getRszfwname());
                    employeeCore.setEventtype(1); // 人事事件
                    employeeCore.setEventtypename("入职");
                    employeeCore.setXtupdateby("inport");
                    employeeCore.setXtupdatedate(thisDate);
                    iEmployeeCoreService.save(employeeCore);

                    // 人事事件
                    Event event = new Event();
                    event.setEmployeeid(employee.getEmployeeid());
                    event.setCoreid(employeeCore.getId());
                    event.setStartdate(startdate);
                    event.setEvent(1);
                    event.setEventname("入职");
                    event.setEventdate(startdate);
                    event.setXtUpdateby("inport");
                    event.setXtUpdatedate(thisDate);
                    iEventService.save(event);

                    // 学历 1
                    if (ece.getByenddate1() != null && ece.getStudymethod1() != null) {
                        Education education1 = new Education();
                        education1.setEmployeeid(employee.getEmployeeid());
                        education1.setEnddate(ece.getByenddate1()); // 毕业时间
                        education1.setAcademic(ece.getAcademic1()); // 学历
                        education1.setSchool(ece.getSchool1()); // 毕业学校
                        education1.setProfession(ece.getProfession1()); // 专业
                        education1.setDegrees(ece.getDegrees1()); // 学位
                        education1.setStudymethod(ece.getStudymethod1()); // 学习方式
                        education1.setStudymethodname(ece.getStudymethodname1()); // 学习方式名称
                        education1.setTutor(ece.getTutor1()); // 导师
                        education1.setZqflag(1); // 是否职前 默认 1 是 0 否
                        education1.setXtUpdateby("inport"); // 更新人
                        education1.setXtUpdatedate(thisDate); // 更新时间
                        iEducationService.save(education1);
                    }
                    // 学历 2
                    if (ece.getByenddate2() != null && ece.getStudymethod2() != null) {
                        Education education2 = new Education();
                        education2.setEmployeeid(employee.getEmployeeid());
                        education2.setEnddate(ece.getByenddate2()); // 毕业时间
                        education2.setAcademic(ece.getAcademic2()); // 学历
                        education2.setSchool(ece.getSchool2()); // 毕业学校
                        education2.setProfession(ece.getProfession2()); // 专业
                        education2.setDegrees(ece.getDegrees2()); // 学位
                        education2.setStudymethod(ece.getStudymethod2()); // 学习方式
                        education2.setStudymethodname(ece.getStudymethodname2()); // 学习方式名称
                        education2.setTutor(ece.getTutor2()); // 导师
                        education2.setZqflag(1); // 是否职前 默认 1 是 0 否
                        education2.setXtUpdateby("inport"); // 更新人
                        education2.setXtUpdatedate(thisDate); // 更新时间
                        iEducationService.save(education2);
                    }

                    // 学历 2
                    if (ece.getByenddate3() != null && ece.getStudymethod3() != null) {
                        Education education3 = new Education();
                        education3.setEmployeeid(employee.getEmployeeid());
                        education3.setEnddate(ece.getByenddate3()); // 毕业时间
                        education3.setAcademic(ece.getAcademic3()); // 学历
                        education3.setSchool(ece.getSchool3()); // 毕业学校
                        education3.setProfession(ece.getProfession3()); // 专业
                        education3.setDegrees(ece.getDegrees3()); // 学位
                        education3.setStudymethod(ece.getStudymethod3()); // 学习方式
                        education3.setStudymethodname(ece.getStudymethodname3()); // 学习方式名称
                        education3.setTutor(ece.getTutor3()); // 导师
                        education3.setZqflag(1); // 是否职前 默认 1 是 0 否
                        education3.setXtUpdateby("inport"); // 更新人
                        education3.setXtUpdatedate(thisDate); // 更新时间
                        iEducationService.save(education3);
                    }

                    // 学历 4
                    if (ece.getByenddate4() != null && ece.getStudymethod4() != null) {
                        Education education4 = new Education();
                        education4.setEmployeeid(employee.getEmployeeid());
                        education4.setEnddate(ece.getByenddate4()); // 毕业时间
                        education4.setAcademic(ece.getAcademic4()); // 学历
                        education4.setSchool(ece.getSchool4()); // 毕业学校
                        education4.setProfession(ece.getProfession4()); // 专业
                        education4.setDegrees(ece.getDegrees4()); // 学位
                        education4.setStudymethod(ece.getStudymethod4()); // 学习方式
                        education4.setStudymethodname(ece.getStudymethodname4()); // 学习方式名称
                        education4.setTutor(ece.getTutor4()); // 导师
                        education4.setZqflag(1); // 是否职前 默认 1 是 0 否
                        education4.setXtUpdateby("inport"); // 更新人
                        education4.setXtUpdatedate(thisDate); // 更新时间
                        iEducationService.save(education4);
                    }
                }
            }
        }
    }
}
