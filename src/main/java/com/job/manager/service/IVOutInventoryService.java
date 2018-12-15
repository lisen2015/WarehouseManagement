package com.job.manager.service;

import com.job.manager.daoManager.PagedList;
import com.job.manager.model.VOutInventory;

/**
 * Created by Li.chen on 2018/12/4 10:29.
 */
public interface IVOutInventoryService {
    PagedList<VOutInventory> getAllVOutInventoryByPage(int page, int rows);
}
