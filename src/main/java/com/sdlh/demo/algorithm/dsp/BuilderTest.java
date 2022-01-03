package com.sdlh.demo.algorithm.dsp;

import lombok.*;

@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
class User {
    private String username;
    private String password;
}
// 测试类
public class BuilderTest {
    public static void main(String[] args) {
        User user1 = User.builder()
                         .password("123456")
                         .username("admin")
                         .build();
        System.out.println(user1);

        User user2 = user1.toBuilder().username("admin2").build();
        // 验证user2是否是基于user1的现有属性创建的
        System.out.println(user2);
        // 验证对象是否是同一对象
        System.out.println(user1 == user2);
    }
}
