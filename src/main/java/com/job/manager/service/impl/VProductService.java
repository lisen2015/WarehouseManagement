package com.job.manager.service.impl;

import com.job.manager.daoManager.BaseDaoManager;
import com.job.manager.daoManager.PagedList;
import com.job.manager.daoManager.SearchFilter;
import com.job.manager.model.VProduct;
import com.job.manager.service.IVProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Li.chen on 2018/12/4 10:33.
 */
@Service
public class VProductService implements IVProductService {
    @Resource
    private BaseDaoManager<VProduct> vproductManager;

    @Override
    public VProduct getVProductById(String id) {
        return vproductManager.getObject(id);
    }

    @Override
    public PagedList<VProduct> getAllVProductByPage(int page, int rows) {
        SearchFilter esf= SearchFilter.getSearchFilter(page,rows);
        esf.setOrderBy("inputTime desc");
        return vproductManager.pagedObjects(esf);
    }
}
