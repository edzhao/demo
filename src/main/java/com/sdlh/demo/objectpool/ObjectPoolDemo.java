package com.sdlh.demo.objectpool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.ObjectPool;

@Slf4j
public class ObjectPoolDemo {
    public static void main(String[] args) {
        ObjectPool<UserPO> userPOPool = MyObjectPool.getUserPOPool();
        new Thread(() -> {
            try {
                for (int i=0; i < 1000; i++) {
                    UserPO userPO = userPOPool.borrowObject();
                    userPO.setName("zak");
                    userPO.setAge(38);
                    userPO.setPassword("123456");
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }).start();


    }
}
