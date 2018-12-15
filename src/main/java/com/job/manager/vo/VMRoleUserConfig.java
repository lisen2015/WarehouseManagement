package com.job.manager.vo;


import com.job.manager.util.StringHelper;

import java.io.Serializable;
import java.util.Date;

public class VMRoleUserConfig implements Serializable {
	/**
	 * 
	 */
	private static final Long serialVersionUID = -2038895280693871447L;
	private String id;
	private String roleId;
	private String roleName;
	private Date inputTime;
	private Integer adminLevel;
	public VMRoleUserConfig(String id, String roleId, String roleName, Date inputTime) {
		this.id = id;
		this.roleName = roleName;
		this.inputTime = inputTime;
		this.roleId = roleId;
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
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}
