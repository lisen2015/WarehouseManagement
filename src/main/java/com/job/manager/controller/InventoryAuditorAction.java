package com.job.manager.controller;

import com.job.manager.daoManager.PagedList;
import com.job.manager.model.OutInventory;
import com.job.manager.model.VOutInventory;
import com.job.manager.service.IOutInventoryService;
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
@RequestMapping("/manager/inventoryauditor")
public class InventoryAuditorAction {
    @Resource
    private IOutInventoryService iOutInventoryService;
    @Resource
    private IVOutInventoryService iVOutInventoryService;

    @RequestMapping("")
    public String listPage(ModelMap map) throws Exception {
        return "/manager/config/inventoryAuditorList";
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public
    @ResponseBody
    PagedList<VOutInventory> outInventoryList(@RequestParam int page, @RequestParam int rows) {
        return iVOutInventoryService.getAllVOutInventoryByPage(page, rows);
    }

    @RequestMapping(value = "/auditor", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> auditorInventory(@Valid @ModelAttribute OutInventory outInventory, Errors errors) {
        Map<String, Object> map = new HashMap<>();
        if (errors.hasErrors()) {
            map.put("state", 2);
            for (FieldError fieldError : errors.getFieldErrors()) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return map;
        }
        if (iOutInventoryService.update(outInventory)) {
            map.put("state", 1);
            map.put("message", "审核成功！！！");
        } else {
            map.put("state", 0);
            map.put("message", "审核失败，请重试！！！");
        }
        return map;
    }
}
