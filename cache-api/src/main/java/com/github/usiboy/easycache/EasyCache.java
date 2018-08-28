package com.github.usiboy.easycache;

import java.util.Map;
import java.util.Set;


/**
 * 缓存的操作接口
 *
 * @author jueming
 */
public interface EasyCache {

    public static final String DEFAULT_CACHE_NAME = "defaultCache";

    /**
     * 获取缓存组件的名称，特殊情况下可为空，如果全局只有一个cache时，可以为空，默认的名称为defaultCache，如果包含了两个，并记载在spring的缓存管理器中，则需要设置名称
     * @return
     */
    String getName();

    /**
     * 批量获取缓存
     *
     * @param key
     * @return
     * @throws CacheException
     */
    <T> Map<String, T> gets(String... key) throws CacheException;

    /**
     * 根据key得到相应的值,等待时间默认5秒
     *
     * @param key
     * @return
     */
    <T> T get(String key) throws CacheException;

    /**
     * 根据key得到相应的值
     *
     * @param key
     * @param timeout 等待时间,单位为毫秒
     * @return
     */
    <T> T get(final String key, final long timeout) throws CacheException;

    /**
     * 根据key得到相应的值并且延长该缓存时间
     *
     * @param key
     * @param newExpireTime
     * @return
     * @throws CacheException
     */
    <T> T getAndTouch(String key, final int newExpireTime) throws CacheException;

    /**
     * 将缓存的值增加
     *
     * @param key
     * @param value
     * @throws CacheException
     */
    void incr(String key, Long value) throws CacheException;

    /**
     * 将缓存的值减少
     *
     * @param key
     * @param value
     * @throws CacheException
     */
    void decr(String key, Long value) throws CacheException;

    /**
     * 设置缓存的值,默认过期时间为一个月
     *
     * @param key   缓存key
     * @param value 存储的值
     * @return
     */
    boolean set(final String key, final Object value) throws CacheException;

    /**
     * 设置缓存的值
     *
     * @param key   缓存key
     * @param exp   过期时间,单位为秒,0表示永久存储
     * @param value 存储的值
     * @return
     */
    boolean set(final String key, final Object value, final int exp) throws CacheException;

    /**
     * 更新缓存数据的超时时间
     *
     * @param key
     * @param newExpireTime 新的过期时间
     * @return
     */
    boolean touch(String key, int newExpireTime) throws CacheException;

    /**
     * 删除某个key的缓存
     *
     * @param key
     * @return
     */
    boolean delete(String key) throws CacheException;

    /**
     * 是否包含某个属性
     *
     * @param key
     * @return
     */
    boolean containKey(String key) throws CacheException;

    /**
     * 得到所有的key属性值
     *
     * @return
     */
    Set<String> keys() throws CacheException;

    /**
     * 当key不存在的时候，通过add方法返回成功，如果key存在，通过add方法将立即返回失败
     *
     * @param key
     * @param expireTime
     * @param value
     * @return
     * @throws CacheException
     */
    boolean add(String key, Object value, int expireTime) throws CacheException;

}
