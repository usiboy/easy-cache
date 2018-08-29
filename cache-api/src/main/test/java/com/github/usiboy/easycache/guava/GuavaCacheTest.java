package com.github.usiboy.easycache.guava;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * Created by liumingjian on 2017/5/3.
 */
public class GuavaCacheTest {

    GuavaCache cache;

    @Before
    public void setUp() throws Exception {
        cache = new GuavaCache();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSet1() throws Exception {
        Assert.assertTrue(cache.set("test", "hello world"));
        Assert.assertEquals("hello world", cache.get("test"));
    }

    @Test
    public void testSet2() throws Exception {
        final int maximumSize = 10;
        cache.setMaximumSize(maximumSize);
        for(int i = 0;i <= maximumSize;i++){
            cache.set(i + "", i);
        }
        Assert.assertNull(cache.get("0"));
    }

    @Test
    public void testSet3() throws Exception {
        final long defaultExpire = 2L;
        cache.setDefaultExpire(defaultExpire);
        cache.set("test", "hello world");
        Thread.sleep(1000);
        Assert.assertTrue(cache.containKey("test"));
        Thread.sleep(1001);
        Assert.assertFalse(cache.containKey("test"));
    }

    @Test
    public void testGets() throws Exception {
        final int count = 10;
        for(int i = 0;i < count;i++){
            cache.set(i + "", i);
        }
        Map<String, Object> map = cache.gets("0", "1", "2", "9", "100");
        Assert.assertEquals(4, map.size());
        Assert.assertEquals(0, ((Integer)map.get("0")).intValue());
        Assert.assertEquals(1, ((Integer)map.get("1")).intValue());
        Assert.assertEquals(2, ((Integer) map.get("2")).intValue());
        Assert.assertEquals(9, ((Integer) map.get("9")).intValue());
    }

    @Test
    public void testDelete1() throws Exception {
        final int count = 10;
        for(int i = 0;i < count;i++){
            cache.set(i + "", i);
        }
        cache.delete("1");
        Assert.assertFalse(cache.containKey("1"));
    }

    @Test
    public void testIncr1() throws Exception {
        cache.incr("test", 1L);
        cache.incr("test", 1L);
        cache.incr("test", 4L);
        Assert.assertEquals(6L, ((Long)cache.get("test")).longValue());
    }

    @Test(expected = ClassCastException.class)
    public void testIncr2() throws Exception {
        cache.set("test", "0");
        cache.incr("test", 1L);
    }

    @Test
    public void testDesr1() throws Exception {
        cache.decr("test", 6L);
        cache.decr("test", 1L);
        cache.decr("test", 1L);
        Assert.assertEquals(-8L, ((Long)cache.get("test")).longValue());
    }

    @Test(expected = ClassCastException.class)
    public void testDesr2() throws Exception {
        cache.set("test", "0");
        cache.decr("test", 1L);
    }
}