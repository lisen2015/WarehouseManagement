package com.job.manager.controller;

import com.job.manager.daoManager.PagedList;
import com.job.manager.model.MUser;
import com.job.manager.service.ICacheService;
import com.job.manager.service.IMRoleService;
import com.job.manager.service.IMUserService;
import com.job.manager.util.ProjectContext;
import com.job.manager.util.StringHelper;
import com.job.manager.vo.VMRole;
import com.job.manager.vo.VMRoleUserConfig;
import com.job.manager.vo.VMUser;
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
import java.util.ResourceBundle;

/**
 * The type M user controller.
 */
@Controller
@RequestMapping("/manager/user")
public class MUserAction {

    private static ResourceBundle rb=ResourceBundle.getBundle("globalConfig");
    @Resource
    private ICacheService cacheService;
    @Resource
    private IMUserService userService;
    @Resource
    private IMRoleService roleService;

    /**
     * List page string.
     *
     * @return the string
     * @version * 2016-07-07 chenchen create
     */
    @RequestMapping("")
    public String listPage() {
        return "/manager/config/userList";
    }

    /**
     * Role list paged list.
     *
     * @param page  the page
     * @param rows  the rows
     * @param value the value
     * @param type  the type
     * @return the paged list
     * @version * 2016-07-07 chenchen create
     */
//======================用户管理开始============================================================
    @RequestMapping(value = "/userList", method = RequestMethod.POST)
    public
    @ResponseBody
    PagedList<VMUser> roleList(@RequestParam int page, @RequestParam int rows, @RequestParam(value = "value", required = false) String value, @RequestParam(value = "type", required = false) String type) {
        if (type != null && value != null && !value.trim().equals("")) {
            return userService.findByType(type, value, page, rows, 999);
        } else {
            return userService.getallUserByPage(page, rows, 999);
        }
    }


