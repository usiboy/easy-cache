# 简介
简化缓存中间件的调用，将各种缓存中间件的调用策略集成在同一接口中，并由Spring Cache托管维护。避免了因不同的缓存中间件切换或者不同环境下部署造成的代码入侵的问题。

# 使用

## 结合Spring使用
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:cache="http://www.springframework.org/schema/cache"
	xmlns:context="http://www.springframework.org/schema/context"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/cache http://www.springframework.org/schema/cache/spring-cache-3.2.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.2.xsd">
	
	<cache:annotation-driven cache-manager="cacheManager"/>

    <context:component-scan base-package="com.github.usiboy.easycache.spring"></context:component-scan>

	<bean class="com.github.usiboy.easycache.simple.SimpleCache"></bean>

    <!-- 首先配置一下Spring Cache的管理器，并标注cache，将管理器注入进去。这样就可以支持@Cache注解 -->
	<bean class="com.github.usiboy.easycache.spring.SpringCacheManager" id="cacheManager">
		
	</bean>
</beans>
```

注解的使用
```java
@Service
public class SampleService {

    /**
    * defaultCache是SimpleCache默认的缓存名称，这个可以自定义，需要在SimpleCache中设置name属性 
    */
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
```