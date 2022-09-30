package cc.mrbird.febs.system.domain;


import cc.mrbird.febs.common.authentication.JWTUtil;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.shiro.SecurityUtils;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 *
 * @author MrBird
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Education implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 职工编号
     */
    private Integer employeeid;

    /**
     * 结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private Date enddate;

    /**
     * 学历
     */
    private Integer academic;

    private String academicname;

    /**
     * 学校
     */
    private String school;

    private String profession;

    /**
     * 学位
     */
    private Integer degrees;

    private String degreesname;


    /**
     * 学习方式
     */
    private Integer studymethod;

    private String studymethodname;

    /**
     * 导师
     */
    private String tutor;

    /**
     * 学历路径
     */
    private String academicpath;

    /**
     * 学位路径
     */
    private String degreespath;

    /**
     * 学历证书电子备案表名称
     */
    private String xlzsdzbaname;

    /**
     * 学历证书电子备案表路径
     */
    private String xlzsdzbapath;

    /**
     * 备注
     */
    private String memo;

    private String xtUpdateby;

    @TableField("xt_updatedate")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date xtUpdatedate;

    private Integer zqflag;

    private Integer state;

    private String operation;

    private transient String code;

    private transient String xwzsfileid;
    private transient String xlzsfileid;
    private transient String dzbabfileid;

}
