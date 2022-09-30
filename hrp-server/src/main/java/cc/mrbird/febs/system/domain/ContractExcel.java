package cc.mrbird.febs.system.domain;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

/**
 * @author MrBird
 */
@Data
@Excel("Sheet1")
public class ContractExcel {

    /**
     * 编码
     */
    @ExcelField(value = "人员编号")
    private String code;

    /**
     * 姓名
     */
    @ExcelField(value = "姓名")
    private String employeename;

    /**
     * 性别
     */
    @ExcelField(value = "性别")
    private String gender;

    /**
     * 合同类型
     */
    @ExcelField(value = "合同类型", writeConverterExp = "1=固定期合同,2=无固定期合同", readConverterExp = "固定期合同=1,无固定期合同=2")
    private String contracttype;

    /**
     * 开始时间 TimeConverter
     */
    @ExcelField(value = "合同开始时间")
    private String startdate;

    /**
     * 结束时间
     */
    @ExcelField(value = "合同结束时间")
    private String enddate;

    /**
     * 备注
     */
    @ExcelField(value = "备注")
    private String memo;
}
