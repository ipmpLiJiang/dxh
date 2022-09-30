package cc.mrbird.febs.system.domain;

import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

/**
 * 
 *
 * @author MrBird
 */
@Data
public class EmployeeJobLevelTitleGetReport {

    @ExcelField(value = "岗位等级编码")
    private Integer joblevel;

    @ExcelField(value = "岗位等级名称")
    private String joblevelname;

    @ExcelField(value = "人事子范围编码")
    private Integer rszfw;

    @ExcelField(value = "取得职称")
    private Integer titleget;

    @ExcelField(value = "小计")
    private Integer xj;

    @ExcelField(value = "正高医师")
    private Integer zgys;

    @ExcelField(value = "正高护理")
    private Integer zghl;

    @ExcelField(value = "正高医技")
    private Integer zgyj;

    @ExcelField(value = "正高其他")
    private Integer zgqt;

    @ExcelField(value = "副高医师")
    private Integer fgys;

    @ExcelField(value = "副高护理")
    private Integer fghl;

    @ExcelField(value = "副高医技")
    private Integer fgyj;

    @ExcelField(value = "副高其他")
    private Integer fgqt;

    @ExcelField(value = "中级医师")
    private Integer zjys;

    @ExcelField(value = "中级护理")
    private Integer zjhl;

    @ExcelField(value = "中级医技")
    private Integer zjyj;

    @ExcelField(value = "中级其他")
    private Integer zjqt;

    @ExcelField(value = "初级医师")
    private Integer cjys;

    @ExcelField(value = "初级护理")
    private Integer cjhl;

    @ExcelField(value = "初级医技")
    private Integer cjyj;

    @ExcelField(value = "初级其他")
    private Integer cjqt;

    @ExcelField(value = "员级医师")
    private Integer yjys;

    @ExcelField(value = "员级护理")
    private Integer yjhl;

    @ExcelField(value = "员级医技")
    private Integer yjyj;

    @ExcelField(value = "员级其他")
    private Integer yjqt;

    @ExcelField(value = "未定医师")
    private Integer wdys;

    @ExcelField(value = "未定护理")
    private Integer wdhl;

    @ExcelField(value = "未定医技")
    private Integer wdyj;

    @ExcelField(value = "未定其他")
    private Integer wdqt;

}
