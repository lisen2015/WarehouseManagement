package com.job.manager.service;

import com.job.manager.model.Product;

/**
 * Created by Li.chen on 2018/12/4 10:29.
 */
public interface IProductService {

    boolean delete(String[] ids);

    boolean add(Product product);

    boolean update(Product product);

    boolean updateInventory(String id, Integer number);

    boolean updateAllNumber(String id, Integer number);
}
