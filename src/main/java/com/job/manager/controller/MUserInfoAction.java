package com.job.manager.controller;

import com.job.manager.model.MUser;
import com.job.manager.service.IMAuthorityService;
import com.job.manager.service.IMRoleService;
import com.job.manager.service.IMUserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MUserInfoAction {
	@Resource
	private IMUserService userService;
	@Resource
	private IMAuthorityService authorityService;
	@Resource
	private IMRoleService orgService;
	@RequestMapping(value="/manager/loginUser",method=RequestMethod.GET)
	public String loginUser(HttpServletRequest request){
		return "/manager/config/loginUserInfo";
	}
	@RequestMapping(value="/manager/loginUser/exist",method=RequestMethod.POST)
	public @ResponseBody Object exist(@RequestParam String field,@RequestParam String value,@RequestParam String id){
		return !userService.exist(field,value,id);
	}
	@RequestMapping(value="/manager/loginUser/addUser",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> addUser(@Valid @ModelAttribute MUser mUser, Errors errors, HttpServletRequest request){
		Map<String, Object> map=new HashMap<String, Object>();

		if (errors.hasErrors()) {
			 map.put("state", 2);
			 for (FieldError fieldError : errors.getFieldErrors()) {
		            map.put(fieldError.getField(), fieldError.getDefaultMessage());
		        }
			return map;
		}
		 MUser user=(MUser)request.getSession().getAttribute("user");
		 try {

		     //用户名是否重复
		     if (userService.exist("loginName",mUser.getLoginName(), mUser.getId())) {
		    	 map.put("state", 2);
				 return map;
		     }

		     MUser newUser=userService.update(mUser);
			 if(newUser!=null){
				 if(mUser.getId()==newUser.getId())
					 request.getSession().setAttribute("user",newUser);
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
	@RequestMapping(value="/manager/resetPassword",method=RequestMethod.GET)
	public String resetPassword(HttpServletRequest request){
		return "/manager/config/resetPassword";
	}
	@RequestMapping(value="/manager/resetPassword/Info",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> resetPasswordInfo(@RequestParam String newLoginPassword,@RequestParam String loginPassword,HttpServletRequest request){
		MUser user=(MUser)request.getSession().getAttribute("user");
		Map<String, Object> map=new HashMap<String, Object>();
			 try {
				 loginPassword=userService.encrypt(loginPassword);
				 newLoginPassword=userService.encrypt(newLoginPassword);
			   if(user.getLoginPassword().equals(loginPassword)){
				    if(userService.resetPass(user.getId(),newLoginPassword)){
				       user.setLoginPassword(newLoginPassword);
					   map.put("state", 1);
				     }else{
					   map.put("state", 0);
					   map.put("message", "密码修改失败！");
				     }
			 }else{
				 map.put("state", 2);
				 map.put("message", "您输入的密码有误！");

			 }
			 }catch(Exception e){
				 map.put("state", 0);
				 map.put("message", "服务器出错<br />error："+e.getMessage());
			 }
			 return map;
	}
}