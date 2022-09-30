package cc.mrbird.febs.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Properties;

/**
 * 注册通用枚举类型的转换工厂
 * 对于在RequestBody中的nested枚举，则有jackson进行deserialize；
 *
 * @author shellwalker
 * @date 2019-04-18 19:29
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {
//    @Autowired
//    private FebsProperties properties;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DefaultConversionService defaultConversionService = (DefaultConversionService) DefaultConversionService.getSharedInstance();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**","/upload/**").addResourceLocations("file:c:/qc/upload/");
//        registry.addResourceHandler("/img/**").addResourceLocations("file:"+properties.getUploadPath());
//        registry.addResourceHandler("/img/**").addResourceLocations("file:c:/qc/upload/");
    }

}
