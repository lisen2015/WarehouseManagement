package com.job.manager.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by cc on 16/7/4.
 */
public class MAuthFunctionRole implements Serializable {

    private static final long serialVersionUID = -5139837641145009835L;
    private String id;
    private Integer isAuth;
    private Timestamp inputTime;
    private String createUser;
    private String lastUpdateUser;
    private String functionId;
    private String roleId;
    private MFunctionConfig function;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Integer isAuth) {
        this.isAuth = isAuth;
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

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public MFunctionConfig getFunction() {
        return function;
    }

    public void setFunction(MFunctionConfig function) {
        this.function = function;
    }
}
