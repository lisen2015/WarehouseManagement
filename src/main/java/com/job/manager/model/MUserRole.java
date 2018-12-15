package com.job.manager.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by cc on 16/7/4.
 */
public class MUserRole implements Serializable {
    private static final long serialVersionUID = -4134398296294976402L;
    private String id;
    @NotBlank
    @Length(min = 1,max = 50)
    private String roleName;
    private String roleDesc;
    private Integer roleIndex;
    private Integer isValidate;
    private Timestamp inputTime;
    private Timestamp updateTime;
    private Timestamp deleteTime;
    private String createUser;
    private String lastUpdateUser;

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

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String orgDesc) {
        this.roleDesc = orgDesc;
    }

    public Integer getRoleIndex() {
        return roleIndex;
    }

    public void setRoleIndex(Integer roleIndex) {
        this.roleIndex = roleIndex;
    }

    public Integer getIsValidate() {
        return isValidate;
    }

    public void setIsValidate(Integer isValidate) {
        this.isValidate = isValidate;
    }

    public Timestamp getInputTime() {
        return inputTime;
    }

    public void setInputTime(Timestamp inputTime) {
        this.inputTime = inputTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Timestamp getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Timestamp deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }
}
