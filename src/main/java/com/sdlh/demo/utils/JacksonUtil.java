/*
 * Copyright 2019 成都深地领航能源科技有限公司. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sdlh.demo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.util.TimeZone;

/**
 * Jackson转换工具
 *
 * @author 成都深地领航能源科技有限公司
 */
public class JacksonUtil {
    private JacksonUtil() {}

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();

    static {
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        objectMapper.setFilterProvider(simpleFilterProvider);
        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
            @Override
            public Object findFilterId(Annotated a) {
                return a.getName();
            }
        });
    }

    public static String objectToJSONStr(Object object) throws JsonProcessingException {
        simpleFilterProvider.setDefaultFilter(SimpleBeanPropertyFilter.serializeAll());
        return objectMapper.writeValueAsString(object);
    }

    public static String filterOutAllExceptToJSONStr(Object object,String... propertyArray) throws JsonProcessingException {
        simpleFilterProvider.setDefaultFilter(SimpleBeanPropertyFilter.filterOutAllExcept(propertyArray));
        return objectMapper.writeValueAsString(object);
    }

    public static String serializeAllExceptToJSONStr(Object object,String... propertyArray) throws JsonProcessingException {
        simpleFilterProvider.setDefaultFilter(SimpleBeanPropertyFilter.serializeAllExcept(propertyArray));
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T jsonToObject(String JSONStr, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(JSONStr, clazz);
    }

    public static <T> T jsonToCollection(String JSONStr) throws JsonProcessingException {
        return objectMapper.readValue(JSONStr, new TypeReference<T>() {});
    }

}
