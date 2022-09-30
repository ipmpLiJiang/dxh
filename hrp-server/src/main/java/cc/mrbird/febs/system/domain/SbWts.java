package cc.mrbird.febs.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author MrBird
 */
@Data
@Excel("社保未推送")
public class SbWts {

    private String idnumber;

    /**
     * 员工编号
     */
    @ExcelField(value = "员工编号", required = true, maxLength = 20)
    private transient String code;

    @ExcelField(value = "员工姓名", required = true, maxLength = 20)
    private transient String employeename;

    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(timezone = "Asia/Shanghai", pattern = "yyyy-MM")
    private Date period;

    private String dept;

    @ExcelField(value = "科室")
    private String deptname;

    private String ward;

    //    @ExcelField(value = "病区")
    private String wardname;

    //    @ExcelField(value = "员工状态", writeConverterExp = "1=在职,2=退休,3=返聘,4=外调,5=离职,6=死亡,7=离休", readConverterExp = "在职=1,退休=2,返聘=3,外调=4,离职=5,死亡=6,离休=7")
    private Integer employeestatu;

    @ExcelField(value = "员工类型", writeConverterExp = "1=在编,2=合同,3=代理,4=协和在编,5=协和非编", readConverterExp = "在编=1,合同=2,代理=3,协和在编=4,协和非编=5")
    private String employeetype;

    //    @ExcelField(value = "人事范围")
    private String rsfwName;

    @ExcelField(value = "人事范围")
    private String rszfwName;

    @ExcelField(value = "养老个人缴纳合计")
    private Float yanglaoTotal;

    @ExcelField(value = "医疗个人缴纳合计")
    private Float ylTotal;

    @ExcelField(value = "失业个人缴纳合计")
    private Float syTotal;

    @ExcelField(value = "大额医疗个人缴纳合计")
    private Float deylTotal;

    @ExcelField(value = "在编养老个人缴纳合计")
    private Float zxylTotal;

    @ExcelField(value = "在编年金缴纳合计")
    private Float zxnjTotal;

    private String type;

}
