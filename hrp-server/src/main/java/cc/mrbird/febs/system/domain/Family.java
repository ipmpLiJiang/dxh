package cc.mrbird.febs.system.domain;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Date;

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
public class Family implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 员工编号
     */
    private Integer employeeid;

    /**
     * 姓名
     */
    private String name;

    /**
     * 称谓
     */
    private String appellation;

    /**
     * 称谓
     */
    private String cw;

    /**
     * 性别
     */
    private String gender;

    /**
     * 出生日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date birth;

    /**
     * 工作单位及职务
     */
    private String job;

    /**
     * 联系电话
     */
    private String tel;

    private String operation;

    @TableField("xt_updateby")
    private String xtUpdateby;

    @TableField("xt_updatedate")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date xtUpdatedate;

    private transient String code;

}
