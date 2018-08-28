package com.github.usiboy.easycache.spring;

import com.github.usiboy.easycache.EasyCache;
import org.junit.Assert;
import org.junit.Test;

public class CacheValueResolverTest {

    CacheValueResolver resolver;

    char seperateChar = '#';

    @Test
    public void testEmptyValue(){
        final String cacheValue = "";
        initResolver(cacheValue);
        Assert.assertEquals(EasyCache.DEFAULT_CACHE_NAME, resolver.getCacheName());
        Assert.assertNull(resolver.getExpiry());
        Assert.assertNull(resolver.getReadTimeout());
    }

    @Test
    public void testNullValue(){
        final String cacheValue = null;
        initResolver(cacheValue);
        Assert.assertEquals(EasyCache.DEFAULT_CACHE_NAME, resolver.getCacheName());
        Assert.assertNull(resolver.getExpiry());
        Assert.assertNull(resolver.getReadTimeout());
    }

    @Test
    public void testOnlyName(){
        final String cacheValue = "testCache123";
        initResolver(cacheValue);
        Assert.assertEquals(cacheValue, resolver.getCacheName());
        Assert.assertNull(resolver.getExpiry());
        Assert.assertNull(resolver.getReadTimeout());
    }

    @Test
    public void testSpecialStringName(){
        final String cacheValue = "$!@$%^";
        initResolver(cacheValue);
        Assert.assertEquals(cacheValue, resolver.getCacheName());
        Assert.assertNull(resolver.getExpiry());
        Assert.assertNull(resolver.getReadTimeout());
    }

    @Test
    public void testContainExpiry(){
        final String cacheValue = "testCache#10000";
        initResolver(cacheValue);
        Assert.assertEquals("testCache", resolver.getCacheName());
        Assert.assertEquals(10000, resolver.getExpiry().intValue());
        Assert.assertNull(resolver.getReadTimeout());
    }

    @Test
    public void testContainReadTimeout(){
        final String cacheValue = "testCache#,10000";
        initResolver(cacheValue);
        Assert.assertEquals("testCache", resolver.getCacheName());
        Assert.assertNull(resolver.getExpiry());
        Assert.assertEquals(10000, resolver.getReadTimeout().intValue());
    }

    @Test
    public void testContainExpiryAndReadTimeout(){
        final String cacheValue = "testCache#10000,10000";
        initResolver(cacheValue);
        Assert.assertEquals("testCache", resolver.getCacheName());
        Assert.assertEquals(10000, resolver.getExpiry().intValue());
        Assert.assertEquals(10000, resolver.getReadTimeout().intValue());
    }

    void initResolver(String cacheValue){
        resolver = CacheValueResolver.build(cacheValue, seperateChar);
    }
}