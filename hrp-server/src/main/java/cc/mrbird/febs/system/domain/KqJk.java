package cc.mrbird.febs.system.domain;

import java.io.Serializable;
import java.util.Date;

import cc.mrbird.febs.common.annotation.KqFiled;
import cc.mrbird.febs.common.converter.TimeConverter;
import cc.mrbird.febs.common.utils.CustomerDoubleSerialize;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 *
 * @author MrBird
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Excel("考勤表")
public class KqJk implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;


    /**
     * 员工编号
     */
    @ExcelField(value = "编号")
    private String employeecode;

    /**
     * 员工姓名
     */
    @ExcelField(value = "员工姓名")
    private String employeename;

    /**
     * 人事科室
     */
    @ExcelField(value = "人事科室")
    private String dept;

    /**
     * 考勤科室
     */
    @ExcelField(value = "考勤单元")
    private transient String kqdept;

    @ExcelField(value = "提交科室")
    private String tjdept;

    @ExcelField(value = "员工类型",writeConverterExp = "1=在编,2=合同,3=代理,4=协和在编,5=协和非编",readConverterExp="在编=1,合同=2,代理=3,协和在编=4,协和非编=5")
    private transient Integer employeetype;

    private Integer rsfw;

    private Integer rszfw;
    /**
     * 开始时间
     */
    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    @ExcelField(value = "考勤年月",writeConverter = TimeConverter.class)
    private Date startdate;

    @ExcelField(value = "提交状态",writeConverterExp = "1=已提交,0=未提交")
    private transient Integer state;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    private Date enddate;

    /**
     * 身份证号
     */
    private String idnumber;

    @KqFiled
    @ExcelField(value = "a01")
    private String a01;

    @KqFiled
    @ExcelField(value = "a02")
    private String a02;

    @KqFiled
    @ExcelField(value = "a03")
    private String a03;

    @KqFiled
    @ExcelField(value = "a04")
    private String a04;

    @KqFiled
    @ExcelField(value = "a05")
    private String a05;

    @KqFiled
    @ExcelField(value = "a06")
    private String a06;

    @KqFiled
    @ExcelField(value = "a07")
    private String a07;

    @KqFiled
    @ExcelField(value = "a08")
    private String a08;

    @KqFiled
    @ExcelField(value = "a09")
    private String a09;

    @KqFiled
    @ExcelField(value = "a10")
    private String a10;

    @KqFiled
    @ExcelField(value = "a11")
    private String a11;

    @KqFiled
    @ExcelField(value = "a12")
    private String a12;

    @KqFiled
    @ExcelField(value = "a13")
    private String a13;

    @KqFiled
    @ExcelField(value = "a14")
    private String a14;

    @KqFiled
    @ExcelField(value = "a15")
    private String a15;

    @KqFiled
    @ExcelField(value = "a16")
    private String a16;

    @KqFiled
    @ExcelField(value = "a17")
    private String a17;

    @KqFiled
    @ExcelField(value = "a18")
    private String a18;

    @KqFiled
    @ExcelField(value = "a19")
    private String a19;

    @KqFiled
    @ExcelField(value = "a20")
    private String a20;

    @KqFiled
    @ExcelField(value = "a21")
    private String a21;

    @KqFiled
    @ExcelField(value = "a22")
    private String a22;

    @KqFiled
    @ExcelField(value = "a23")
    private String a23;

    @KqFiled
    @ExcelField(value = "a24")
    private String a24;

    @KqFiled
    @ExcelField(value = "a25")
    private String a25;

    @KqFiled
    @ExcelField(value = "a26")
    private String a26;

    @KqFiled
    @ExcelField(value = "a27")
    private String a27;

    @KqFiled
    @ExcelField(value = "a28")
    private String a28;

    @KqFiled
    @ExcelField(value = "a29")
    private String a29;

    @KqFiled
    @ExcelField(value = "a30")
    private String a30;

    @KqFiled
    @ExcelField(value = "a31")
    private String a31;

    private String sqbid;

    private transient String employeeid;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private transient Double gwgz;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private transient Double xjgz;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private transient Double wsf;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private transient Double jcxjx;

    private transient String operation;

    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    private Date shdate;

//    private transient Integer shstatus;

}
