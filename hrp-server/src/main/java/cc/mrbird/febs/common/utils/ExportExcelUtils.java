package cc.mrbird.febs.common.utils;

import cc.mrbird.febs.common.domain.ExportAfferentCustomExcel;
import cc.mrbird.febs.system.domain.AttandanceUser;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.ExcelField;
import com.wuwenze.poi.factory.ExcelMappingFactory;
import com.wuwenze.poi.pojo.ExcelMapping;
import com.wuwenze.poi.pojo.ExcelProperty;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ExportExcelUtils {
    public static void exportCustomExcel_han(HttpServletResponse response, List<?> list, String customDataJson, String sheelName) throws NoSuchFieldException, IllegalAccessException, IOException {
        List<ExportAfferentCustomExcel> exportList = new ArrayList<>();
        if (customDataJson != null && !customDataJson.equals("")) {
            exportList = JSON.parseObject(customDataJson, new TypeReference<List<ExportAfferentCustomExcel>>() {
            });
        } else {
            Object obj = list.get(0);
            Class objClass = obj.getClass();
            Field[] fields = objClass.getDeclaredFields();
            for (Field field : fields) {
                TableField tableField = field.getAnnotation(TableField.class);
                if (tableField != null) {
                    ExportAfferentCustomExcel afferentCustomExcel = new ExportAfferentCustomExcel();
                    ExcelField excelField = field.getAnnotation(ExcelField.class);
                    afferentCustomExcel.setTitle(excelField != null ? excelField.value() : field.getName());
                    afferentCustomExcel.setDataIndex(field.getName());
                    exportList.add(afferentCustomExcel);
                }
            }
        }
        Object fieldValue = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Map<String, Object>> rows = new ArrayList<>();
        Class objClass2 = list.get(0).getClass();
        boolean isTrue = false;
        ExcelMapping excelMapping = ExcelMappingFactory.get(objClass2);
        List<ExcelProperty> excelPropertyList=excelMapping.getPropertyList();
        for (Object item : list) {
            Class objClass = item.getClass();
            Map<String, Object> row = new LinkedHashMap<>();
            for (ExportAfferentCustomExcel export : exportList) {
                isTrue = true;
                // fieldValue = field.get(item);
                fieldValue=  ReflectUtil.invoke(item, "get"+ StrUtil.sub(export.getDataIndex(),0,1).toUpperCase()+StrUtil.sub(export.getDataIndex(),1,export.getDataIndex().length()));

                List<ExcelProperty> excelProperty=excelPropertyList.stream().filter(p->p.getName().equals(export.getDataIndex())).collect(Collectors.toList());
                if(excelProperty!=null&&excelProperty.size()>0) {
                    fieldValue=  ExportExcelMapping.buildCellValueByExcelProperty(fieldValue, excelProperty.get(0));
                }
                if (fieldValue == null) fieldValue = "";
                row.put(export.getTitle(), fieldValue.toString());
            }
            if (isTrue) rows.add(row);
            isTrue = false;
        }

        if (sheelName.equals("") || sheelName == null) {
            sheelName = "Sheet1";
        }
        // ?????????????????????writer
        ExcelWriter writer = ExcelUtil.getWriterWithSheet(sheelName);
        // ?????????????????????????????????????????????????????????
//                writer.merge(rows.size() - 1, "???????????????");

        int rowCount = 0;
        int sheetColumnCount = exportList.size();
        // ???????????????????????????????????????????????????????????????
        if (list.size() == 0) {
            List<String> rowHead = new ArrayList<>();
            for (ExportAfferentCustomExcel export : exportList) {
                rowHead.add(export.getTitle());
            }
            writer.writeHeadRow(rowHead);
        } else {
            rowCount = rows.size();
            writer.write(rows, true);
        }
        //?????????????????????????????????????????????????????????
        writer.autoSizeColumnAll();

        //??????Row??????
        writer.setRowHeight(0, 25);

        //??????Row??????
        for (int i = 1; i <= rowCount; i++) {
            writer.setRowHeight(i, 20);
        }

        StyleSet style = writer.getStyleSet();
        CellStyle cellStyle = style.getHeadCellStyle();
        Font f1 = writer.createFont();
        f1.setBold(true);
        f1.setFontName("??????");
        short fontHeight = 280;
        f1.setFontHeight(fontHeight);
        cellStyle.setFont(f1);

        List<org.apache.poi.ss.usermodel.Sheet> sheetList = writer.getSheets();
        for (org.apache.poi.ss.usermodel.Sheet sheet : sheetList) {
            for (int i = 0; i <= sheetColumnCount; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        //response???HttpServletResponse??????
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //test.xls??????????????????????????????????????????????????????????????????????????????
        response.setHeader("Content-Disposition", "attachment;filename=test.xls");
        ServletOutputStream out = response.getOutputStream();

        writer.flush(out, true);
        // ??????writer???????????????
        writer.close();
        //????????????????????????Servlet???
        IoUtil.close(out);
    }

    public static void exportCustomExcelCutome(HttpServletResponse response, List<?> list, String customDataJson, List<AttandanceUser> dynamicData, String sheelName) throws NoSuchFieldException, IllegalAccessException, IOException {
        ServletOutputStream out = response.getOutputStream();
        List<ExportAfferentCustomExcel> exportList = new ArrayList<>();
        if (customDataJson != null && !customDataJson.equals("")) {
            exportList = JSON.parseObject(customDataJson, new TypeReference<List<ExportAfferentCustomExcel>>() {
            });
            List<String> filedTypeStr= exportList.stream().map(p->p.getDataIndex()).collect(Collectors.toList());
            dynamicData =dynamicData.stream().filter(p->filedTypeStr.contains(p.getEmployeecode())).collect(Collectors.toList());
        } else {
            Object obj = list.get(0);
            Class objClass = obj.getClass();
            Field[] fields = objClass.getDeclaredFields();
            for (Field field : fields) {
                TableField tableField = field.getAnnotation(TableField.class);
                if (tableField != null) {
                    ExportAfferentCustomExcel afferentCustomExcel = new ExportAfferentCustomExcel();
                    ExcelField excelField = field.getAnnotation(ExcelField.class);
                    afferentCustomExcel.setTitle(excelField != null ? excelField.value() : field.getName());
                    afferentCustomExcel.setDataIndex(field.getName());
                    exportList.add(afferentCustomExcel);
                }
            }
        }
        Object fieldValue = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Map<String, Object>> rows = new ArrayList<>();
        boolean isTrue = false;
        for (Object item : list) {

            String userAcoount = "";
            List<String> userList = new ArrayList<>();

            Class objClass = item.getClass();
            Map<String, Object> row = new LinkedHashMap<>();
            for (ExportAfferentCustomExcel export : exportList) {
                isTrue = true;

                if (export.getIsDynamic() == 0) {

                        Field field = objClass.getDeclaredField(export.getDataIndex());
                        field.setAccessible(true);
                        fieldValue = field.get(item);


                        // ???????????????Boolean ????????????
                        /**
                         if (field.getGenericType().toString().equals("class java.lang.Boolean")) {
                         fieldValue = (Boolean) field.get(item) == true ? "???" : "???";
                         }*/

                        // ???????????????boolean ??????????????????????????? ???????????????????????????????????? isXXX??? ???????????????isXXX???
                        // ???????????????getter????????????
                        if (field.getGenericType().toString().equals("boolean")) {
                            fieldValue = (boolean) field.get(item) == true ? "???" : "???";
                        }
                        // ???????????????Date
                        if (field.getGenericType().toString().equals("class java.util.Date")) {
                            fieldValue = (Date) field.get(item);
                            if (fieldValue != null) {
                                fieldValue = formatter.format(fieldValue);
                            }
                        }

                    if (export.getDataIndex().equals("employeeid")) {
                        userAcoount = fieldValue == null ? "" : fieldValue.toString();
                        userList.add(userAcoount);
                    }
                } else {
                    List<AttandanceUser> listUser = dynamicData.stream().filter(p -> userList.contains(p.getEmployeeid()) && p.getEmployeecode().equals(export.getDataIndex())).collect(Collectors.toList());
                    if (listUser.size() > 0) {
                        fieldValue = listUser.get(0).getBan();
                    } else {
                        fieldValue = null;
                    }
                }
                if (fieldValue == null) fieldValue = "";
                row.put(export.getTitle(), fieldValue.toString());
            }

            if (isTrue) rows.add(row);
            isTrue = false;
        }

        if (sheelName.equals("") || sheelName == null) {
            sheelName = "Sheet1";
        }
        // ?????????????????????writer
        ExcelWriter writer = ExcelUtil.getWriterWithSheet(sheelName);
        // ?????????????????????????????????????????????????????????
//                writer.merge(rows.size() - 1, "???????????????");

        int rowCount = 0;
        int sheetColumnCount = exportList.size();
        // ???????????????????????????????????????????????????????????????
        if (list.size() == 0) {
            List<String> rowHead = new ArrayList<>();
            for (ExportAfferentCustomExcel export : exportList) {
                rowHead.add(export.getTitle());
            }
            writer.writeHeadRow(rowHead);
        } else {
            rowCount = rows.size();
            writer.write(rows, true);
        }
//        SXSSFSheet sheet = (SXSSFSheet)writer.getSheet();
//        //??????????????????SXSSFSheet  ????????????trackAllColumnsForAutoSizing??????
//        sheet.trackAllColumnsForAutoSizing();
//        //?????????????????????????????????????????????????????????
//        writer.autoSizeColumnAll();
        writer.autoSizeColumnAll();
        //??????Row??????
        writer.setRowHeight(0, 25);

        //??????Row??????
        for (int i = 1; i <= rowCount; i++) {
            writer.setRowHeight(i, 20);
        }

        StyleSet style = writer.getStyleSet();
        CellStyle cellStyle = style.getHeadCellStyle();
        Font f1 = writer.createFont();
        f1.setBold(true);
        f1.setFontName("??????");
        short fontHeight = 280;
        f1.setFontHeight(fontHeight);
        cellStyle.setFont(f1);

        List<org.apache.poi.ss.usermodel.Sheet> sheetList = writer.getSheets();
        for (org.apache.poi.ss.usermodel.Sheet sheet : sheetList) {
            for (int i = 0; i <= sheetColumnCount; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        //response???HttpServletResponse??????
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //test.xls??????????????????????????????????????????????????????????????????????????????
        response.setHeader("Content-Disposition", "attachment;filename=test.xls");


        writer.flush(out, true);
        // ??????writer???????????????
        writer.close();
        //????????????????????????Servlet???
        IoUtil.close(out);
    }

    public static void exportExcel(HttpServletResponse response, Class<?> clazz, List<?> listData, String sheetName) throws IOException {
        ExcelMapping excelMappingData = ExcelMappingFactory.get(clazz);
        int sheetColumnCount = excelMappingData.getPropertyList().size();
        ExcelWriter writer = ExcelUtil.getWriterWithSheet(sheetName);

        //?????????????????????????????????????????????????????????
        //writer.merge(row1.size() - 1, "????????????1");
        //writer.passCurrentRow();

        List<String> rowHead = new ArrayList<>();
        Map<String, String> headerAliasData = new LinkedHashMap<>();
        for (ExcelProperty item : excelMappingData.getPropertyList()) {
            rowHead.add(item.getColumn());
            headerAliasData.put(item.getName(), item.getColumn());
        }

        int rowCount = 0;
        if (listData != null) {
            rowCount = listData.size();
        }
        if (rowCount == 0) {
            writer.writeHeadRow(rowHead);
        } else {
            writer.setHeaderAlias(headerAliasData);
            writer.write(listData, true);
        }

        //?????????????????????????????????????????????????????????
        writer.autoSizeColumnAll();
        //??????Row??????
        writer.setRowHeight(0, 25);

        //??????Row?????? ?????? ???
//        for (int i = 1; i <= rowCount; i++) {
//            writer.setRowHeight(i, 20);
//        }

        StyleSet style = writer.getStyleSet();
        CellStyle cellStyle = style.getHeadCellStyle();
        Font f1 = writer.createFont();
        f1.setBold(true);
        f1.setFontName("??????");
        short fontHeight = 280;
        f1.setFontHeight(fontHeight);
        cellStyle.setFont(f1);

        List<Sheet> sheetList = writer.getSheets();
        for (org.apache.poi.ss.usermodel.Sheet sheet : sheetList) {
            for (int i = 0; i <= sheetColumnCount; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        //response???HttpServletResponse??????
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //test.xls??????????????????????????????????????????????????????????????????????????????
        response.setHeader("Content-Disposition", "attachment;filename=test.xls");
        ServletOutputStream out = response.getOutputStream();

        writer.flush(out, true);
        // ??????writer???????????????
        writer.close();
        //????????????????????????Servlet???
        IoUtil.close(out);
    }

    public static void exportCustomExcel(HttpServletResponse response, List<?> list, String customDataJson, String sheelName) throws NoSuchFieldException, IllegalAccessException, IOException {
        List<cc.mrbird.febs.export.domain.ExportAfferentCustomExcel> exportList = new ArrayList<>();
        if (customDataJson != null && !customDataJson.equals("")) {
            exportList = JSON.parseObject(customDataJson, new TypeReference<List<cc.mrbird.febs.export.domain.ExportAfferentCustomExcel>>() {
            });
        } else {
            Object obj = list.get(0);
            Class objClass = obj.getClass();
            Field[] fields = objClass.getDeclaredFields();
            for (Field field : fields) {
                TableField tableField = field.getAnnotation(TableField.class);
                if (tableField != null) {
                    cc.mrbird.febs.export.domain.ExportAfferentCustomExcel afferentCustomExcel = new cc.mrbird.febs.export.domain.ExportAfferentCustomExcel();
                    ExcelField excelField = field.getAnnotation(ExcelField.class);
                    afferentCustomExcel.setTitle(excelField != null ? excelField.value() : field.getName());
                    afferentCustomExcel.setDataIndex(field.getName());
                    exportList.add(afferentCustomExcel);
                }
            }
        }
        Object fieldValue = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Map<String, Object>> rows = new ArrayList<>();
        boolean isTrue = false;
        for (Object item : list) {
            Class objClass = item.getClass();
            Map<String, Object> row = new LinkedHashMap<>();
            for (cc.mrbird.febs.export.domain.ExportAfferentCustomExcel export : exportList) {
                isTrue = true;
                Field field = objClass.getDeclaredField(export.getDataIndex());
                field.setAccessible(true);
                fieldValue = field.get(item);
                // ???????????????Boolean ????????????
                if (field.getGenericType().toString().equals("class java.lang.Boolean")) {
                    fieldValue = (Boolean) field.get(item) == true ? "???" : "???";
                }

                // ???????????????boolean ??????????????????????????? ???????????????????????????????????? isXXX??? ???????????????isXXX???
                // ???????????????getter????????????
                if (field.getGenericType().toString().equals("boolean")) {
                    fieldValue = (boolean) field.get(item) == true ? "???" : "???";
                }
                // ???????????????Date
                if (field.getGenericType().toString().equals("class java.util.Date")) {
                    fieldValue = (Date) field.get(item);
                    if (fieldValue != null) {
                        fieldValue = formatter.format(fieldValue);
                    }
                }
                if (fieldValue == null) fieldValue = "";
                row.put(export.getTitle(), fieldValue.toString());
            }
            if (isTrue) rows.add(row);
            isTrue = false;
        }

        if (sheelName.equals("") || sheelName == null) {
            sheelName = "Sheet1";
        }
        // ?????????????????????writer
        ExcelWriter writer = ExcelUtil.getWriterWithSheet(sheelName);
        // ?????????????????????????????????????????????????????????
//                writer.merge(rows.size() - 1, "???????????????");

        int rowCount = 0;
        int sheetColumnCount = exportList.size();
        // ???????????????????????????????????????????????????????????????
        if (list.size() == 0) {
            List<String> rowHead = new ArrayList<>();
            for (cc.mrbird.febs.export.domain.ExportAfferentCustomExcel export : exportList) {
                rowHead.add(export.getTitle());
            }
            writer.writeHeadRow(rowHead);
        } else {
            rowCount = rows.size();
            writer.write(rows, true);
        }
        //?????????????????????????????????????????????????????????
        writer.autoSizeColumnAll();
        //??????Row??????
        writer.setRowHeight(0, 25);

        //??????Row?????? ?????? ???
//        for (int i = 1; i <= rowCount; i++) {
//            writer.setRowHeight(i, 20);
//        }

        StyleSet style = writer.getStyleSet();
        CellStyle cellStyle = style.getHeadCellStyle();
        Font f1 = writer.createFont();
        f1.setBold(true);
        f1.setFontName("??????");
        short fontHeight = 280;
        f1.setFontHeight(fontHeight);
        cellStyle.setFont(f1);


        List<Sheet> sheetList = writer.getSheets();
        for (org.apache.poi.ss.usermodel.Sheet sheet : sheetList) {
            for (int i = 0; i <= sheetColumnCount; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        //response???HttpServletResponse??????
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //test.xls??????????????????????????????????????????????????????????????????????????????
        response.setHeader("Content-Disposition", "attachment;filename=test.xls");
        ServletOutputStream out = response.getOutputStream();

        writer.flush(out, true);
        // ??????writer???????????????
        writer.close();
        //????????????????????????Servlet???
        IoUtil.close(out);
    }

    public static void exportTemplateExcel(HttpServletResponse response, List<?> list, String customDataJson, String tempUrl, int startRowCount) throws NoSuchFieldException, IllegalAccessException, IOException {
        List<cc.mrbird.febs.export.domain.ExportAfferentCustomExcel> exportList = new ArrayList<>();
        if (customDataJson != null && !customDataJson.equals("")) {
            exportList = JSON.parseObject(customDataJson, new TypeReference<List<cc.mrbird.febs.export.domain.ExportAfferentCustomExcel>>() {
            });
        } else {
            Object obj = list.get(0);
            Class objClass = obj.getClass();
            Field[] fields = objClass.getDeclaredFields();
            for (Field field : fields) {
                TableField tableField = field.getAnnotation(TableField.class);
                if (tableField != null) {
                    cc.mrbird.febs.export.domain.ExportAfferentCustomExcel afferentCustomExcel = new cc.mrbird.febs.export.domain.ExportAfferentCustomExcel();
                    ExcelField excelField = field.getAnnotation(ExcelField.class);
                    afferentCustomExcel.setTitle(excelField != null ? excelField.value() : field.getName());
                    afferentCustomExcel.setDataIndex(field.getName());
                    exportList.add(afferentCustomExcel);
                }
            }
        }
        Object fieldValue = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Map<String, Object>> rows = new ArrayList<>();
        boolean isTrue = false;
        for (Object item : list) {
            Class objClass = item.getClass();
            Map<String, Object> row = new LinkedHashMap<>();
            for (cc.mrbird.febs.export.domain.ExportAfferentCustomExcel export : exportList) {
                isTrue = true;
                Field field = objClass.getDeclaredField(export.getDataIndex());
                field.setAccessible(true);
                fieldValue = field.get(item);
                // ???????????????Boolean ????????????
                if (field.getGenericType().toString().equals("class java.lang.Boolean")) {
                    fieldValue = (Boolean) field.get(item) == true ? "???" : "???";
                }

                // ???????????????boolean ??????????????????????????? ???????????????????????????????????? isXXX??? ???????????????isXXX???
                // ???????????????getter????????????
                if (field.getGenericType().toString().equals("boolean")) {
                    fieldValue = (boolean) field.get(item) == true ? "???" : "???";
                }
                // ???????????????Date
                if (field.getGenericType().toString().equals("class java.util.Date")) {
                    fieldValue = (Date) field.get(item);
                    if (fieldValue != null) {
                        fieldValue = formatter.format(fieldValue);
                    }
                }
                if (fieldValue == null) fieldValue = "";
                row.put(export.getTitle(), fieldValue.toString());
            }
            if (isTrue) rows.add(row);
            isTrue = false;
        }

        // ?????????????????????writer
        InputStream inputStream = new FileInputStream(tempUrl);
        ExcelReader reader = ExcelUtil.getReader(inputStream, 0);
        ExcelWriter writer = reader.getWriter();
        // ?????????????????????????????????????????????????????????
//                writer.merge(rows.size() - 1, "???????????????");
        //????????????????????? startRowCount4
        for (int i = 0; i < startRowCount; i++) {
            writer.passCurrentRow();
        }

        int rowCount = 0;
        startRowCount = startRowCount == 0 ? 1 : startRowCount;
        int sheetColumnCount = exportList.size();
        // ???????????????????????????????????????????????????????????????
        if (list.size() > 0) {
            rowCount = rows.size() + startRowCount;
            writer.write(rows, false);
        }
        //?????????????????????????????????????????????????????????
        writer.autoSizeColumnAll();
        //??????Row??????
        writer.setRowHeight(0, 25);

        //??????Row?????? ?????? ???
        /*
        for (int i = startRowCount; i <= rowCount; i++) {
            writer.setRowHeight(i, 20);
            //???????????????????????????
//            for (int j = 0; j <= sheetColumnCount; j++) {
//                Cell cellfor = writer.getCell(j, i);//?????????????????? ??????????????????
//                if(cellfor!=null) {
//                    CellStyle cellStylefor = cellfor.getCellStyle();
//                    cellStylefor.setWrapText(true);
//                }
//            }
            Cell cellfor = writer.getCell(0, i);//?????????????????? ??????????????????
            if(cellfor!=null) {
                CellStyle cellStylefor = cellfor.getCellStyle();
                cellStylefor.setWrapText(true);
            }
        }*/

        StyleSet style = writer.getStyleSet();
        CellStyle cellStyle = style.getHeadCellStyle();
        Font f1 = writer.createFont();
        f1.setBold(true);
        f1.setFontName("??????");
        short fontHeight = 280;
        f1.setFontHeight(fontHeight);
        cellStyle.setFont(f1);

        List<Sheet> sheetList = writer.getSheets();
        for (org.apache.poi.ss.usermodel.Sheet sheet : sheetList) {
            for (int i = 0; i <= sheetColumnCount; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        //response???HttpServletResponse??????
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //test.xls??????????????????????????????????????????????????????????????????????????????
        response.setHeader("Content-Disposition", "attachment;filename=test.xls");
        ServletOutputStream out = response.getOutputStream();

        writer.flush(out, true);
        // ??????writer???????????????
        writer.close();
        //????????????????????????Servlet???
        IoUtil.close(out);
    }

    public static void exportTemplateFile(HttpServletResponse response, Class<?> clazz, String sheetName) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriterWithSheet(sheetName);

        ExcelMapping excelMappingData = ExcelMappingFactory.get(clazz);
        Map<String, Integer> sheetColumnCountMap = new LinkedHashMap<>();
        sheetColumnCountMap.put(sheetName, excelMappingData.getPropertyList().size());

        List<String> rowHead = new ArrayList<>();
        for (ExcelProperty item : excelMappingData.getPropertyList()) {
            rowHead.add(item.getColumn());
        }
        writer.writeHeadRow(rowHead);

        //?????????????????????????????????????????????????????????
        writer.autoSizeColumnAll();
        //??????Row??????
        writer.setRowHeight(0, 40);

        StyleSet style = writer.getStyleSet();
        CellStyle cellStyle = style.getHeadCellStyle();
        Font f1 = writer.createFont();
        f1.setBold(true);
        f1.setFontName("??????");
        short fontHeight = 280;
        f1.setFontHeight(fontHeight);
        cellStyle.setFont(f1);
        style.setBackgroundColor(IndexedColors.WHITE, true);

        List<Sheet> sheetList = writer.getSheets();
        for (org.apache.poi.ss.usermodel.Sheet sheet : sheetList) {
            int count = sheetColumnCountMap.get(sheet.getSheetName());
            for (int i = 0; i <= count; i++) {
                sheet.autoSizeColumn(i);
            }
        }

        //response???HttpServletResponse??????
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //test.xls??????????????????????????????????????????????????????????????????????????????
        response.setHeader("Content-Disposition", "attachment;filename=test.xls");
        ServletOutputStream out = response.getOutputStream();

        writer.flush(out, true);
        // ??????writer???????????????
        writer.close();
        //????????????????????????Servlet???
        IoUtil.close(out);
    }

    public static void exportTemplateFileT(HttpServletResponse response, Class<?> clazz1, String sheetName1, Class<?> clazz2, String sheetName2) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriterWithSheet(sheetName1);

        ExcelMapping excelMappingData = ExcelMappingFactory.get(clazz1);
        Map<String, Integer> sheetColumnCountMap = new LinkedHashMap<>();
        sheetColumnCountMap.put(sheetName1, excelMappingData.getPropertyList().size());

        List<String> rowHead = new ArrayList<>();
        for (ExcelProperty item : excelMappingData.getPropertyList()) {
            rowHead.add(item.getColumn());
        }
        writer.writeHeadRow(rowHead);

        //?????????????????????????????????????????????????????????
        writer.autoSizeColumnAll();
        //??????Row??????
        writer.setRowHeight(0, 40);

        ExcelMapping excelMappingMain = ExcelMappingFactory.get(clazz2);
        sheetColumnCountMap.put(sheetName2, excelMappingMain.getPropertyList().size());

        rowHead.clear();
        writer.setSheet(sheetName2);

        for (ExcelProperty item : excelMappingMain.getPropertyList()) {
            rowHead.add(item.getColumn());
        }

        writer.writeHeadRow(rowHead);

        //?????????????????????????????????????????????????????????
        writer.autoSizeColumnAll();
        //??????Row??????
        writer.setRowHeight(0, 40);

        StyleSet style = writer.getStyleSet();
        CellStyle cellStyle = style.getHeadCellStyle();
        Font f1 = writer.createFont();
        f1.setBold(true);
        f1.setFontName("??????");
        short fontHeight = 280;
        f1.setFontHeight(fontHeight);
        cellStyle.setFont(f1);
        style.setBackgroundColor(IndexedColors.WHITE, true);

        List<Sheet> sheetList = writer.getSheets();
        for (org.apache.poi.ss.usermodel.Sheet sheet : sheetList) {
            int count = sheetColumnCountMap.get(sheet.getSheetName());
            for (int i = 0; i <= count; i++) {
                sheet.autoSizeColumn(i);
            }
        }

        //response???HttpServletResponse??????
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //test.xls??????????????????????????????????????????????????????????????????????????????
        response.setHeader("Content-Disposition", "attachment;filename=test.xls");
        ServletOutputStream out = response.getOutputStream();

        writer.flush(out, true);
        // ??????writer???????????????
        writer.close();
        //????????????????????????Servlet???
        IoUtil.close(out);
    }

}
