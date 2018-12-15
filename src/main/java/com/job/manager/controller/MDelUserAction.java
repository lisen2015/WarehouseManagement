package com.job.manager.controller;

import com.job.manager.daoManager.PagedList;
import com.job.manager.model.MUser;
import com.job.manager.service.IMAuthorityService;
import com.job.manager.service.IMRoleService;
import com.job.manager.service.IMUserService;
import com.job.manager.vo.VMUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller  
@RequestMapping("/manager/delUser")
public class MDelUserAction {
	@Resource
	private IMUserService userService;
	@Resource
	private IMAuthorityService authorityService;
	@Resource
	private IMRoleService roleService;
	@RequestMapping("")
	public String listPage(ModelMap map){
		return "/manager/config/delUserList";
	}
	//======================用户管理开始============================================================
	@RequestMapping(value="/userList",method=RequestMethod.POST) 
	public @ResponseBody
    PagedList<VMUser> roleList(@RequestParam int page, @RequestParam int rows, @RequestParam(value="id",required=false,defaultValue="0") long id, @RequestParam(value="value",required=false) String value, @RequestParam(value="type",required=false) String type, HttpServletRequest request){
		//List<Long> list=(List<Long>) request.getSession().getAttribute("userList");
		if (type!=null && value!=null && !value.trim().equals("")) {
			return userService.findByType(type,value,page,rows,0);
		}else {
			return userService.getallUserByPage(page,rows,0);
		}
			
	}
	
	
	
	@RequestMapping(value="/delUser",method=RequestMethod.POST) 
	public @ResponseBody Map<String,Object> deluser(@RequestParam String[] id,HttpServletRequest request){
		Map<String, Object> map=new HashMap<String, Object>();
		if (id.length==0) {
			 map.put("state", 1);
			 map.put("message", "要删除的数据不存在！");
			return map;
		}else{
			
			 MUser user=(MUser)request.getSession().getAttribute("muser");
			 try {
				 if(userService.deleteReal(id)){
					 map.put("state", 1);
				 }else{
					 map.put("state", 0);
					 map.put("message", "要删除的数据不存在！");
				 }
			 }catch(Exception e){
				 map.put("state", 0);
				 map.put("message", "服务器出错<br />error："+e.getMessage());
			 }
			 return map;
		}
		
	}
	
	@RequestMapping(value="/redoUser",method=RequestMethod.POST) 
	public @ResponseBody Map<String,Object> redoUser(@RequestParam String[] id,HttpServletRequest request){
		Map<String, Object> map=new HashMap<String, Object>();
		if (id.length==0) {
			 map.put("state", 2);
			 map.put("message", "要恢复的用户不存在！");
			return map;
		}else{
			 MUser user=(MUser)request.getSession().getAttribute("muser");
			 try {
				 if(userService.batchRecover(id)){
					 map.put("state", 1);
				 }else{
					 map.put("state", 0);
					 map.put("message", "要恢复的用户不存在！");
				 }
			 }catch(Exception e){
				 map.put("state", 0);
				 map.put("message", "服务器出错<br />error："+e.getMessage());
			 }
			 return map;
		}
		
	}
	
	

	
	
	//======================用户管理结束============================================================
	

	//======================用户组织管理开始========================================================
	@RequestMapping(value="/role")
	public String  role(ModelMap map,@RequestParam(value="id",required=false,defaultValue="0") long id){
		map.put("id", id);
		return "/manager/config/addRoleUser";
	}
	@RequestMapping(value="/role/detail")
	public String  roleDetail(ModelMap map,@RequestParam(value="id",required=false,defaultValue="0") long id,HttpServletRequest request){
		map.put("id", id);
		return "/manager/config/detailRoleUser";
	}
	

	@RequestMapping(value="/role/del",method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> delRoleUser(@RequestParam(value="ids",required=false,defaultValue="[0]") String[] ids,HttpServletRequest request){
		Map<String, Object> map=new HashMap<String, Object>();
		if (ids==null || ids.length<=0 ) {
			 map.put("state", 2);
			 map.put("message", "传入参数错误！！！");
			return map;
		}else{
			MUser user=(MUser)request.getSession().getAttribute("muser");
			 try {
				if(roleService.deleteRoleUser(ids)){
					 map.put("state", 1);
				 }else{
					 map.put("state", 0);
					 map.put("message", "操作失败，请重试！！！");
				 }
			 }catch(Exception e){
				 map.put("state", 0);
				 map.put("message", "服务器出错<br />error："+e.getMessage());
			 }
			 return map;
		}
	}

	//======================用户组织管理结束========================================================
}
