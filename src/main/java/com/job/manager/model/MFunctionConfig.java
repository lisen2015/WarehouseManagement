package com.job.manager.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by cc on 16/7/4.
 */
public class MFunctionConfig implements Serializable {
    private static final long serialVersionUID = 925189760464265648L;
    private String id;
    private String channelId;
    @NotBlank
    @Length(min = 1,max = 50)
    private String functionName;
    private Integer functionIndex;
    @Length(max = 1000)
    private String htmlUrl;
    @NotBlank
    @Length(min = 1,max = 50)
    private String htmlId;
    private Integer isValidate;
    private Timestamp inputTime;
    private Timestamp updateTime;
    private Timestamp deleteTime;
    private String createUser;
    private String lastUpdateUser;
    public void setMFunctionConfig(String functionName,String htmlId,String htmlUrl,Integer functionIndex,Timestamp updateTime){
        this.functionName=functionName;
        this.htmlId=htmlId;
        this.htmlUrl=htmlUrl;
        this.updateTime=updateTime;
        this.functionIndex=functionIndex;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Integer getFunctionIndex() {
        return functionIndex;
    }

    public void setFunctionIndex(Integer functionIndex) {
        this.functionIndex = functionIndex;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getHtmlId() {
        return htmlId;
    }

    public void setHtmlId(String htmlId) {
        this.htmlId = htmlId;
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
