package com.github.usiboy.easycache.guava;

import com.github.usiboy.easycache.CacheException;
import com.github.usiboy.easycache.EasyCache;
import com.google.common.base.Function;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by liumingjian on 2017/5/3.
 */
public class GuavaCache implements EasyCache {

    long maximumSize = 2000;

    long defaultExpire = 600;

    Cache<String, Object> cache;

    public void startup(){
        getCache();
    }

    Cache<String, Object> buildCache(){
        return CacheBuilder.newBuilder().maximumSize(maximumSize)
                .expireAfterWrite(defaultExpire, TimeUnit.SECONDS)
                .build();
    }

    Cache<String, Object> getCache(){
        if(null == cache){
            synchronized (this){
                if(null == cache){
                    cache = buildCache();
                }
            }
        }
        return cache;
    }

    @Override
    public <T> Map<String, T> gets(String... key) throws CacheException {
        try {
            final ImmutableMap<String, Object> allPresent = getCache().getAllPresent(Arrays.asList(key));
            return Maps.toMap(allPresent.keySet().iterator(), new Function<String, T>() {
                @Override
                public T apply(String input) {
                    return (T) allPresent.get(input);
                }
            });
        }catch(Exception e){
            throw new CacheException(e);
        }
    }

    @Override
    public <T> T get(String key) throws CacheException {
        try {
            return (T) getCache().getIfPresent(key);
        }catch(Exception e){
            throw new CacheException(e);
        }
    }

    @Override
    public <T> T get(String key, long timeout) throws CacheException {
        return get(key);
    }

    @Override
    public <T> T getAndTouch(String key, int newExpireTime) throws CacheException {
        // guava的cache没办法对每一个key设置不同的touch时间
        return get(key);
    }

    @Override
    public void incr(String key, Long value) throws CacheException {
        synchronized (this) {
            Object origin = get(key);
            if (null == origin) {
                set(key, value);
                return;
            }
            if (!(origin instanceof Number)) {
                throw new CacheException(key + "'s value type is not Number!");
            }

            Number ovalue = (Number)origin;
            set(key, ovalue.longValue() + value);
        }
    }

    @Override
    public void decr(String key, Long value) throws CacheException {
        synchronized (this) {
            Object origin = get(key);
            if (null == origin) {
                set(key, 0L - value);
                return;
            }
            if (!(origin instanceof Number))
                throw new CacheException(key + "'s value type is not Number!");

            Number ovalue = (Number)origin;
            set(key, ovalue.longValue() - value);
        }
    }

    @Override
    public boolean set(String key, Object value) throws CacheException {
        try {
            getCache().put(key, value);
            return true;
        }catch(Exception e){
            throw new CacheException(e);
        }
    }

    @Override
    public boolean set(String key, Object value, int exp) throws CacheException {
        // guava的cache没办法对每一个key设置不同的过期时间，只能设置一个统一的过期时间
        return set(key, value);
    }

    @Override
    public boolean touch(String key, int newExpireTime) throws CacheException {
        // guava的cache没办法对每一个key设置不同的touch时间
        return false;
    }

    @Override
    public boolean delete(String key) throws CacheException {
        try {
            getCache().invalidate(key);
            return true;
        }catch(Exception e){
            throw new CacheException(e);
        }
    }

    @Override
    public boolean containKey(String key) throws CacheException {
        return getCache().asMap().containsKey(key);
    }

    @Override
    public Set<String> keys() throws CacheException {
        return getCache().asMap().keySet();
    }

    @Override
    public boolean add(String key, Object value, int expireTime) throws CacheException {
        return !cache.asMap().containsKey(key) && set(key, value, expireTime);
    }

    public long getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(long maximumSize) {
        this.maximumSize = maximumSize;
    }

    public long getDefaultExpire() {
        return defaultExpire;
    }

    public void setDefaultExpire(long defaultExpire) {
        this.defaultExpire = defaultExpire;
    }
}
