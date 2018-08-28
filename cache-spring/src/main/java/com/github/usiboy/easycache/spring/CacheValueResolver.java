package com.github.usiboy.easycache.spring;

import com.github.usiboy.easycache.EasyCache;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * <pre>
 * 使用Spring Cache注解时，传递进来的value的解析器，格式为: cacheName#expiry,readtimeout，使用'#'符号隔开，例如
 * <p>'testCache#60,5'，缓存实例名称为testCache，60表示缓存时间，5表示读取超时时间，5秒读取超时
 * <p>'testCache#,5' ,缓存实例名称为testCache，缓存时间使用默认配置，5表示读取超时时间
 * <p>'testCache' ,缓存实例名称为testCache，缓存时间使用默认配置，读取超时时间也使用默认的
 * </pre>
 * @author liumingjian
 * @date 2018/8/28
 **/
public class CacheValueResolver implements Serializable{

    private static final long serialVersionUID = 8859752193800701648L;

    private String cacheName;

    private Integer expiry;

    private Integer readTimeout;

    private CacheValueResolver(){}

    public static CacheValueResolver build(String cacheValue, char seperateChar){
        CacheValueResolver resolver = new CacheValueResolver();
        try {
            if(null == cacheValue){
                resolver.cacheName = cacheValue;
                return resolver;
            }
            int seperateIndex = cacheValue.lastIndexOf(seperateChar);
            // 如果没有设置#分隔符，则表示整个cacheValue都是缓存名称
            if (seperateIndex == -1) {
                resolver.cacheName = cacheValue;
                return resolver;
            }
            resolver.cacheName = cacheValue.substring(0, seperateIndex);

            String props = cacheValue.substring(seperateIndex + 1).trim();
            int seperatePropIndex = props.lastIndexOf(',');
            if (seperatePropIndex == -1) {
                resolver.expiry = Integer.parseInt(props.trim());
                return resolver;
            }
            if (seperatePropIndex == 0) {
                resolver.readTimeout = Integer.parseInt(props.substring(seperatePropIndex + 1).trim());
                return resolver;
            }

            resolver.expiry = Integer.parseInt(props.substring(0, seperatePropIndex).trim());
            resolver.readTimeout = Integer.parseInt(props.substring(seperatePropIndex + 1).trim());
            return resolver;
        }finally{
            if(StringUtils.isEmpty(resolver.cacheName)){
                resolver.cacheName = EasyCache.DEFAULT_CACHE_NAME;
            }
        }
    }

    public String getCacheName() {
        return cacheName;
    }

    public Integer getExpiry() {
        return expiry;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }
}
