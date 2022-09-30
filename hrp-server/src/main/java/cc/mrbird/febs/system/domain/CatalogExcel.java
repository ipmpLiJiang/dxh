package cc.mrbird.febs.system.domain;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

/**
 * 
 *
 * @author MrBird
 */
@Data
@Excel("Sheet1")
public class CatalogExcel {

    @ExcelField(value = "员工编号")
    private String code;

    @ExcelField(value = "姓名")
    private String employeename;

    @ExcelField(value = "大类")
    private String lbname;

    @ExcelField(value = "材料名称")
    private String textname;

    @ExcelField(value = "排序")
    private int secondno;

    @ExcelField(value = "类号")
    private String sortno;

    @ExcelField(value = "年")
    private Integer year;

    @ExcelField(value = "月")
    private Integer month;

    @ExcelField(value = "日")
    private Integer date;

    @ExcelField(value = "份数")
    private Integer count;

    @ExcelField(value = "页数")
    private Integer page;

    @ExcelField(value = "备注")
    private String memo;

    @ExcelField(value = "文件")
    private String filename;


}
