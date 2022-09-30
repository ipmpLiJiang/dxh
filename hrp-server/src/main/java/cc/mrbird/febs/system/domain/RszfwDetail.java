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
public class RszfwDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private int keyy;
    /**
     * 人事子范围名称
     */
    private String valuee;

    /**
     * 人事范围编号
     */
    private int rsfwid;

    private String xtUpdateby;

    private LocalDate xtUpdatedate;


}
