package com.github.usiboy.easycache.spring;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

public class SampleService2 {

	@Cacheable(value="cache1#60", key="'data_' + #key")
	public String getCache1(String key){
		return key + ":hello world!";
	}

	@Cacheable(value="cache2#60", key="'data_' + #key")
	public String getCache2(String key){
		return key + ":hello world!";
	}

	@CachePut(value="cache1#30", key="'data_' + #name")
	public void insertCache1(String name, String value){
		
	}

	@CachePut(value="cache2#30", key="'data_' + #name")
	public void insertCache2(String name, String value){

	}
	
	@CacheEvict(value="cache1#60", key="'data_' + #key")
	public void deleteCache1(String key){
		
	}

	@CacheEvict(value="cache2#60", key="'data_' + #key")
	public void deleteCache2(String key){

	}
}
