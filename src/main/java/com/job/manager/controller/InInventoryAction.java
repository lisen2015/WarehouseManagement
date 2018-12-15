package com.job.manager.controller;

import com.job.manager.daoManager.PagedList;
import com.job.manager.model.InInventory;
import com.job.manager.model.VInInventory;
import com.job.manager.service.IInInventoryService;
import com.job.manager.service.IProductService;
import com.job.manager.service.IVInInventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Li.chen on 2018/12/4 10:53.
 */
@Controller
@RequestMapping("/manager/ininventory")
public class InInventoryAction {
    @Resource
    private IInInventoryService iInInventoryService;

    @Resource
    private IVInInventoryService iVInInventoryService;

    @Resource
    private IProductService iProductService;

    @RequestMapping("")
    public String listPage(ModelMap map) throws Exception {
        return "/manager/config/inInventoryList";
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public
    @ResponseBody
    PagedList<VInInventory> inInventoryList(@RequestParam int page, @RequestParam int rows) {
        return iVInInventoryService.getAllVIInventoryByPage(page, rows);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> addInInventory(@Valid @ModelAttribute InInventory inInventory, Errors errors) {
        Map<String, Object> map = new HashMap<>();
        if (errors.hasErrors()) {
            map.put("state", 2);
            for (FieldError fieldError : errors.getFieldErrors()) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return map;
        }

        String productId = inInventory.getProductId() == null ? "" : inInventory.getProductId();
        Integer productInNumber = inInventory.getInNumber() == null ? 0 : inInventory.getInNumber();
        inInventory.setInNumber(productInNumber);
        inInventory.setProductId(productId);
        inInventory.setSupplier(inInventory.getSupplier() == null ? "自产/未知供应商" : inInventory.getSupplier());

        // 更新库存
        if (iProductService.updateAllNumber(productId, productInNumber)) {
            // 新增记录
            if (iInInventoryService.add(inInventory)) {
                map.put("state", 1);
            } else {
                map.put("state", 0);
                map.put("message", "操作失败，请重试！！！");
            }
        } else {
            map.put("state", 0);
            map.put("message", "入库失败，请重试！！！");
        }
        return map;
    }
}
