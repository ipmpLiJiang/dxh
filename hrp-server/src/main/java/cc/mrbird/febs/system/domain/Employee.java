package cc.mrbird.febs.system.domain;


import cc.mrbird.febs.common.converter.TimeConverter;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 *
 * @author MrBird
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Excel("花名册报表")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer employeeid;

    /*****编号信息******/
    @ExcelField(value = "人员编号")
    private transient String code;

    @NotBlank(message = "员工姓名必填")
    @ExcelField(value = "姓名", required = true, maxLength = 5,
            comment = "提示：必填，长度不能超过5个字符")
    private String employeename;

    @NotBlank(message = "性别必填")
    @ExcelField(value = "性别")
    private String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    @ExcelField(value = "出生年月",writeConverter = TimeConverter.class)
    private Date birth;

    @NotBlank(message = "身份证号必填")
    @ExcelField(value = "身份证号")
    private String idnumber;
    @ExcelField(value = "籍贯")
    private String homeaddress;
    @ExcelField(value = "民族")
    private String nation;

    @ExcelField(value = "状态",writeConverterExp = "1=在职,2=退休,3=返聘,4=外调,5=离职,6=死亡,7=离休")
    private transient String statusname;

    @ExcelField(value = "人员类型",writeConverterExp = "1=在编,2=合同,3=代理,4=协和在编,5=协和非编")
    private transient String employeetype;


    @ExcelField(value = "科室")
    private transient String deptname;

    @ExcelField(value = "病区")
    private transient String wardname;
    @ExcelField(value = "人事子范围")
    private transient String rszfwname;
    @ExcelField(value = "政治面貌",writeConverterExp = "1=中国共产党党员,2=中国共产主义青年团团员,3=九三学社社员,4=中国农工民主党党员,5=中国民主同盟盟员,6=中国民主促进会成员,7=中国民主建国会成员,8=中国致公党党员,9=中国国民党革命委员会成员,10=群众")
    private String politicaloutlookname;
    @ExcelField(value = "最高学历",writeConverterExp = "1=初中,2=高中,3=中专,4=大专,5=本科,6=硕士,7=博士")
    private transient String academicname;
    /*****学历信息******/
    @ExcelField(value = "学校")
    private transient String academicschool;

    @ExcelField(value = "专业")
    private transient String academicprofession;

    @ExcelField(value = "学习形式",writeConverterExp = "1=全日制,2=在职,3=函授,4=自考,5=网络教育,6=脱产,7=业余,8=开放大学")
    private transient String academicstudymethod;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    @ExcelField(value = "毕业时间",writeConverter = TimeConverter.class)
    private transient Date academicenddate;

    /*****学位信息******/
    @ExcelField(value = "最高学位",writeConverterExp = "1=学士,2=硕士,3=博士")
    private transient String degreesname;

    @ExcelField(value = "学校")
    private transient String degreesschool;

    @ExcelField(value = "专业")
    private transient String degreesprofession;

    @ExcelField(value = "学习形式",writeConverterExp = "1=全日制,2=在职,3=函授,4=自考,5=网络教育,6=脱产,7=业余,8=开放大学")
    private transient String degreesstudymethod;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    @ExcelField(value = "毕业时间",writeConverter = TimeConverter.class)
    private transient Date degreesenddate;
    /*****职前学历信息******/
    @ExcelField(value = "最高学历（职前）",writeConverterExp = "1=初中,2=高中,3=中专,4=大专,5=本科,6=硕士,7=博士")
    private transient String zqacademicname;

    @ExcelField(value = "学校（职前）")
    private transient String zqacademicschool;

    @ExcelField(value = "专业（职前）")
    private transient String zqacademicprofession;

    @ExcelField(value = "学习形式（职前）",writeConverterExp = "1=全日制,2=在职,3=函授,4=自考,5=网络教育,6=脱产,7=业余,8=开放大学")
    private transient String zqacademicstudymethod;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    @ExcelField(value = "毕业时间（职前）",writeConverter = TimeConverter.class)
    private transient Date zqacademicenddate;

    /*****职前学位信息******/
    @ExcelField(value = "最高学位（职前）",writeConverterExp = "1=学士,2=硕士,3=博士")
    private transient String zqdegreesname;

    @ExcelField(value = "学校（职前）")
    private transient String zqdegreesschool;

    @ExcelField(value = "专业（职前）")
    private transient String zqdegreesprofession;

    @ExcelField(value = "学习形式（职前）",writeConverterExp = "1=全日制,2=在职,3=函授,4=自考,5=网络教育,6=脱产,7=业余,8=开放大学")
    private transient String zqdegreesstudymethod;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    @ExcelField(value = "毕业时间（职前）",writeConverter = TimeConverter.class)
    private transient Date zqdegreesenddate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    @ExcelField(value = "来院时间",writeConverter = TimeConverter.class)
    private Date comedate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    @ExcelField(value = "参加工作时间",writeConverter = TimeConverter.class)
    private Date workdate;
    /*****合同信息******/
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    @ExcelField(value = "首次签约时间",writeConverter = TimeConverter.class)
    private transient Date contractstartdate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    @ExcelField(value = "合同到期时间",writeConverter = TimeConverter.class)
    private transient Date contractenddate;
    /*****职称信息******/
    @ExcelField(value = "聘用职称")
    private transient String titlename;

    @ExcelField(value = "聘用专业技术职务")
    private transient String dutyname;
    /*****岗位类型信息******/
    @ExcelField(value = "聘用专业岗位等级")
    private transient String technicallevelname;

    @ExcelField(value = "聘用管理岗位等级")
    private transient String mangelevelname;

    @ExcelField(value = "聘用工勤岗位等级")
    private transient String workerlevelname;

    /*****取得职称信息******/
    @ExcelField(value = "取得职称")
    private transient String qdtitlename;

    @ExcelField(value = "取得专业技术职务")
    private transient String qddutyname;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    @ExcelField(value = "取得时间",writeConverter = TimeConverter.class)
    private transient Date qddate;







    private transient String gzdeptname;





    private transient String  rsfwname;



    /**
     * 考勤用户数据
     */
    private transient List<AttandanceUser> attandanceUsers;



    private String phonenumber;






    private Integer politicaloutlook;



    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date rddate;

    private String passportno;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date rtdate;

    private String picturepath;

    @TableField("xt_updateby")
    private String xtUpdateby;

    @TableField("xt_updatedate")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date xtUpdatedate;

    private Integer emolumentstatus;

    private String workfilenum;

    private String retirefilenum;

    private String deathfilenum;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date txdate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date fpdate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date lzdate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date swdate;

    private String memo;

    private transient String search;
    /*****核心信息******/
    private transient String job;

    private transient String position;














    /*****查询条件******/
    private transient String deptids;//科室id

    private transient String states;//状态

    private transient String rsfws;//人事范围

    private transient String rszfws;//人事子范围

    private transient Integer joblevel; //聘用等级

    private transient Integer jobtype; //岗位类型

    private transient Integer titleget;//取得职称

    private transient Integer academic; //学历

    private transient Integer degree; //学位

    private transient Integer zqacademic;// 职前学历

    private transient Integer zqdegree; //职前学位

    private transient Integer title;


    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    private transient Date    birthstartdate;// 出生开始日期

    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    private transient   Date  birthenddate;//出生结束日期


    private transient String employeetypes;//人员状态

    private transient String eventtypes;//事件类型



    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private transient Date startdate;

    private transient String state;

    private transient Integer rsfw;

    private transient Integer rszfw;

    private String kqdept;

    private transient List<KqRmxb> rmxList;

    private Integer gpflag;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date gpdate;

    private String zyzgzbh;

    private String zyfw;

}
