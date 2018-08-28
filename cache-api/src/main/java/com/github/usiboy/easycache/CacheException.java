/**
 * @date 2014年4月14日
 * @author huangjie
 */
package com.github.usiboy.easycache;

/**
 * 操作缓存的异常
 * 作为缓存的父类异常
 * @author jueming
 */
public class CacheException extends Exception{
	
	private static final long serialVersionUID = -6867165720650881604L;

	public CacheException(String msg) {
		super(msg);
	}
	
	public CacheException(Exception e){
		super(e);
	}
}
