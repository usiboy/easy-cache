package com.github.usiboy.easycache.guava;

import com.github.usiboy.easycache.AbstractEasyCache;
import com.github.usiboy.easycache.CacheException;
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
public class GuavaCache extends AbstractEasyCache {

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
