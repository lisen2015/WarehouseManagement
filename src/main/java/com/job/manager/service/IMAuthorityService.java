/*															
 * FileName：IMAuthorityService.java						
 *			
 * Description：权限操作的业务类接口，用于定义权限操作						
 * 																	
 * History：
 * 版本号 作者 		日期       	简介
 *  1.0   chenchen	2014-7-16		Create		
 */
package com.job.manager.service;

import com.job.manager.model.MAuthFunctionRole;
import com.job.manager.model.MAuthRole;

import java.util.List;
import java.util.Map;

/**
 * The interface Im authority service.
 */
public interface IMAuthorityService {


    /**
     * Find user channel authorities map.
     *
     * @param userId the user id
     * @return the map
     * @version * 2016-07-06 chenchen create
     */
    Map<String,Boolean> findAllUserChannelAuthorities(String userId);


    /**
     * Find role channel authorities by list.
     *
     * @param channelParentId the channel parent id
     * @param orgId           the role id
     * @return the list
     * @version * 2016-07-06 chenchen create
     */
    List<Map<String,Object>> findRoleChannelAuthoritiesBy(String channelParentId, String orgId);


    /**
     * Find user all function authorities map.
     *
     * @param userId the user id
     * @return the map
     * @version * 2016-07-21 chenchen create
     */
    Map<String, Boolean> findUserAllFunctionAuthorities(String userId);

    /**
     * Add role authority boolean.
     *
     * @param list the list
     * @return the boolean
     * @version * 2016-07-06 chenchen create
     */
    boolean addRoleAuthority(List<MAuthRole> list);

    /**
     * Find role function authorities by list.
     *
     * @param channelId the channel id
     * @param orgId     the role id
     * @return the list
     * @version * 2016-07-06 chenchen create
     */
    List<Map<String,Object>> findRoleFunctionAuthoritiesBy(String channelId, String orgId);

    /**
     * Add role function authority boolean.
     *
     * @param list the list
     * @return the boolean
     * @version * 2016-07-06 chenchen create
     */
    boolean addRoleFunctionAuthority(List<MAuthFunctionRole> list);


}
