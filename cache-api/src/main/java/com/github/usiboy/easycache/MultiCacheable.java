package com.github.usiboy.easycache;

import java.util.Set;

/**
 * 当实现的{@link EasyCache}中，缓存组件内部包含了多个不同名称的缓存时，需要实现这个接口，其内部的缓存组件获取需要根据名称获取，例如ehcache的缓存组件中，内部包含了多个内部缓存组件，所以ehcache的应该要接入这个接口
 * @author liumingjian
 * @date 2018/8/28
 **/
public interface MultiCacheable {

    /**
     * 获取缓存组件中所包含多个内部缓存组件的名称
     * @return
     */
    Set<String> getNames();

    /**
     * 获取缓存组件中内部的具体名称的缓存组件
     * @param name
     * @return
     */
    EasyCache getCache(String name);
}
