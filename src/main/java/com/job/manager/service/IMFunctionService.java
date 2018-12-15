/*															
 * FileName：IMFunctionService.java						
 *			
 * Description：栏目功能配置的业务接口							
 * 																	
 * History：
 * 版本号 作者 		日期       	简介
 *  1.0   chenchen	2014-7-16		Create		
 */
package com.job.manager.service;

import com.job.manager.model.MFunctionConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.util.List;
import java.util.Map;

/**
 * The interface Im function service.
 */
public interface IMFunctionService {

    /**
     * Gets list by channel id.
     *
     * @param channelId the channel id
     * @return the list by channel id
     * @version * 2016-07-05 chenchen create
     */
    List<MFunctionConfig> getListByChannelId(String channelId);

    /**
     * Add function boolean.
     *
     * @param functionConfig the function config
     * @return the boolean
     * @version * 2016-07-05 chenchen create
     */
    @CacheEvict(value = "system",key = "'allFunction'")
    boolean addFunction(MFunctionConfig functionConfig);

    /**
     * Gets all function map.
     *
     * @return the all function map
     * @version * 2016-07-05 chenchen create
     */
    @Cacheable(value = "system",key = "'allFunction'")
    Map<String, MFunctionConfig> getAllFunctionMap();

    /**
     * Update function boolean.
     *
     * @param functionConfig the function config
     * @return the boolean
     * @version * 2016-07-05 chenchen create
     */
    @CacheEvict(value = "system",key = "'allFunction'")
    boolean updateFunction(MFunctionConfig functionConfig);

    /**
     * Delete boolean.
     *
     * @param ids the ids
     * @return the boolean
     * @version * 2016-07-05 chenchen create
     */
    @Caching(evict = {
            @CacheEvict(value = "system",key = "'allFunction'"),
            @CacheEvict(value = "system", key = "'orgFunctionAuth'")
    })
    boolean delete(String[] ids);

    /**
     * Exist boolean.
     *
     * @param field the filed
     * @param value the value
     * @param excludedId    the id
     * @return the boolean
     * @version * 2016-07-05 chenchen create
     */
    boolean exist(String field, Object value, String excludedId);

    void updateIndex(String sId, Integer sIndex, Integer tIndex, String point);
}
