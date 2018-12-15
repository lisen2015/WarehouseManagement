/*
 * FileName：IMUserService.java
 *
 * Description： 用户service接口
 *
 * History：
 * 版本号 		作者 			日期       			简介
 * 1.0        	chenchen    	2016-07-05  		Create
 */
package com.job.manager.service;

import com.job.manager.daoManager.PagedList;
import com.job.manager.model.MUser;
import com.job.manager.vo.VMUser;

import java.util.List;
import java.util.Map;

/**
 * The interface Im user service.
 */
public interface IMUserService {

    /**
     * Add m user.
     *
     * @param user the user
     * @return the m user
     * @version
     * 2016-07-05 chenchen create
     */
    MUser add(MUser user);




    /**
     * Update m user.
     *
     * @param user the user
     * @return the m user
     * @version
     * 2016-07-05 chenchen create
     */
    MUser update(MUser user);




    /**
     * Delete boolean.
     *
     * @param id the id
     * @return the boolean
     * @version
     * 2016-07-05 chenchen create
     */
    boolean delete(String[] id);

    /**
     * Delete real boolean.
     *
     * @param id the id
     * @return the boolean
     * @version
     * 2016-07-05 chenchen create
     */
    boolean deleteReal(String[] id);

    /**
     * Batch recover boolean.
     *
     * @param id the id
     * @return the boolean
     * @version
     * 2016-07-05 chenchen create
     */
    boolean batchRecover(String[] id);


    /**
     * Get m user.
     *
     * @param id the id
     * @return the m user
     * @version
     * 2016-07-05 chenchen create
     */
    MUser get(String id);


    /**
     * Find user not by map.
     *
     * @param table   the table
     * @param id       the id
     * @param value    the value
     * @param pageNo   the page no
     * @param pageSize the page size
     * @return the map
     * @version
     * 2016-07-05 chenchen create
     */
    Map<String, Object> findUserNotBy(String table, String id, String value, int pageNo, int pageSize);

    /**
     * Find user by map.
     *
     * @param table   the table
     * @param id       the id
     * @param value    the value
     * @param pageNo   the page no
     * @param pageSize the page size
     * @return the map
     * @version
     * 2016-07-05 chenchen create
     */
    Map<String, Object> findUserBy(String table, String id, String value, int pageNo, int pageSize);

    /**
     * Login by name m user.
     *
     * @param loginName the login name
     * @param password  the password
     * @return the m user
     * @version
     * 2016-07-05 chenchen create
     */
    MUser loginByName(String loginName, String password);


    /**
     * Login by email m user.
     *
     * @param email    the email
     * @param password the password
     * @return the m user
     * @version
     * 2016-07-05 chenchen create
     */
    MUser loginByEmail(String email, String password);


    /**
     * Login by tel m user.
     *
     * @param tel      the tel
     * @param password the password
     * @return the m user
     * @version * 2016-07-05 chenchen create
     */
    MUser loginByTel(String tel, String password);


    /**
     * Exist boolean.
     *
     * @param field      the field
     * @param value      the value
     * @param excludedId the excluded id
     * @return the boolean
     * @version
     * 2016-07-05 chenchen create
     */
    boolean exist(String field, Object value, String excludedId);


    /**
     * Reset pass boolean.
     *
     * @param ids the ids
     * @return the boolean
     * @version
     * 2016-07-05 chenchen create
     */
    boolean resetPass(String[] ids);


    /**
     * Encrypt string.
     *
     * @param password the password
     * @return the string
     * @version
     * 2016-07-05 chenchen create
     */
    String encrypt(String password);


    /**
     * Reset pass boolean.
     *
     * @param id               the id
     * @param newLoginPassword the new login password
     * @return the boolean
     * @version
     * 2016-07-05 chenchen create
     */
    boolean resetPass(String id, String newLoginPassword);


    /**
     * Find by type paged list.
     *
     * @param type     the type
     * @param value    the value
     * @param page     the page
     * @param rows     the rows
     * @param flag     the flag 0:已删除,1:有效,2申请中,3:已拒绝
     * @return the paged list
     * @version
     * 2016-07-05 chenchen create
     */
    PagedList<VMUser> findByType(String type, String value, int page, int rows, int flag);


    /**
     * Gets user by page.
     *
     * @param page     the page
     * @param rows     the rows
     * @param flag     the flag 0:已删除,1:有效,2申请中,3:已拒绝
     * @return the user by page
     * @version
     * 2016-07-05 chenchen create
     */
    PagedList<VMUser> getallUserByPage(int page, int rows, int flag) ;

    /**
     * Gets .
     *
     * @param value the value
     * @return the
     * @version
     * 2016-07-05 chenchen create
     */
    List<MUser> getall(String value);


    /**
     * Gets .
     *
     * @param userList the user list
     * @return the
     * @version
     * 2016-07-05 chenchen create
     */
    List<MUser> getall(Object[] userList);

    /**
     * Exist boolean.
     *
     * @param field the field
     * @param value the value
     * @return the boolean
     * @version
     * 2016-07-05 chenchen create
     */
    boolean exist(String field, Object value);
}
