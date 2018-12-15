package com.job.manager.vo;


import com.job.manager.util.StringHelper;

import java.util.Date;

/**
 * MUser entity. @author MyEclipse Persistence Tools
 */

public class VMUser implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 3512951955194764595L;
	private String id;
	private String loginName;
	private String userName;
	private String email;
    private int isValidate;
    private String isValidateStr;
	private Date inputTime;
	private Date deleteTime;
    public VMUser(){}
    public VMUser(String id, String loginName, String userName, String email, int isValidate, Date inputTime, Date deleteTime) {
        this.id = id;
        this.loginName = loginName;
        this.userName = userName;
        this.email = email;
        this.isValidate = isValidate;
        this.inputTime = inputTime;
        this.deleteTime = deleteTime;
    }

    public VMUser(String id, String loginName, String userName) {
        this.id = id;
        this.loginName = loginName;
        this.userName = userName;
    }

    public String getIsValidateStr() {
        String valStr = "已删除";
        switch(this.getIsValidate()){
            case 1:
                valStr = "已启用";
                break;
            case 2:
                valStr = "申请中";
                break;
            case 3:
                valStr = "已拒绝";
                break;
        }
        return valStr;
    }

    public void setIsValidateStr(String isValidateStr) {
        this.isValidateStr = isValidateStr;
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

    public int getIsValidate() {
        return isValidate;
    }

    public void setIsValidate(int isValidate) {
        this.isValidate = isValidate;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }


    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }
    public String getInputTimeStr(){
        return StringHelper.toDateString(this.inputTime,"yyyy-MM-dd HH:mm");
    }
    public String getDeleteTimeStr(){
        return StringHelper.toDateString(this.deleteTime,"yyyy-MM-dd HH:mm");
    }

}