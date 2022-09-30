package cc.mrbird.febs.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * @author MrBird
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Catalog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 员工id
     */
    private Long employeeid;

    /**
     * 所属id
     */
    private String parentid;

    /**
     * 一级排序
     */
    private String firstno;

    private int secondno;

    /**
     * 排序
     */
    private String sortno;

    /**
     * 名称
     */
    private String textname;

    private Integer year;

    private Integer month;

    private Integer date;

    private Integer count;

    private Integer page;

    private String memo;

    private String filename;

    private String sername;

    private transient List<Catalog> children= new ArrayList<>();

}
