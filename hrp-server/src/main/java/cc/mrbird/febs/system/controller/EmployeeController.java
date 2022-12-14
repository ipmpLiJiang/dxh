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
            message = "??????Excel??????";
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
            message = "??????Excel??????";
            log.error(message, e);
        }
    }

    @Log("????????????")
    @PostMapping
    public Map<String, Object> addEmployee(@Valid Employee employee) throws FebsException {
        return getResult(HttpStatus.OK, FebsConstant.INSERT_SUCCESS, this.iEmployeeService.createEmployee(employee));
    }

    @Log("????????????")
    @PutMapping
    public Map<String, Object> updateEmployee(@Valid Employee employee) {
        this.iEmployeeService.updateEmployee(employee);
        return getResult(HttpStatus.OK, FebsConstant.UPDATE_SUCCESS, null);
    }

    @Log("??????????????????")
    @PutMapping("/updateKqDept")
    public Map<String, Object> updateKqDept(Employee employee) {
        this.iEmployeeService.updateKqDept(employee.getEmployeeid(), employee.getKqdept());
        return getResult(HttpStatus.OK, FebsConstant.UPDATE_SUCCESS, null);
    }

    @Log("??????????????????")
    @RequestMapping(value = "/picture/upload/{dataType}", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public Map<String, String> upLoadFile(@RequestParam MultipartFile file) throws FebsException {
        return fileService.upLoadFile(file);
    }

    @Log("??????????????????")
    @GetMapping("/download/{fileName}/{path}")
    public void downLoadFile(HttpServletResponse response, @PathVariable(value = "fileName") @NotBlank(message = "?????????????????????") String fileName, @PathVariable(value = "path") @NotBlank(message = "????????????????????????") String path) throws FebsException, IOException {
        fileService.downLoadOAFile(response, fileName, path);
    }

    @Log("??????????????????")
    @DeleteMapping("/delete/{fileName}")
    public Map<String, Object> deleteFile(@PathVariable(value = "fileName") String fileName, String dataType, String data, String column) {
        fileService.deleteFile(fileName, dataType, data, column);
        return getResult(HttpStatus.OK, FebsConstant.DELETE_SUCCESS, null);
    }

    /**
     * ?????? Excel
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Employee employee) throws FebsException {
        try {
            List<Employee> list = this.iEmployeeService.getEmployeeReport(employee);
            dictService.getDictNameByEmployees(list);
            ExcelKit.$Export(Employee.class, response).downXlsx(list, false);
        } catch (Exception e) {
            message = "??????Excel??????";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST, message);
        }
    }


    @PostMapping("/exportEmployeeAgeTitle")
    public void exportEmployeeAgeTitle(HttpServletResponse response, Employee employee) throws FebsException {
        try {
            String dataJson = "[" +
                    "{title: \"???????????????\", dataIndex: \"rszfwname\"}," +
                    "{title: \"??????25x\",dataIndex: \"yj25x\"}," +
                    "{title: \"??????30\",dataIndex: \"yj30\"}," +
                    "{title: \"??????35\",dataIndex: \"yj35\"}," +
                    "{title: \"??????40\",dataIndex: \"yj40\"}," +
                    "{title: \"??????41s\",dataIndex: \"yj41s\"}," +
                    "{title: \"??????25x\",dataIndex: \"cj25x\"}," +
                    "{title: \"??????30\",dataIndex: \"cj30\"}," +
                    "{title: \"??????35\",dataIndex: \"cj35\"}," +
                    "{title: \"??????40\",dataIndex: \"cj40\"}," +
                    "{title: \"??????41s\",dataIndex: \"cj41s\"}," +
                    "{title: \"??????25x\",dataIndex: \"zj25x\"}," +
                    "{title: \"??????30\",dataIndex: \"zj30\"}," +
                    "{title: \"??????35\",dataIndex: \"zj35\"}," +
                    "{title: \"??????40\",dataIndex: \"zj40\"}," +
                    "{title: \"??????41s\",dataIndex: \"zj41s\"}," +
                    "{title: \"??????25x\",dataIndex: \"fg25x\"}," +
                    "{title: \"??????30\",dataIndex: \"fg30\"}," +
                    "{title: \"??????35\",dataIndex: \"fg35\"}," +
                    "{title: \"??????40\",dataIndex: \"fg40\"}," +
                    "{title: \"??????41s\",dataIndex: \"fg41s\"}," +
                    "{title: \"??????25x\",dataIndex: \"zg25x\"}," +
                    "{title: \"??????30\",dataIndex: \"zg30\"}," +
                    "{title: \"??????35\",dataIndex: \"zg35\"}," +
                    "{title: \"??????40\",dataIndex: \"zg40\"}," +
                    "{title: \"??????41s\",dataIndex: \"zg41s\"}," +
                    "{title: \"??????25x\",dataIndex: \"wd25x\"}," +
                    "{title: \"??????30\",dataIndex: \"wd30\"}," +
                    "{title: \"??????35\",dataIndex: \"wd35\"}," +
                    "{title: \"??????40\",dataIndex: \"wd40\"}," +
                    "{title: \"??????41s\",dataIndex: \"wd41s\"}," +
                    "]";
            List<EmployeeAgeTitleReport> list = iEmployeeService.findEmployeeAgeTitleReport(employee);
            String filePath = "c:/qc/upload/";
            String tempUrl = filePath + "employeeAgeTitleTemp.xlsx";
            ExportExcelUtils.exportTemplateExcel(response, list, dataJson, tempUrl, 2);
        } catch (Exception e) {
            message = "??????Excel??????";
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
                    "{title: \"????????????\", dataIndex: \"joblevelname\"}," +
                    "{title: \"??????\",dataIndex: \"xj\"}," +
                    "{title: \"????????????\",dataIndex: \"yjys\"}," +
                    "{title: \"????????????\",dataIndex: \"yjhl\"}," +
                    "{title: \"????????????\",dataIndex: \"yjyj\"}," +
                    "{title: \"????????????\",dataIndex: \"yjqt\"}," +
                    "{title: \"????????????\",dataIndex: \"cjys\"}," +
                    "{title: \"????????????\",dataIndex: \"cjhl\"}," +
                    "{title: \"????????????\",dataIndex: \"cjyj\"}," +
                    "{title: \"????????????\",dataIndex: \"cjqt\"}," +
                    "{title: \"????????????\",dataIndex: \"zjys\"}," +
                    "{title: \"????????????\",dataIndex: \"zjhl\"}," +
                    "{title: \"????????????\",dataIndex: \"zjyj\"}," +
                    "{title: \"????????????\",dataIndex: \"zjqt\"}," +
                    "{title: \"????????????\",dataIndex: \"fgys\"}," +
                    "{title: \"????????????\",dataIndex: \"fghl\"}," +
                    "{title: \"????????????\",dataIndex: \"fgyj\"}," +
                    "{title: \"????????????\",dataIndex: \"fgqt\"}," +
                    "{title: \"????????????\",dataIndex: \"zgys\"}," +
                    "{title: \"????????????\",dataIndex: \"zghl\"}," +
                    "{title: \"????????????\",dataIndex: \"zgyj\"}," +
                    "{title: \"????????????\",dataIndex: \"zgqt\"}," +
                    "{title: \"????????????\",dataIndex: \"wdys\"}," +
                    "{title: \"????????????\",dataIndex: \"wdhl\"}," +
                    "{title: \"????????????\",dataIndex: \"wdyj\"}," +
                    "{title: \"????????????\",dataIndex: \"wdqt\"}," +
                    "]";
            List<EmployeeJobLevelTitleGetReport> list = iEmployeeService.findEmployeeJoblevelTitleGetReport(employee);
            String filePath = "c:/qc/upload/";
            String tempUrl = filePath + "employeeJobLevelTitleGetTemp.xlsx";
            ExportExcelUtils.exportTemplateExcel(response, list, dataJson, tempUrl, 2);
        } catch (Exception e) {
            message = "??????Excel??????";
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
                    "{title: \"????????????\", dataIndex: \"rylb\"}," +
                    "{title: \"??????????????????\",dataIndex: \"joblevelname\"}," +
                    "{title: \"????????????\",dataIndex: \"xlxj\"}," +
                    "{title: \"??????????????????\",dataIndex: \"xldzyx\"}," +
                    "{title: \"????????????\",dataIndex: \"xlbk\"}," +
                    "{title: \"????????????\",dataIndex: \"xlss\"}," +
                    "{title: \"????????????\",dataIndex: \"xlbs\"}," +
                    "{title: \"??????35??????\",dataIndex: \"nl35x\"}," +
                    "{title: \"??????40\",dataIndex: \"nl40\"}," +
                    "{title: \"??????45\",dataIndex: \"nl45\"}," +
                    "{title: \"??????50\",dataIndex: \"nl50\"}," +
                    "{title: \"??????54\",dataIndex: \"nl54\"}," +
                    "{title: \"??????59\",dataIndex: \"nl59\"}," +
                    "{title: \"??????60??????\",dataIndex: \"nl60s\"}," +
                    "]";
            List<EmployeeBirthEducationReport> list = iEmployeeService.findEmployeeBirthEducationReport(employee);
            String filePath = "c:/qc/upload/";
            String tempUrl = filePath + "employeeBirthEducationTemp.xlsx";
            ExportExcelUtils.exportTemplateExcel(response, list, dataJson, tempUrl, 2);
        } catch (Exception e) {
            message = "??????Excel??????";
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
                throw new FebsException(HttpStatus.BAD_REQUEST, "??????????????????");
            }
            String filename = file.getOriginalFilename();
            if (!StringUtils.endsWith(filename, ".xlsx")) {
                throw new FebsException(HttpStatus.BAD_REQUEST, "?????????.xlsx??????????????????");
            }
            // ??????????????????
            long beginTimeMillis = System.currentTimeMillis();
            final List<EmployeeCoreEducationExcel> data = Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            ExcelKit.$Import(EmployeeCoreEducationExcel.class).readXlsx(
                    file.getInputStream(),
                    new ExcelReadHandler<EmployeeCoreEducationExcel>() {
                        @Override
                        public void onSuccess(int sheet, int row, EmployeeCoreEducationExcel excel) {
                            // ????????????????????????????????????
                            row = 2;
                            data.add(excel);
                        }

                        @Override
                        public void onError(int sheet, int row, List<ExcelErrorField> errorFields) {
                            // ????????????????????????????????? error??????
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
            message = "??????Excel????????????," + e.getMessage();
            log.error(message);
            throw new FebsException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @PostMapping("importEmployeeCoreEducation")
    public FebsResponse importEmployeeCoreEducations(@RequestParam MultipartFile file) {
        int success = 0;
        ModelMap map = new ModelMap();
        if (file.isEmpty()) {
            message = "?????????";
        } else {
            boolean blError = false;
            try {
                String filePath = "c:/qc/upload/"; // ??????????????????
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
                                message = "??????????????????????????????Excel" + i + "????????????????????????.";
                                EmployeeCoreEducationExcel entity = new EmployeeCoreEducationExcel();
                                String strRzdate = this.importTernaryOperate(obj.get(i), 0);//????????????
                                if (StringUtils.isNotBlank(strRzdate)) {
                                    entity.setRzdate(DateUtil.parse(strRzdate));
                                }
                                String strEmployeecode = this.importTernaryOperate(obj.get(i), 1);//??????
                                entity.setEmployeecode(strEmployeecode);
                                String strEmployeeName = this.importTernaryOperate(obj.get(i), 2);//??????
                                entity.setEmployeename(strEmployeeName);
                                String strGender = this.importTernaryOperate(obj.get(i), 3);//??????
                                entity.setGender(strGender);
                                String strBirth = this.importTernaryOperate(obj.get(i), 4);//????????????
                                if (StringUtils.isNotBlank(strBirth)) {
                                    entity.setBirth(DateUtil.parse(strBirth));
                                }
                                String strIdnumber = this.importTernaryOperate(obj.get(i), 5);//????????????
                                entity.setIdnumber(strIdnumber);
                                String strPhonenumber = this.importTernaryOperate(obj.get(i), 6);//?????????
                                entity.setPhonenumber(strPhonenumber);
                                String strHomeaddress = this.importTernaryOperate(obj.get(i), 7);//??????
                                entity.setHomeaddress(strHomeaddress);
                                String strNation = this.importTernaryOperate(obj.get(i), 8);//??????
                                entity.setNation(strNation);
                                String strWorkdate = this.importTernaryOperate(obj.get(i), 9);//??????????????????
                                if (StringUtils.isNotBlank(strWorkdate)) {
                                    entity.setWorkdate(DateUtil.parse(strWorkdate));
                                }
                                String strComedate = this.importTernaryOperate(obj.get(i), 10);//????????????
                                if (StringUtils.isNotBlank(strComedate)) {
                                    entity.setComedate(DateUtil.parse(strComedate));
                                }
                                String strPoliticaloutlookName = this.importTernaryOperate(obj.get(i), 11);//????????????
                                if (StringUtils.isNotBlank(strPoliticaloutlookName)) {
                                    arr = strPoliticaloutlookName.split(" ");
                                    if (arr.length > 0) {
                                        entity.setPoliticaloutlook(Integer.parseInt(arr[0]));
                                        entity.setPoliticaloutlookname(arr[1]);
                                    }
                                    arr = new String[]{};
                                }
                                String strRddate = this.importTernaryOperate(obj.get(i), 12);//????????????
                                if (StringUtils.isNotBlank(strRddate)) {
                                    entity.setRddate(DateUtil.parse(strRddate));
                                }
                                String strRtdate = this.importTernaryOperate(obj.get(i), 13);//????????????
                                if (StringUtils.isNotBlank(strRtdate)) {
                                    entity.setRtdate(DateUtil.parse(strRtdate));
                                }
                                String strWardName = this.importTernaryOperate(obj.get(i), 14);//??????
                                if (StringUtils.isNotBlank(strWardName)) {
                                    arr = strWardName.split(" ");
                                    if (arr.length > 0) {
                                        entity.setWardname(arr[0]);
                                    }
                                    arr = new String[]{};
                                }
                                String strJobName = this.importTernaryOperate(obj.get(i), 15);//??????
                                entity.setJobname(strJobName);
                                String strPositionname = this.importTernaryOperate(obj.get(i), 16);//??????
                                entity.setPositionname(strPositionname);
                                String strEmployeetypeName = this.importTernaryOperate(obj.get(i), 17);//????????????
                                if (StringUtils.isNotBlank(strEmployeetypeName)) {
                                    arr = strEmployeetypeName.split(" ");
                                    if (arr.length > 0) {
                                        entity.setEmployeetype(Integer.parseInt(arr[0]));
                                        entity.setEmployeetypename(arr[1]);
                                    }
                                    arr = new String[]{};
                                }
                                String strRsfwName = this.importTernaryOperate(obj.get(i), 18);//????????????
                                if (StringUtils.isNotBlank(strRsfwName)) {
                                    arr = strRsfwName.split(" ");
                                    if (arr.length > 0) {
                                        entity.setRsfw(Integer.parseInt(arr[0]));
                                        entity.setRsfwname(arr[1]);
                                    }
                                    arr = new String[]{};
                                }
                                String strRszfwName = this.importTernaryOperate(obj.get(i), 19);//???????????????
                                if (StringUtils.isNotBlank(strRszfwName)) {
                                    arr = strRszfwName.split(" ");
                                    if (arr.length > 1) {
                                        entity.setRszfw(Integer.parseInt(arr[0]));
                                        entity.setRszfwname(arr[1]);
                                    }
                                    arr = new String[]{};
                                }
                                // ??????1
                                String strByenddate1 = this.importTernaryOperate(obj.get(i), 20);//????????????
                                if (StringUtils.isNotBlank(strByenddate1)) {
                                    entity.setByenddate1(DateUtil.parse(strByenddate1));
                                }
                                String strAcademic1 = this.importTernaryOperate(obj.get(i), 21);//??????
                                if (StringUtils.isNotBlank(strAcademic1)) {
                                    arr = strAcademic1.split(" ");
                                    if (arr.length > 0) {
                                        entity.setAcademic1(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strDegrees1 = this.importTernaryOperate(obj.get(i), 22);//??????
                                if (StringUtils.isNotBlank(strDegrees1)) {
                                    arr = strDegrees1.split(" ");
                                    if (arr.length > 0) {
                                        entity.setDegrees1(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strStudymethod1 = this.importTernaryOperate(obj.get(i), 23);//????????????
                                if (StringUtils.isNotBlank(strStudymethod1)) {
                                    arr = strStudymethod1.split(" ");
                                    if (arr.length > 0) {
                                        entity.setStudymethod1(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strSchool1 = this.importTernaryOperate(obj.get(i), 24);//????????????
                                entity.setSchool1(strSchool1);
                                String strProfession1 = this.importTernaryOperate(obj.get(i), 25);//??????
                                entity.setProfession1(strProfession1);
                                String strTutor1 = this.importTernaryOperate(obj.get(i), 26);//??????
                                entity.setTutor1(strTutor1);

                                // ??????2
                                String strByenddate2 = this.importTernaryOperate(obj.get(i), 27);//????????????
                                if (StringUtils.isNotBlank(strByenddate2)) {
                                    entity.setByenddate2(DateUtil.parse(strByenddate2));
                                }
                                String strAcademic2 = this.importTernaryOperate(obj.get(i), 28);//??????
                                if (StringUtils.isNotBlank(strAcademic2)) {
                                    arr = strAcademic2.split(" ");
                                    if (arr.length > 0) {
                                        entity.setAcademic2(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strDegrees2 = this.importTernaryOperate(obj.get(i), 29);//??????
                                if (StringUtils.isNotBlank(strDegrees2)) {
                                    arr = strDegrees2.split(" ");
                                    if (arr.length > 0) {
                                        entity.setDegrees2(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strStudymethod2 = this.importTernaryOperate(obj.get(i), 30);//????????????
                                if (StringUtils.isNotBlank(strStudymethod2)) {
                                    arr = strStudymethod2.split(" ");
                                    if (arr.length > 0) {
                                        entity.setStudymethod2(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strSchool2 = this.importTernaryOperate(obj.get(i), 31);//????????????
                                entity.setSchool2(strSchool2);
                                String strProfession2 = this.importTernaryOperate(obj.get(i), 32);//??????
                                entity.setProfession2(strProfession2);
                                String strTutor2 = this.importTernaryOperate(obj.get(i), 33);//??????
                                entity.setTutor2(strTutor2);

                                // ??????3
                                String strByenddate3 = this.importTernaryOperate(obj.get(i), 34);//????????????
                                if (StringUtils.isNotBlank(strByenddate3)) {
                                    entity.setByenddate3(DateUtil.parse(strByenddate3));
                                }
                                String strAcademic3 = this.importTernaryOperate(obj.get(i), 35);//??????
                                if (StringUtils.isNotBlank(strAcademic3)) {
                                    arr = strAcademic3.split(" ");
                                    if (arr.length > 0) {
                                        entity.setAcademic3(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strDegrees3 = this.importTernaryOperate(obj.get(i), 36);//??????
                                if (StringUtils.isNotBlank(strDegrees3)) {
                                    arr = strDegrees3.split(" ");
                                    if (arr.length > 0) {
                                        entity.setDegrees3(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strStudymethod3 = this.importTernaryOperate(obj.get(i), 37);//????????????
                                if (StringUtils.isNotBlank(strStudymethod3)) {
                                    arr = strStudymethod3.split(" ");
                                    if (arr.length > 0) {
                                        entity.setStudymethod3(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strSchool3 = this.importTernaryOperate(obj.get(i), 38);//????????????
                                entity.setSchool3(strSchool3);
                                String strProfession3 = this.importTernaryOperate(obj.get(i), 39);//??????
                                entity.setProfession3(strProfession3);
                                String strTutor3 = this.importTernaryOperate(obj.get(i), 40);//??????
                                entity.setTutor3(strTutor3);

                                // ??????4
                                String strByenddate4 = this.importTernaryOperate(obj.get(i), 41);//????????????
                                if (StringUtils.isNotBlank(strByenddate4)) {
                                    entity.setByenddate4(DateUtil.parse(strByenddate4));
                                }
                                String strAcademic4 = this.importTernaryOperate(obj.get(i), 42);//??????
                                if (StringUtils.isNotBlank(strAcademic4)) {
                                    arr = strAcademic4.split(" ");
                                    if (arr.length > 0) {
                                        entity.setAcademic4(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strDegrees4 = this.importTernaryOperate(obj.get(i), 43);//??????
                                if (StringUtils.isNotBlank(strDegrees4)) {
                                    arr = strDegrees4.split(" ");
                                    if (arr.length > 0) {
                                        entity.setDegrees4(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strStudymethod4 = this.importTernaryOperate(obj.get(i), 44);//????????????
                                if (StringUtils.isNotBlank(strStudymethod4)) {
                                    arr = strStudymethod4.split(" ");
                                    if (arr.length > 0) {
                                        entity.setStudymethod4(Integer.parseInt(arr[0]));
                                    }
                                    arr = new String[]{};
                                }
                                String strSchool4 = this.importTernaryOperate(obj.get(i), 45);//????????????
                                entity.setSchool4(strSchool4);
                                String strProfession4 = this.importTernaryOperate(obj.get(i), 46);//??????
                                entity.setProfession4(strProfession4);
                                String strTutor4 = this.importTernaryOperate(obj.get(i), 47);//??????
                                entity.setTutor4(strTutor4);

                                eceList.add(entity);
                            }
                        } else {
                            blError = true;
                            message = "Excel???????????????Sheet???????????? ?????????????????????";
                        }
                        if (!blError) {
                            if (eceList.size() > 0) {
                                iEmployeeService.importEmployeeCoreEducationEvnt(eceList);
                                success = 1;
                                message = "Excel????????????.";
                            } else {
                                message = "Excel?????????????????????????????????.";
                            }
                        }
                    } else {
                        message = "Excel????????????????????????????????????????????????.";
                    }
                } else {
                    message = "Excel????????????,???????????????1???Sheet.";
                }
            } catch (Exception ex) {
                //message = ex.getMessage();
                if ("".equals(message)) {
                    message = "Excel????????????.";
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
