package com.job.manager.service.impl;

import com.job.manager.daoManager.BaseDaoManager;
import com.job.manager.model.OutInventory;
import com.job.manager.service.IOutInventoryService;
import com.job.manager.service.IProductService;
import com.job.manager.util.StringHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Li.chen on 2018/12/4 10:33.
 */
@Service
public class OutInventoryService implements IOutInventoryService {
    @Resource
    private BaseDaoManager<OutInventory> outInventoryManager;

    @Resource
    private IProductService iProductService;

    @Override
    public boolean add(OutInventory outInventory) {
        return StringHelper.isNotEmpty((String) outInventoryManager.insert(outInventory));
    }

    @Override
    public boolean update(OutInventory outInventory) {
        if (iProductService.updateInventory(outInventory.getProductId(), outInventory.getOutNumber())) {
            OutInventory z_outInventory = outInventoryManager.getObject(outInventory.getId());
            if (z_outInventory != null){
//            outInventory.setStatus(status); // 0 审核中   1 审核通过    2  拒绝
                z_outInventory.setStatus(outInventory.getStatus());
                z_outInventory.setDescription(outInventory.getDescription());
                outInventoryManager.update(z_outInventory);
                return true;
            }
        }
        return false;
    }

    @Override
    public OutInventory getOutInventory(String id) {
        return outInventoryManager.getObject(id);
    }
}
