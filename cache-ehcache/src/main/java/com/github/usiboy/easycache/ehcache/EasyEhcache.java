package com.github.usiboy.easycache.ehcache;

import com.github.usiboy.easycache.AbstractEasyCache;
import com.github.usiboy.easycache.CacheException;
import net.sf.ehcache.CacheManager;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

/**
 * 使用Ehcache实现的EasyCache
 * @author liumingjian
 * @date 2018/8/28
 **/
public class EasyEhcache extends AbstractEasyCache {

    @Resource
    CacheManager cacheManager;

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public <T> Map<String, T> gets(String... key) throws CacheException {
        return null;
    }

    @Override
    public <T> T get(String key) throws CacheException {
        return null;
    }

    @Override
    public <T> T get(String key, long timeout) throws CacheException {
        return null;
    }

    @Override
    public <T> T getAndTouch(String key, int newExpireTime) throws CacheException {
        return null;
    }

    @Override
    public void incr(String key, Long value) throws CacheException {

    }

    @Override
    public void decr(String key, Long value) throws CacheException {

    }

    @Override
    public boolean set(String key, Object value) throws CacheException {
        return false;
    }

    @Override
    public boolean set(String key, Object value, int exp) throws CacheException {
        return false;
    }

    @Override
    public boolean touch(String key, int newExpireTime) throws CacheException {
        return false;
    }

    @Override
    public boolean delete(String key) throws CacheException {
        return false;
    }

    @Override
    public boolean containKey(String key) throws CacheException {
        return false;
    }

    @Override
    public Set<String> keys() throws CacheException {
        return null;
    }

    @Override
    public boolean add(String key, Object value, int expireTime) throws CacheException {
        return false;
    }
}
