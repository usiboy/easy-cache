package com.github.usiboy.easycache.spring;

import com.github.usiboy.easycache.EasyCache;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 光云缓存管理器
 * @author JackyLIU
 *
 */
public class SpringCacheManager implements CacheManager, InitializingBean {
	
	/**
	 * 读取超时时间
	 */
	private Integer readTimeout;
	
	/**
	 * 缓存过期时间
	 */
	private Integer expiry;
	
	/**
	 * key和属性值的分割符号
	 */
	private char seperateChar = '#';
	
	/**
	 * 目前只支持单个Cache
	 */
	// TODO 后续会支持多种类型的Cache
	@Autowired(required=false)
	private EasyCache cache;
	
	private List<String> cacheNames;
	
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

	public char getSeperateChar() {
		return seperateChar;
	}

	public void setSeperateChar(char seperateChar) {
		this.seperateChar = seperateChar;
	}

	public EasyCache getCache() {
		return cache;
	}

	public void setCache(EasyCache cache) {
		this.cache = cache;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		cacheNames = new ArrayList<String>();
		cacheNames.add("defaultCache");
	}

	/**
	 * <p>name在seperateChar之前是缓存实例的名称，后半部分是缓存的配置，例如'defaultCache#60'，
	 * 表示缓存实例名称为defaultCache，60表示缓存时间expiry为60秒;
	 * <p>'testKey#60,5'，缓存实例名称为defaultCache，60表示缓存时间，5表示读取超时时间，5秒读取超时
	 * <p>'testKey#,5' ,缓存实例名称为defaultCache，缓存时间使用默认配置，5表示读取超时时间。
	 */
	@Override
	public Cache getCache(String name) {
		SpringCache sc = new SpringCache(cache);
		int seperateIndex = name.lastIndexOf(seperateChar);
		if(seperateIndex == -1) {
			return sc;
		}
		
		String props = name.substring(seperateIndex + 1).trim();
		int seperatePropIndex = props.lastIndexOf(',');
		if(seperatePropIndex == -1){
			sc.setExpiry(Integer.parseInt(props.trim()));
			return sc;
		}
		if(seperatePropIndex == 0){
			sc.setReadTimeout(Integer.parseInt(props.substring(seperatePropIndex + 1).trim()));
			return sc;
		}
		sc.setExpiry(Integer.parseInt(props.substring(0, seperatePropIndex).trim()));
		sc.setReadTimeout(Integer.parseInt(props.substring(seperatePropIndex + 1).trim()));
		
		return sc;
	}

	@Override
	public Collection<String> getCacheNames() {
		return cacheNames;
	}

}
