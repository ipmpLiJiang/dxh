package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.*;
import cc.mrbird.febs.system.domain.*;
import cc.mrbird.febs.system.service.DictService;
import cc.mrbird.febs.system.service.FileService;
import cc.mrbird.febs.system.service.IEmployeeService;
import cc.mrbird.febs.system.service.IKqRmxbService;
import cn.hutool.Hutool;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.handler.ExcelReadHandler;
import com.wuwenze.poi.pojo.ExcelErrorField;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("/system/employee")
public class EmployeeController extends BaseController {
    private String message;

    @Autowired
    IEmployeeService iEmployeeService;

    @Autowired
    DictService dictService;

    @Autowired
    FileService fileService;
    @Autowired
    IKqRmxbService iKqRmxbService;

    @GetMapping
    public List<Employee> employeeList(String search) {
        return this.iEmployeeService.findEmployees(search);
    }

    @GetMapping("/getEmployeeInfo")
    public Map<String, Object> getEmployee(Integer employeeid) {
        return this.iEmployeeService.getEmployee(employeeid);
    }

    @GetMapping("/getEmployeeReport")
    public Map<String, Object> getEmployeeReport(QueryRequest request, Employee employee) {
        Page<Employee> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        return getDataTable(this.iEmployeeService.getEmployeeReport(page, employee));
    }

    @GetMapping("/deptFindEmployeeList")
    public Map<String, Object> deptFindEmployeeList(QueryRequest request, Employee employee) {
        Page<Employee> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        return getDataTable(this.iEmployeeService.deptFindEmployeeList(page, employee));
    }

    @GetMapping("/userAttandance")
    public Map<String, Object> userAttandance(QueryRequest request, Employee employee) {
        Page<Employee> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        IPage<Employee> listUser = this.iEmployeeService.getAttandUser(page, employee);
        List<Employee> listRecord = listUser.getRecords();

        List<AttandanceUser> allUsers = this.iKqRmxbService.getAllPbByMOnth(DateUtil.format(employee.getStartdate(), "yyyy-MM"));
        for (Employee employee2 : listRecord
        ) {
            String empId = String.valueOf(employee2.getEmployeeid());
            List<AttandanceUser> attandances = allUsers.stream().filter(p -> p.getEmployeeid() != null && p.getEmployeeid().equals(empId)).collect(Collectors.toList());
            employee2.setAttandanceUsers(attandances);
        }

        return getDataTable(listUser);
    }