    /**
     * Add map.
     *
     * @param mUser  the m user
     * @param errors the errors
     * @return the map
     * @version * 2016-07-07 chenchen create
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> add(@Valid @ModelAttribute MUser mUser, Errors errors) {
        Map<String, Object> map = new HashMap<>();
        if (errors.hasErrors()) {
            map.put("state", 2);
            for (FieldError fieldError : errors.getFieldErrors()) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return map;
        }
        try {
            //用户名是否重复
            if (userService.exist("loginName", mUser.getLoginName(), mUser.getId())) {
                map.put("state", 2);
                return map;
            }

            MUser newUser = userService.add(mUser);
            if (newUser != null) {
                map.put("state", 1);
            } else {
                map.put("state", 0);
                map.put("message", "操作失败，请重试！！！");
            }
        } catch (Exception e) {
            map.put("state", 0);
            map.put("message", "服务器出错<br />error：" + e.getMessage());
        }
        return map;
    }

    /**
     * Update map.
     *
     * @param mUser  the m user
     * @param errors the errors
     * @return the map
     * @version * 2016-07-07 chenchen create
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> update(@Valid @ModelAttribute MUser mUser, Errors errors) {
        Map<String, Object> map = new HashMap<>();
        if (errors.hasErrors()) {
            map.put("state", 2);
            for (FieldError fieldError : errors.getFieldErrors()) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return map;
        }

        try {


            //用户名是否重复
            if (userService.exist("loginName", mUser.getLoginName(), mUser.getId())) {
                map.put("state", 2);
                return map;
            }

            MUser newUser = userService.update(mUser);
            if (newUser != null) {
                map.put("state", 1);
            } else {
                map.put("state", 0);
                map.put("message", "操作失败，请重试！！！");
            }
        } catch (Exception e) {
            map.put("state", 0);
            map.put("message", "服务器出错<br />error：" + e.getMessage());
        }
        return map;
    }

    /**
     * Deluser map.
     *
     * @param id the id
     * @return the map
     * @version * 2016-07-07 chenchen create
     */
    @RequestMapping(value = "/delUser", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> delUser(@RequestParam String[] id) {
        Map<String, Object> map = new HashMap<>();
        if (id.length == 0) {
            map.put("state", 1);
            map.put("message", "要删除的数据不存在！");
            return map;
        } else {
            String[] value = new String[id.length];
            boolean flag = true;
            String user = null;
            for(int i = 0 ; i < id.length ; i ++) {
                //根据id获取 缓存空间数据
                value[i] = (String) cacheService.getByNS(id[i], rb.getString("xmem.dataLocked.namespace"));
                //当id不为空 且用户名不等于当前用户 则数据被锁定
                if(StringHelper.isNotEmpty(value[i])&&(!value.equals(ProjectContext.getUser().getLoginName()))){
                    flag = false;
                    user = value[i];
                }
            }
            if(flag) {
                //添加锁定数据
                for (int i = 0; i < id.length; i++) {
                    cacheService.setByNS(id[i], ProjectContext.getUser().getLoginName(), 60000, rb.getString("xmem.dataLocked.namespace"));
                }
                try {
                    if (userService.delete(id)) {
                        map.put("state", 1);
                    } else {
                        map.put("state", 0);
                        map.put("message", "要删除的数据不存在！");
                    }
                } catch (Exception e) {
                    map.put("state", 0);
                    map.put("message", "服务器出错<br />error：" + e.getMessage());
                }
            } else{
                map.put("state", 2);
                map.put("lockedUser", user);
            }
            //删除锁定数据
            for(int i = 0 ; i < id.length ; i ++) {
                cacheService.clearByNS(id[i],rb.getString("xmem.dataLocked.namespace"));
            }
            return map;
        }

    }

    /**
     * Reset pass map.
     *
     * @param id the id
     * @return the map
     * @version * 2016-07-07 chenchen create
     */
    @RequestMapping(value = "/resetPass", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> resetPass(@RequestParam String[] id) {
        Map<String, Object> map = new HashMap<>();
        if (id.length == 0) {
            map.put("state", 2);
            map.put("message", "要重置密码的用户不存在！");
            return map;
        } else {
            String[] value = new String[id.length];
            boolean flag = true;
            String user = null;
            for(int i = 0 ; i < id.length ; i ++) {
                //根据id获取 缓存空间数据
                value[i] = (String) cacheService.getByNS(id[i], rb.getString("xmem.dataLocked.namespace"));
                //当id不为空 且用户名不等于当前用户 则数据被锁定
                if(StringHelper.isNotEmpty(value[i])&&(!value.equals(ProjectContext.getUser().getLoginName()))){
                    flag = false;
                    user = value[i];
                }
            }
            //判断是否删除
            if(flag) {
                //添加锁定数据
                for (int i = 0; i < id.length; i++) {
                    cacheService.setByNS(id[i], ProjectContext.getUser().getLoginName(), 60000, rb.getString("xmem.dataLocked.namespace"));
                }
                try {
                    if (userService.resetPass(id)) {
                        map.put("state", 1);
                    } else {
                        map.put("state", 0);
                        map.put("message", "要重置密码的用户不存在！");
                    }
                } catch (Exception e) {
                    map.put("state", 0);
                    map.put("message", "服务器出错<br />error：" + e.getMessage());
                }
            }else{
                map.put("state", 2);
                map.put("lockedUser", user);
            }
            //删除锁定数据
            for(int i = 0 ; i < id.length ; i ++) {
                cacheService.clearByNS(id[i],rb.getString("xmem.dataLocked.namespace"));
            }
            return map;
        }
    }

    /**
     * Exist object.
     *
     * @param field the filed
     * @param value the value
     * @param id    the id
     * @return the object
     * @version * 2016-07-07 chenchen create
     */
    @RequestMapping(value = "/exist", method = RequestMethod.POST)
    public
    @ResponseBody
    Object exist(@RequestParam String field, @RequestParam String value, @RequestParam String id) {
        return !userService.exist(field, value, id);
    }



//======================用户组织管理开始========================================================

    /**
     * Role string.
     *
     * @param map the map
     * @param id  the id
     * @return the string
     * @version * 2016-07-07 chenchen create
     */
    @RequestMapping(value = "/role")
    public String role(ModelMap map, @RequestParam(value = "id", required = false, defaultValue = "0") String id) {
        map.put("id", id);
        return "/manager/config/addRoleUser";
    }

    /**
     * Role detail string.
     *
     * @param map     the map
     * @param id      the id
     * @param request the request
     * @return the string
     * @version * 2016-07-07 chenchen create
     */
    @RequestMapping(value = "/role/detail")
    public String roleDetail(ModelMap map, @RequestParam(value = "id", required = false, defaultValue = "0") String id, HttpServletRequest request) {
        map.put("id", id);
        return "/manager/config/detailRoleUser";
    }

    /**
     * Role list list.
     *
     * @param id the id
     * @return the list
     * @version * 2016-07-07 chenchen create
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/role/list", method = RequestMethod.POST)
    public
    @ResponseBody
    List<VMRole> roleList(String id) {
        return roleService.findRoleNotByUser(id);
    }

    /**
     * Detail user role list list.
     *
     * @param id the id
     * @return the list
     * @version * 2016-07-07 chenchen create
     */
    @RequestMapping(value = "/role/detailList", method = RequestMethod.POST)
    public
    @ResponseBody
    List<VMRoleUserConfig> detailUserRoleList(@RequestParam String id) {
        return roleService.findRoleByUser(id);
    }

    /**
     * Del role user map.
     *
     * @param ids     the ids
     * @param request the request
     * @return the map
     * @version * 2016-07-07 chenchen create
     */
    @RequestMapping(value = "/role/del", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> delRoleUser(@RequestParam(value = "ids", required = false, defaultValue = "[0]") String[] ids, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (ids == null || ids.length <= 0) {
            map.put("state", 2);
            map.put("message", "传入参数错误！！！");
            return map;
        } else {
            MUser user = (MUser) request.getSession().getAttribute("muser");
            try {
                if (roleService.deleteRoleUser(ids)) {
                    map.put("state", 1);
                } else {
                    map.put("state", 0);
                    map.put("message", "操作失败，请重试！！！");
                }
            } catch (Exception e) {
                map.put("state", 0);
                map.put("message", "服务器出错<br />error：" + e.getMessage());
            }
            return map;
        }
    }

    /**
     * Add role user map.
     *
     * @param ids     the ids
     * @param userId  the user id
     * @param request the request
     * @return the map
     * @version * 2016-07-07 chenchen create
     */
    @RequestMapping(value = "/role/add", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> addRoleUser(@RequestParam String[] ids, @RequestParam String userId, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (ids == null || ids.length <= 0) {
            map.put("state", 2);
            map.put("message", "传入参数错误！！！");
            return map;
        } else if (StringHelper.isEmpty(userId)) {
            map.put("state", 2);
            map.put("message", "传入参数错误！！！");
            return map;
        } else {
            MUser user = (MUser) request.getSession().getAttribute("muser");
            try {
                if (roleService.addRoleByUser(userId, ids)) {
                    map.put("state", 1);
                } else {
                    map.put("state", 0);
                    map.put("message", "操作失败，请重试！！！");
                }
            } catch (Exception e) {
                map.put("state", 0);
                map.put("message", "服务器出错<br />error：" + e.getMessage());
            }
            return map;
        }
    }

    //======================用户组织管理结束========================================================
}
