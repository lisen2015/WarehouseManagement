/*															
 * FileName：MFunctionAction.java						
 *			
 * Description：栏目功能配置的controller层，用于控制页面跳转，和调用业务接口							
 * 																	
 * History：
 * 版本号 作者 		日期       	简介
 *  1.0   chenchen	2014-7-16		Create		
 */
package com.job.manager.controller;

import com.job.manager.model.MFunctionConfig;
import com.job.manager.service.IMAuthorityService;
import com.job.manager.service.IMFunctionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller  
@RequestMapping("/manager/function")
public class MFunctionAction {
	@Resource
	private IMFunctionService functionService;
	@Resource
	private IMAuthorityService authorityService;
	//======================功能管理开始============================================================
	@RequestMapping(value="",method=RequestMethod.POST) 
	public String listPage(@RequestParam String channelId,@RequestParam String channelName, ModelMap map){
		map.addAttribute("channelId", channelId);
		map.addAttribute("channelName", channelName);
		return "/manager/config/functionList";
	}
	@RequestMapping(value="/list",method=RequestMethod.POST) 
	public @ResponseBody List<MFunctionConfig> list(@RequestParam String channelId){
		return functionService.getListByChannelId(channelId);
			
	}
	
	/**
	 * 用于新增数据
	 * @param functionConfig 功能model对象
	 * @param htmlUrlList	htmlUrl
	 * @param errors
	 * @return
	 * @version
	 * 	2014-08-24		chenchen	create
	 * 	2014-09-24		chenchen	修改方法htmlurl传值
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST) 
	public @ResponseBody Map<String,Object> add(@Valid @ModelAttribute MFunctionConfig functionConfig,
                                                @RequestParam String htmlUrlList, Errors errors){
		Map<String, Object> map=new HashMap<String, Object>();
		if (errors.hasErrors()) {
			 map.put("state", 2);
			 for (FieldError fieldError : errors.getFieldErrors()) {  
		            map.put(fieldError.getField(), fieldError.getDefaultMessage());  
		        }  
			return map;
		}
		 try {
			 /*
			  * htmlUrlList必填，且长度必须小于1000
			  */
			 if (htmlUrlList==null || htmlUrlList.length()>1000) {
				map.put("state", 0);
				map.put("message", "操作失败，用户输入数据不合法！！！");
				return map; 
			 }
             functionConfig.setFunctionName(functionConfig.getFunctionName()==null?"":functionConfig.getFunctionName().trim());
             functionConfig.setHtmlId(functionConfig.getHtmlId()==null?"":functionConfig.getHtmlId().trim());
             functionConfig.setHtmlUrl(functionConfig.getHtmlUrl()==null?"":functionConfig.getHtmlUrl().trim());
             functionConfig.setHtmlUrl(htmlUrlList);
			 if(functionService.addFunction(functionConfig)){
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
	@RequestMapping(value="/update",method=RequestMethod.POST) 
	public @ResponseBody Map<String,Object> update(@Valid @ModelAttribute MFunctionConfig functionConfig,
                                                   @RequestParam String htmlUrlList, Errors errors){
		Map<String, Object> map=new HashMap<String, Object>();
		if (errors.hasErrors()) {
			 map.put("state", 2);
			 for (FieldError fieldError : errors.getFieldErrors()) {  
		            map.put(fieldError.getField(), fieldError.getDefaultMessage());  
		        }  
			return map;
		}
		 try {
			 /*
			  * htmlUrlList必填，且长度必须小于1000
			  */
			 if (htmlUrlList==null || htmlUrlList.length()>1000) {
				map.put("state", 0);
				map.put("message", "操作失败，用户输入数据不合法！！！");
				return map; 
			 }
			 functionConfig.setFunctionName(functionConfig.getFunctionName()==null?"": functionConfig.getFunctionName().trim());
			 functionConfig.setHtmlId(functionConfig.getHtmlId()==null?"": functionConfig.getHtmlId().trim());
			 functionConfig.setHtmlUrl(functionConfig.getHtmlUrl()==null?"": functionConfig.getHtmlUrl().trim());
			 functionConfig.setHtmlUrl(htmlUrlList);
			 if(functionService.updateFunction(functionConfig)){
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
	@RequestMapping(value="/del",method=RequestMethod.POST) 
	public @ResponseBody Map<String,Object> del(@RequestParam String[] id){
		Map<String, Object> map=new HashMap<String, Object>();
		if (id.length==0) {
			 map.put("state", 1);
			 map.put("message", "要删除的数据不存在！");
			return map;
		}else{	
			 try {
				 if(functionService.delete(id)){
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
	@RequestMapping(value="/exist",method=RequestMethod.POST) 
	public @ResponseBody Object exist(@RequestParam String field,@RequestParam String value,@RequestParam String id){
		return !functionService.exist(field,value,id);

	}
	//======================功能管理结束============================================================
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
            functionService.updateIndex(sId, sIndex, tIndex, point);
            map.put("state", 1);
        } catch (Exception e) {
            map.put("state", 0);
            map.put("message", "服务器出错<br />error：" + e.getMessage());
            e.printStackTrace();
        }
        return map;
    }
}
