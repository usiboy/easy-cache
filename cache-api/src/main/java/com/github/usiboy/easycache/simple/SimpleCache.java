/**
 * @date 2014年4月14日
 * @author huangjie
 */
package com.github.usiboy.easycache.simple;

import com.github.usiboy.easycache.AbstractEasyCache;
import com.github.usiboy.easycache.CacheException;
import com.github.usiboy.easycache.simple.ExpiryMap.ExpiryValue;
import org.apache.commons.lang3.SerializationUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 使用{@link ExpiryMap}可过期的map缓存
 *
 * @author JackyLIU
 */
public class SimpleCache extends AbstractEasyCache {

    private final static ExpiryMap<String, Object> cache = new ExpiryMap<String, Object>(Collections.synchronizedMap(new HashMap<String, ExpiryValue<Object>>()));

    /**
     * 默认过期时间10分钟
     */
    private long defaultExpiry = 10 * 3600;

    public long getDefaultExpiry() {
        return defaultExpiry;
    }

    public void setDefaultExpiry(long defaultExpiry) {
        this.defaultExpiry = defaultExpiry;
    }

    @PostConstruct
    public void startup() {
        cache.startup();
    }

    @PreDestroy
    public void shutdown() {
        cache.shutdown();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Map<String, T> gets(String... keys) throws CacheException {
        Map<String, T> map = new HashMap<String, T>(keys.length);
        for (String key : keys) {
            if (!cache.containsKey(key)) {
                continue;
            }
            Object value = cache.getValue(key);
            if (null == value) {
                continue;
            }
            map.put(key, (T) value);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key) throws CacheException {
        return (T) cache.getValue(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key, long timeout) throws CacheException {
        return (T) cache.getValue(key);
    }

    @Override
    public boolean set(String key, Object value) throws CacheException {
        return set(key, value, (int) defaultExpiry);
    }

    @Override
    public boolean set(String key, Object value, int exp) throws CacheException {
        if (!(value instanceof Serializable)) {
            throw new IllegalArgumentException("value need to implement Serializable interface");
        }
        cache.put(key, exp, SerializationUtils.clone((Serializable) value));
        return true;
    }

    @Deprecated
    @Override
    public boolean touch(String key, int newExpireTime) throws CacheException {
        return false;
    }

    @Override
    public boolean delete(String key) throws CacheException {

        return cache.remove(key) != null;
    }

    @Override
    public boolean containKey(String key) throws CacheException {

        return cache.containsKey(key);
    }

    @Override
    public Set<String> keys() throws CacheException {
        return cache.getMap().keySet();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getAndTouch(String key, int newExpireTime)
            throws CacheException {
        ExpiryValue<Object> ev = cache.get(key);
        ev.setExpiry(ev.getExpiry() + newExpireTime);
        return (T) ev.getValue();
    }

    @Override
    public void incr(String key, Long value) throws CacheException {
        Long curv = 0L;
        if (containKey(key)) {
            curv = get(key);
        }
        cache.put(key, defaultExpiry, curv + value);
    }

    @Override
    public void decr(String key, Long value) throws CacheException {
        Long curv = 0L;
        if (containKey(key)) {
            curv = get(key);
        }
        cache.put(key, defaultExpiry, curv - value);
    }

    @Override
    public boolean add(String key, Object value, int expireTime) throws CacheException {
        return !cache.containsKey(key) && set(key, value, expireTime);
    }
}
