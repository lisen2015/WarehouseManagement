package com.job.manager.model;

import com.job.manager.util.StringHelper;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Li.chen on 2018/12/03 15:35.
 */
public class MDepartment implements Serializable {
    private static final long serialVersionUID = -8660971211297427815L;
    private String id;
    @NotBlank
    @Length(min=1, max=100)
    private String departmentName;
    private String departmentDesc;
    private Timestamp inputTime;
    private Timestamp updateTime;
    private String createUser;
    private String lastUpdateUser;
    private String inputTimeStr;
    private String updateTimeStr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentDesc) {
        this.departmentName = departmentDesc;
    }

    public String getDepartmentDesc() {
        return departmentDesc;
    }

    public void setDepartmentDesc(String departmentDesc) {
        this.departmentDesc = departmentDesc;
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

    public String getInputTimeStr() {
        return StringHelper.toDateString(this.inputTime,"yyyy-MM-dd HH:mm");
    }

    public String getUpdateTimeStr() {
        return StringHelper.toDateString(this.updateTime,"yyyy-MM-dd HH:mm");
    }

    @Override
    public String toString() {
        return "MDepartment{" +
                "id='" + id + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", departmentDesc='" + departmentDesc + '\'' +
                ", inputTime=" + inputTime +
                ", updateTime=" + updateTime +
                ", createUser='" + createUser + '\'' +
                ", lastUpdateUser='" + lastUpdateUser + '\'' +
                '}';
    }
}
