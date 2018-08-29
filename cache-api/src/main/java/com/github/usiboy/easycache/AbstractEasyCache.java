package com.github.usiboy.easycache;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liumingjian
 * @date 2018/8/28
 **/
public abstract class AbstractEasyCache implements EasyCache{

    protected String name;

    private volatile Lock lock;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean add(String key, Object value, int expireTime) throws CacheException {
        getLock().lock();
        try {
            return !containKey(key) && set(key, value, expireTime);
        }finally {
            getLock().unlock();
        }
    }

    @Override
    public Long incr(String key, Long value) throws CacheException {
        getLock().lock();
        try{
            Long origin = get(key);
            if(null == origin){
                origin = 0L;
            }
            final Long result = origin + value;
            set(key, result);
            return result;
        }finally {
            getLock().unlock();
        }
    }

    @Override
    public Long decr(String key, Long value) throws CacheException {
        getLock().lock();
        try{
            Long origin = get(key);
            if(null == origin){
                origin = 0L;
            }
            final Long result = origin - value;
            set(key, result);
            return result;
        }finally {
            getLock().unlock();
        }
    }

    protected Lock getLock(){
        if(null != lock){
            return lock;
        }
        synchronized (this){
            if(null != lock){
                return lock;
            }
            lock = new ReentrantLock();
        }
        return lock;
    }
}
