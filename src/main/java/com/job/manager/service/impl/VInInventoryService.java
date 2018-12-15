package com.job.manager.service.impl;

import com.job.manager.daoManager.BaseDaoManager;
import com.job.manager.daoManager.PagedList;
import com.job.manager.daoManager.SearchFilter;
import com.job.manager.model.VInInventory;
import com.job.manager.service.IVInInventoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Li.chen on 2018/12/4 10:33.
 */
@Service
public class VInInventoryService implements IVInInventoryService {
    @Resource
    private BaseDaoManager<VInInventory> vinInventoryManager;

    @Override
    public VInInventory getVIInventoryById(String id) {
        return vinInventoryManager.getObject(id);
    }

    @Override
    public PagedList<VInInventory> getAllVIInventoryByPage(int page, int rows) {
        SearchFilter esf= SearchFilter.getSearchFilter(page,rows);
        esf.setOrderBy("inputTime desc");
        return vinInventoryManager.pagedObjects(esf);
    }
}
