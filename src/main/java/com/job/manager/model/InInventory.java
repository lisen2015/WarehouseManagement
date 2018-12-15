package com.job.manager.model;

import com.job.manager.util.StringHelper;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Li.chen on 2018/12/5 17:20.
 */
public class InInventory implements Serializable {
    private static final long serialVersionUID = -8660971211297427815L;
    private String id;
    @NotBlank
    @Length(min=1, max=100)
    private String productId;
    private String supplier;
    private Integer inNumber;
    private Timestamp inputTime;
    private String createUser;
    private String inputTimeStr;

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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getInputTimeStr() {
        return StringHelper.toDateString(this.inputTime,"yyyy-MM-dd HH:mm");
    }
}
