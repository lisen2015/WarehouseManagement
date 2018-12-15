/*															
 * FileName：VMRole.java
 * Description：用于前台展示组织树，数据基类
 * History：
 * 版本号  作者 日期       简介绍
 *  1.0   chenchen  2014-08-06  查询、操作	
 */
package com.job.manager.vo;

import com.job.manager.util.StringHelper;

import java.io.Serializable;
import java.util.Date;

public class VMRole implements Serializable {
	/**
	 * 
	 */
	private static final Long serialVersionUID = -1340330596296125950L;
    private String id;
    private String roleName;
    private Date inputTime;

    public VMRole(String id, String roleName, Date inputTime) {
        this.id = id;
        this.roleName = roleName;
        this.inputTime = inputTime ;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public Date getInputTime() {
        return inputTime;
    }
    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }
    public String getInputTimeString() {
        return StringHelper.toDateString(inputTime,"yyyy-MM-dd HH:mm");
    }
	
}