    @PostMapping("/excelMonth")
    public void export(QueryRequest request, Employee employee, String dataJson, HttpServletResponse response) throws FebsException {
        String message = "";
        try {
            request.setPageNum(1);
            request.setPageSize(10000);
            User currentUser = FebsUtil.getCurrentUser();
            Page<Employee> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);

            request.setSortField("employeeid");
            request.setSortOrder("ascend");
            List<Employee> listRecord = this.iEmployeeService.getAttandUser(page, employee).getRecords();

            List<AttandanceUser> allUsers = this.iKqRmxbService.getAllPbByMOnth(DateUtil.format(employee.getStartdate(), "yyyy-MM"));
            ExportExcelUtils.exportCustomExcelCutome(response, listRecord, dataJson, allUsers, "");
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
        }
    }

    @GetMapping("/userAttandanceWeek")
    public Map<String, Object> userAttandanceWeek(QueryRequest request, Employee employee) {
        Page<Employee> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        IPage<Employee> listUser = this.iEmployeeService.getAttandUser(page, employee);
        List<Employee> listRecord = listUser.getRecords();

        String startDate = DateUtil.format(DateUtil.beginOfWeek(employee.getStartdate()), "yyyy-MM-dd");
        String endDate = DateUtil.format(DateUtil.endOfWeek(employee.getStartdate()), "yyyy-MM-dd");
        List<AttandanceUser> allUsers = this.iKqRmxbService.getAllPbByWeek(startDate, endDate);
        for (Employee employee2 : listRecord
        ) {
            String empId = String.valueOf(employee2.getEmployeeid());
            List<AttandanceUser> attandances = allUsers.stream().filter(p -> p.getEmployeeid() != null && p.getEmployeeid().equals(empId)).collect(Collectors.toList());
            employee2.setAttandanceUsers(attandances);
        }

        return getDataTable(listUser);
    }

    @PostMapping("/excelWeek")
    public void exportWeek(QueryRequest request, Employee employee, String dataJson, HttpServletResponse response) throws FebsException {
        String message = "";
        try {
            request.setPageNum(1);
            request.setPageSize(10000);
            User currentUser = FebsUtil.getCurrentUser();
            Page<Employee> page = new Page<>();
            SortUtil.handlePageSort(request, page, false);

            request.setSortField("employeeid");
            request.setSortOrder("ascend");
            List<Employee> listRecord = this.iEmployeeService.getAttandUser(page, employee).getRecords();

            String startDate = DateUtil.format(DateUtil.beginOfWeek(employee.getStartdate()), "yyyy-MM-dd");
            String endDate = DateUtil.format(DateUtil.endOfWeek(employee.getStartdate()), "yyyy-MM-dd");
            List<AttandanceUser> allUsers = this.iKqRmxbService.getAllPbByWeek(startDate, endDate);

            //ExcelKit.$Export(DcaBAuditdynamic.class,response).downXlsx(dcaBAuditdynamics,false);
            ExportExcelUtils.exportCustomExcelCutome(response, listRecord, dataJson, allUsers, "");
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
        }
    }

    @Log("新增职工")
    @PostMapping
    public Map<String, Object> addEmployee(@Valid Employee employee) throws FebsException {
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS, this.iEmployeeService.createEmployee(employee));
    }

    @Log("修改职工")
    @PutMapping
    public Map<String, Object> updateEmployee(@Valid Employee employee) {
        this.iEmployeeService.updateEmployee(employee);
        return getResult(HttpStatus.OK, FebsConstant.UPDATE_SUCCESS, null);
    }

    @Log("修改考勤科室")
    @PutMapping("/updateKqDept")
    public Map<String, Object> updateKqDept(Employee employee) {
        this.iEmployeeService.updateKqDept(employee.getEmployeeid(), employee.getKqdept());
        return getResult(HttpStatus.OK, FebsConstant.UPDATE_SUCCESS, null);
    }

    @Log("上传职工照片")
    @RequestMapping(value = "/picture/upload/{dataType}", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public Map<String, String> upLoadFile(@RequestParam MultipartFile file) throws FebsException {
        return fileService.upLoadFile(file);
    }

    @Log("下载职工附件")
    @GetMapping("/download/{fileName}/{path}")
    public void downLoadFile(HttpServletResponse response, @PathVariable(value = "fileName") @NotBlank(message = "文件名不能为空") String fileName, @PathVariable(value = "path") @NotBlank(message = "文件路径不能为空") String path) throws FebsException, IOException {
        fileService.downLoadOAFile(response, fileName, path);
    }

    @Log("删除职工附件")
    @DeleteMapping("/delete/{fileName}")
    public Map<String, Object> deleteFile(@PathVariable(value = "fileName") String fileName, String dataType, String data, String column) {
        fileService.deleteFile(fileName, dataType, data, column);
        return getResult(HttpStatus.OK, FebsConstant.DELETE_SUCCESS, null);
    }

    /**
     * 导出 Excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Employee employee) throws FebsException {
        try {
            List<Employee> list = this.iEmployeeService.getEmployeeReport(employee);
            dictService.getDictNameByEmployees(list);
            ExcelKit.$Export(Employee.class, response).downXlsx(list, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST, message);
        }
    }


    @PostMapping("/exportEmployeeAgeTitle")
    public void exportEmployeeAgeTitle(HttpServletResponse response, Employee employee) throws FebsException {
        try {
            String dataJson = "[" +
                    "{title: \"人事子范围\", dataIndex: \"rszfwname\"}," +
                    "{title: \"员级25x\",dataIndex: \"yj25x\"}," +
                    "{title: \"员级30\",dataIndex: \"yj30\"}," +
                    "{title: \"员级35\",dataIndex: \"yj35\"}," +
                    "{title: \"员级40\",dataIndex: \"yj40\"}," +
                    "{title: \"员级41s\",dataIndex: \"yj41s\"}," +
                    "{title: \"初级25x\",dataIndex: \"cj25x\"}," +
                    "{title: \"初级30\",dataIndex: \"cj30\"}," +
                    "{title: \"初级35\",dataIndex: \"cj35\"}," +
                    "{title: \"初级40\",dataIndex: \"cj40\"}," +
                    "{title: \"初级41s\",dataIndex: \"cj41s\"}," +
                    "{title: \"中级25x\",dataIndex: \"zj25x\"}," +
                    "{title: \"中级30\",dataIndex: \"zj30\"}," +
                    "{title: \"中级35\",dataIndex: \"zj35\"}," +
                    "{title: \"中级40\",dataIndex: \"zj40\"}," +
                    "{title: \"中级41s\",dataIndex: \"zj41s\"}," +
                    "{title: \"副高25x\",dataIndex: \"fg25x\"}," +
                    "{title: \"副高30\",dataIndex: \"fg30\"}," +
                    "{title: \"副高35\",dataIndex: \"fg35\"}," +
                    "{title: \"副高40\",dataIndex: \"fg40\"}," +
                    "{title: \"副高41s\",dataIndex: \"fg41s\"}," +
                    "{title: \"正高25x\",dataIndex: \"zg25x\"}," +
                    "{title: \"正高30\",dataIndex: \"zg30\"}," +
                    "{title: \"正高35\",dataIndex: \"zg35\"}," +
                    "{title: \"正高40\",dataIndex: \"zg40\"}," +
                    "{title: \"正高41s\",dataIndex: \"zg41s\"}," +
                    "{title: \"未定25x\",dataIndex: \"wd25x\"}," +
                    "{title: \"未定30\",dataIndex: \"wd30\"}," +
                    "{title: \"未定35\",dataIndex: \"wd35\"}," +
                    "{title: \"未定40\",dataIndex: \"wd40\"}," +
                    "{title: \"未定41s\",dataIndex: \"wd41s\"}," +
                    "]";
            List<EmployeeAgeTitleReport> list = iEmployeeService.findEmployeeAgeTitleReport(employee);
            String filePath = "c:/qc/upload/";
            String tempUrl = filePath + "employeeAgeTitleTemp.xlsx";
            ExportExcelUtils.exportTemplateExcel(response, list, dataJson, tempUrl, 2);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST, message);
        }
    }


    @GetMapping("employeeAgeTitleReportList")
    public FebsResponse getEmployeeAgeTitleReportLists(QueryRequest request, Employee employee) {
        List<EmployeeAgeTitleReport> list = iEmployeeService.findEmployeeAgeTitleReport(employee);
        return new FebsResponse().data(list);
    }

    @PostMapping("/exportEmployeeJobLevelTitleGet")
    public void exportEmployeeJobLevelTitleGet(HttpServletResponse response, Employee employee) throws FebsException {
        try {
            String dataJson = "[" +
                    "{title: \"聘任岗级\", dataIndex: \"joblevelname\"}," +
                    "{title: \"小计\",dataIndex: \"xj\"}," +
                    "{title: \"员级医师\",dataIndex: \"yjys\"}," +
                    "{title: \"员级护理\",dataIndex: \"yjhl\"}," +
                    "{title: \"员级医技\",dataIndex: \"yjyj\"}," +
                    "{title: \"员级其他\",dataIndex: \"yjqt\"}," +
                    "{title: \"初级医师\",dataIndex: \"cjys\"}," +
                    "{title: \"初级护理\",dataIndex: \"cjhl\"}," +
                    "{title: \"初级医技\",dataIndex: \"cjyj\"}," +
                    "{title: \"初级其他\",dataIndex: \"cjqt\"}," +
                    "{title: \"中级医师\",dataIndex: \"zjys\"}," +
                    "{title: \"中级护理\",dataIndex: \"zjhl\"}," +
                    "{title: \"中级医技\",dataIndex: \"zjyj\"}," +
                    "{title: \"中级其他\",dataIndex: \"zjqt\"}," +
                    "{title: \"副高医师\",dataIndex: \"fgys\"}," +
                    "{title: \"副高护理\",dataIndex: \"fghl\"}," +
                    "{title: \"副高医技\",dataIndex: \"fgyj\"}," +
                    "{title: \"副高其他\",dataIndex: \"fgqt\"}," +
                    "{title: \"正高医师\",dataIndex: \"zgys\"}," +
                    "{title: \"正高护理\",dataIndex: \"zghl\"}," +
                    "{title: \"正高医技\",dataIndex: \"zgyj\"}," +
                    "{title: \"正高其他\",dataIndex: \"zgqt\"}," +
                    "{title: \"未定医师\",dataIndex: \"wdys\"}," +
                    "{title: \"未定护理\",dataIndex: \"wdhl\"}," +
                    "{title: \"未定医技\",dataIndex: \"wdyj\"}," +
                    "{title: \"未定其他\",dataIndex: \"wdqt\"}," +
                    "]";
            List<EmployeeJobLevelTitleGetReport> list = iEmployeeService.findEmployeeJoblevelTitleGetReport(employee);
            String filePath = "c:/qc/upload/";
            String tempUrl = filePath + "employeeJobLevelTitleGetTemp.xlsx";
            ExportExcelUtils.exportTemplateExcel(response, list, dataJson, tempUrl, 2);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST, message);
        }
    }


    @GetMapping("employeeJobLevelTitleGetReportList")
    public FebsResponse getEmployeeJobLevelTitleGetReportLists(QueryRequest request, Employee employee) {
        List<EmployeeJobLevelTitleGetReport> list = iEmployeeService.findEmployeeJoblevelTitleGetReport(employee);
        return new FebsResponse().data(list);
    }

    @PostMapping("/exportEmployeeBirthEducation")
    public void exportEmployeeBirthEducation(HttpServletResponse response, Employee employee) throws FebsException {
        try {
            String dataJson = "[" +
                    "{title: \"人员类别\", dataIndex: \"rylb\"}," +
                    "{title: \"岗位等级名称\",dataIndex: \"joblevelname\"}," +
                    "{title: \"学历小计\",dataIndex: \"xlxj\"}," +
                    "{title: \"学历大专以下\",dataIndex: \"xldzyx\"}," +
                    "{title: \"学历本科\",dataIndex: \"xlbk\"}," +
                    "{title: \"学历硕士\",dataIndex: \"xlss\"}," +
                    "{title: \"学历博士\",dataIndex: \"xlbs\"}," +
                    "{title: \"年龄35以下\",dataIndex: \"nl35x\"}," +
                    "{title: \"年龄40\",dataIndex: \"nl40\"}," +
                    "{title: \"年龄45\",dataIndex: \"nl45\"}," +
                    "{title: \"年龄50\",dataIndex: \"nl50\"}," +
                    "{title: \"年龄54\",dataIndex: \"nl54\"}," +
                    "{title: \"年龄59\",dataIndex: \"nl59\"}," +
                    "{title: \"年龄60以上\",dataIndex: \"nl60s\"}," +
                    "]";
            List<EmployeeBirthEducationReport> list = iEmployeeService.findEmployeeBirthEducationReport(employee);
            String filePath = "c:/qc/upload/";
            String tempUrl = filePath + "employeeBirthEducationTemp.xlsx";
            ExportExcelUtils.exportTemplateExcel(response, list, dataJson, tempUrl, 2);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST, message);
        }
    }


    @GetMapping("employeeBirthEducationReportList")
    public FebsResponse getEmployeeBirthEducationLists(QueryRequest request, Employee employee) {
        List<EmployeeBirthEducationReport> list = iEmployeeService.findEmployeeBirthEducationReport(employee);
        return new FebsResponse().data(list);
    }

    @PostMapping("importBere")
    public FebsResponse importBeres(@RequestParam("file") MultipartFile file) throws FebsException {
        try {
            if (file.isEmpty()) {
                throw new FebsException(HttpStatus.BAD_REQUEST, "导入数据为空");
            }
            String filename = file.getOriginalFilename();
            if (!StringUtils.endsWith(filename, ".xlsx")) {
                throw new FebsException(HttpStatus.BAD_REQUEST, "只支持.xlsx类型文件导入");
            }
            // 开始导入操作
            long beginTimeMillis = System.currentTimeMillis();
            final List<EmployeeCoreEducationExcel> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            ExcelKit.$Import(EmployeeCoreEducationExcel.class).readXlsx(
                    file.getInputStream(),
                    new ExcelReadHandler<EmployeeCoreEducationExcel>() {
                        @Override
                        public void onSuccess(int sheet, int row, EmployeeCoreEducationExcel excel) {
                            // 数据校验成功时，加入集合
                            row = 2;
                            data.add(excel);
                        }

                        @Override
                        public void onError(int sheet, int row, List<ExcelErrorField> errorFields) {
                            // 数据校验失败时，记录到 error集合
                            error.add(ImmutableMap.of("row", row, "errorFields", errorFields));
                        }
                    });

            if (!data.isEmpty()) {
            }
            long time = ((System.currentTimeMillis() - beginTimeMillis));
            ImmutableMap<String, Object> result = ImmutableMap.of(
                    "time", time,
                    "data", data,
                    "error", error
            );
            return new FebsResponse().data(result);
        } catch (Exception e) {
            message = "导入Excel数据失败," + e.getMessage();
            log.error(message);
            throw new FebsException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @PostMapping("importEmployeeCoreEducation")
    public FebsResponse importEmployeeCoreEducations(@RequestParam MultipartFile file) {
        int success = 0;
        ModelMap map = new ModelMap();
        if (file.isEmpty()) {
            message = "空文件";
        } else {
            boolean blError = false;
            try {
                String filePath = "c:/qc/upload/"; // 上传后的路径
                String uid = UUID.randomUUID().toString();
                File getFile = FileHelpers.fileUpLoad(file, filePath, uid, "EmployeeCoreEducationTemp");
                Map<Integer, String> sheetMap = ImportExcelUtils.getSheelNames(getFile);

                if (sheetMap.size() > 0) {
                    List<Object[]> obj = ImportExcelUtils.importExcelBySheetIndex(getFile, 0, 0, 0);
                    if (obj.size() > 1) {
                        List<EmployeeCoreEducationExcel> eceList = new ArrayList<>();
                        if (obj.get(0).length >= 47) {
                            String[] arr = new String[]{};
                            for (int i = 2; i < obj.size(); i++) {
                                message = "数据读取失败，请确保Excel" + i + "列表数据正确无误.";
                                EmployeeCoreEducationExcel entity = new EmployeeCoreEducationExcel();
                                String strRzdate = this.importTernaryOperate(obj.get(i), 0);//入职时间
                                if (StringUtils.isNotBlank(strRzdate)) {
                                    entity.setRzdate(DateUtil.parse(strRzdate));
                                }
                                String strEmployeecode = this.importTernaryOperate(obj.get(i), 1);//工号
                                entity.setEmployeecode(strEmployeecode);
                                String strEmployeeName = this.importTernaryOperate(obj.get(i), 2);//姓名
                                entity.setEmployeename(strEmployeeName);
                                String strGender = this.importTernaryOperate(obj.get(i), 3);//性别
                                entity.setGender(strGender);
                                String strBirth = this.importTernaryOperate(obj.get(i), 4);//出生年月
                                if (StringUtils.isNotBlank(strBirth)) {
                                    entity.setBirth(DateUtil.parse(strBirth));
                                }
                                String strIdnumber = this.importTernaryOperate(obj.get(i), 5);//身份证号
                                entity.setIdnumber(strIdnumber);
                                String strPhonenumber = this.importTernaryOperate(obj.get(i), 6);//手机号
                                entity.setPhonenumber(strPhonenumber);
                                String strHomeaddress = this.importTernaryOperate(obj.get(i), 7);//籍贯
                                entity.setHomeaddress(strHomeaddress);
                                String strNation = this.importTernaryOperate(obj.get(i), 8);//民族
                                entity.setNation(strNation);
                                String strWorkdate = this.importTernaryOperate(obj.get(i), 9);//参加工作时间
                                if (StringUtils.isNotBlank(strWorkdate)) {
                                    entity.setWorkdate(DateUtil.parse(strWorkdate));
                                }
                                String strComedate = this.importTernaryOperate(obj.get(i), 10);//来院时间
                                if (StringUtils.isNotBlank(strComedate)) {
                                    entity.setComedate(DateUtil.parse(strComedate));
                                }
                                String strPoliticaloutlookName = this.importTernaryOperate(obj.get(i), 11);//政治面貌
                                if (StringUtils.isNotBlank(strPoliticaloutlookName)) {
                                    arr = strPoliticaloutlookName.split(" ");
                                    if (arr.length > 0) {
                                        entity.setPoliticaloutlook(Integer.parseInt(arr[0]));
                                        entity.setPoliticaloutlookname(arr[1]);
                                    }
                                    arr = new String[]{};
                                }
                                String strRddate = this.importTernaryOperate(obj.get(i), 12);//入党时间
                                if (StringUtils.isNotBlank(strRddate)) {
                                    entity.setRddate(DateUtil.parse(strRddate));
                                }
                                String strRtdate = this.importTernaryOperate(obj.get(i), 13);//入团时间
                                if (StringUtils.isNotBlank(strRtdate)) {
                                    entity.setRtdate(DateUtil.parse(strRtdate));
                                }
                                String strWardName = this.importTernaryOperate(obj.get(i), 14);//病区
                                if (StringUtils.isNotBlank(strWardName)) {
                                    arr = strWardName.split(" ");
                                    if (arr.length > 0) {
                                        entity.setWardname(arr[0]);
                                    }
                                    arr = new String[]{};
                                }
                                String strJobName = this.importTernaryOperate(obj.get(i), 15);//岗位
                                entity.setJobname(strJobName);
                                String strPositionname = this.importTernaryOperate(obj.get(i), 16);//职位
                                entity.setPositionname(strPositionname);
                                String strEmployeetypeName = this.importTernaryOperate(obj.get(i), 17);//人员类型
                                if (StringUtils.isNotBlank(strEmployeetypeName)) {
                                    arr = strEmployeetypeName.split(" ");
                                    if (arr.length > 0) {
                                        entity.setEmployeetype(Integer.parseInt(arr[0]));
                                        entity.setEmployeetypename(arr[1]);
                                    }
                                    arr = new String[]{};
                                }
                                String strRsfwName = this.importTernaryOperate(obj.get(i), 18);//人事范围
                                if (StringUtils.isNotBlank(strRsfwName)) {
                                    arr = strRsfwName.split(" ");
                                    if (arr.length > 0) {
                                        entity.setRsfw(Integer.parseInt(arr[0]));
                                        entity.setRsfwname(arr[1]);
                                    }
                                    arr = new String[]{};
                                }
                                String strRszfwName = this.importTernaryOperate(obj.get(i), 19);//人事子范围
                                if (StringUtils.isNotBlank(strRszfwName)) {
                                    arr = strRszfwName.split(" ");
                                    if (arr.length > 1) {
                                        entity.setRszfw(Integer.parseInt(arr[0]));
                                        entity.setRszfwname(arr[1]);
                                    }
                                    arr = new String[]{};
                                }
                                // 学历1
                                String strByenddate1 = this.importTernaryOperate(obj.get(i), 20);//毕业时间
                                if (StringUtils.isNotBlank(strByenddate1)) {
                                    entity.setByenddate1(DateUtil.parse(strByenddate1));
                                }
                                String strAcademic1 = this.importTernaryOperate(obj.get(i), 21);//学历
                                if (StringUtils.isNotBlank(strAcademic1)) {
                                    arr = strAcademic1.split(" ");
                                    if (arr.length > 0) {
                                        entity.setAcademic1(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strDegrees1 = this.importTernaryOperate(obj.get(i), 22);//学位
                                if (StringUtils.isNotBlank(strDegrees1)) {
                                    arr = strDegrees1.split(" ");
                                    if (arr.length > 0) {
                                        entity.setDegrees1(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strStudymethod1 = this.importTernaryOperate(obj.get(i), 23);//学习形式
                                if (StringUtils.isNotBlank(strStudymethod1)) {
                                    arr = strStudymethod1.split(" ");
                                    if (arr.length > 0) {
                                        entity.setStudymethod1(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strSchool1 = this.importTernaryOperate(obj.get(i), 24);//毕业学校
                                entity.setSchool1(strSchool1);
                                String strProfession1 = this.importTernaryOperate(obj.get(i), 25);//专业
                                entity.setProfession1(strProfession1);
                                String strTutor1 = this.importTernaryOperate(obj.get(i), 26);//导师
                                entity.setTutor1(strTutor1);

                                // 学历2
                                String strByenddate2 = this.importTernaryOperate(obj.get(i), 27);//毕业时间
                                if (StringUtils.isNotBlank(strByenddate2)) {
                                    entity.setByenddate2(DateUtil.parse(strByenddate2));
                                }
                                String strAcademic2 = this.importTernaryOperate(obj.get(i), 28);//学历
                                if (StringUtils.isNotBlank(strAcademic2)) {
                                    arr = strAcademic2.split(" ");
                                    if (arr.length > 0) {
                                        entity.setAcademic2(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strDegrees2 = this.importTernaryOperate(obj.get(i), 29);//学位
                                if (StringUtils.isNotBlank(strDegrees2)) {
                                    arr = strDegrees2.split(" ");
                                    if (arr.length > 0) {
                                        entity.setDegrees2(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strStudymethod2 = this.importTernaryOperate(obj.get(i), 30);//学习形式
                                if (StringUtils.isNotBlank(strStudymethod2)) {
                                    arr = strStudymethod2.split(" ");
                                    if (arr.length > 0) {
                                        entity.setStudymethod2(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strSchool2 = this.importTernaryOperate(obj.get(i), 31);//毕业学校
                                entity.setSchool2(strSchool2);
                                String strProfession2 = this.importTernaryOperate(obj.get(i), 32);//专业
                                entity.setProfession2(strProfession2);
                                String strTutor2 = this.importTernaryOperate(obj.get(i), 33);//导师
                                entity.setTutor2(strTutor2);

                                // 学历3
                                String strByenddate3 = this.importTernaryOperate(obj.get(i), 34);//毕业时间
                                if (StringUtils.isNotBlank(strByenddate3)) {
                                    entity.setByenddate3(DateUtil.parse(strByenddate3));
                                }
                                String strAcademic3 = this.importTernaryOperate(obj.get(i), 35);//学历
                                if (StringUtils.isNotBlank(strAcademic3)) {
                                    arr = strAcademic3.split(" ");
                                    if (arr.length > 0) {
                                        entity.setAcademic3(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strDegrees3 = this.importTernaryOperate(obj.get(i), 36);//学位
                                if (StringUtils.isNotBlank(strDegrees3)) {
                                    arr = strDegrees3.split(" ");
                                    if (arr.length > 0) {
                                        entity.setDegrees3(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strStudymethod3 = this.importTernaryOperate(obj.get(i), 37);//学习形式
                                if (StringUtils.isNotBlank(strStudymethod3)) {
                                    arr = strStudymethod3.split(" ");
                                    if (arr.length > 0) {
                                        entity.setStudymethod3(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strSchool3 = this.importTernaryOperate(obj.get(i), 38);//毕业学校
                                entity.setSchool3(strSchool3);
                                String strProfession3 = this.importTernaryOperate(obj.get(i), 39);//专业
                                entity.setProfession3(strProfession3);
                                String strTutor3 = this.importTernaryOperate(obj.get(i), 40);//导师
                                entity.setTutor3(strTutor3);

                                // 学历4
                                String strByenddate4 = this.importTernaryOperate(obj.get(i), 41);//毕业时间
                                if (StringUtils.isNotBlank(strByenddate4)) {
                                    entity.setByenddate4(DateUtil.parse(strByenddate4));
                                }
                                String strAcademic4 = this.importTernaryOperate(obj.get(i), 42);//学历
                                if (StringUtils.isNotBlank(strAcademic4)) {
                                    arr = strAcademic4.split(" ");
                                    if (arr.length > 0) {
                                        entity.setAcademic4(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strDegrees4 = this.importTernaryOperate(obj.get(i), 43);//学位
                                if (StringUtils.isNotBlank(strDegrees4)) {
                                    arr = strDegrees4.split(" ");
                                    if (arr.length > 0) {
                                        entity.setDegrees4(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strStudymethod4 = this.importTernaryOperate(obj.get(i), 44);//学习形式
                                if (StringUtils.isNotBlank(strStudymethod4)) {
                                    arr = strStudymethod4.split(" ");
                                    if (arr.length > 0) {
                                        entity.setStudymethod4(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strSchool4 = this.importTernaryOperate(obj.get(i), 45);//毕业学校
                                entity.setSchool4(strSchool4);
                                String strProfession4 = this.importTernaryOperate(obj.get(i), 46);//专业
                                entity.setProfession4(strProfession4);
                                String strTutor4 = this.importTernaryOperate(obj.get(i), 47);//导师
                                entity.setTutor4(strTutor4);

                                eceList.add(entity);
                            }
                        } else {
                            blError = true;
                            message = "Excel导入失败，Sheet明细扣款 列表列数不正确";
                        }
                        if (!blError) {
                            if (eceList.size() > 0) {
                                iEmployeeService.importEmployeeCoreEducationEvnt(eceList);
                                success = 1;
                                message = "Excel导入成功.";
                            } else {
                                message = "Excel导入失败，导入数据为空.";
                            }
                        }
                    } else {
                        message = "Excel导入失败，请确认列表数据是否正确.";
                    }
                } else {
                    message = "Excel导入失败,需确保存在1个Sheet.";
                }
            } catch (Exception ex) {
                //message = ex.getMessage();
                if ("".equals(message)) {
                    message = "Excel导入失败.";
                }
                log.error(message, ex);
            }
        }

        map.put("success", success);
        map.put("message", message);
        return new FebsResponse().data(map);
    }

    public String importTernaryOperate(Object[] obj, int nThis) {
        if (obj.length >= nThis + 1) {
            return obj[nThis] != null ? obj[nThis].toString().trim() : "";
        } else {
            return "";
        }
    }

}
