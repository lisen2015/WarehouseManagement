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

import com.job.manager.model.MDepartment;

import java.util.List;

/**
 * The interface Im department service.
 */
public interface IMDepartmentService {

    List<MDepartment> findDepartmentById(String id);

    boolean delete(String[] ids);

    boolean add(MDepartment department);

    boolean update(MDepartment department);

    List<MDepartment> getAll();

}
