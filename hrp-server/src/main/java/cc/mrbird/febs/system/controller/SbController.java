package cc.mrbird.febs.system.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.EntityUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.*;
import cc.mrbird.febs.system.service.*;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.wuwenze.poi.ExcelKit;
import com.wuwenze.poi.handler.ExcelReadHandler;
import com.wuwenze.poi.pojo.ExcelErrorField;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("/system/sb")
public class SbController extends BaseController {
    @Autowired
    ISbService iSbService;

    private String message;
    @Autowired
    FileService fileService;

    @Autowired
    EntityUtil entityUtil;

    @Autowired
    ISbBzylService iSbBzylService;

    @Autowired
    ISbDeylService iSbDeylService;

    @Autowired
    ISbGsService iSbGsService;

    @Autowired
    ISbShengyuService iSbShengyuService;

    @Autowired
    ISbSyService iSbSyService;

    @Autowired
    ISbYanglaoService sbYanglaoService;

    @Autowired
    ISbYlService iSbYlService;

    @Autowired
    ICodeService codeService;

    @Autowired
    IEmployeeCoreService employeeCoreService;

    @GetMapping
    public List<SbReport> getSbReport(Period period) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return iSbService.getSummaryReport(period.getPeriod());
    }

    @GetMapping("getDifferentReport")
    public List<SbReport> getDifferentReport(Period period) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return iSbService.getDifferentReport(period.getPeriod());
    }

    @GetMapping("getHistoryList")
    public Map<String, Object> getHistoryList(QueryRequest request,SbReport sbReport) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        IPage page = this.iSbService.getHistoryList(request,sbReport);
        if(page.getRecords().size() > 0) {
            List<Code> codeList = codeService.findCodeMaxEndDateLists();
            List<Code> queryCode= new ArrayList<>();
            List<EmployeeCore> employeeCoreList = employeeCoreService.list();
            //employeeCoreService.findEmployeeCoreMaxEndDateLists();
            List<EmployeeCore> queryEmployeeCore= new ArrayList<>();
            for (Object obj : page.getRecords()) {
                if(sbReport.getBxtype()== FebsConstant.SB_BZYL){
                    SbBzyl sb = (SbBzyl)obj;
                    queryCode = codeList.stream().filter(s->s.getEmployeeid().equals(sb.getEmployeeid()) &&
                            s.getStartdate().compareTo(sb.getPaydate()) <= 0 &&
                            s.getEnddate().compareTo(sb.getPaydate()) >= 0).collect(Collectors.toList());
                    if(queryCode.size() > 0)
                        sb.setEmployeecode(queryCode.get(0).getEmployeecode());

                    queryEmployeeCore = employeeCoreList.stream().filter(s->s.getEmployeeid().equals(sb.getEmployeeid()) &&
                            s.getStartdate().compareTo(sb.getPaydate()) <= 0 &&
                            s.getEnddate().compareTo(sb.getPaydate()) >= 0).collect(Collectors.toList());

                    if(queryEmployeeCore.size() > 0)
                        sb.setEmployeetype(queryEmployeeCore.get(0).getEmployeetype());

                }else if(sbReport.getBxtype()==FebsConstant.SB_DEYL){
                    SbDeyl sb = (SbDeyl)obj;
                    queryCode = codeList.stream().filter(s->s.getEmployeeid().equals(sb.getEmployeeid()) &&
                            s.getStartdate().compareTo(sb.getPaydate()) <= 0 &&
                            s.getEnddate().compareTo(sb.getPaydate()) >= 0).collect(Collectors.toList());
                    if(queryCode.size() > 0)
                        sb.setEmployeecode(queryCode.get(0).getEmployeecode());

                    queryEmployeeCore = employeeCoreList.stream().filter(s->s.getEmployeeid().equals(sb.getEmployeeid()) &&
                            s.getStartdate().compareTo(sb.getPaydate()) <= 0 &&
                            s.getEnddate().compareTo(sb.getPaydate()) >= 0).collect(Collectors.toList());

                    if(queryEmployeeCore.size() > 0)
                        sb.setEmployeetype(queryEmployeeCore.get(0).getEmployeetype());
                }else if(sbReport.getBxtype()==FebsConstant.SB_GS){
                    SbGs sb = (SbGs)obj;
                    queryCode = codeList.stream().filter(s->s.getEmployeeid().equals(sb.getEmployeeid()) &&
                            s.getStartdate().compareTo(sb.getPaydate()) <= 0 &&
                            s.getEnddate().compareTo(sb.getPaydate()) >= 0).collect(Collectors.toList());
                    if(queryCode.size() > 0)
                        sb.setEmployeecode(queryCode.get(0).getEmployeecode());

                    queryEmployeeCore = employeeCoreList.stream().filter(s->s.getEmployeeid().equals(sb.getEmployeeid()) &&
                            s.getStartdate().compareTo(sb.getPaydate()) <= 0 &&
                            s.getEnddate().compareTo(sb.getPaydate()) >= 0).collect(Collectors.toList());

                    if(queryEmployeeCore.size() > 0)
                        sb.setEmployeetype(queryEmployeeCore.get(0).getEmployeetype());
                }else if(sbReport.getBxtype()==FebsConstant.SB_SHENGYU){
                    SbShengyu sb = (SbShengyu)obj;
                    queryCode = codeList.stream().filter(s->s.getEmployeeid().equals(sb.getEmployeeid()) &&
                            s.getStartdate().compareTo(sb.getPaydate()) <= 0 &&
                            s.getEnddate().compareTo(sb.getPaydate()) >= 0).collect(Collectors.toList());
                    if(queryCode.size() > 0)
                        sb.setEmployeecode(queryCode.get(0).getEmployeecode());

                    queryEmployeeCore = employeeCoreList.stream().filter(s->s.getEmployeeid().equals(sb.getEmployeeid()) &&
                            s.getStartdate().compareTo(sb.getPaydate()) <= 0 &&
                            s.getEnddate().compareTo(sb.getPaydate()) >= 0).collect(Collectors.toList());

                    if(queryEmployeeCore.size() > 0)
                        sb.setEmployeetype(queryEmployeeCore.get(0).getEmployeetype());
                }else if(sbReport.getBxtype()==FebsConstant.SB_SY){
                    SbSy sb = (SbSy)obj;
                    queryCode = codeList.stream().filter(s->s.getEmployeeid().equals(sb.getEmployeeid()) &&
                            s.getStartdate().compareTo(sb.getPaydate()) <= 0 &&
                            s.getEnddate().compareTo(sb.getPaydate()) >= 0).collect(Collectors.toList());
                    if(queryCode.size() > 0)
                        sb.setEmployeecode(queryCode.get(0).getEmployeecode());

                    queryEmployeeCore = employeeCoreList.stream().filter(s->s.getEmployeeid().equals(sb.getEmployeeid()) &&
                            s.getStartdate().compareTo(sb.getPaydate()) <= 0 &&
                            s.getEnddate().compareTo(sb.getPaydate()) >= 0).collect(Collectors.toList());

                    if(queryEmployeeCore.size() > 0)
                        sb.setEmployeetype(queryEmployeeCore.get(0).getEmployeetype());
                }else if(sbReport.getBxtype()==FebsConstant.SB_YANGLAO){
                    SbYanglao sb = (SbYanglao)obj;
                    queryCode = codeList.stream().filter(s->s.getEmployeeid().equals(sb.getEmployeeid()) &&
                            s.getStartdate().compareTo(sb.getPaydate()) <= 0 &&
                            s.getEnddate().compareTo(sb.getPaydate()) >= 0).collect(Collectors.toList());
                    if(queryCode.size() > 0)
                        sb.setEmployeecode(queryCode.get(0).getEmployeecode());

                    queryEmployeeCore = employeeCoreList.stream().filter(s->s.getEmployeeid().equals(sb.getEmployeeid()) &&
                            s.getStartdate().compareTo(sb.getPaydate()) <= 0 &&
                            s.getEnddate().compareTo(sb.getPaydate()) >= 0).collect(Collectors.toList());

                    if(queryEmployeeCore.size() > 0)
                        sb.setEmployeetype(queryEmployeeCore.get(0).getEmployeetype());
                }else if(sbReport.getBxtype()==FebsConstant.SB_YL){
                    SbYl sb = (SbYl)obj;
                    queryCode = codeList.stream().filter(s->s.getEmployeeid().equals(sb.getEmployeeid()) &&
                            s.getStartdate().compareTo(sb.getPaydate()) <= 0 &&
                            s.getEnddate().compareTo(sb.getPaydate()) >= 0).collect(Collectors.toList());
                    if(queryCode.size() > 0)
                        sb.setEmployeecode(queryCode.get(0).getEmployeecode());

                    queryEmployeeCore = employeeCoreList.stream().filter(s->s.getEmployeeid().equals(sb.getEmployeeid()) &&
                            s.getStartdate().compareTo(sb.getPaydate()) <= 0 &&
                            s.getEnddate().compareTo(sb.getPaydate()) >= 0).collect(Collectors.toList());

                    if(queryEmployeeCore.size() > 0)
                        sb.setEmployeetype(queryEmployeeCore.get(0).getEmployeetype());
                }
            }
        }
        return getDataTable(page);
    }


    /**
     * 导入Excel数据，并新增社保信息
     */
    @PostMapping("import")
    public FebsResponse importExcels(@RequestParam("file") MultipartFile file) throws FebsException {
        try {
            fileService.validFile(file);
            // 开始导入操作
            long beginTimeMillis = System.currentTimeMillis();
            final List<SbYanglao> data = Lists.newArrayList();
            final List<SbBzyl> bzylData= Lists.newArrayList();
            final List<SbDeyl> deylData=Lists.newArrayList();
            final List<SbGs> gsData=Lists.newArrayList();
            final List<SbShengyu> shengyuData=Lists.newArrayList();
            final List<SbSy> syData=Lists.newArrayList();
            final List<SbYanglao> yanglaoData = Lists.newArrayList();
            final List<SbYl> ylData=Lists.newArrayList();
            final List<Map<String, Object>> error = Lists.newArrayList();
            log.info("dl:0");
            ExcelKit.$Import(SbYanglao.class).readXlsx(file.getInputStream(), new ExcelReadHandler<SbYanglao>() {
                @Override
                public void onSuccess(int sheet, int row, SbYanglao sbYanglao) {
                    if(sheet==0){
                        log.info("dl:1");
                        if (sbYanglao.getJs()!=null && !sbYanglao.getJs().equals(0) ) {
                            log.info("dl:2");
                            // 数据校验成功时，加入集合
                            if (sbYanglao.getBxtype() == 1) {

                                yanglaoData.add(sbYanglao);
                                log.info("dl:3");
                            } else if (sbYanglao.getBxtype() == 2) {

                                SbYl sbYl = new SbYl();
                                entityUtil.getEntity(sbYanglao, sbYl);
                                ylData.add(sbYl);
                                log.info("dl:4");
                            } else if (sbYanglao.getBxtype() == 3) {

                                SbSy sbSy = new SbSy();
                                entityUtil.getEntity(sbYanglao, sbSy);
                                syData.add(sbSy);
                                log.info("dl:5");
                            } else if (sbYanglao.getBxtype() == 4) {
                                SbGs sbGs = new SbGs();
                                entityUtil.getEntity(sbYanglao, sbGs);
                                gsData.add(sbGs);
                                log.info("dl:6");
                            } else if (sbYanglao.getBxtype() == 5) {
                                SbShengyu sbShengyu = new SbShengyu();
                                entityUtil.getEntity(sbYanglao, sbShengyu);
                                shengyuData.add(sbShengyu);
                                log.info("dl:7");
                            } else if (sbYanglao.getBxtype() == 6) {
                                SbDeyl sbDeyl = new SbDeyl();
                                entityUtil.getEntity(sbYanglao, sbDeyl);
                                deylData.add(sbDeyl);
                                log.info("dl:8");
                            } else if (sbYanglao.getBxtype() == 7) {
                                SbBzyl sbBzyl = new SbBzyl();
                                entityUtil.getEntity(sbYanglao, sbBzyl);
                                bzylData.add(sbBzyl);
                                log.info("dl:9");
                            }
                            data.add(sbYanglao);
                        }
                    }
                }
                @Override
                public void onError(int sheet, int row, List<ExcelErrorField> errorFields) {
                    log.info("dl:10"+row);
                    // 数据校验失败时，记录到 error集合
                    error.add(ImmutableMap.of("row", row, "errorFields", errorFields));
                }
            });
            if(data.size()>0) {
                Date shDate = data.get(0).getShdate();
                if (!bzylData.isEmpty()) {
                    this.iSbBzylService.deleteByDate(shDate);
                    this.iSbBzylService.saveBatch(bzylData);
                    log.info("dl:11");
                }
                if (!deylData.isEmpty()) {
                    this.iSbDeylService.deleteByDate(shDate);
                    this.iSbDeylService.saveBatch(deylData);
                    log.info("dl:12");
                }
                if (!gsData.isEmpty()) {
                    this.iSbGsService.deleteByDate(shDate);
                    this.iSbGsService.saveBatch(gsData);
                    log.info("dl:13");
                }
                if (!shengyuData.isEmpty()) {
                    this.iSbShengyuService.deleteByDate(shDate);
                    this.iSbShengyuService.saveBatch(shengyuData);
                    log.info("dl:14");
                }
                if (!syData.isEmpty()) {
                    this.iSbSyService.deleteByDate(shDate);
                    this.iSbSyService.saveBatch(syData);
                    log.info("dl:15");
                }
                if (!yanglaoData.isEmpty()) {
                    this.sbYanglaoService.deleteByDate(shDate);
                    this.sbYanglaoService.saveBatch(yanglaoData);
                    log.info("dl:16");
                }
                if (!ylData.isEmpty()) {
                    this.iSbYlService.deleteByDate(shDate);
                    this.iSbYlService.saveBatch(ylData);
                    log.info("dl:17");
                }
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
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }

    @PostMapping("export")
    public void export(HttpServletResponse response,Period period) throws FebsException {
        try {
            List<SbReport> list = iSbService.getSummaryReport(period.getPeriod());
            ExcelKit.$Export(SbReport.class, response).downXlsx(list, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }

    @PostMapping("export/HistoryList")
    public void exportHistoryList(HttpServletResponse response,SbReport sbReport) throws FebsException {
        try {
            List<SbYanglao> list = iSbService.getHistoryList(sbReport);
            ExcelKit.$Export(SbYanglao.class, response).downXlsx(list, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }

    @Log("下载职工附件")
    @GetMapping("/download/{fileName}")
    public void downLoadFile(HttpServletResponse response, @PathVariable(value = "fileName") @NotBlank(message = "文件名不能为空") String fileName) throws FebsException, IOException {
        fileService.downLoadFile(response, fileName);
    }

    @PostMapping("export/DifferentReport")
    public void exportDifferentReport(HttpServletResponse response, Period period) throws FebsException {
        try {
            List<T> list = iSbService.getDifferentReport(period.getPeriod());
            ExcelKit.$Export(SbDifferent.class, response).downXlsx(list, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(HttpStatus.BAD_REQUEST,message);
        }
    }

}
