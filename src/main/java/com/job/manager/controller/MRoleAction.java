/*															
 * FileName：MRoleAction.java
 *			
 * Description：组织管理的controller层，用于控制页面跳转，和调用业务接口
 * 																	
 * History：
 * 版本号 		作者 			日期       			简介
 *  1.0   	chenchen	2014-7-16		Create
 *  1.0.1	nijiaqi		2014-10-10		新增方法exist判断组织ID是否重复
 */
package com.job.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.job.manager.model.MAuthFunctionRole;
import com.job.manager.model.MAuthRole;
import com.job.manager.model.MUserRole;
import com.job.manager.service.ICacheService;
import com.job.manager.service.IMAuthorityService;
import com.job.manager.service.IMRoleService;
import com.job.manager.service.IMUserService;
import com.job.manager.util.ProjectContext;
import com.job.manager.util.StringHelper;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * The type M org controller.
 */
@Controller
@RequestMapping("/manager/role")
public class MRoleAction {


    private static ResourceBundle rb=ResourceBundle.getBundle("globalConfig");
    @Resource
    private ICacheService cacheService;
    private static Logger log = Logger.getLogger(MRoleAction.class);
    @Resource
    private IMRoleService roleService;
    @Resource
    private IMUserService userService;

    @Resource
    private IMAuthorityService authorityService;


    /**
     * List page string.
     *
     * @param map the map
     * @return the string
     * @throws Exception the exception
     * @version * 2016-07-08 chenchen create
     */
    @RequestMapping("")
    public String listPage(ModelMap map) throws Exception {
        return "/manager/config/roleList";
    }

    /**
     * Role list list.
     *
     * @param value   the value
     * @param type    the type
     * @return the list
     * @version * 2016-07-08 chenchen create
     */
    @RequestMapping(value = "/roleList", method = RequestMethod.POST)
    public
    @ResponseBody
    List<MUserRole> roleList(@RequestParam(value = "value", required = false) String value, @RequestParam(value = "type", required = false) String type) {
        if (type != null && value != null && !value.trim().equals("")) {
            return roleService.findByType(type, value);
        } else {
            return roleService.getAll();
        }
    }

