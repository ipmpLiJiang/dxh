package cc.mrbird.febs.ylj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import cc.mrbird.febs.common.converter.*;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author viki
 * @since 2021-11-25
 */

@Excel("养老金")
@Data
@Accessors(chain = true)
public class YljBRecord implements Serializable{

private static final long serialVersionUID=1L;

                    @TableId(value = "id" , type = IdType.AUTO)
                
    private Long id;

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

    /**
     * 状态
     */
        

    private Integer state;

    /**
     * 是否删除
     */
    @TableField("IS_DELETEMARK")
        

    private Integer isDeletemark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
        

    private Date createTime;
    private transient String createTimeFrom;
    private transient String createTimeTo;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
        

    private Date modifyTime;
    private transient String modifyTimeFrom;
    private transient String modifyTimeTo;

    /**
     * 创建人
     */
    @TableField("CREATE_USER_ID")
        

    private Long createUserId;

    /**
     * 修改人
     */
    @TableField("MODIFY_USER_ID")
        

    private Long modifyUserId;

    /**
     * 系统编号
     */
    private transient Integer employeeid;

    private transient String employeecode;

    private transient String employeename;


    public static final String ID ="id" ;

    public static final String GRBH ="grbh" ;

    public static final String USER_NAME ="user_name" ;

    public static final String ID_CARD ="id_card" ;

    public static final String FKSSQ ="fkssq" ;

    public static final String START_DATE ="start_date" ;

    public static final String END_DATE ="end_date" ;

    public static final String JFLX ="jflx" ;

    public static final String JFJSHJ ="jfjshj" ;

    public static final String YLDWJF ="yldwjf" ;

    public static final String YLGRJF ="ylgrjf" ;

    public static final String YLDWLX ="yldwlx" ;

    public static final String YLGRLX ="ylgrlx" ;

    public static final String YLXJ ="ylxj" ;

    public static final String NJDWJFXZ ="njdwjfxz" ;

    public static final String NJDWJFSZ ="njdwjfsz" ;

    public static final String NJGRJF ="njgrjf" ;

    public static final String DWXZXL ="dwxzxl" ;

    public static final String DWSZLX ="dwszlx" ;

    public static final String GRLX ="grlx" ;

    public static final String ZSJE ="zsje" ;

    public static final String NJXJ ="njxj" ;

    public static final String DWYJHJ ="dwyjhj" ;

    public static final String GRYJHJ ="gryjhj" ;

    public static final String ZJ ="zj" ;

    public static final String STATE ="state" ;

    public static final String IS_DELETEMARK ="IS_DELETEMARK" ;

    public static final String CREATE_TIME ="CREATE_TIME" ;

    public static final String MODIFY_TIME ="MODIFY_TIME" ;

    public static final String CREATE_USER_ID ="CREATE_USER_ID" ;

    public static final String MODIFY_USER_ID ="MODIFY_USER_ID" ;

        }