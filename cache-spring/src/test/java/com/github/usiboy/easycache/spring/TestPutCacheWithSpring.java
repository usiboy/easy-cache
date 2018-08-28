package com.github.usiboy.easycache.spring;

import com.github.usiboy.easycache.CacheException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-*.xml")
public class TestPutCacheWithSpring {

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
		sample.insertData(key, "hello world");
		Mockito.verify(mgr.getCache(), Mockito.atMost(1)).set("data_" + key, 30);
		Mockito.verify(mgr.getCache(), Mockito.atMost(0)).get("data_" + key);
	}

}
