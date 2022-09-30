package cc.mrbird.febs.common.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;


public enum PeriodStatusEnum implements IEnum<Integer> {

    CLOSE(0, "关闭"),
    OPEN(1, "开启");

    private final Integer value;

    private final String name;

    PeriodStatusEnum(Integer value,String name){
        this.value=value;
        this.name=name;
    }

    @JsonValue
    public Integer getValue() {
        return value;
    }
    public String getName() {
        return name;
    };

}
