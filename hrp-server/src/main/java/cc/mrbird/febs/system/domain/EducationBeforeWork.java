package cc.mrbird.febs.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EducationBeforeWork implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 系统编号
     */
    private Integer employeeid;

    /**
     * 学历
     */
    private Integer academic;

    private String academicname;

    /**
     * 学位
     */
    private Integer degrees;

    private String degreesname;

    /**
     * 毕业学校
     */
    private String school;

    /**
     * 学习方式
     */
    private Integer studymethod;

    private String profession;

    /**
     * 毕业时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date enddate;

    /**
     * 备注
     */
    private String memo;


}
