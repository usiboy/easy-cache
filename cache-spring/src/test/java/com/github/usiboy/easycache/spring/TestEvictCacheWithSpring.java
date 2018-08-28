package com.github.usiboy.easycache.spring;

import com.github.usiboy.easycache.CacheException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-*.xml")
public class TestEvictCacheWithSpring {

	@Resource
	private SampleService sample;
	
	@Resource
	private SpringCacheManager mgr;
	
	@Before
	public void setUp(){
		mgr.setCache(Mockito.spy(mgr.getCache()));
	}

	@After
	public void tearDown(){
		Mockito.reset(mgr.getCache());
	}
	
	@Test
	public void test() throws CacheException {
		final String key = "test";
		String expected = sample.getData(key);
		Mockito.verify(mgr.getCache(), Mockito.atMost(1)).set("data_" + key, 60);
		Mockito.verify(mgr.getCache(), Mockito.atMost(1)).get("data_" + key);
		
		Assert.assertEquals(expected, mgr.getCache().get("data_" + key));
		sample.deleteData(key);
		Mockito.verify(mgr.getCache(), Mockito.atMost(1)).delete("data_" + key);
		
		Assert.assertNull(mgr.getCache().get("data_" + key));
	}
}
