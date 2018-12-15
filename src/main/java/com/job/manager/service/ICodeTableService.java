/*															
 * FileName：ICodeTableService.java						
 *			
 * Description：字典管理的业务类接口，用于定义字典增删改查操作						
 * 																	
 * History：
 * 版本号 作者 		日期       	简介
 *  1.0   chenchen	2014-7-16		Create		
 */
package com.job.manager.service;

import com.job.manager.model.CodeTable;
import com.job.manager.model.NewsClassJson;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

/**
 * The interface Code table service.
 */
public interface ICodeTableService {

    /**
     * 新增
     *
     * @param code the code
     * @return boolean
     * @version * 2016-07-06 chenchen create
     * @cUser 陈晨
     * @cDate 2014 -7-17
     * @mUser 陈晨
     * @mDate 2014 -7-17
     */
    @CacheEvict(value = "codeList",allEntries = true)
    boolean add(CodeTable code);

    /**
     * 修改
     *
     * @param code the code
     * @return boolean
     * @version * 2016-07-06 chenchen create
     * @cUser 陈晨
     * @cDate 2014 -7-17
     * @mUser 陈晨
     * @mDate 2014 -7-17
     */
    @CacheEvict(value = "codeList",allEntries = true)
    boolean update(CodeTable code);

    /**
     * Delete boolean.
     *
     * @param id the id
     * @return the boolean
     * @version * 2016-07-06 chenchen create
     */
    @CacheEvict(value = "codeList",allEntries = true)
    boolean delete(String[] id);


    /**
     * Gets list by parent.
     *
     * @param parentId the parent id
     * @return the list by parent
     * @version * 2016-07-06 chenchen create
     */
    @Cacheable(value = "codeList",key = "#parentId")
    List<CodeTable> getListByParent(String parentId);

    /**
     * Get code table.
     *
     * @param id the id
     * @return the code table
     * @version * 2016-07-06 chenchen create
     */
    CodeTable get(String id);

    /**
     * Exist boolean.
     *
     * @param filed the filed
     * @param id    the id
     * @param ids   the ids
     * @return the boolean
     * @version * 2016-07-06 chenchen create
     */
    boolean exist(String filed, String id, String ids);



    /**
     * Delete.
     *
     * @param codeList the code list
     * @version * 2016-07-06 chenchen create
     */
    @CacheEvict(value = "codeList",allEntries = true)
    void delete(List<CodeTable> codeList);


    /**
     * 用于获取combotree
     * @return
     * @version * 2016-09-05 shijiayi create
     */
    Map<String, NewsClassJson> getSystemMessageConfig();
}
