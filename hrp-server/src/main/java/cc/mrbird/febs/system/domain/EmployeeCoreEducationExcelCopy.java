package cc.mrbird.febs.system.domain;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

/**
 *
 *
 * @author MrBird
 */
@Data
@Excel("Sheet1")
public class EmployeeCoreEducationExcelCopy {

    @ExcelField(value = "入职时间")
    private Date rzdate;

    @ExcelField(value = "工号")
    private String employeecode;

    @ExcelField(value = "姓名")
    private String employeename;

    @ExcelField(value = "性别")
    private String gender;

    @ExcelField(value = "出生年月")
    private Date birth;

    @ExcelField(value = "身份证号")
    private String idnumber;

    @ExcelField(value = "手机号")
    private String phonenumber;

    @ExcelField(value = "籍贯")
    private String homeaddress;

    @ExcelField(value = "民族")
    private String nation;

    @ExcelField(value = "参加工作时间")
    private Date workdate;

    @ExcelField(value = "来院时间")
    private Date comedate;

    @ExcelField(value = "政治面貌")
    private String politicaloutlook;

    @ExcelField(value = "政治面貌名称")
    private String politicaloutlookname;

    @ExcelField(value = "入党时间")
    private Date rddate;

    @ExcelField(value = "入团时间")
    private Date rtdate;

    @ExcelField(value = "开始时间")
    private Date startdate;

    @ExcelField(value = "结束时间")
    private Date enddate;

    @ExcelField(value = "人员状态")
    private Integer status;

    @ExcelField(value = "人员状态名称")
    private String statusname;

    @ExcelField(value = "病区代码")
    private Integer ward;

    @ExcelField(value = "病区名称")
    private String wardname;

    @ExcelField(value = "科室父代码ID")
    private Integer depttype;

    @ExcelField(value = "科室代码ID")
    private Integer dept;

    @ExcelField(value = "科室名称")
    private String deptname;

    @ExcelField(value = "岗位")
    private String job;

    @ExcelField(value = "岗位名称")
    private String jobname;

    @ExcelField(value = "职位")
    private String position;

    @ExcelField(value = "职位名称")
    private String positionname;

    @ExcelField(value = "人员类型")
    private Integer employeetype;

    @ExcelField(value = "人员类型名称")
    private String employeetypename;

    @ExcelField(value = "人事范围")
    private Integer rsfw;

    @ExcelField(value = "人事范围名称")
    private String rsfwname;

    @ExcelField(value = "人事子范围")
    private Integer rszfw;

    @ExcelField(value = "人事子范围名称")
    private String rszfwname;

    @ExcelField(value = "人事事件")
    private Integer eventtype;

    @ExcelField(value = "人事事件名称")
    private String eventtypename;

    @ExcelField(value = "更新人")
    private String xtupdateby;

    @ExcelField(value = "更新时间")
    private Date xtupdatedate;

    @ExcelField(value = "毕业时间1")
    private Date byenddate1;

    @ExcelField(value = "学历1")
    private Integer academic1;

    @ExcelField(value = "毕业学校1")
    private String school1;

    @ExcelField(value = "专业1")
    private String profession1;

    @ExcelField(value = "学位1")
    private Integer degrees1;

    @ExcelField(value = "学习方式1")
    private Integer studymethod1;

    @ExcelField(value = "学习方式名称1")
    private String studymethodname1;

    @ExcelField(value = "导师1")
    private String tutor1;

    @ExcelField(value = "毕业时间2")
    private Date byenddate2;

    @ExcelField(value = "学历2")
    private Integer academic2;

    @ExcelField(value = "毕业学校2")
    private String school2;

    @ExcelField(value = "专业2")
    private String profession2;

    @ExcelField(value = "学位2")
    private Integer degrees2;

    @ExcelField(value = "学习方式2")
    private Integer studymethod2;

    @ExcelField(value = "学习方式名称2")
    private String studymethodname2;

    @ExcelField(value = "导师2")
    private String tutor2;

    @ExcelField(value = "毕业时间3")
    private Date byenddate3;

    @ExcelField(value = "学历3")
    private Integer academic3;

    @ExcelField(value = "毕业学校3")
    private String school3;

    @ExcelField(value = "专业3")
    private String profession3;

    @ExcelField(value = "学位3")
    private Integer degrees3;

    @ExcelField(value = "学习方式3")
    private Integer studymethod3;

    @ExcelField(value = "学习方式名称3")
    private String studymethodname3;

    @ExcelField(value = "导师3")
    private String tutor3;

    @ExcelField(value = "毕业时间4")
    private Date byenddate4;

    @ExcelField(value = "学历4")
    private Integer academic4;

    @ExcelField(value = "毕业学校4")
    private String school4;

    @ExcelField(value = "专业4")
    private String profession4;

    @ExcelField(value = "学位4")
    private Integer degrees4;

    @ExcelField(value = "学习方式4")
    private Integer studymethod4;

    @ExcelField(value = "学习方式名称4")
    private String studymethodname4;

    @ExcelField(value = "导师4")
    private String tutor4;

    @ExcelField(value = "人员类型")
    private Integer event;

    @ExcelField(value = "人员类型名称")
    private String eventname;

}
