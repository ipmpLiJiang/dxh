package cc.mrbird.febs.system.domain;

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

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author MrBird
 */
@Data
@Excel("社保结转表")
public class Cwgz implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String autoid;

    /**
     * 人员编号
     */
    @ExcelField(value = "人员编号")
    private String cpsnnum;

    /**
     * 姓名
     */
    @ExcelField(value = "姓名")
    private String cpsnname;

    /**
     * 发放年月
     */
    @DateTimeFormat(pattern="yyyy/MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy/MM")
    private Date period;

    /**
     * 身份证号
     */
    private String Idnum;


    /**
     * 部门编码
     */
    private String cDeptnum;

    /**
     * 部门名称
     */
    @ExcelField(value = "科室")
    private String cdeptname;

    /**
     * 人员类别
     */
    @ExcelField(value = "人员类型",writeConverterExp = "1=在编,2=合同,3=代理,4=协和在编,5=协和非编",readConverterExp="在编=1,合同=2,代理=3,协和在编=4,协和非编=5")
    private String rpersontype;

    /**
     * 社保编号
     */
    @ExcelField(value = "社保个人编号")
    private String socialnum;


    /**
     * 雇佣状态
     */
    private String rEmployState;


    /**
     * 岗位工资
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Gwgz;

    /**
     * 冲销津贴
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Cxjt;

    /**
     * 薪级工资
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Xjgz;

    /**
     * 护龄津贴
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Hljt;

    /**
     * 护士10%
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Hs10;

    /**
     * 高出部
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Gcb;

    /**
     * 特岗津贴
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Tgjt;

    /**
     * 房贴
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Ft;

    /**
     * 卫生费
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Wsf;

    /**
     * 医疗补助
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Ylbz;

    /**
     * 独子补贴
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Dzbt1;

    /**
     * 基础性绩效
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Jcxjx;

    /**
     * 高聘
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Gp;

    /**
     * 交通补贴
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Jtbt;

    /**
     * 物业补贴
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Wybt;

    /**
     * 差额补贴
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Cebt;

    /**
     * 工资预付
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Gzyf;

    /**
     * 调代2016
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Td2016;

    /**
     * 调代2017
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Td2017;

    /**
     * 调代2018
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Td2018;

    /**
     * 调代2019
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Td2019;

    /**
     * 调代2020
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Td2020;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Td2021;

    /**
     * 上存
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Sc;

    /**
     * 其他
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Qt;

    /**
     * 劳模补助
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Lmbz;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double drugCost;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double hydropowerCost;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double roomCharge;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double meetingCost;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double accumulationCost;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double incomeTax;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Zgongyl;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Zynj;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Deylgrbj;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double HrOtherReduce;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double SaveDown;

    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double bzyl;

    /**
     * 应发合计
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double answerTotal;

    /**
     * 病假天数
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Integer Bjts;

    /**
     * 病假扣款
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double Kqkk;

    /**
     * 大额医疗个人缴纳
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "大额医疗个人",  maxLength = 20)
    private Double deylgrjn;

    /**
     * 工伤单位
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "工伤单位",  maxLength = 20)
    private Double gsdwjn;

    /**
     * 补助医疗单位
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "公务员单位",  maxLength = 20)
    private Double bzyldw;

    /**
     * 生育单位
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "生育单位",  maxLength = 20)
    private Double shengyudw;

    /**
     * 失业个人缴纳
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "失业个人",  maxLength = 20)
    private Double shiygrbj;

    /**
     * 失业单位
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "失业单位",  maxLength = 20)
    private Double shiydw;

    /**
     * 养老个人缴纳
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "养老个人",  maxLength = 20)
    private Double yanglgr;

    /**
     * 养老单位
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "养老单位",  maxLength = 20)
    private Double yangldw;

    /**
     * 医疗个人缴纳
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "医疗个人",  maxLength = 20)
    private Double yilgrjn;

    /**
     * 医疗单位
     */
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    @ExcelField(value = "医疗单位",  maxLength = 20)
    private Double yildw;

}
