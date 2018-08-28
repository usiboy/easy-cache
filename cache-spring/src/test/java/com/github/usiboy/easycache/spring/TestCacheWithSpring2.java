package com.github.usiboy.easycache.spring;

import com.github.usiboy.easycache.CacheException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.cache.Cache;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * 基于{@link com.github.usiboy.easycache.MultiCacheable}进行测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring2.xml")
public class TestCacheWithSpring2 {

	@Resource
	private SampleService2 sample;
	
	@Resource
	private SpringCacheManager mgr;

	final String CACHE1_NAME = "cache1";

    final String CACHE2_NAME = "cache2";

    /**
     * 测试cache1
     * @throws CacheException
     */
	@Test
	public void test1() {
		final String key = "test";
		final String cacheKey = "data_" + key;

        Cache cache = mgr.getCache(CACHE1_NAME);
        Assert.assertNotNull(cache);
        Assert.assertEquals(CACHE1_NAME, cache.getName());
        String expected = sample.getCache1(key);
        Assert.assertEquals(expected, cache.get(cacheKey).get());

        final String value = "Hello World";
        sample.insertCache1(key, value);
        String result = sample.getCache1(key);
        Assert.assertEquals(result, cache.get(cacheKey).get());

        sample.deleteCache1(key);
        Assert.assertNull(cache.get(cacheKey));
	}

    /**
     * 测试cache2
     * @throws CacheException
     */
    @Test
    public void test2() {
        final String key = "test";
        final String cacheKey = "data_" + key;

        Cache cache = mgr.getCache(CACHE2_NAME);
        Assert.assertNotNull(cache);
        Assert.assertEquals(CACHE2_NAME, cache.getName());
        String expected = sample.getCache2(key);
        Assert.assertEquals(expected, cache.get(cacheKey).get());

        final String value = "Hello World";
        sample.insertCache2(key, value);
        String result = sample.getCache2(key);
        Assert.assertEquals(result, cache.get(cacheKey).get());

        sample.deleteCache2(key);
        Assert.assertNull(cache.get(cacheKey));
    }

}
