package com.job.manager.controller;

import com.job.manager.model.MDepartment;
import com.job.manager.service.IMDepartmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Li.chen on 2018/12/3 16:14.
 */
@Controller
@RequestMapping("/manager/department")
public class MDepartmentAction {
    @Resource
    private IMDepartmentService imDepartmentService;

    @RequestMapping("")
    public String listPage(ModelMap map) throws Exception {
        return "/manager/config/departmentList";
    }


    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public
    @ResponseBody
    List<MDepartment> departmentList() {
        return imDepartmentService.getAll();
    }


    @RequestMapping(value = "/departmentList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<MDepartment> departmentListForSelect() {
        return imDepartmentService.getAll();
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> addDepartment(@Valid @ModelAttribute MDepartment department, Errors errors) {
        Map<String, Object> map = new HashMap<>();
        if (errors.hasErrors()) {
            map.put("state", 2);
            for (FieldError fieldError : errors.getFieldErrors()) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return map;
        }

        department.setDepartmentName(department.getDepartmentName() == null ? "" : department.getDepartmentName().trim());
        department.setDepartmentDesc(department.getDepartmentDesc() == null ? "" : department.getDepartmentDesc().trim());
        if (imDepartmentService.add(department)) {
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
    Map<String, Object> updateDepartment(@Valid @ModelAttribute MDepartment department, Errors errors) {
        Map<String, Object> map = new HashMap<>();
        if (errors.hasErrors()) {
            map.put("state", 2);
            for (FieldError fieldError : errors.getFieldErrors()) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return map;
        }
        department.setDepartmentName(department.getDepartmentName() == null ? "" : department.getDepartmentName().trim());
        department.setDepartmentDesc(department.getDepartmentDesc() == null ? "" : department.getDepartmentDesc().trim());
        if (imDepartmentService.update(department)) {
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
    Map<String, Object> delDepartment(@RequestParam(value = "ids", required = false, defaultValue = "[0]") String[] ids, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        if (ids == null || ids.length <= 0) {
            map.put("state", 2);
            map.put("message", "传入参数错误！！！");
            return map;
        } else {
            if (imDepartmentService.delete(ids)) {
                map.put("state", 1);
            } else {
                map.put("state", 0);
                map.put("message", "操作失败，请重试！！！");
            }
            return map;
        }
    }
}
