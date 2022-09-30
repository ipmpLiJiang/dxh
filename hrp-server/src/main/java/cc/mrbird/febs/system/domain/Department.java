package cc.mrbird.febs.system.domain;


import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 
 *
 * @author MrBird
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 科室id
     */
    private String keyy;
    /**
     * 科室名称
     */
    private String valuee;

    /**
     * 科室描述
     */
    private String description;

    /**
     * 科室类型
     */
    private Long departmenttype;

    /**
     * 父科室id
     */
    private String parentid;

    /**
     * 父科室名称
     */
    private String parentvaluee;

    /**
     * 编号
     */
    private String code;

    /**
     * 所属院区id
     */
    private Long orgid;

    private transient String oaid;

    private String path;

    private transient String dj;


}
