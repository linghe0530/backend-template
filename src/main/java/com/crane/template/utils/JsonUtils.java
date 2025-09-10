package com.crane.template.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author crane
 * @date 2025.06.25 下午10:15
 * @description
 **/
@Slf4j
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        // 2. 忽略未知字段（JSON 中有多余字段时不报错）
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T parse(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            log.error("json parse error:", e);
            throw new RuntimeException();

        }
    }

    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("to json error:", e);
            throw new RuntimeException();
        }
    }

    /**
     * 将JSON字符串转为List集合
     */
    public static <T> List<T> parseList(String json) {
        // 转换为List<User>
        try {
            if (StringUtils.isBlank(json)) {
                return CollUtils.emptyList();
            }
            return OBJECT_MAPPER.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object parse(String json, Type type) throws JsonProcessingException {
        if (json == null || json.isEmpty()) {
            return null;
        }
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructType(type);
        return OBJECT_MAPPER.readValue(json, javaType);
    }
}
