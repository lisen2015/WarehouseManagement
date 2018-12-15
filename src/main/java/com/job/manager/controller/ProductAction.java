package com.job.manager.controller;

import com.job.manager.daoManager.PagedList;
import com.job.manager.model.Product;
import com.job.manager.model.VProduct;
import com.job.manager.service.IProductService;
import com.job.manager.service.IVProductService;
import com.job.manager.util.StringHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Li.chen on 2018/12/4 10:53.
 */
@Controller
@RequestMapping("/manager/product")
public class ProductAction {
    @Resource
    private IProductService iProductService;

    @Resource
    private IVProductService iVProductService;

    @RequestMapping("")
    public String listPage(ModelMap map) throws Exception {
        return "/manager/config/productList";
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public
    @ResponseBody
    PagedList<VProduct> productList(@RequestParam int page, @RequestParam int rows) {
        return iVProductService.getAllVProductByPage(page, rows);
    }

    @RequestMapping(value = "/getProduct", method = RequestMethod.POST)
    public
    @ResponseBody VProduct getProduct(@RequestParam String pid) {
        return iVProductService.getVProductById(pid);
    }

    @RequestMapping(value = "/updateAllNumber", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> updateAllNumber(@RequestParam String pid, @RequestParam int number) {
        Map<String, Object> map = new HashMap<>();
        if (StringHelper.isNotEmpty(iVProductService.getVProductById(pid).toString())) {
            if (iProductService.updateAllNumber(pid, number)){
                map.put("state", 1);
                map.put("message", "入库成功！！！");
            } else {
                map.put("state", 0);
                map.put("message", "入库失败，请重试！！！");
            }
        } else {
            map.put("state", 0);
            map.put("message", "操作失败，请重试！！！");
        }
        return map;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> addProduct(@Valid @ModelAttribute Product product, Errors errors) {
        Map<String, Object> map = new HashMap<>();
        if (errors.hasErrors()) {
            map.put("state", 2);
            for (FieldError fieldError : errors.getFieldErrors()) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return map;
        }

        product.setProductName(product.getProductName() == null ? "" : product.getProductName().trim());
        product.setPrice(product.getPrice() == null ? 0 : product.getPrice());
        product.setDepartment(product.getDepartment() == null ? "" : product.getDepartment().trim());
        product.setAllNumber(0);
        product.setOutinventory(0);

        if (iProductService.add(product)) {
            map.put("state", 1);
        } else {
            map.put("state", 0);
            map.put("message", "操作失败，请重试！！！");
        }
        return map;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> updateProduct(@Valid @ModelAttribute Product product, Errors errors) {
        Map<String, Object> map = new HashMap<>();
        if (errors.hasErrors()) {
            map.put("state", 2);
            for (FieldError fieldError : errors.getFieldErrors()) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return map;
        }

        product.setProductName(product.getProductName() == null ? "" : product.getProductName().trim());
        product.setPrice(product.getPrice() == null ? 0 : product.getPrice());
        product.setDepartment(product.getDepartment() == null ? "" : product.getDepartment().trim());

        if (iProductService.update(product)) {
            map.put("state", 1);
        } else {
            map.put("state", 0);
            map.put("message", "操作失败，请重试！！！");
        }
        return map;
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> delProduct(@RequestParam(value = "ids", required = false, defaultValue = "[0]") String[] ids, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        if (ids == null || ids.length <= 0) {
            map.put("state", 2);
            map.put("message", "传入参数错误！！！");
            return map;
        } else {
            if (iProductService.delete(ids)) {
                map.put("state", 1);
            } else {
                map.put("state", 0);
                map.put("message", "操作失败，请重试！！！");
            }
            return map;
        }
    }
}
