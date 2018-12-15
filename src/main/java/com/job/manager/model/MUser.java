package com.job.manager.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by cc on 16/7/4.
 */
public class MUser implements Serializable {
    private static final long serialVersionUID = -8660971211297427815L;
    private String id;
    @NotBlank
    @Length(min=1, max=20)
    private String loginName;
    @NotBlank
    @Length(min=6)
    private String loginPassword;
    @NotBlank
    @Length(min=1, max=20)
    private String userName;
    @Email
    @Length(min=0, max=50)
    private String email;
    private Integer isValidate;
    private Timestamp inputTime;
    private Timestamp updateTime;
    private Timestamp deleteTime;
    private String createUser;
    private String lastUpdateUser;
    public MUser(){}
    public MUser(String id, String loginName, String userName, String email, Integer isValidate, Timestamp inputTime,Timestamp deleteTime) {
        this.id = id;
        this.loginName = loginName;
        this.userName = userName;
        this.email = email;
        this.isValidate = isValidate;
        this.inputTime = inputTime;
        this.deleteTime = deleteTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
