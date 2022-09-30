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
public class Ward implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 病区id
     */
    private String keyy;

    /**
     * 病区名称
     */
    private String valuee;

    /**
     * 病区描述
     */
    private String description;

    /**
     * 所属科室id
     */
    private String deptid;


}
