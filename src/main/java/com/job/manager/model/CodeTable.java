package com.job.manager.model;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by cc on 16/7/4.
 */
public class CodeTable implements Serializable {

    private static final long serialVersionUID = 7449042533713321664L;
    private String id;
    private String parentId;
    private Integer codeLevel;
    @Length(max = 500)
    private String codeValue1;
    private String codeValue2;
    @Length(max = 500)
    private String codeDesc;
    private Integer codeIndex;
    private Integer isEnd;
    private Integer isValidate;
    private Timestamp inputTime;
    private Timestamp updateTime;
    private Timestamp deleteTime;
    private String createUser;
    private String lastUpdateUser;
    private List<CodeTable> children;

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

    public Integer getCodeLevel() {
        return codeLevel;
    }

    public void setCodeLevel(Integer codeLevel) {
        this.codeLevel = codeLevel;
    }

    public String getCodeValue1() {
        return codeValue1;
    }

    public void setCodeValue1(String codeValue1) {
        this.codeValue1 = codeValue1;
    }

    public String getCodeValue2() {
        return codeValue2;
    }

    public void setCodeValue2(String codeValue2) {
        this.codeValue2 = codeValue2;
    }

    public String getCodeDesc() {
        return codeDesc;
    }

    public void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc;
    }

    public Integer getCodeIndex() {
        return codeIndex;
    }

    public void setCodeIndex(Integer codeIndex) {
        this.codeIndex = codeIndex;
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
    public String getState() {
        return isEnd==0?"closed":"open";
    }

    public List<CodeTable> getChildren() {
        return children;
    }

    public void setChildren(List<CodeTable> children) {
        this.children = children;
    }
}
