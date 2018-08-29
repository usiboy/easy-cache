package com.github.usiboy.easycache.ehcache;

import com.github.usiboy.easycache.CacheException;
import com.github.usiboy.easycache.EasyCache;
import net.sf.ehcache.CacheManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class EasyEhcacheTest {

    final static String CACHE_FILE = "/ehcache.xml";

    CacheManager cacheManager;

    EasyEhcache easyEhcache;

    @Before
    public void setUp() throws Exception {
        URL url = this.getClass().getResource(CACHE_FILE);
        cacheManager = CacheManager.create(url);
        cacheManager.clearAll();

        easyEhcache = new EasyEhcache();
        easyEhcache.setCacheManager(cacheManager);
    }

    @After
    public void tearDown() throws Exception {
        cacheManager.clearAll();
    }

    @Test
    public void test1() throws CacheException {
        EasyCache cache1 = easyEhcache.getCache("cache1");
        Assert.assertEquals("cache1", cache1.getName());
        Assert.assertNotNull(cache1);
        cache1.set("key1", "Hello");
        cache1.set("key2", "World");
        Assert.assertEquals("Hello", cache1.get("key1"));
        Assert.assertEquals("World", cache1.get("key2"));
        Assert.assertTrue(cache1.containKey("key1"));
        Assert.assertFalse(cache1.add("key1", "Hello", 0));
        Assert.assertTrue(cache1.add("key3", "NiuB", 0));
        Map<String, String> map = cache1.gets("key1", "key2");
        Assert.assertEquals("Hello", map.get("key1"));
        Assert.assertEquals("World", map.get("key2"));
        Assert.assertTrue(cache1.delete("key1"));
    }

    @Test
    public void testIncr() throws CacheException, InterruptedException, ExecutionException {
        final int threadCount = 20;
        final int loop = 10000;
        final EasyCache cache1 = easyEhcache.getCache("cache1");
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<Callable<Integer>> callables = new ArrayList<Callable<Integer>>(threadCount);
        for(int i = 0;i < threadCount;i++){
            callables.add(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    for(int i = 0;i < loop;i++){
                        cache1.incr("counter", 1L);
                    }
                    return loop;
                }
            });
        }
        List<Future<Integer>> futures = executorService.invokeAll(callables);
        Integer result = 0;
        for(Future<Integer> future : futures){
            result += future.get();
        }
        Assert.assertEquals(result.longValue(), ((Long)cache1.get("counter")).longValue());
    }

    @Test
    public void testDecr() throws CacheException, InterruptedException, ExecutionException {
        final int threadCount = 20;
        final int loop = 10000;
        final EasyCache cache1 = easyEhcache.getCache("cache1");
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<Callable<Integer>> callables = new ArrayList<Callable<Integer>>(threadCount);
        for(int i = 0;i < threadCount;i++){
            callables.add(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    for(int i = 0;i < loop;i++){
                        cache1.decr("counter", 1L);
                    }
                    return loop;
                }
            });
        }
        List<Future<Integer>> futures = executorService.invokeAll(callables);
        Integer result = 0;
        for(Future<Integer> future : futures){
            result -= future.get();
        }
        Assert.assertEquals(result.longValue(), ((Long)cache1.get("counter")).longValue());
    }
}