package com.crane.template.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;

/**
 * @author crane
 * @date 2025.09.02 下午2:10
 * @description
 **/
@Configuration
public class JacksonConfig {



    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        // 自定义序列化器：只对字段名包含"id"的Long类型进行转换
        module.addSerializer(Long.class, new JsonSerializer<>() {
            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // 获取当前序列化的字段名
                String fieldName = gen.getOutputContext().getCurrentName();

                // 如果字段名包含"id"（不区分大小写），则序列化为String
                if (fieldName != null && fieldName.toLowerCase().contains("id")) {
                    gen.writeString(value.toString());
                } else {
                    // 其他Long字段保持数字类型
                    gen.writeNumber(value);
                }
            }
        });

        objectMapper.registerModule(module);
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }
}