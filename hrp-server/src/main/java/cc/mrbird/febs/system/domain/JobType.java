package cc.mrbird.febs.system.domain;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class JobType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 职工编号
     */
    private Integer employeeid;

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
     * 岗位类型
     */
    private Integer jobtype;

    private String jobtypename;

    /**
     * 岗位等级
     */
    private Integer joblevel;

    /**
     * 岗位等级名称
     */
    private String joblevelname;
    /**
     * 附件路径
     */
    private String certificatepath;

    /**
     * 附件名称
     */
    private String certificatename;

    private String xtUpdateby;

    private Date xtUpdatedate;

    private String prdw;

    private transient String zsfileid;
}
