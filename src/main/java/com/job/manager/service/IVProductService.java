package com.job.manager.service;

import com.job.manager.daoManager.PagedList;
import com.job.manager.model.VProduct;

/**
 * Created by Li.chen on 2018/12/4 10:29.
 */
public interface IVProductService {
    VProduct getVProductById(String id);

    PagedList<VProduct> getAllVProductByPage(int page, int rows);
}
