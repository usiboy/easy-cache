package com.github.usiboy.easycache.ehcache;

import com.github.usiboy.easycache.AbstractEasyCache;
import com.github.usiboy.easycache.CacheException;
import com.github.usiboy.easycache.EasyCache;
import com.github.usiboy.easycache.MultiCacheable;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 使用Ehcache实现的EasyCache，不能直接调用当前类的{@link EasyCache}方法，而应该调用{@link EasyEhcache#getCache(String)}方法，获取EasyCache实例进行缓存操作
 * @author liumingjian
 * @date 2018/8/28
 **/
public class EasyEhcache extends AbstractEasyCache implements MultiCacheable {

    @Resource
    CacheManager cacheManager;

    Set<String> cacheNames;

    Map<String, EasyCache> innerCaches = new HashMap<String, EasyCache>();

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public <T> Map<String, T> gets(String... key) throws CacheException {
        throw new CacheException("Not Implemented, please invoking getCache(name)");
    }

    @Override
    public <T> T get(String key) throws CacheException {
        throw new CacheException("Not Implemented, please invoking getCache(name)");
    }

    @Override
    public <T> T get(String key, long timeout) throws CacheException {
        throw new CacheException("Not Implemented, please invoking getCache(name)");
    }

    @Override
    public Long incr(String key, Long value) throws CacheException {
        throw new CacheException("Not Implemented, please invoking getCache(name)");
    }

    @Override
    public Long decr(String key, Long value) throws CacheException {
        throw new CacheException("Not Implemented, please invoking getCache(name)");
    }

    @Override
    public boolean set(String key, Object value) throws CacheException {
        throw new CacheException("Not Implemented, please invoking getCache(name)");
    }

    @Override
    public boolean set(String key, Object value, int exp) throws CacheException {
        throw new CacheException("Not Implemented, please invoking getCache(name)");
    }

    @Override
    public boolean delete(String key) throws CacheException {
        throw new CacheException("Not Implemented, please invoking getCache(name)");
    }

    @Override
    public boolean containKey(String key) throws CacheException {
        throw new CacheException("Not Implemented, please invoking getCache(name)");
    }

    @Override
    public Set<String> keys() throws CacheException {
        throw new CacheException("Not Implemented, please invoking getCache(name)");
    }

    @Override
    public Set<String> getNames() {
        if(null == cacheNames){
            cacheNames = new HashSet<String>();
            final String[] names = cacheManager.getCacheNames();
            for(String name : names){
                cacheNames.add(name);
            }
        }
        return cacheNames;
    }

    @Override
    public EasyCache getCache(String name) {
        if(innerCaches.containsKey(name)){
            return innerCaches.get(name);
        }
        Cache innerCache = cacheManager.getCache(name);
        if(null == innerCache){
            innerCaches.put(name, null);
            return null;
        }
        InnerEhcache innerEhcache = new InnerEhcache(cacheManager.getCache(name));
        innerEhcache.setName(name);
        innerCaches.put(name, innerEhcache);
        return innerEhcache;
    }
}
