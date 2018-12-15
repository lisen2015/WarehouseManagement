package com.job.manager.model;

import com.job.manager.util.StringHelper;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Li.chen on 2018/12/4 10:05.
 */
public class Product implements Serializable {
    private static final long serialVersionUID = -8660971211297427815L;
    private String id;
    @NotBlank
    @Length(min=1, max=100)
    private String productName;
    private String department;
    private Integer outinventory;
    private Integer inventorys;
    private Integer allNumber;
    private Double price;
    private Double allPrice;
    private Timestamp inputTime;
    private Timestamp updateTime;
    private String createUser;
    private String lastUpdateUser;
    private String inputTimeStr;
    private String updateTimeStr;

    public String getInputTimeStr() {
        return StringHelper.toDateString(this.inputTime,"yyyy-MM-dd HH:mm");
    }

    public String getUpdateTimeStr() {
        return StringHelper.toDateString(this.updateTime,"yyyy-MM-dd HH:mm");
    }

    public Integer getOutinventory() {
        return outinventory;
    }

    public void setOutinventory(Integer outinventory) {
        this.outinventory = outinventory;
    }

    public Integer getInventorys() {
        return this.allNumber - this.outinventory;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getAllNumber() {
        return allNumber;
    }

    public void setAllNumber(Integer allNumber) {
        this.allNumber = allNumber;
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

    public void setInputTimeStr(String inputTimeStr) {
        this.inputTimeStr = inputTimeStr;
    }

    public void setUpdateTimeStr(String updateTimeStr) {
        this.updateTimeStr = updateTimeStr;
    }

    public Double getAllPrice() {
        return this.allNumber * this.price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", productName='" + productName + '\'' +
                ", department='" + department + '\'' +
                ", outinventory=" + outinventory +
                ", inventorys=" + inventorys +
                ", allNumber=" + allNumber +
                ", price=" + price +
                ", allPrice=" + allPrice +
                ", inputTime=" + inputTime +
                ", updateTime=" + updateTime +
                ", createUser='" + createUser + '\'' +
                ", lastUpdateUser='" + lastUpdateUser + '\'' +
                ", inputTimeStr='" + inputTimeStr + '\'' +
                ", updateTimeStr='" + updateTimeStr + '\'' +
                '}';
    }
}
