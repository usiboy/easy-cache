package com.github.usiboy.easycache.spring;

import com.github.usiboy.easycache.EasyCache;
import com.github.usiboy.easycache.simple.SimpleCache;
import org.junit.Assert;
import org.junit.Test;

public class TestGetCache {

	@Test
	public void test() throws Exception {
		SpringCacheManager mgr = new SpringCacheManager();
		EasyCache easyCache = new SimpleCache();
		mgr.setCache(easyCache);
		mgr.afterPropertiesSet();
		SpringCache cache = (SpringCache)mgr.getCache("defaultCache#60,5");
		Assert.assertEquals(60, cache.getExpiry().intValue());
		Assert.assertEquals(5, cache.getReadTimeout().intValue());
		
		cache = (SpringCache)mgr.getCache("defaultCache#, 5");
		Assert.assertNull(cache.getExpiry());
		Assert.assertEquals(5, cache.getReadTimeout().intValue());
		
		cache = (SpringCache)mgr.getCache("defaultCache#  60  ");
		Assert.assertEquals(60, cache.getExpiry().intValue());
		Assert.assertNull(cache.getReadTimeout());
		
		cache = (SpringCache)mgr.getCache("defaultCache");
		Assert.assertNull(cache.getExpiry());
		Assert.assertNull(cache.getReadTimeout());
	}

}
