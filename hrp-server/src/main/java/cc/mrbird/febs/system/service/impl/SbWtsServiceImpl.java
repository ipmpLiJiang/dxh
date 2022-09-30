package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.system.dao.SbWtsMapper;
import cc.mrbird.febs.system.domain.*;
import cc.mrbird.febs.system.service.*;
import cc.mrbird.febs.ylj.entity.YljBRecord;
import cc.mrbird.febs.ylj.service.IYljBRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Service
public class SbWtsServiceImpl extends ServiceImpl<SbWtsMapper, SbWts> implements ISbWtsService {

    @Autowired
    ISbYanglaoService iSbYanglaoService;

    @Autowired
    ISbYlService iSbYlService;

    @Autowired
    ISbDeylService iSbDeylService;

    @Autowired
    ISbSyService iSbSyService;

    @Autowired
    IYljBRecordService iYljBRecordService;

    @Autowired
    IEmployeeService iEmployeeService;

    @Autowired
    IDepartmentService iDepartmentService;

    @Autowired
    IRszfwDetailService iRszfwDetailService;


    public List<SbWts> getSbWtsList(SbWts sbWts) {
        List<SbWts> sbWtsList = new ArrayList<>();

        List<SbYanglao> yanglaoList = iSbYanglaoService.getYanglaoByIdNumberNotEmolumentLists(sbWts.getPeriod(), 1);

        List<SbYl> ylList = iSbYlService.getYlByIdNumberNotEmolumentLists(sbWts.getPeriod(), 1);

        List<SbSy> syList = iSbSyService.getSyByIdNumberNotEmolumentLists(sbWts.getPeriod(), 1);

        List<SbDeyl> deylList = iSbDeylService.getDeylByIdNumberNotEmolumentLists(sbWts.getPeriod(), 1);

        List<YljBRecord> recordList = iYljBRecordService.getRecordByIdCardNotEmolumentLists(sbWts.getPeriod(), "当期应缴");


        // 养老
        for (SbYanglao item : yanglaoList) {
            if (StringUtils.isNotBlank(item.getIdnumber())) {
                SbWts wts = new SbWts();
                wts.setEmployeename(item.getEmployeename());
                wts.setIdnumber(item.getIdnumber());
                wts.setType("yanglao");
                wts.setYanglaoTotal(item.getYj());
                sbWtsList.add(wts);
            }
        }

        // 医疗
        for (SbYl item : ylList) {
            if (StringUtils.isNotBlank(item.getIdnumber())) {
                SbWts wts = new SbWts();
                wts.setEmployeename(item.getEmployeename());
                wts.setIdnumber(item.getIdnumber());
                wts.setType("yl");
                wts.setYlTotal(item.getYj());
                sbWtsList.add(wts);
            }
        }

        // 失业
        for (SbSy item : syList) {
            if (StringUtils.isNotBlank(item.getIdnumber())) {
                SbWts wts = new SbWts();
                wts.setEmployeename(item.getEmployeename());
                wts.setIdnumber(item.getIdnumber());
                wts.setType("sy");
                wts.setSyTotal(item.getYj());
                sbWtsList.add(wts);
            }
        }

        // 大额医疗
        for (SbDeyl item : deylList) {
            if (StringUtils.isNotBlank(item.getIdnumber())) {
                SbWts wts = new SbWts();
                wts.setEmployeename(item.getEmployeename());
                wts.setIdnumber(item.getIdnumber());
                wts.setType("deyl");
                wts.setDeylTotal(item.getYj());
                sbWtsList.add(wts);
            }
        }
        // 在线养老、年金
        for (YljBRecord item : recordList) {
            if (StringUtils.isNotBlank(item.getIdCard())) {
                SbWts wts = new SbWts();
                wts.setEmployeename(item.getEmployeename());
                wts.setIdnumber(item.getIdCard());
                wts.setType("nj");
                Float ylgrjf = item.getYlgrjf() == null ? 0f : item.getYlgrjf();
                Float ylgrlx = item.getYlgrlx() == null ? 0f : item.getYlgrlx();
                Float njgrjf = item.getNjgrjf() == null ? 0f : item.getNjgrjf();
                Float grlx = item.getGrlx() == null ? 0f : item.getGrlx();

                wts.setZxylTotal(ylgrjf + ylgrlx);
                wts.setZxnjTotal(njgrjf + grlx);
                sbWtsList.add(wts);
            }
        }

        List<SbWts> list = new ArrayList<>();
        if (sbWtsList.size() > 0) {
            Employee equery = new Employee();
            equery.setStartdate(sbWts.getPeriod());
            List<Employee> employeeList = iEmployeeService.getEmployeeReport3(equery);
            List<Employee> employeeQuery = new ArrayList<>();

            Department department=new Department();
            List<Department>deptQuery=new ArrayList<>();
            List<Department>departmentList=iDepartmentService.findDepartmentList(department);

            List<RszfwDetail>rszfwDetails=iRszfwDetailService.findAllRszfwDetail();
            List<RszfwDetail>rszfwQuery= new ArrayList<>();

            Map<String, List<SbWts>> map = sbWtsList.stream().collect(Collectors.groupingBy(SbWts::getIdnumber));

            for (String idnumber : map.keySet()) {
                sbWtsList = map.get(idnumber);

                SbWts wts = new SbWts();
                wts.setIdnumber(idnumber);
                employeeQuery = employeeList.stream().filter(s -> s.getIdnumber().equals(idnumber)).collect(Collectors.toList());
                if (employeeQuery.size() > 0) {
                    Employee e = employeeQuery.get(0);
                    wts.setCode(e.getCode());
                    wts.setEmployeename(e.getEmployeename());
                    wts.setEmployeetype(e.getEmployeetype());
                    if(StringUtils.isNotBlank(e.getDeptname())) {
                        deptQuery = departmentList.stream().filter(s -> s.getKeyy().equals(e.getDeptname())).collect(Collectors.toList());
                        if(deptQuery.size() > 0)
                            wts.setDeptname(deptQuery.get(0).getValuee());
                    }
//                    wts.setRsfwName(e.getRsfwname());
                    if(StringUtils.isNotBlank(e.getRszfwname())) {
                        rszfwQuery = rszfwDetails.stream().filter(s -> s.getKeyy() == Integer.parseInt(e.getRszfwname())).collect(Collectors.toList());
                        if(rszfwQuery.size() > 0)
                        wts.setRszfwName(rszfwQuery.get(0).getValuee());
                    }
                }
                List<SbWts> yanglaoWtsList = sbWtsList.stream().filter(s -> s.getType().equals("yanglao")).collect(Collectors.toList());
                List<SbWts> ylWtsList = sbWtsList.stream().filter(s -> s.getType().equals("yl")).collect(Collectors.toList());
                List<SbWts> syWtsList = sbWtsList.stream().filter(s -> s.getType().equals("sy")).collect(Collectors.toList());
                List<SbWts> deylWtsList = sbWtsList.stream().filter(s -> s.getType().equals("deyl")).collect(Collectors.toList());
                List<SbWts> njWtsList = sbWtsList.stream().filter(s -> s.getType().equals("nj")).collect(Collectors.toList());
                if (yanglaoWtsList.size() > 0) {
                    if (StringUtils.isBlank(wts.getEmployeename())) {
                        wts.setEmployeename(yanglaoWtsList.get(0).getEmployeename());
                    }
                    wts.setYanglaoTotal(yanglaoWtsList.get(0).getYanglaoTotal());
                }
                if (ylWtsList.size() > 0) {
                    if (StringUtils.isBlank(wts.getEmployeename())) {
                        wts.setEmployeename(ylWtsList.get(0).getEmployeename());
                    }
                    wts.setYlTotal(ylWtsList.get(0).getYlTotal());
                }
                if (syWtsList.size() > 0) {
                    if (StringUtils.isBlank(wts.getEmployeename())) {
                        wts.setEmployeename(syWtsList.get(0).getEmployeename());
                    }
                    wts.setSyTotal(syWtsList.get(0).getSyTotal());
                }
                if (deylWtsList.size() > 0) {
                    if (StringUtils.isBlank(wts.getEmployeename())) {
                        wts.setEmployeename(deylWtsList.get(0).getEmployeename());
                    }
                    wts.setDeylTotal(deylWtsList.get(0).getDeylTotal());
                }
                if (njWtsList.size() > 0) {
                    if (StringUtils.isBlank(wts.getEmployeename())) {
                        wts.setEmployeename(njWtsList.get(0).getEmployeename());
                    }
                    wts.setZxylTotal(njWtsList.get(0).getZxylTotal());
                    wts.setZxnjTotal(njWtsList.get(0).getZxnjTotal());
                }
                list.add(wts);
            }
        }
        return list;
    }
}
