package com.job.manager.service.impl;

import com.job.manager.daoManager.BaseDaoManager;
import com.job.manager.model.InInventory;
import com.job.manager.service.IInInventoryService;
import com.job.manager.util.StringHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Li.chen on 2018/12/4 10:33.
 */
@Service
public class InInventoryService implements IInInventoryService {
    @Resource
    private BaseDaoManager<InInventory> inInventoryManager;
    @Override
    public boolean add(InInventory inInventory) {
//        inInventory.setCreateUser(ProjectContext.getUser().getId());
        return StringHelper.isNotEmpty((String) inInventoryManager.insert(inInventory));
    }
}
