package com.job.manager.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by cc on 16/7/4.
 */
public class MUserRoleConfig implements Serializable {
    private static final long serialVersionUID = -2172164818852199146L;
    private String id;
    private Timestamp inputTime;
    private String createUser;
    private String lastUpdateUser;
    private String userId;
    private String roleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getInputTime() {
        return inputTime;
    }

    public void setInputTime(Timestamp inputTime) {
        this.inputTime = inputTime;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String orgId) {
        this.roleId = orgId;
    }
}
