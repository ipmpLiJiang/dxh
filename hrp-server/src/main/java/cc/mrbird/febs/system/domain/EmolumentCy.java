package cc.mrbird.febs.system.domain;

import cc.mrbird.febs.common.utils.CustomerDoubleSerialize;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author MrBird
 */
@Data
@Excel("薪酬差异")
public class EmolumentCy {
    /**
     * 员工编号
     */
    @ExcelField(value = "员工编号", required = true, maxLength = 20)
    private transient String code;

    @ExcelField(value = "姓名", required = true, maxLength = 20)
    private transient String employeename;

    @ExcelField(value = "科室")
    private  String deptname;


//    @ExcelField(value = "病区")
//    private  String wardname;

    @ExcelField(value = "员工类型",writeConverterExp = "1=在编,2=合同,3=代理,4=协和在编,5=协和非编",readConverterExp="在编=1,合同=2,代理=3,协和在编=4,协和非编=5")
    private  Integer employeetype;

    @ExcelField(value = "员工状态",writeConverterExp = "1=在职,2=退休,3=返聘,4=外调,5=离职,6=死亡,7=离休",readConverterExp = "在职=1,退休=2,返聘=3,外调=4,离职=5,死亡=6,离休=7")
    private  Integer employeestatu;

//    private  String rszfw;

//    @ExcelField(value = "人员子范围")
//    private  String rszfwname;

    @ExcelField(value = "上月应发(元) ",  maxLength = 20)
    private transient Double syyf;

    @ExcelField(value = "本月应发(元)",  maxLength = 20)
    private transient Double byyf;

    @ExcelField(value = "差异金额(元)",  maxLength = 20)
    private transient Double ce;


}
