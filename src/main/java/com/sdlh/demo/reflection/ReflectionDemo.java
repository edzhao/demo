/*
 * Copyright 2019 成都深地领航能源科技有限公司. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sdlh.demo.reflection;

import com.sdlh.demo.concurrent.Employee;
import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * @author 成都深地领航能源科技有限公司
 */
@Slf4j
public class ReflectionDemo {
    public static void main(String[] args) {
        introspectTest(Employee.builder()
                               .name("jude")
                               .age(29)
                               .sex("M")
                               .build(), "name");
    }

    private static void introspectTest(Employee employee, String property) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(Employee.class);
            PropertyDescriptor[] descrtptors=beanInfo.getPropertyDescriptors();
            if(descrtptors!=null&&descrtptors.length>0){
                Stream.of(descrtptors)
                      .filter(descrtptor -> descrtptor.getName().equals(property))
                      .findAny()
                      .ifPresent(descrtptor -> {
                          Method readMethod = descrtptor.getReadMethod();
                          Object result;
                          try {
                              result = readMethod.invoke(employee);
                              log.info(property + "=" + result);
                          } catch (Exception e) {
                              log.error(e.getMessage(), e);
                          }
                      });
            }
        } catch (IntrospectionException e) {
            log.error(e.getMessage(), e);
        }
    }
}
