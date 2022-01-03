package com.sdlh.demo.springboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/testScope")
public class TestScopeController {
    private String name;

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest servletRequest;

    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public void userProfile() {
        String user = servletRequest.getParameter("name");
        log.info("请求" + servletRequest.hashCode() + ",参数name=" + user);
        name = user;
        orderService.setOrderNum(name);
        try {
            for(int i = 0; i < 60; i++) {
                log.info("threadId:" + Thread.currentThread().getId() + ",name:" + name + ",order:" + orderService.getOrderNum());
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
