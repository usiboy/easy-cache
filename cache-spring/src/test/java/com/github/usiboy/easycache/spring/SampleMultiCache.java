package com.github.usiboy.easycache.spring;

import com.github.usiboy.easycache.EasyCache;
import com.github.usiboy.easycache.MultiCacheable;
import com.github.usiboy.easycache.simple.SimpleCache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 实现了{@link MultiCacheable}接口的Cache类
 * @author liumingjian
 * @date 2018/8/28
 **/
public class SampleMultiCache extends SimpleCache implements MultiCacheable{

    private Map<String, EasyCache> names = new HashMap<String, EasyCache>();

    /**
     * 这里默认注册两个names进去
     */
    public SampleMultiCache(){
        SimpleCache cache1 = new SimpleCache();
        cache1.setName("cache1");
        SimpleCache cache2 = new SimpleCache();
        cache2.setName("cache2");
        names.put(cache1.getName(), cache1);
        names.put(cache2.getName(), cache2);
    }


    @Override
    public Set<String> getNames() {
        return names.keySet();
    }

    @Override
    public EasyCache getCache(String name) {
        return names.get(name);
    }
}
