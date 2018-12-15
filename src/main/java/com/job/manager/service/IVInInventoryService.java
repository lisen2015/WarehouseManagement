package com.job.manager.service;

import com.job.manager.daoManager.PagedList;
import com.job.manager.model.VInInventory;

/**
 * Created by Li.chen on 2018/12/4 10:29.
 */
public interface IVInInventoryService {
    VInInventory getVIInventoryById(String id);

    PagedList<VInInventory> getAllVIInventoryByPage(int page, int rows);
}
