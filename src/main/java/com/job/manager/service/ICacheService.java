/*														
 * ICacheService.java						
 *			
 * Description：	xmemecached缓存包装业务类接口
 * 																	
 * History：
 * 版本号 			作者 			日期       				简介
 *  1.0   		chenchen	2015年09月30日		Create	
 */
package com.job.manager.service;

import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.exception.MemcachedException;

import java.util.concurrent.TimeoutException;


/**
 * The interface Cache service.
 *
 * @author 自定义业务实现接口
 */
public interface ICacheService {

    /**
     * 读取缓存
     *
     * @param key the key
     * @return object
     * @throws MemcachedException   缓存调用错误
     * @throws InterruptedException 线程中断错误
     * @throws TimeoutException     超时错误
     * @version 2015年8月10日    chenchen	create
     */
    public Object get(String key);

    /**
     * 写入缓存
     *
     * @param key   the key
     * @param value the value
     * @param time  过期时间(秒)
     * @return
     * @throws MemcachedException   缓存调用错误
     * @throws InterruptedException 线程中断错误
     * @throws TimeoutException     超时错误
     * @version 2015年8月10日    chenchen	create
     */
    public void set(String key, Object value, int time);

    /**
     * 读取缓存
     *
     * @param key the key
     * @param ns  命名空间
     * @return by ns
     * @throws MemcachedException   缓存调用错误
     * @throws InterruptedException 线程中断错误
     * @throws TimeoutException     超时错误
     * @version 2015年8月10日    chenchen	create
     */
    public Object getByNS(String key, String ns)  ;

    /**
     * 写入缓存
     *
     * @param key   the key
     * @param value the value
     * @param time  过期时间(秒)
     * @param ns    命名空间
     * @return
     * @throws MemcachedException   缓存调用错误
     * @throws InterruptedException 线程中断错误
     * @throws TimeoutException     超时错误
     * @version 2015年8月10日    chenchen	create
     */
    public void setByNS(String key, Object value, int time, String ns) ;

    /**
     * 根据命名空间清理缓存
     *
     * @param key the key
     * @param ns  the ns
     * @throws MemcachedException   the memcached exception
     * @throws InterruptedException the interrupted exception
     * @throws TimeoutException     the timeout exception
     * @version * 2016-08-30 chenchen create
     */
    void clearByNS(String key, String ns);

    /**
	 * 清除命名空间缓存
	 * @param ns 命名空间
	 * @throws MemcachedException 缓存调用错误
	 * @throws InterruptedException 线程中断错误
	 * @throws TimeoutException 超时错误
	 * @version
	 * 	2015年9月30日		chenchen	create
	 */
	public void clearNS(String ns);
	/**
	 * 清除缓存
	 * @param key 缓存key
	 * @throws MemcachedException 缓存调用错误
	 * @throws InterruptedException 线程中断错误
	 * @throws TimeoutException 超时错误
	 * @version
	 * 	2015年9月30日		chenchen	create
	 */
	public void clearCached(String key);
	
	/**
	 * 存入数据（并发环境）
	 * @param key
	 * @param value
	 * @param time
	 * @param cas
	 * @version
	 * 	2015年11月12日		chenchen	create
	 */
	public void setByCAS(String key, Object value, int time, long cas);
	/**
	 * 获取数据（并发环境）
	 * @param key
	 * @version
	 * 	2015年11月12日		chenchen	create
	 */
	public GetsResponse<Object> getByCAS(String key);
	
}
