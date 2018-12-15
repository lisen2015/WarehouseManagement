package com.job.manager.service;

import com.job.manager.model.OutInventory;

/**
 * Created by Li.chen on 2018/12/4 10:29.
 */
public interface IOutInventoryService {
    boolean add(OutInventory outInventory);

    boolean update(OutInventory outInventory);

    OutInventory getOutInventory(String id);
}
