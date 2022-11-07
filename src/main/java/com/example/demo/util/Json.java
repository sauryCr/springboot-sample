package com.example.demo.util;

import com.alibaba.excel.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class Json {
    
    private static ObjectMapper objectMapper;
    
    static {
        init();
    }
    
    public static void init() {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }
    
    public static Optional<String> stringify(Object obj) {
        if (obj == null) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(objectMapper.writeValueAsString(obj));
        } catch (Exception e) {
            log.error("JSON 序列化错误", e);
        }
        return Optional.empty();
    }
    
    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("JSON 序列化错误", e);
        }
        return null;
    }
    
    public static <T> Optional<T> convert(Object obj, Class<T> clazz) {
        if (obj == null) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(objectMapper.convertValue(obj, clazz));
        } catch (Exception e) {
            log.error("JSON 转换错误", e);
        }
        return Optional.empty();
    }
    
    public static <T> T convert4Object(Object obj, Class<T> clazz) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.convertValue(obj, clazz);
        } catch (Exception e) {
            log.error("JSON 转换错误", e);
        }
        return null;
    }
    
    public static <T> Optional<T> convert(Object obj, TypeReference<T> typeReference) {
        if (obj == null) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(objectMapper.convertValue(obj, typeReference));
        } catch (Exception e) {
            log.error("JSON 转换错误", e);
        }
        return Optional.empty();
    }
    
    public static <T> T convert4Object(Object obj, TypeReference<T> typeReference) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.convertValue(obj, typeReference);
        } catch (Exception e) {
            log.error("JSON 转换错误", e);
        }
        return null;
    }
    
    public static <T> Optional<T> parse(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(objectMapper.readValue(json, clazz));
        } catch (Exception e) {
            log.error("JSON 解析错误, json={}, clazz={}", json, clazz, e);
        }
        return Optional.empty();
    }
    
    public static <T> T parse4String(String json, Class<T> clazz) {
        return parse(json, clazz).orElse(null);
    }
    
    public static <T> T parse4String(String json, TypeReference<T> typeReference) {
        return parse(json, typeReference).orElse(null);
    }
    
    public static <T> Optional<T> parse(String json, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(json)) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(objectMapper.readValue(json, typeReference));
        } catch (Exception e) {
            log.error("JSON 解析错误", e);
        }
        return Optional.empty();
    }
    
    public static <T> Optional<T> parse(String json, JavaType type) {
        if (StringUtils.isEmpty(json)) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(objectMapper.readValue(json, type));
        } catch (Exception e) {
            log.error("JSON 解析错误", e);
        }
        return Optional.empty();
    }
    
    
    public static TypeFactory typeFactory() {
        return objectMapper.getTypeFactory();
    }
    
}