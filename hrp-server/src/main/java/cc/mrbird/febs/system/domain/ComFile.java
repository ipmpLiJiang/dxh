package cc.mrbird.febs.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ComFile implements Serializable {

    private static final long serialVersionUID=1L;
    /**
     * 主键
     */
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 文件名称
     */
    private String clientName;
    /**
     * 服务器地址
     */
    private String serverName;
    /**
     * 是否删除
     */
    private Integer isDeletemark;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date createTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "Asia/Shanghai",pattern = "yyyy-MM-dd")
    private Date modifyTime;
    /**
     * 创建人
     */
    private Long createUserId;
    /**
     * 修改人
     */
    private Long modifyUserId;
    /**
     * 记录ID
     */
    private String refTabId;
    /**
     * 表名
     */
    private String refTabTable;
}
