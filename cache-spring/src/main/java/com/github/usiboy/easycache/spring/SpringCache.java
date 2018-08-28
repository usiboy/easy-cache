package com.github.usiboy.easycache.spring;

import org.apache.log4j.Logger;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import com.github.usiboy.easycache.EasyCache;

/**
 * 包装了{@link EasyCache}接口,get如果需要设置读取超时，需要在key中使用'#'分割变量，格式为key#timeout,单位秒
 * 。set如果需要设置超时时间，和get方式一样，格式key#expiry，单位毫秒
 * @author JackyLIU
 *
 */
public class SpringCache implements Cache {

	private final EasyCache cache;
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	// TODO 目前先使用默认名称
	private final String name = "defaultCache";
	
	/**
	 * 读取超时时间
	 */
	private Integer readTimeout;
	
	/**
	 * 缓存过期时间
	 */
	private Integer expiry;
	
	public SpringCache(EasyCache cache){
		this.cache = cache;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getNativeCache() {
		return cache;
	}

	public Integer getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(Integer readTimeout) {
		this.readTimeout = readTimeout;
	}

	public Integer getExpiry() {
		return expiry;
	}

	public void setExpiry(Integer expiry) {
		this.expiry = expiry;
	}

	@Override
	public ValueWrapper get(Object key) {
		Object value = __get(key);
		if(null == value){
			if(logger.isDebugEnabled()){
				logger.debug(new StringBuilder("Cache miss. Get by key ").append(key).append("from cache ").append(getName()));
			}
			return null;
		}
		
		return new SimpleValueWrapper(value);
	}

	@Override
	public void put(final Object key, final Object value) {
		if(null == key){
			logger.warn(new StringBuilder("Put cache key miss"));
			return ;
		}
		
		try {
			if(null != expiry)
				cache.set(key.toString(), value, expiry);
			else
				cache.set(key.toString(), value);
		} catch (Exception e) {
			logger.error(new StringBuilder("put cache error,").append(e.getMessage()), e);
		}
	}

	@Override
	public void evict(final Object key) {
		if(key == null){
			logger.warn(new StringBuilder("the key is empty, can't evict cache"));
			return ;
		}
		
		try {
			cache.delete(key.toString());
			if(logger.isDebugEnabled())
				logger.debug(new StringBuilder("Evict ").append(key).append(" from ").append(getName()).append(" success"));
		} catch (Exception e) {
			logger.error(new StringBuilder("Evict ").append(key).append(" error:").append(e.getMessage()), e);
		}
	}

	@Override
	public void clear() {
		throw new IllegalStateException("Cannot clear cache.");
	}

	private Object __get(final Object key){
		if(null == key){
			logger.warn(new StringBuilder("the key is empty, can't get cache"));
			return null;
		}
		
		try{
			Object result = null;
			if(readTimeout == null) {
				result = cache.get(key.toString());
			} else {
				result = cache.get(key.toString(), (long)readTimeout);
			}
			
			if(logger.isDebugEnabled()) {
				logger.debug(new StringBuilder("get ").append(key).append(" from ").append(getName()).append(" success"));
			}
			return result;
		}catch(Exception e){
			logger.error(new StringBuilder("Get ").append(key).append(" from ").append(getName()).append(" success"));
			return null;
		}
	}
}
