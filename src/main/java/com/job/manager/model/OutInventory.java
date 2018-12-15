package com.job.manager.model;

import com.job.manager.util.StringHelper;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Li.chen on 2018/12/5 17:20.
 */
public class OutInventory implements Serializable {
    private static final long serialVersionUID = -8660971211297427815L;
    private String id;
    @NotBlank
    @Length(min=1, max=100)
    private String productId;
    private String outPersion;
    private Integer outNumber;
    private Integer status;
    private String commit;
    private String description;
    private String departmentId;
    private Timestamp inputTime;
    private String createUser;
    private String lastUpdateUser;
    private Timestamp updateTime;
    private String inputTimeStr;
    private String updateTimeStr;

    public String getInputTimeStr() {
        return StringHelper.toDateString(this.inputTime,"yyyy-MM-dd HH:mm");
    }
    public String getUpdateTimeStr() {
        return StringHelper.toDateString(this.updateTime,"yyyy-MM-dd HH:mm");
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOutPersion() {
        return outPersion;
    }

    public void setOutPersion(String outPersion) {
        this.outPersion = outPersion;
    }

    public Integer getOutNumber() {
        return outNumber;
    }

    public void setOutNumber(Integer outNumber) {
        this.outNumber = outNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
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

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
