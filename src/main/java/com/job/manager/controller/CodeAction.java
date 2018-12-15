/*															
 * FileName：CodeAction.java						
 *			
 * Description：字段管理controller层，用于处理栏目获取						
 * 																	
 * History：
 * 版本号 			作者 			日期       			简介
 *  1.0   		chenchen	2014-7-16		Create	//asddas
 */
package com.job.manager.controller;

import com.job.manager.model.CodeTable;
import com.job.manager.model.MUser;
import com.job.manager.service.ICacheService;
import com.job.manager.service.ICodeTableService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class CodeAction {

	private static ResourceBundle rb=ResourceBundle.getBundle("globalConfig");
	@Resource
	private ICacheService cacheService;
	@Resource
	private ICodeTableService codeService;
	//=====================================字典管理=======================================================================
	@RequestMapping("/manager/code")
	public String listPage(ModelMap map){
		return "/manager/config/codeList";
	}
	@RequestMapping(value="/manager/code/codeList",method=RequestMethod.POST)
	public @ResponseBody List<CodeTable> CodeList(@RequestParam(value="id",required=false,defaultValue="0") String id){
        return codeService.getListByParent(id);
	}
	@RequestMapping(value="/manager/code/add",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> add(@Valid @ModelAttribute CodeTable code, Errors errors, HttpServletRequest request){
		Map<String, Object> map=new HashMap<String, Object>();
		if (errors.hasErrors()) {
			 map.put("state", 2);
			 for (FieldError fieldError : errors.getFieldErrors()) {  
		            map.put(fieldError.getField(), fieldError.getDefaultMessage());  
		        }  
			return map;
		}
		 try {
			 code.setCodeDesc(code.getCodeDesc()==null?"":code.getCodeDesc().trim());
			 code.setCodeValue1(code.getCodeValue1()==null?"":code.getCodeValue1().trim());
			 code.setCodeValue2(code.getCodeValue2()==null?"":code.getCodeValue2().trim());
             code.setParentId(code.getParentId()==null?"":code.getParentId());
             code.setCodeLevel(code.getCodeLevel()==null?1:code.getCodeLevel());
             code.setIsEnd(1);
			 if(codeService.add(code)){
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
	@RequestMapping(value="/manager/code/update",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> update(@Valid @ModelAttribute CodeTable code, Errors errors, HttpServletRequest request){
		Map<String, Object> map=new HashMap<String, Object>();
		if (errors.hasErrors()) {
			 map.put("state", 2);
			 for (FieldError fieldError : errors.getFieldErrors()) {  
		            map.put(fieldError.getField(), fieldError.getDefaultMessage());  
		        }  
			return map;
		}
		 MUser user=(MUser)request.getSession().getAttribute("muser");
		 try {
			 code.setCodeDesc(code.getCodeDesc()==null?"":code.getCodeDesc().trim());
			 code.setCodeValue1(code.getCodeValue1()==null?"":code.getCodeValue1().trim());
			 code.setCodeValue2(code.getCodeValue2()==null?"":code.getCodeValue2().trim());
			 if(codeService.update(code)){
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
	@RequestMapping(value="/manager/code/delCode",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> delCode(@RequestParam String[] id,HttpServletRequest request){
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
					if(codeService.delete(id)){
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
    @RequestMapping(value="/manager/code/exist",method=RequestMethod.POST)
    public @ResponseBody Object exist(@RequestParam String field,@RequestParam String value,@RequestParam String id){
        return !codeService.exist(field,value,id);

    }
	//============================================================================================================
	
	
}
