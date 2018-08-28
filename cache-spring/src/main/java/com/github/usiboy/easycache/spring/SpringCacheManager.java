package com.github.usiboy.easycache.spring;

import com.github.usiboy.easycache.CacheException;
import com.github.usiboy.easycache.EasyCache;
import com.github.usiboy.easycache.MultiCacheable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.*;

/**
 * 光云缓存管理器
 * @author JackyLIU
 *
 */
public class SpringCacheManager implements CacheManager, InitializingBean {
	
	/**
	 * 读取超时时间
	 */
	Integer readTimeout;
	
	/**
	 * 缓存过期时间
	 */
	Integer expiry;
	
	/**
	 * key和属性值的分割符号
	 */
	char seperateChar = '#';
	
	/**
	 * 如果配置中只用到一种类型的缓存，那么在spring的配置中，只需要构造一个asyCache的实例，这里将会自动注入进来。但是如果包含了多个缓存实例，则需要手动注入到 {@link #caches}属性中
	 */
	@Autowired(required=false)
	EasyCache cache;

	/**
	 * 默认情况下，如果未对它进行设置，那么会从cache中加载到这个集合中。如果在spring配置中注入了设置，那么会在spring的afterPropertiesSet的过程中做一些初始化
	 */
	List<EasyCache> caches;

	/**
	 * 通过caches的名称转换为hash map
	 */
	Map<String, EasyCache> cacheNameRefs;
	
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
		initCaches();
		initCacheNames();
	}

	/**
	 * 初始化caches
	 */
	void initCaches() throws CacheException {
		// 这个表示caches已经在spring的bean中初始化了
		if(null != caches && caches.size() > 0){
			return ;
		}
		// 这里需要抛出异常了，表示没有任何一个cache的实例加载在Spring管理器中
		if(null == cache){
			throw new CacheException("请设置至少一个cache实例");
		}
		caches = new ArrayList<EasyCache>(2);
		caches.add(cache);
	}

	/**
	 * 调用此方法前，必须要先调用{@link #initCaches()}。这一部根据cache中是否实现了{@link com.github.usiboy.easycache.MultiCacheable}的接口，如果实现了，则需要加载其中的缓存名称，如果未实现，则表示cache组件只有自身一个，直接调用cache.getName即可
	 */
	void initCacheNames() throws CacheException {
		cacheNameRefs = new HashMap<String, EasyCache>();
		for(EasyCache easyCache : caches){
			if(easyCache instanceof MultiCacheable){
				loadMultiCacheable((MultiCacheable)easyCache);
				continue;
			}
			loadEasyCache(easyCache);
		}
	}

	void loadMultiCacheable(MultiCacheable cache) throws CacheException {
		final Set<String> names = cache.getNames();
		if(null == names || names.size() == 0){
			// 如果未设置名称，则使用默认的名称
			loadCacheNameRef(EasyCache.DEFAULT_CACHE_NAME, (EasyCache) cache);
			return ;
		}
		for(String name : names){
			loadCacheNameRef(name, cache.getCache(name));
		}
	}

	void loadEasyCache(EasyCache cache) throws CacheException {
		loadCacheNameRef(StringUtils.isEmpty(cache.getName())?EasyCache.DEFAULT_CACHE_NAME:cache.getName(), cache);
	}

	void loadCacheNameRef(String name, EasyCache cache) throws CacheException {
		// 如果cacheNameRefs已经加载过了，则不允许再次添加
		if(cacheNameRefs.containsKey(EasyCache.DEFAULT_CACHE_NAME)){
			throw new CacheException("名称为[" + name + "]的缓存已经存在，请重新设置缓存组件名称");
		}
		cacheNameRefs.put(name, cache);
	}

	/**
	 * <p>name在seperateChar之前是缓存实例的名称，后半部分是缓存的配置，例如'defaultCache#60'，
	 * 表示缓存实例名称为defaultCache，60表示缓存时间expiry为60秒;
	 * <p>'testCache#60,5'，缓存实例名称为testCache，60表示缓存时间，5表示读取超时时间，5秒读取超时
	 * <p>'testCache#,5' ,缓存实例名称为testCache，缓存时间使用默认配置，5表示读取超时时间
	 * <p>'testCache' ,缓存实例名称为testCache，缓存时间使用默认配置，读取超时时间也使用默认的
	 */
	@Override
	public Cache getCache(String name) {
		CacheValueResolver resolver = CacheValueResolver.build(name, seperateChar);
        EasyCache easyCache = cacheNameRefs.get(resolver.getCacheName());
        if(null == easyCache){
            throw new IllegalArgumentException("can't found cacheName[" + resolver.getCacheName() + "]");
        }
        SpringCache sc = new SpringCache(easyCache);
        sc.setExpiry(resolver.getExpiry());
        sc.setReadTimeout(resolver.getReadTimeout());
		return sc;
	}

	@Override
	public Collection<String> getCacheNames() {
		return cacheNameRefs.keySet();
	}

	public List<EasyCache> getCaches() {
		return caches;
	}

	public void setCaches(List<EasyCache> caches) {
		this.caches = caches;
	}
}
