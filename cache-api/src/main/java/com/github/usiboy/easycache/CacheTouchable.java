package com.github.usiboy.easycache;

/**
 * 缓存组件是否支持touch的功能
 * @author liumingjian
 * @date 2018/8/28
 **/
public interface CacheTouchable {

    /**
     * 更新缓存数据的超时时间
     *
     * @param key
     * @param newExpireTime 新的过期时间
     * @return
     */
    boolean touch(String key, int newExpireTime) throws CacheException;

    /**
     * 根据key得到相应的值并且延长该缓存时间
     *
     * @param key
     * @param newExpireTime
     * @return
     * @throws CacheException
     */
    <T> T getAndTouch(String key, final int newExpireTime) throws CacheException;
}
