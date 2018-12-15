package com.job.manager.controller;

import com.job.manager.daoManager.PagedList;
import com.job.manager.model.OutInventory;
import com.job.manager.model.VOutInventory;
import com.job.manager.service.IOutInventoryService;
import com.job.manager.service.IProductService;
import com.job.manager.service.IVOutInventoryService;
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
@RequestMapping("/manager/outinventory")
public class OutInventoryAction {
    @Resource
    private IOutInventoryService iOutInventoryService;
    @Resource
    private IVOutInventoryService iVOutInventoryService;

    @Resource
    private IProductService iProductService;

    @RequestMapping("")
    public String listPage(ModelMap map) throws Exception {
        return "/manager/config/outInventoryList";
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public
    @ResponseBody
    PagedList<VOutInventory> outInventoryList(@RequestParam int page, @RequestParam int rows) {
        return iVOutInventoryService.getAllVOutInventoryByPage(page, rows);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> addInInventory(@Valid @ModelAttribute OutInventory outInventory, Errors errors) {
        Map<String, Object> map = new HashMap<>();
        if (errors.hasErrors()) {
            map.put("state", 2);
            for (FieldError fieldError : errors.getFieldErrors()) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return map;
        }

        outInventory.setProductId(outInventory.getProductId());
        outInventory.setDepartmentId(outInventory.getDepartmentId());
        outInventory.setOutNumber(outInventory.getOutNumber() == null ? 0 : outInventory.getOutNumber());
        outInventory.setOutPersion(outInventory.getOutPersion() == null ? "匿名" : outInventory.getOutPersion().trim());
        outInventory.setCommit(outInventory.getCommit() == null ? "" : outInventory.getCommit().trim());
        outInventory.setStatus(0); // 0 审核中   1 审核通过    2  拒绝

        // 更新库存
        if (iOutInventoryService.add(outInventory)) {
            map.put("state", 1);
        } else {
            map.put("state", 0);
            map.put("message", "操作失败，请重试！！！");
        }
//        if (iProductService.updateInventory(productId, productInNumber)) {
//            // 新增记录
//
//        } else {
//            map.put("state", 0);
//            map.put("message", "入库失败，请重试！！！");
//        }
        return map;
    }
}
