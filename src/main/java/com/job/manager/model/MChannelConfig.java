package com.job.manager.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by cc on 16/7/4.
 */
public class MChannelConfig implements Serializable {
    private static final long serialVersionUID = -7218370893941139596L;
    private String id;
    private String parentId;
    @NotBlank
    @Length(min = 1,max = 50)
    private String channelName;

    private Integer channelIndex;
    @NotNull
    private Integer channelLevel;
    @Length(max = 500)
    private String channelUrl;
    @Length(max = 500)
    private String channelUrl2;
    private Integer isEnd;
    private Integer isValidate;
    private Timestamp inputTime;
    private Timestamp updateTime;
    private Timestamp deleteTime;
    private String createUser;
    private String lastUpdateUser;
    private List<MChannelConfig> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Integer getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(Integer channelIndex) {
        this.channelIndex = channelIndex;
    }

    public Integer getChannelLevel() {
        return channelLevel;
    }

    public void setChannelLevel(Integer channelLevel) {
        this.channelLevel = channelLevel;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public String getChannelUrl2() {
        return channelUrl2;
    }

    public void setChannelUrl2(String channelUrl2) {
        this.channelUrl2 = channelUrl2;
    }

    public Integer getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(Integer isEnd) {
        this.isEnd = isEnd;
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

    public List<MChannelConfig> getChildren() {
        return children;
    }

    public void setChildren(List<MChannelConfig> children) {
        this.children = children;
    }
}
