package cc.mrbird.febs.ylj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Excel("养老金")
@Data
public class YljRecord_extend {
    /**
     * 个人编号
     */

    @ExcelField(value ="个人编号")
    private String grbh;

    /**
     * 姓名
     */

    @ExcelField(value ="姓名")
    private String userName;

    /**
     * 公民身份证号码
     */

     @ExcelField(value ="公民身份证号码")
    private String idCard;

    /**
     * 费款所属期
     */

    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    @ExcelField(value = "费款所属期", required = true, dateFormat = "yyyyMM")
    private Date fkssq;

    /**
     * 开始年月
     */

    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    @ExcelField(value = "开始年月", required = true, dateFormat = "yyyyMM")
    private Date startDate;

    /**
     * 终止年月
     */
    @DateTimeFormat(pattern="yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM")
    @ExcelField(value = "终止年月", required = true, dateFormat = "yyyyMM")
    private Date endDate;

    /**
     * 缴费类型
     */

      @ExcelField(value ="缴费类型")
    private String jflx;

    /**
     * 缴费基数合计
     */

     @ExcelField(value ="缴费基数合计")
    private Float jfjshj;

    /**
     * 养老单位缴费
     */

      @ExcelField(value ="养老单位缴费")
    private Float yldwjf;

    /**
     * 养老个人缴费
     */

      @ExcelField(value ="养老个人缴费")
    private Float ylgrjf;

    /**
     * 养老单位利息
     */

     @ExcelField(value ="养老单位利息")
    private Float yldwlx;

    /**
     * 养老个人利息
     */

      @ExcelField(value ="养老个人利息")
    private Float ylgrlx;

    /**
     * 养老小计
     */

      @ExcelField(value ="养老小计")
    private Float ylxj;

    /**
     * 年金单位缴费虚账
     */

      @ExcelField(value ="年金单位缴费虚账")
    private Float njdwjfxz;

    /**
     * 年金单位缴费实账
     */

      @ExcelField(value ="年金单位缴费实账")
    private Float njdwjfsz;

    /**
     * 年金个人缴费
     */

      @ExcelField(value ="年金个人缴费")
    private Float njgrjf;

    /**
     * 单位虚账利息
     */

    @ExcelField(value ="单位虚账利息")
    private Float dwxzxl;

    /**
     * 单位实账利息
     */

     @ExcelField(value ="单位实账利息")
    private Float dwszlx;

    /**
     * 个人利息
     */

      @ExcelField(value ="个人利息")
    private Float grlx;

    /**
     * 做实金额
     */

      @ExcelField(value ="做实金额")
    private Float zsje;

    /**
     * 年金小计
     */

      @ExcelField(value ="年金小计")
    private Float njxj;

    /**
     * 单位应缴合计
     */

       @ExcelField(value ="单位应缴合计")
    private Float dwyjhj;

    /**
     * 个人应缴合计
     */

     @ExcelField(value ="个人应缴合计")
    private Float gryjhj;

    /**
     * 总计
     */

      @ExcelField(value ="总计")
    private Float zj;
}
