package com.job.manager.service.impl;

import com.job.manager.daoManager.BaseDaoManager;
import com.job.manager.daoManager.PagedList;
import com.job.manager.daoManager.SearchFilter;
import com.job.manager.model.VOutInventory;
import com.job.manager.service.IVOutInventoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Li.chen on 2018/12/4 10:33.
 */
@Service
public class VOutInventoryService implements IVOutInventoryService {
    @Resource
    private BaseDaoManager<VOutInventory> voutInventoryManager;

    @Override
    public PagedList<VOutInventory> getAllVOutInventoryByPage(int page, int rows) {
        SearchFilter esf= SearchFilter.getSearchFilter(page,rows);
        esf.setOrderBy("inputTime desc");
        return voutInventoryManager.pagedObjects(esf);
    }
}
