package cc.mrbird.febs.system.domain;

import cc.mrbird.febs.common.utils.CustomerDoubleSerialize;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author MrBird
 */
@Data
@Excel("薪酬表")
public class Emolument implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 员工编号
     */
    @ExcelField(value = "员工编号", required = true, maxLength = 20)
    private transient String code;

    @ExcelField(value = "员工姓名", required = true, maxLength = 20)
    private transient String employeename;

    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    private Date period;


    private  String dept;

    @ExcelField(value = "科室")
    private  String deptname;

    private  String ward;

    @ExcelField(value = "病区")
    private  String wardname;

    @ExcelField(value = "员工状态",writeConverterExp = "1=在职,2=退休,3=返聘,4=外调,5=离职,6=死亡,7=离休",readConverterExp = "在职=1,退休=2,返聘=3,外调=4,离职=5,死亡=6,离休=7")
    private  Integer employeestatu;

    @ExcelField(value = "员工类型",writeConverterExp = "1=在编,2=合同,3=代理,4=协和在编,5=协和非编",readConverterExp="在编=1,合同=2,代理=3,协和在编=4,协和非编=5")
    private  Integer employeetype;

    private  String rszfw;

    @ExcelField(value = "人员子范围")
    private  String rszfwname;

    private transient String job;

    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 期间
     */
    private Integer periodid;

    /**
     * 员工编号
     */
    @ExcelField(value = "系统编号")
    private Integer employeeid;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "其他",  maxLength = 20)
    private Double qt;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "岗位工资",  maxLength = 20)
    private Double gwgz;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "冲销津贴",  maxLength = 20)
    private Double cxjt;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "薪级工资",  maxLength = 20)
    private Double xjgz;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "护龄津贴",  maxLength = 20)
    private Double hljt;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "护士10%",  maxLength = 20)
    private Double hs;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "高出部",  maxLength = 20)
    private Double gcb;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "特岗津贴",  maxLength = 20)
    private Double tgjt;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "房贴",  maxLength = 20)
    private Double ft;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "卫生费",  maxLength = 20)
    private Double wsf;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "独子补贴",  maxLength = 20)
    private Double dzbt;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "基础性绩效",  maxLength = 20)
    private Double jcxjx;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "高聘",  maxLength = 20)
    private Double gp;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "交通补贴",  maxLength = 20)
    private Double jtbt;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "物业补贴",  maxLength = 20)
    private Double wybt;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "差额补贴",  maxLength = 20)
    private Double cebt;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "工资预付",  maxLength = 20)
    private Double gzyf;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "调代2016", maxLength = 20)
    private Double td2016;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "调代2017",  maxLength = 20)
    private Double td2017;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "调代2018",  maxLength = 20)
    private Double td2018;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "调代2019",  maxLength = 20)
    private Double td2019;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "调代2020",  maxLength = 20)
    private Double td2020;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "调代2021",  maxLength = 20)
    private Double td2021;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "上存",  maxLength = 20)
    private Double sc;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "应发金额", maxLength = 20)
    private Double issue;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "医疗补助",  maxLength = 20)
    private Double ylbz;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "劳模补助",  maxLength = 20)
    private Double lmbz;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "药费",  maxLength = 20)
    private Double drugcost;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "水电",  maxLength = 20)
    private Double hydropowercost;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "房费",  maxLength = 20)
    private Double roomcharge;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "会费",  maxLength = 20)
    private Double meetingcost;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "公积金",  maxLength = 20)
    private Double accumulationcost;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "所得税",  maxLength = 20)
    private Double incometax;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "职工养老",  maxLength = 20)
    private Double zgongyl;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "职业年金",  maxLength = 20)
    private Double zynj;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "大病医",  maxLength = 20)
    private Double deylgrbj;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "代理养老",  maxLength = 20)
    private transient Double bzyl;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "其它扣款项",  maxLength = 20)
    private Double hrotherreduce;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "下存",  maxLength = 20)
    private Double savedown;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "实发",  maxLength = 20)
    private Double actual;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private transient Double byyf;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private transient Double syyf;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private transient Double ce;


    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private transient Double bzylYj;
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private transient Double deylGr;
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private transient Double syGr;
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private transient Double yanglaoGr;
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private transient Double ylGr;


    private Long status;

    private transient String ids;

    private transient String employeeids;
    private transient int emolumentstatus;

    private transient String deptids;

    private transient String states;

    private transient String employeetypes;

    private transient String  rszfws;

    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    private transient Date startdate;

    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    private transient Date enddate;

    private String idnumber;

    private transient Integer count;

    private transient String emolumentflag;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private transient Date comestartdate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private transient Date comeenddate;



}
