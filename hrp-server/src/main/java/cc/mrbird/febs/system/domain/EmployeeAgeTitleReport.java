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
public class EmployeeAgeTitleReport {

    @ExcelField(value = "人事子范围编码")
    private Integer rszfw;

    @ExcelField(value = "人事子范围")
    private String rszfwname;

    @ExcelField(value = "正高25x")
    private Integer zg25x;

    @ExcelField(value = "正高30")
    private Integer zg30;

    @ExcelField(value = "正高35")
    private Integer zg35;

    @ExcelField(value = "正高40")
    private Integer zg40;

    @ExcelField(value = "正高41s")
    private Integer zg41s;

    @ExcelField(value = "副高25x")
    private Integer fg25x;

    @ExcelField(value = "副高30")
    private Integer fg30;

    @ExcelField(value = "副高35")
    private Integer fg35;

    @ExcelField(value = "副高40")
    private Integer fg40;

    @ExcelField(value = "副高41s")
    private Integer fg41s;

    @ExcelField(value = "中级25x")
    private Integer zj25x;

    @ExcelField(value = "中级30")
    private Integer zj30;

    @ExcelField(value = "中级35")
    private Integer zj35;

    @ExcelField(value = "中级40")
    private Integer zj40;

    @ExcelField(value = "中级41s")
    private Integer zj41s;

    @ExcelField(value = "初级25x")
    private Integer cj25x;

    @ExcelField(value = "初级30")
    private Integer cj30;

    @ExcelField(value = "初级35")
    private Integer cj35;

    @ExcelField(value = "初级40")
    private Integer cj40;

    @ExcelField(value = "初级41s")
    private Integer cj41s;

    @ExcelField(value = "员级25x")
    private Integer yj25x;

    @ExcelField(value = "员级30")
    private Integer yj30;

    @ExcelField(value = "员级35")
    private Integer yj35;

    @ExcelField(value = "员级40")
    private Integer yj40;

    @ExcelField(value = "员级41s")
    private Integer yj41s;

    @ExcelField(value = "未定25x")
    private Integer wd25x;

    @ExcelField(value = "未定30")
    private Integer wd30;

    @ExcelField(value = "未定35")
    private Integer wd35;

    @ExcelField(value = "未定40")
    private Integer wd40;

    @ExcelField(value = "未定41s")
    private Integer wd41s;

}
