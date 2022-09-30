package cc.mrbird.febs.system.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date startdate;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date enddate;

    /**
     * 合同类型
     */
    private Integer contracttype;

    private String contracttypename;

    /**
     * 备注
     */
    private String memo;

    /**
     * 员工系统编号
     */
    private Integer employeeid;

    private String xtUpdateby;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date xtUpdatedate;



    public Contract(){
        xtUpdatedate=new Date();
    }

    private transient String code;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private transient Date inputdate;

    private transient String deptids;//科室id

    private transient String gender;

    /**
     * 姓名
     */
    private transient String employeename;

    /**
     * 科室
     */
    private transient String deptname;

    /**
     * 人员类型
     */
    private transient String employeetype;

    /**
     * 人事子范围
     */
    private transient String rszfwname;
}