    /**
     * Move index map.
     *
     * @param sId    the s id
     * @param sIndex the s index
     * @param tIndex the t index
     * @param point  the point
     * @return the map
     * @version * 2016-07-08 chenchen create
     */
    @RequestMapping(value = "/move", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> moveIndex(@RequestParam String sId, @RequestParam Integer sIndex, @RequestParam Integer tIndex, @RequestParam String point) {
        Map<String, Object> map = new HashMap<>();//用于返回前台数据
        try {
            roleService.updateIndex(sId, sIndex, tIndex, point);
            map.put("state", 1);
        } catch (Exception e) {
            map.put("state", 0);
            map.put("message", "服务器出错<br />error：" + e.getMessage());
            e.printStackTrace();
        }
        return map;
    }



    /**
     * Add map.
     *
     * @param role     the role
     * @param errors  the errors
     * @return the map
     * @version * 2016-07-08 chenchen create
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> add(@Valid @ModelAttribute MUserRole role, Errors errors) {
        Map<String, Object> map = new HashMap<>();
        if (errors.hasErrors()) {
            map.put("state", 2);
            for (FieldError fieldError : errors.getFieldErrors()) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return map;
        }

        role.setRoleDesc(role.getRoleDesc() == null ? "" : role.getRoleDesc().trim());
        role.setRoleName(role.getRoleName() == null ? "" : role.getRoleName().trim());
        if (roleService.add(role)) {
            map.put("state", 1);
        } else {
            map.put("state", 0);
            map.put("message", "操作失败，请重试！！！");
        }
        return map;
    }

    /**
     * Update map.
     *
     * @param role     the role
     * @param errors  the errors
     * @return the map
     * @version * 2016-07-08 chenchen create
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> update(@Valid @ModelAttribute MUserRole role, Errors errors) {
        Map<String, Object> map = new HashMap<>();
        if (errors.hasErrors()) {
            map.put("state", 2);
            for (FieldError fieldError : errors.getFieldErrors()) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return map;
        }
        role.setRoleDesc(role.getRoleDesc() == null ? "" : role.getRoleDesc().trim());
        role.setRoleName(role.getRoleName() == null ? "" : role.getRoleName().trim());
        if (roleService.update(role)) {
            map.put("state", 1);
        } else {
            map.put("state", 0);
            map.put("message", "操作失败，请重试！！！");
        }
        return map;
    }

    /**
     * Del role map.
     *
     * @param id      the id
     * @return the map
     * @version * 2016-07-08 chenchen create
     */
    @RequestMapping(value = "/delRole", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> delRole(@RequestParam String[] id) {
        Map<String, Object> map = new HashMap<>();

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
                if (roleService.delete(id)) {
                    map.put("state", 1);
                } else {
                    map.put("state", 0);
                    map.put("message", "要删除的数据不存在！");
                }
            } catch (Exception e) {
                map.put("state", 0);
                map.put("message", "服务器出错<br />error：" + e.getMessage());
                e.printStackTrace();
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


    /**
     * User list string.
     *
     * @param map the map
     * @param id  the id
     * @return the string
     * @version * 2016-07-08 chenchen create
     */
    @RequestMapping(value = "/user/list")
    public String userList(ModelMap map, @RequestParam(value = "id", required = false, defaultValue = "0") String id) {
        map.put("user", "role");
        map.put("id", id);
        return "/manager/config/addUser";
    }

    /**
     * Detaillist string.
     *
     * @param map the map
     * @param id  the id
     * @return the string
     * @version * 2016-07-08 chenchen create
     */
    @RequestMapping(value = "/user/detailUser")
    public String detailList(ModelMap map, @RequestParam(value = "id", required = false, defaultValue = "0") String id) {
        map.put("user", "role");
        map.put("id", id);
        return "/manager/config/detailUser";
    }

    /**
     * User list map.
     *
     * @param id    the id
     * @param value the value
     * @param page  the page
     * @param rows  the rows
     * @return the map
     * @version * 2016-07-08 chenchen create
     */
    @RequestMapping(value = "/user/userList", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> userList(@RequestParam String id, @RequestParam(value = "value", required = false) String value, @RequestParam int page, @RequestParam int rows) {
        return userService.findUserNotBy("role", id, value, page, rows);
    }

    /**
     * Detail list map.
     *
     * @param id    the id
     * @param value the value
     * @param page  the page
     * @param rows  the rows
     * @return the map
     * @version * 2016-07-08 chenchen create
     */
    @RequestMapping(value = "/user/detailUserList", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> detailList(@RequestParam String  id, @RequestParam(value = "value", required = false) String value, @RequestParam int page, @RequestParam int rows) {
        return userService.findUserBy("role", id, value, page, rows);
    }

    /**
     * Del role user map.
     *
     * @param ids     the ids
     * @param request the request
     * @return the map
     * @version * 2016-07-08 chenchen create
     */
    @RequestMapping(value = "/user/del", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> delORGUser(@RequestParam(value = "ids", required = false, defaultValue = "[0]") String[] ids, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        if (ids == null || ids.length <= 0) {
            map.put("state", 2);
            map.put("message", "传入参数错误！！！");
            return map;
        } else {
            if (roleService.deleteRoleUser(ids)) {
                map.put("state", 1);
            } else {
                map.put("state", 0);
                map.put("message", "操作失败，请重试！！！");
            }
            return map;
        }
    }


    /**
     * Add role user map.
     *
     * @param ids     the ids
     * @param roleId  the role id
     * @return the map
     * @version * 2016-07-08 chenchen create
     */
    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> addORGUser(@RequestParam String[] ids, @RequestParam String roleId) {
        Map<String, Object> map = new HashMap<>();
        if (ids == null || ids.length <= 0) {
            map.put("state", 2);
            map.put("message", "传入参数错误！！！");
            return map;
        } else if (StringHelper.isEmpty(roleId)) {
            map.put("state", 2);
            map.put("message", "传入参数错误！！！");
            return map;
        } else {
            if (roleService.addRoleUser(roleId, ids)) {
                map.put("state", 1);
            } else {
                map.put("state", 0);
                map.put("message", "操作失败，请重试！！！");
            }
            return map;
        }
    }
//======================资源权限管理开始============================================================

    /**
     * 返回组织资源权限配置页面
     * @param map
     * @param id
     * @return
     * @version
     *  2016-07-21 chenchen create
     */
    @RequestMapping(value = "/authority", method = RequestMethod.GET)
    public String authority(ModelMap map, @RequestParam(value = "id", required = false, defaultValue = "0") String id) {
        map.put("authority", "role");
        map.put("id", id);
        map.put("typeId", "roleId");
        return "/manager/config/authority";
    }

    /**
     * 获取栏目权限集合
     * @param id
     * @param roleId
     * @return
     * @version
     *  2016-07-21 chenchen create
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/authority/list", method = RequestMethod.POST)
    public
    @ResponseBody
    List authorityList(@RequestParam(value = "id", required = false, defaultValue = "0") String id, @RequestParam String roleId) {
        return authorityService.findRoleChannelAuthoritiesBy(id, roleId);
    }

    /**
     * 获取客户端提交的栏目权限数据，并保存
     *
     * @param list    List<MAuthorityRole> 后台权限角色配置表
     * @return
     * @version
     *  2016-07-21 chenchen create
     */
    @RequestMapping(value = "/authority/add", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> addResourceAuthority(@RequestBody List<JSONObject> list) throws InterruptedException, MemcachedException, TimeoutException {
        Map<String, Object> map = new HashMap<>();
        if (list == null || list.size() <= 0) {
            map.put("state", 2);
            map.put("message", "传入参数错误！！！");
            return map;
        } else {
            List<MAuthRole> authList = new ArrayList<>();
            list.stream().forEach(json -> authList.add(json.toJavaObject(MAuthRole.class)));
            if (authorityService.addRoleAuthority(authList)) {

                map.put("state", 1);
            } else {
                map.put("state", 0);
                map.put("message", "操作失败，请重试！！！");
            }
            return map;
        }
    }

    /**
     * 获取功能权限集合
     *
     * @param channelId 栏目ID
     * @param roleId    组织ID
     * @return
     * @version
     * 2016-07-21 chenchen create
     */
    @RequestMapping(value = "/authority/functionList", method = RequestMethod.POST)
    public
    @ResponseBody
    List<Map<String, Object>> functionList(@RequestParam(required = false) String channelId, @RequestParam String roleId) {
        if (StringHelper.isEmpty(channelId)) {
            return new ArrayList<>();
        } else {
            return authorityService.findRoleFunctionAuthoritiesBy(channelId, roleId);
        }

    }

    /**
     * 获取客户端提交的功能权限数据，并保存
     *
     * @param list    List<MAuthorityRole> 后台权限角色配置表
     * @return
     * @cUser 陈晨
     * @cDate 2014-7-22
     * @mUser 陈晨
     * @mDate 2014-7-22
     */
    @RequestMapping(value = "/authority/addFunction", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> addFunction(@RequestBody List<JSONObject> list) throws InterruptedException, MemcachedException, TimeoutException {
        Map<String, Object> map = new HashMap<>();
        if (list == null || list.size() <= 0) {
            map.put("state", 2);
            map.put("message", "传入参数错误！！！");
            return map;
        } else {
            List<MAuthFunctionRole> authList = new ArrayList<>();
            list.stream().forEach(auth -> authList.add(auth.toJavaObject(MAuthFunctionRole.class)));
            if (authorityService.addRoleFunctionAuthority(authList)) {
                map.put("state", 1);
            } else {
                map.put("state", 0);
                map.put("message", "操作失败，请重试！！！");
            }
            return map;
        }
    }
    //======================权限管理结束============================================================
}
