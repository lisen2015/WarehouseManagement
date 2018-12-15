package com.job.manager.model;

import com.job.manager.util.StringHelper;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Li.chen on 2018/12/5 17:20.
 */
public class VOutInventory implements Serializable {
    private static final long serialVersionUID = -8660971211297427815L;
    private String id;
    private String productId;
    private String productName;
    private String outPersion;
    private Integer outNumber;
    private Double price;
    private Timestamp inputTime;
    private Integer status;
    private String userName;
    private String commit;
    private String description;
    private String departmentName;
    private Timestamp auditorTime;
    private String auditorUser;

    private Double allPrice;
    private String inputTimeStr;
    private String auditorTimeStr;

    public String getInputTimeStr() {
        return StringHelper.toDateString(this.inputTime,"yyyy-MM-dd HH:mm");
    }

    public String getAuditorTimeStr() {
        return StringHelper.toDateString(this.auditorTime,"yyyy-MM-dd HH:mm");
    }

    public Double getAllPrice() {
        return this.outNumber * this.price;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Timestamp getInputTime() {
        return inputTime;
    }

    public void setInputTime(Timestamp inputTime) {
        this.inputTime = inputTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Timestamp getAuditorTime() {
        return auditorTime;
    }

    public void setAuditorTime(Timestamp auditorTime) {
        this.auditorTime = auditorTime;
    }

    public String getAuditorUser() {
        return auditorUser;
    }

    public void setAuditorUser(String auditorUser) {
        this.auditorUser = auditorUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
