package cc.mrbird.febs.system.domain;

import java.time.LocalDate;
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
public class JobTypeDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private String keyy;
    /**
     * 岗位类型名称
     */
    private String valuee;

    private String xtUpdateby;

    private LocalDate xtUpdatedate;


}
