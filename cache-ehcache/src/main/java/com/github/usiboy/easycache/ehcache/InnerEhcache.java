package com.github.usiboy.easycache.ehcache;

import com.github.usiboy.easycache.AbstractEasyCache;
import com.github.usiboy.easycache.CacheException;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import java.util.*;

/**
 * 这个缓存对象由{@link EasyEhcache}构造出来
 * @author liumingjian
 * @date 2018/8/28
 **/
class InnerEhcache extends AbstractEasyCache{

    final Cache cache;

    InnerEhcache(Cache cache){
        this.cache = cache;
    }

    @Override
    public <T> Map<String, T> gets(String... key) throws CacheException {
        Map<Object, Element> results = this.cache.getAll(Arrays.asList(key));
        Map<String, T> list = new HashMap<String, T>();
        for(Map.Entry<Object, Element> entry : results.entrySet()){
            list.put(entry.getKey().toString(), (T)entry.getValue().getObjectValue());
        }
        return list;
    }

    @Override
    public <T> T get(String key) throws CacheException {
        Element el = this.cache.get(key);
        return el==null?null:(T)el.getObjectValue();
    }

    /**
     * Ehcache不需要设置超时时间
     * @param key
     * @param timeout 等待时间,单位为毫秒
     * @param <T>
     * @return
     * @throws CacheException
     */
    @Override
    public <T> T get(String key, long timeout) throws CacheException {
        return get(key);
    }

    @Override
    public boolean set(String key, Object value) throws CacheException {
        this.cache.put(new Element(key, value));
        return true;
    }

    /**
     * ehcache不支持过期时间在API层面上的控制
     * @param key   缓存key
     * @param value 存储的值
     * @param exp   过期时间,单位为秒,0表示永久存储
     * @return
     * @throws CacheException
     */
    @Override
    public boolean set(String key, Object value, int exp) throws CacheException {
        return set(key, value);
    }

    @Override
    public boolean delete(String key) throws CacheException {
        return cache.remove(key);
    }

    @Override
    public boolean containKey(String key) throws CacheException {
        return cache.isKeyInCache(key);
    }

    @Override
    public Set<String> keys() throws CacheException {
        Set<String> set = new HashSet<String>();
        List keys = cache.getKeys();
        for(Object key : keys){
            if(key == null){
                continue;
            }
            set.add(key.toString());
        }
        return set;
    }
}
