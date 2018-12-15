/*															
 * FileName：IMRoleService.java
 *			
 * Description：组织管理的业务层接口，用于定义业务方法接口						
 * 																	
 * History：
 * 版本号 		作者 			日期       			简介
 *  1.0   	chenchen	2014-7-16		Create	
 *  1.0.1	nijiaqi		2014-10-10		新增方法exist判断组织ID是否重复	
 *  1.0.2	chenchen	2014-10-22		删除所有批量删除的方法，并修改delete方法
 *  1.0.3	chenchen	2014-10-22		增加方法canDelete
 */
package com.job.manager.service;

import com.job.manager.model.MUserRole;
import com.job.manager.vo.VMRole;
import com.job.manager.vo.VMRoleUserConfig;

import java.util.List;

/**
 * The interface Im org service.
 */
public interface IMRoleService {


    /**
     * Find Role by user list.
     *
     * @param id the id
     * @return the list
     * @version * 2016-07-06 chenchen create
     */
    List<VMRoleUserConfig> findRoleByUser(String id);


    /**
     * Add org user boolean.
     *
     * @param roleId the role id
     * @param ids    the ids
     * @return the boolean
     * @version * 2016-07-06 chenchen create
     */
    boolean addRoleUser(String roleId, String[] ids);

    /**
     * Delete org user boolean.
     *
     * @param ids the ids
     * @return the boolean
     * @version * 2016-07-06 chenchen create
     */
    boolean deleteRoleUser(String[] ids);

    /**
     * Find by type list.
     *
     * @param type  the type
     * @param value the value
     * @return the list
     * @version * 2016-07-06 chenchen create
     */
    List<MUserRole> findByType(String type, String value);

    /**
     * Find org tree list by parent id list.
     *
     * @param id the id
     * @param b  the b
     * @return the list
     * @version * 2016-07-06 chenchen create
     */
    List<MUserRole> findRoleTreeListByParentId(String id, boolean b);

    /**
     * Add boolean.
     *
     * @param org the org
     * @return the boolean
     * @version * 2016-07-06 chenchen create
     */
    boolean add(MUserRole org);

    /**
     * Update boolean.
     *
     * @param org the org
     * @return the boolean
     * @version * 2016-07-06 chenchen create
     */
    boolean update(MUserRole org);


    /**
     * Find org not by user list.
     *
     * @param id the id
     * @return the list
     * @version * 2016-07-06 chenchen create
     */
    List<VMRole> findRoleNotByUser(String id);

    /**
     * Add org by user boolean.
     *
     * @param userId the user id
     * @param ids    the ids
     * @return the boolean
     * @version * 2016-07-06 chenchen create
     */
    boolean addRoleByUser(String userId, String[] ids);

    /**
     * Delete boolean.
     *
     * @param id the id
     * @return the boolean
     * @version * 2016-07-06 chenchen create
     */
    public boolean delete(String[] id);

    /**
     * Gets all.
     *
     * @return the all
     * @version * 2016-07-06 chenchen create
     */
    List<MUserRole> getAll();

    /**
     * Update index.
     *
     * @param sId    the s id
     * @param sIndex the s index
     * @param tIndex the t index
     * @param point  the point
     * @version * 2016-07-09 chenchen create
     */
    void updateIndex(String sId, Integer sIndex, Integer tIndex, String point);



}
