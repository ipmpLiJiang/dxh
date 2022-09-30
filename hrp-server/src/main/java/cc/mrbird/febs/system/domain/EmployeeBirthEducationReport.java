package cc.mrbird.febs.system.domain;

import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

/**
 *
 *
 * @author MrBird
 */
@Data
public class EmployeeBirthEducationReport {
    private String id;

    @ExcelField(value = "人员类别")
    private String rylb;

    @ExcelField(value = "岗位等级编码")
    private Integer joblevel;

    @ExcelField(value = "岗位等级名称")
    private String joblevelname;

    @ExcelField(value = "出生年月")
    private Date birth;

    @ExcelField(value = "学历")
    private Integer academic;

    @ExcelField(value = "学历小计")
    private Integer xlxj;

    @ExcelField(value = "学历大专以下")
    private Integer xldzyx;

    @ExcelField(value = "学历本科")
    private Integer xlbk;

    @ExcelField(value = "学历硕士")
    private Integer xlss;

    @ExcelField(value = "学历博士")
    private Integer xlbs;

    @ExcelField(value = "年龄35以下")
    private Integer nl35x;

    @ExcelField(value = "年龄40")
    private Integer nl40;

    @ExcelField(value = "年龄45")
    private Integer nl45;

    @ExcelField(value = "年龄50")
    private Integer nl50;

    @ExcelField(value = "年龄54")
    private Integer nl54;

    @ExcelField(value = "年龄59")
    private Integer nl59;

    @ExcelField(value = "年龄60以上")
    private Integer nl60s;


}
