package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.dao.DictMapper;
import cc.mrbird.febs.system.domain.*;
import cc.mrbird.febs.system.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service("dictService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Autowired
    IRsfwDetailService iRsfwDetailService;

    @Autowired
    IJobTypeDetailService iJobTypeDetailService;

    @Autowired
    IJobDetailService iJobDetailService;

    @Autowired
    IPositionService iPositionService;

    @Autowired
    ITitleDetailService iTitleDetailService;

    @Autowired
    IRszfwDetailService iRszfwDetailService;

    @Autowired
    IDepartmentService iDepartmentService;

    @Autowired
    IWardService iWardService;

    @Autowired
    IDutyDetailService iDutyDetailService;

    @Autowired
    IJobLevelDetailService iJobLevelDetailService;

    @Autowired
    IKqPbDetailService iKqPbDetailService;

    @Autowired
    IKqDeptService iKqDeptService;
    @Override
    public IPage<Dict> findDicts(QueryRequest request, Dict dict) {
        try {
            LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();

            if (StringUtils.isNotBlank(dict.getKeyy())) {
                queryWrapper.eq(Dict::getKeyy, dict.getKeyy());
            }
            if (StringUtils.isNotBlank(dict.getValuee())) {
                queryWrapper.eq(Dict::getValuee, dict.getValuee());
            }
            if (StringUtils.isNotBlank(dict.getTableName())) {
                queryWrapper.eq(Dict::getTableName, dict.getTableName());
            }
            if (StringUtils.isNotBlank(dict.getFieldName())) {
                queryWrapper.eq(Dict::getFieldName, dict.getFieldName());
            }

            Page<Dict> page = new Page<>();
            SortUtil.handlePageSort(request, page, true);
            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取字典信息失败", e);
            return null;
        }
    }

    @Override
    public List<Dict> getFieldDict(String tableName, String fieldName){
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(tableName)) {
            queryWrapper.eq(Dict::getTableName, tableName);
        }
        if (StringUtils.isNotBlank(fieldName)) {
            queryWrapper.eq(Dict::getFieldName, fieldName);
        }
        return baseMapper.selectList(queryWrapper);

    }

    @Override
    public Map<String,Object> getDictMap(String deptid){
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        List<Dict>dicts=baseMapper.selectList(queryWrapper);
        Map<String,Object> dict=new HashMap<>();
        List<Dict>employee_status = new ArrayList<>(),employee_type=new ArrayList<>(),academic=new ArrayList<>(),degrees=new ArrayList<>(),cw=new ArrayList<>(),
        study_method=new ArrayList<>(),event=new ArrayList<>(),level=new ArrayList<>(),contracttype=new ArrayList<>(),checkResult=new ArrayList<>(),jftype=new ArrayList<>(),shstatus=new ArrayList<>(),gzxm=new ArrayList<>(),
        getLevel=new ArrayList<>();
        for(Dict d:dicts){
            if (d.getTableName().equals("employee_core")){
                if(d.getFieldName().equals("status")){
                    employee_status.add(d);
                }else if(d.getFieldName().equals("employeetype")){
                    employee_type.add(d);
                }
            }else if(d.getTableName().equals("education")){
                if(d.getFieldName().equals("academic")){
                    academic.add(d);
                }else if(d.getFieldName().equals("degrees")){
                    degrees.add(d);
                }else if(d.getFieldName().equals("study_method")){
                    study_method.add(d);
                }
            }else if(d.getTableName().equals("event")){
                event.add(d);
            }else if(d.getTableName().equals("administrative_level")){
                level.add(d);
            }else if(d.getTableName().equals("contract")){
                contracttype.add(d);
            }else if(d.getTableName().equals("checkinfo")){
                checkResult.add(d);
            }else if(d.getTableName().equals("sb")){
                jftype.add(d);
            }else if(d.getTableName().equals("sqb")){
                shstatus.add(d);
            }else if(d.getTableName().equals("tsxmmx")){
                gzxm.add(d);
            }else if(d.getTableName().equals("family")){
                cw.add(d);
            }else if(d.getTableName().equals("gqtitle")){
                getLevel.add(d);
            }
        }
        dict.put("cw",cw);
        dict.put("gzxm",gzxm);
        dict.put("academic",academic);
        dict.put("degrees",degrees);
        dict.put("studymethod",study_method);
        dict.put("status",employee_status);
        dict.put("employeetype",employee_type);
        dict.put("eventtype",event);
        dict.put("level",level);
        dict.put("contracttype",contracttype);
        dict.put("checkresult",checkResult);
        dict.put("jftype",jftype);
        dict.put("shstatus",shstatus);
        dict.put("getlevel",getLevel);
        List<RsfwDetail> rsfwDetails=iRsfwDetailService.findRsfwDetail();
        //人事范围
        dict.put("rsfw",rsfwDetails);
        List<KqPbDetail> deptPbList=iKqPbDetailService.getPbList(deptid,FebsConstant.PB_STATUS_QY);
        //科室排班
        dict.put("deptpb",deptPbList);
        List<KqPbDetail>tyPbList=iKqPbDetailService.getPbList("TY",FebsConstant.PB_STATUS_QY);
        dict.put("typb",tyPbList);
        List<KqPbDetail> yjPbList=new ArrayList<>();
        List<KqPbDetail> hlPbList=iKqPbDetailService.getPbList("HL",FebsConstant.PB_STATUS_QY);
        //医生需要额外增加班种的科室
        if(deptid.equals("1058")){
            yjPbList.addAll(iKqPbDetailService.getPbList("EK", FebsConstant.PB_STATUS_QY));
        }
        if(deptid.equals("1026")||deptid.equals("1027")){
            yjPbList.addAll(iKqPbDetailService.getPbList("SNK",FebsConstant.PB_STATUS_QY));
        }
        if(deptid.equals("1090")||deptid.equals("1039")||deptid.equals("1038")||deptid.equals("1089")){
            List<KqPbDetail> kqPbDetails=iKqPbDetailService.getPbList("GRK",FebsConstant.PB_STATUS_QY);
            yjPbList.addAll(kqPbDetails);
            hlPbList.addAll(kqPbDetails);
        }
        yjPbList.addAll(iKqPbDetailService.getPbList("YJ",FebsConstant.PB_STATUS_QY));
        dict.put("yjpb",yjPbList);
        dict.put("hlpb",hlPbList);
        List<KqPbDetail> kq=iKqPbDetailService.getPbList(null,null);
        dict.put("kq",kq);
        List<KqDept> kqDeptList=iKqDeptService.findKqDepartmentList();
        dict.put("kqdept",kqDeptList);
        List<RszfwDetail>rszfwDetails=iRszfwDetailService.findAllRszfwDetail();
        //人事子范围
        dict.put("rszfw",rszfwDetails);
        List<JobLevelDetail>jobLevelDetails=iJobLevelDetailService.findJobLevelDetail(null);
        List<Position> positions=iPositionService.findPositionDetail();
        //职位信息
        dict.put("position",positions);
        //岗位等级
        dict.put("joblevel",jobLevelDetails);
        List<TitleDetail> titleDetails=iTitleDetailService.findTitleDetail();
        //职称
        dict.put("title",titleDetails);
        List<JobTypeDetail>jobTypeDetails=iJobTypeDetailService.findJobTypeDetail();
        //岗位类型
        dict.put("jobtype",jobTypeDetails);
        List<JobDetail> jobDetails=iJobDetailService.findJobDetail();
        //岗位
        dict.put("job",jobDetails);
        List<DutyDetail>dutyDetails=iDutyDetailService.findDutyDetail(null);
        //专业技术职务
        dict.put("duty",dutyDetails);
        Department department=new Department();
        List<Department>deptTypes=new ArrayList<>();
        List<Department>departmentList=iDepartmentService.findDepartmentList(department);
        List<Department>departmentsDict=new ArrayList<>();
        List<Department>wardsDict=new ArrayList<>();
        departmentList.stream().forEach(i->{
            if (i.getPath().length()==12){
                deptTypes.add(i);
            }else if(i.getPath().length()==16){
                departmentsDict.add(i);
            }else if(i.getPath().length()==20){
                wardsDict.add(i);
            }
        });
        //科室类型
        dict.put("depttype",deptTypes);
        //科室
        dict.put("dept",departmentsDict);
        //病区
        dict.put("ward",wardsDict);
        return dict;
    }
    @Override
    @Transactional
    public void createDict(Dict dict) {
        this.save(dict);
    }

    @Override
    @Transactional
    public void updateDict(Dict dict) {
        this.baseMapper.updateById(dict);
    }

    @Override
    @Transactional
    public void deleteDicts(String[] dictIds) {
        List<String> list = Arrays.asList(dictIds);
        this.baseMapper.deleteBatchIds(list);
    }

    public void getDictNameByEmployees(List<Employee> employeeList){
        List<Department> departmentList=iDepartmentService.findDepartmentList(new Department());
        List<RsfwDetail> rsfwDetailList=iRsfwDetailService.findRsfwDetail();
        List<RszfwDetail> rszfwDetailList=iRszfwDetailService.findAllRszfwDetail();
        List<JobLevelDetail> jobLevelDetailList=iJobLevelDetailService.findJobLevelDetail(null);
        List<DutyDetail> dutyDetailList=iDutyDetailService.findDutyDetail(null);
        List<TitleDetail> titleDetailList=iTitleDetailService.findTitleDetail();
        for(Employee e: employeeList){
            if(e.getDeptname()!=null){
                e.setDeptname(iDepartmentService.getName(departmentList,e.getDeptname()));
            }
            if(e.getWardname()!=null){
                e.setWardname(iDepartmentService.getName(departmentList,e.getWardname()));
            }
            if(e.getRsfwname()!=null){
                e.setRsfwname(iRsfwDetailService.getName(rsfwDetailList,e.getRsfwname()));
            }
            if (e.getRszfwname() != null) {
                e.setRszfwname(iRszfwDetailService.getName(rszfwDetailList,e.getRszfwname()));
            }
            if(e.getTechnicallevelname()!=null){
                e.setTechnicallevelname(iJobLevelDetailService.getName(jobLevelDetailList,e.getTechnicallevelname()));
            }
            if(e.getMangelevelname()!=null){
                e.setMangelevelname(iJobLevelDetailService.getName(jobLevelDetailList,e.getMangelevelname()));
            }
            if(e.getWorkerlevelname()!=null){
                e.setWorkerlevelname(iJobLevelDetailService.getName(jobLevelDetailList,e.getWorkerlevelname()));
            }
            if(e.getDutyname()!=null){
                e.setDutyname(iDutyDetailService.getName(dutyDetailList,e.getDutyname()));
            }
            if(e.getTitlename()!=null){
                e.setTitlename(iTitleDetailService.getName(titleDetailList,e.getTitlename()));
            }
            if(e.getQddutyname()!=null){
                e.setQddutyname(iDutyDetailService.getName(dutyDetailList,e.getQddutyname()));
            }
            if(e.getQdtitlename()!=null){
                e.setQdtitlename(iTitleDetailService.getName(titleDetailList,e.getQdtitlename()));
            }
        }
    }

    public void getDictNameByEvents(List<Event> eventList){
        List<Department> departmentList=iDepartmentService.findDepartmentList(new Department());
        List<RsfwDetail> rsfwDetailList=iRsfwDetailService.findRsfwDetail();
        List<RszfwDetail> rszfwDetailList=iRszfwDetailService.findAllRszfwDetail();
        for(Event e: eventList){
            if(e.getWardname()!=null){
                e.setWardname(iWardService.getName(e.getWardname()));
            }
            if (e.getDeptname() != null) {
                e.setDeptname(iDepartmentService.getName(departmentList,e.getDeptname()));
            }
            if(e.getRszfwname()!=null){
                e.setRsfwname(iRsfwDetailService.getName(rsfwDetailList,e.getRsfwname()));
            }
            if (e.getRszfwname() != null) {
                e.setRszfwname(iRszfwDetailService.getName(rszfwDetailList,e.getRszfwname()));
            }
        }
    }

    public void getDictNameByEmoluments(List<Emolument> emolumentList){
        List<RszfwDetail> rszfwDetailList=iRszfwDetailService.findAllRszfwDetail();
        for(Emolument e: emolumentList){
            if (e.getRszfws() != null) {
                e.setRszfw(iRszfwDetailService.getName(rszfwDetailList,e.getRszfw()));
            }
        }
    }

    public void getDictNameByKqhzs(List<KqHz> kqHzList){
        for(KqHz k: kqHzList){
            if(k.getDept()!=null){
                k.setDept(iWardService.getName(k.getDept()));
            }
        }
    }
}
