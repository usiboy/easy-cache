package com.github.usiboy.easycache.spring;

import com.github.usiboy.easycache.CacheException;
import com.github.usiboy.easycache.EasyCache;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * 基于{@link com.github.usiboy.easycache.simple.SimpleCache}进行测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring1.xml")
public class TestCacheWithSpring1 {

	@Resource
	private SampleService sample;
	
	@Resource
	private SpringCacheManager mgr;
	
	@Test
	public void test() throws CacheException {
		final String key = "test";
		final String cacheKey = "data_" + key;
        EasyCache cache = mgr.getCache();
        Assert.assertNotNull(cache);
        Assert.assertNull(cache.getName());
        String expected = sample.getData(key);
        Assert.assertEquals(expected, cache.get(cacheKey));

        final String value = "Hello World";
        sample.insertData(key, value);
        String result = sample.getData(key);
        Assert.assertEquals(result, cache.get(cacheKey));

        sample.deleteData(key);
        Assert.assertNull(cache.get(cacheKey));
	}

}
