package com.sdlh.demo.objectpool;

import lombok.Data;

@Data
public class UserPO {
    private String name;
    private String password;
    private int age;

    public UserPO() {}
}
