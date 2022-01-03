package com.sdlh.demo.concurrent;

import com.sdlh.demo.utils.BeanMapUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Exchanger;

@Slf4j
public class ExchangerDemo {
    static class Worker extends Thread {
        private Exchanger<Employee> exchanger;
        private Employee employee;

        public Worker(Exchanger<Employee> exchanger, Employee employee) {
            this.exchanger = exchanger;
            this.employee = employee;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(500);
                Employee exchange = exchanger.exchange(employee);
                log.info("交换过来的对象为：" + exchange);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        try {
            Exchanger<Employee> exchanger = new Exchanger<>();
//            Employee jude = Employee.builder().name("jude").age(29).sex("M").build();
            Map<String, Object> map = new HashMap<>();
            map.put("name", "jude");
            map.put("age", 29);
            map.put("sex", "M");
            Employee jude = BeanMapUtil.mapToBean(map, Employee.class);
            Worker worker1 = new ExchangerDemo.Worker(exchanger, jude);
            worker1.setName("worker1");
            worker1.start();
//            Employee bond = Employee.builder().name("bond").age(31).sex("M").build();
            map.clear();
            map.put("name", "bond");
            map.put("age", 31);
            map.put("sex", "M");
            Employee bond = BeanMapUtil.mapToBean(map, Employee.class);
            Worker worker2 = new ExchangerDemo.Worker(exchanger, bond);
            worker2.setName("worker2");
            worker2.start();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("本次程序共执行" + (System.currentTimeMillis() - start) + "ms");
    }
}