package com.sdlh.demo.objectpool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class UserPOObjectFactory extends BasePooledObjectFactory<UserPO> {
    @Override
    public UserPO create() throws Exception {
        return new UserPO();
    }

    @Override
    public PooledObject<UserPO> wrap(UserPO obj) {
        return new DefaultPooledObject<>(obj);
    }
}
