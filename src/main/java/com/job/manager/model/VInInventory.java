package com.job.manager.model;

import com.job.manager.util.StringHelper;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Li.chen on 2018/12/5 17:20.
 */
public class VInInventory implements Serializable {
    private static final long serialVersionUID = -8660971211297427815L;
    private String id;
    @NotBlank
    @Length(min=1, max=100)
    private String productId;
    private String productName;
    private String supplier;
    private Double price;
    private Double allPrice;
    private Integer inNumber;
    private Timestamp inputTime;
    private String createUserName;
    private String inputTimeStr;

    public String getInputTimeStr() {
        return StringHelper.toDateString(this.inputTime,"yyyy-MM-dd HH:mm");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Integer getInNumber() {
        return inNumber;
    }

    public void setInNumber(Integer inNumber) {
        this.inNumber = inNumber;
    }

    public Timestamp getInputTime() {
        return inputTime;
    }

    public void setInputTime(Timestamp inputTime) {
        this.inputTime = inputTime;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getAllPrice() {
        return this.inNumber * this.price;
    }

    @Override
    public String toString() {
        return "VInInventory{" +
                "id='" + id + '\'' +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", supplier='" + supplier + '\'' +
                ", price=" + price +
                ", allPrice=" + allPrice +
                ", inNumber=" + inNumber +
                ", inputTime=" + inputTime +
                ", createUserName='" + createUserName + '\'' +
                ", inputTimeStr='" + inputTimeStr + '\'' +
                '}';
    }
}
