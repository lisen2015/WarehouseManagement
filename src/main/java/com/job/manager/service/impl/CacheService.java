/*														
 * FileName：CacheService.java	
 * Description：	xmemecached缓存包装业务实现类												
 * History：
 * 版本号 			作者 			日期       			简介
 *  1.0   		chenchen	2015年9月30日		Create	
 */
package com.job.manager.service.impl;

import com.job.manager.service.ICacheService;
import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientCallable;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeoutException;

/** 
 * @ClassName: CacheService 
 * @Description:  
 * @author chenchen
 * @date 2015年9月30日 下午2:51:01 
 *  
 */
@Service
public class CacheService implements ICacheService {
    private static Logger log = Logger.getLogger(CacheService.class);
	@Resource
	private MemcachedClient xmem;
	
	@Override
	public Object get(String key)  {
        try {
            return xmem.get(key);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        }
        return null;
    }
	@Override
	public void set(String key, Object value, int time)  {
        try {
            xmem.set(key, time, value);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        }
    }

	 
	@Override
	public Object getByNS(final String key, String ns)  {
        try {
            return xmem.withNamespace(ns, new MemcachedClientCallable<Object>() {
                @Override
                public Object call(MemcachedClient client)
                        throws MemcachedException, InterruptedException,
                        TimeoutException {
                    return client.get(key);
                }
            });
        } catch (MemcachedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }


	@Override
	public void setByNS(final String key, final Object value, final int time, String ns){
        try {
            xmem.withNamespace(ns, new MemcachedClientCallable<Object>() {
                @Override
                public Object call(MemcachedClient client)
                        throws MemcachedException, InterruptedException,
                        TimeoutException {
                    client.set(key, time, value);
                    log.debug("数据存入缓存成功!key:"+key+"|nameSpace:"+ns+"|time"+time+"\n Object："+value);
                    return null;
                }
            });
        } catch (MemcachedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
	}
    @Override
    public void clearByNS(final String key, String ns) {
        try {
            xmem.withNamespace(ns, new MemcachedClientCallable<Object>() {
                @Override
                public Object call(MemcachedClient client)
                        throws MemcachedException, InterruptedException,
                        TimeoutException {
                    client.delete(key);
                    log.debug("清理缓存成功!key:"+key+"|nameSpace:"+ns);
                    return null;
                }
            });
        } catch (MemcachedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }



    @Override
	public void clearNS(String ns) {
        try {
            xmem.invalidateNamespace(ns);
            log.debug("缓存清理成功！nameSpace:"+ns);
        } catch (MemcachedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

	}

	@Override
	public void clearCached(String key){
        try {
            xmem.delete(key);
            log.debug("缓存清理成功，Key："+key);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        }

	}
	@Override
	public GetsResponse<Object> getByCAS(String key) {
        try {
            return xmem.gets(key);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        }
        return null;
    }
	@Override
	public void setByCAS(String key,Object value ,int time,long cas) {
        try {
            xmem.cas(key, time, value, cas);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        }
    }
}
