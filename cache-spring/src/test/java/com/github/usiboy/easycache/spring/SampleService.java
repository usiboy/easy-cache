package com.github.usiboy.easycache.spring;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SampleService {

	@Cacheable(value="defaultCache#60", key="'data_' + #key")
	public String getData(String key){
		return key + ":hello world!";
	}
	
	@CachePut(value="defaultCache#30", key="'data_' + #name")
	public void insertData(String name, String value){
		
	}
	
	@CacheEvict(value="defaultCache#60", key="'data_' + #key")
	public void deleteData(String key){
		
	}
}
