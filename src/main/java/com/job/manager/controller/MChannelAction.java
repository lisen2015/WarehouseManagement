/*
 * FileName：MChannelAction.java
 *
 * Description：栏目管理模块 action 跳转类
 *
 * History：
 * 版本号  作者 		日期           	简介
 *  1.0   chenchen	2016-07-05		Create
 */
package com.job.manager.controller;

import com.job.manager.model.MChannelConfig;
import com.job.manager.model.MUser;
import com.job.manager.service.ICacheService;
import com.job.manager.service.IMAuthorityService;
import com.job.manager.service.IMChannelService;
import com.job.manager.service.IMFunctionService;
import com.job.manager.util.ProjectContext;
import com.job.manager.util.StringHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * The type M channel controller.
 */
@Controller
@RequestMapping("/manager/channel")
public class MChannelAction {

	private static ResourceBundle rb=ResourceBundle.getBundle("globalConfig");
	@Resource
	private ICacheService cacheService;
	@Resource
	private IMChannelService channelService;
	@Resource
	private IMAuthorityService authorityService;
	@Resource
	private IMFunctionService functionService;

    /**
     * List page string.
     *
     * @param map the map
     * @return the string
     * @version * 2016-07-05 chenchen create
     */
//======================栏目管理开始============================================================
	@RequestMapping("") 
	public String listPage(ModelMap map){
		map.put("zeroChannelId","268f74864f4a11e6b2be0242ac110003");
        return "/manager/config/channelList";
	}

    /**
     * Gets channel list.
     *
     * @param id    the id
     * @return the channel list
     * @version * 2016-07-05 chenchen create
     */
    @RequestMapping(value="/channelList",method=RequestMethod.POST)
	public @ResponseBody List<MChannelConfig> getChannlList(@RequestParam(value="id",required=false,defaultValue="268f74864f4a11e6b2be0242ac110003") String id){
        MChannelConfig channelConfig=channelService.getAllChannelTree().get(id);
        if (channelConfig!=null){
            return channelConfig.getChildren();
        }else{
            return new ArrayList<>();
        }
	}

	@RequestMapping(value="/exist",method=RequestMethod.POST) 
	public @ResponseBody Object exist(@RequestParam String field,@RequestParam String value,@RequestParam String id){
		return !channelService.exist(field,value,id);

	}
	
	@RequestMapping(value="/addChannel",method=RequestMethod.POST) 
	public @ResponseBody Map<String,Object> addChannel(@Valid @ModelAttribute MChannelConfig mChannelConfig,Errors errors,HttpServletRequest request){
		Map<String, Object> map=new HashMap<String, Object>();
		if (errors.hasErrors()) {
			 map.put("state", 2);
			 for (FieldError fieldError : errors.getFieldErrors()) {  
		            map.put(fieldError.getField(), fieldError.getDefaultMessage());  
		        }  
			return map;
		}
		 try {
			 if(StringHelper.isNotEmpty(mChannelConfig.getChannelUrl())&&
                     channelService.exist("channelUrl", mChannelConfig.getChannelUrl(), mChannelConfig.getId()))
			 {
				 map.put("state", 2);
				 map.put("栏目地址", "已存在"); 
				 return map;
			 }
			 mChannelConfig.setChannelName(mChannelConfig.getChannelName()==""?null:mChannelConfig.getChannelName().trim());
			 mChannelConfig.setChannelUrl(mChannelConfig.getChannelUrl()==null?"":mChannelConfig.getChannelUrl().trim());
			 mChannelConfig.setChannelUrl2(mChannelConfig.getChannelUrl2()==null?"":mChannelConfig.getChannelUrl2().trim());
             mChannelConfig.setIsEnd(1);
			 if(channelService.addChannel(mChannelConfig)){
				 map.put("state", 1);
			 }else{
				 map.put("state", 0);
				 map.put("message", "操作失败，请重试！！！");
			 }
		} catch (Exception e) {
			 map.put("state", 0);
			 map.put("message", "服务器出错<br />error："+e.getMessage());
             e.printStackTrace();
		}
		 return map;
	}
	@RequestMapping(value="/updateChannel",method=RequestMethod.POST) 
	public @ResponseBody Map<String,Object> updateChannel(@Valid @ModelAttribute MChannelConfig mChannelConfig,Errors errors,HttpServletRequest request){
		Map<String, Object> map=new HashMap<String, Object>();
		if (errors.hasErrors()) {
			 map.put("state", 2);
			return map;
		}
		 MUser user= ProjectContext.get("user",MUser.class);
		 try {
             if(StringHelper.isNotEmpty(mChannelConfig.getChannelUrl())&&
                     channelService.exist("channelUrl", mChannelConfig.getChannelUrl(), mChannelConfig.getId()))
			 {
				 map.put("state", 2);
				 map.put("message", "栏目地址已存在"); 
				 return map;
			 }
			 mChannelConfig.setChannelName(mChannelConfig.getChannelName()==""?null:mChannelConfig.getChannelName().trim());
			 mChannelConfig.setChannelUrl(mChannelConfig.getChannelUrl()==null?"":mChannelConfig.getChannelUrl().trim());
			 mChannelConfig.setChannelUrl2(mChannelConfig.getChannelUrl2()==null?"":mChannelConfig.getChannelUrl2().trim());
			 if(channelService.updateChannel(mChannelConfig)){
				 map.put("state", 1);
			 }else{
				 map.put("state", 0);
				 map.put("message", "操作失败，请重试！！！");
			 }
		} catch (Exception e) {
			 map.put("state", 0);
			 map.put("message", "服务器出错<br />error："+e.getMessage());
		}
		 return map;
	}
	@RequestMapping(value="/delChannel",method=RequestMethod.POST) 
	public @ResponseBody Map<String,Object> delChannel(String[] id,HttpServletRequest request){
		Map<String, Object> map=new HashMap<String, Object>();
		if (id.length==0) {
			 map.put("state", 1);
			 map.put("message", "要删除的数据不存在！");
			return map;
		}else{
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
					if(channelService.delete(id)){
						map.put("state", 1);
					}else{
						map.put("state", 0);
						map.put("message", "要删除的数据不存在！");
					}
				}catch(Exception e){
					map.put("state", 0);
					map.put("message", "服务器出错<br />error："+e.getMessage());
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
	//======================栏目管理结束============================================================
}
