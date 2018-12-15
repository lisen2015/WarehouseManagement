/*
 * FileName：IMChannelService.java
 *
 * Description：栏目配置的业务接口
 *
 * History：
 * 版本号 作者 		日期       	简介
 *  1.0   chenchen	2014-7-16		Create
 */
package com.job.manager.service;

import com.job.manager.model.MChannelConfig;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;
import java.util.Map;

/**
 * The interface Im channel service.
 */
public interface IMChannelService {

    /**
     * 新增栏目
     *
     * @param mChannelConfig the m channel config
     * @return boolean
     * @version * 2016-07-21 chenchen create
     * @cUser 陈晨
     * @cDate 2014 -7-17
     * @mUser 陈晨
     * @mDate 2014 -7-17
     */
    @CacheEvict(value = "system",key = "'allChannelTree'")
	boolean addChannel(MChannelConfig mChannelConfig);

    /**
     * 修改栏目
     *
     * @param mChannelConfig the m channel config
     * @return boolean
     * @version * 2016-07-21 chenchen create
     * @cUser 陈晨
     * @cDate 2014 -7-17
     * @mUser 陈晨
     * @mDate 2014 -7-17
     */
    boolean updateChannel(MChannelConfig mChannelConfig);

    /**
     * Delete boolean.
     *
     * @param id the id
     * @return the boolean
     * @version * 2016-07-21 chenchen create
     */
    boolean delete(String id);

    /**
     * Get m channel config.
     *
     * @param id the id
     * @return the m channel config
     * @version * 2016-07-21 chenchen create
     */
    MChannelConfig get(String id);

    /**
     * Delete boolean.
     *
     * @param ids the ids
     * @return the boolean
     * @version * 2016-07-21 chenchen create
     */
    boolean delete(String[] ids);

    /**
     * Exist boolean.
     *
     * @param field      the field
     * @param value      the value
     * @param excludedId the excluded id
     * @return the boolean
     * @version * 2016-07-21 chenchen create
     */
    boolean exist(String field, Object value, String excludedId);

    /**
     * Find by type list.
     *
     * @param type  the type
     * @param value the value
     * @return the list
     * @version * 2016-07-21 chenchen create
     */
    List<MChannelConfig> findByType(String type, String value);

    /**
     * 获取栏木树
     *
     * @return all channel tree(key=channelId,value=channel)
     * @version 2014    -12-1		chenchen	create
     */

    Map<String, MChannelConfig> getAllChannelTree();
}
