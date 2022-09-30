package cc.mrbird.febs.common.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;

public class CustomerDoubleSerialize extends JsonSerializer {
    private DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if(o != null) {
            jsonGenerator.writeString(df.format(o));
        }
    }
}
