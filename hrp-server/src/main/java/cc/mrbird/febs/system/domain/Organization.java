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
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 院区id
     */
    private Long orgid;

    /**
     * 院区名称
     */
    private String orgname;

    /**
     * 父院区id(备用)
     */
    private Long parentid;

    /**
     * 院区描述
     */
    private String description;


}
