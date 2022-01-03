package com.sdlh.demo.objectpool;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class MyObjectPool {
    private static ObjectPool<UserPO> pool;
    private static PooledObjectFactory factory;
    // 创建原始数据对象工厂配置信息
    private static GenericObjectPoolConfig config;
    public static final int MAX = 5000;
    static {
        config = new GenericObjectPoolConfig();
        config.setMaxTotal(MAX);
        factory = new UserPOObjectFactory();
        pool = new GenericObjectPool<>(factory, config);
    }

    public static ObjectPool<UserPO> getUserPOPool() {
        return pool;
    }
}
