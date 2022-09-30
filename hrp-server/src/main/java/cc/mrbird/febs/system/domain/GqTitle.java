package cc.mrbird.febs.system.domain;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
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
public class GqTitle implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;

    private Integer employeeid;

    /**
     * 取得时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date getdate;

    /**
     * 取得等级
     */
    private String getlevel;

    /**
     * 证书路径
     */
    private String certificatepath;

    /**
     * 证书名称
     */
    private String certificatename;

    /**
     * 证书编号
     */
    private String number;

    private String xtUpdateby;

    private String xtUpdatedate;

    private transient String code;

    private transient String zsfileid;
}
