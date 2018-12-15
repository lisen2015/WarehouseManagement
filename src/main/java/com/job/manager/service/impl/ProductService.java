package com.job.manager.service.impl;

import com.job.manager.daoManager.BaseDaoManager;
import com.job.manager.daoManager.HQLBuilder;
import com.job.manager.model.Product;
import com.job.manager.service.IProductService;
import com.job.manager.util.StringHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Li.chen on 2018/12/4 10:33.
 */
@Service
public class ProductService implements IProductService {

    @Resource
    private BaseDaoManager<Product> productManager;

    @Override
    public boolean delete(String[] ids) {
        HQLBuilder hqb= HQLBuilder.DELETE(Product.class);
        hqb.addInCondition("id",ids);
        productManager.batchDelete(hqb);
        return true;
    }

    @Override
    public boolean add(Product product) {
        return StringHelper.isNotEmpty((String) productManager.insert(product));
    }

    @Override
    public boolean update(Product product) {
        Product z_product = productManager.getObject(product.getId());
        if (z_product != null) {
            z_product.setProductName(product.getProductName());
            z_product.setPrice(product.getPrice());
            z_product.setDepartment(product.getDepartment());
            productManager.update(z_product);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateInventory(String id, Integer number) {
        Product z_product = productManager.getObject(id);
        if (z_product != null && number > 0) {
            Integer outinventory = z_product.getOutinventory() + number;
            Integer newInvetory = z_product.getAllNumber() - outinventory;
            if (newInvetory > 0) {
                z_product.setOutinventory(outinventory);
                productManager.update(z_product);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean updateAllNumber(String id, Integer number) {
        Product z_product = productManager.getObject(id);
        if (z_product != null && number > 0) {
            Integer newAllNumber = z_product.getAllNumber() + number;
            z_product.setAllNumber(newAllNumber);
            productManager.update(z_product);
            return true;
        } else {
            return false;
        }
    }
}
