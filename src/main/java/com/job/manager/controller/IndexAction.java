/*															
 * FileName：IndexAction.java						
 *			
 * Description：登录后首页controller层，用于处理栏目获取						
 * 																	
 * History：
 * 版本号 		作者 			日期       			简介
 *  1.0   	chenchen	2014-7-16		Create	
 *  1.0.1   chenchen	2014-7-16		update		
 *  	修改userChannel方法
 */
package com.job.manager.controller;
import com.job.manager.model.MChannelConfig;
import com.job.manager.service.ICacheService;
import com.job.manager.service.ICodeTableService;
import com.job.manager.service.IMChannelService;
import com.job.manager.util.ProjectContext;
import com.job.manager.util.StringHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * The type Index action.
 */
@Controller
@RequestMapping("/manager")
public class IndexAction {
    private static ResourceBundle rb=ResourceBundle.getBundle("globalConfig");
    @Resource
    private ICacheService cacheService;
	@Resource
	private IMChannelService channelService;
	@Resource
	private ICodeTableService codeService;


    /**
     * Index string.
     *
     * @return the string
     * @version * 2016-09-01 chenchen create
     */
    @RequestMapping("")
	public String index(){
        return "index";
	}

    /**
     * Index left string.
     *
     * @return the string
     * @version * 2016-09-01 chenchen create
     */
    @RequestMapping("/index/left")
	public String indexLeft(){
		return "index-left";
	}

    /**
     * Welcome string.
     *
     * @return the string
     * @version * 2016-09-01 chenchen create
     */
    @RequestMapping("/index/welcome")
	public String welcome(){
		return "welcome";
	}









/*
    * Description：消息管理-全局消息
	* History:
	* 	版本号  	作者       日期          简介
	*	1.0   	lina    2016-8-22     Create
	*/
	@RequestMapping("/newsBroadcast")
	public String newsBroadcast(){return "newsBroadcast";}

    /**
     * 检查数据是否已锁定
     * @param key     the key
     * @param request the request
     * @return the object
     * @version * 2016-09-01 chenchen create
     */
    @RequestMapping("/checkDataLocked")
    public @ResponseBody Object checkLocked(@RequestParam String key,HttpServletRequest request){
        Map<String, Object> returnMap = new HashMap<>();
        //设置下次获取时间未缓存超时时间的一半
        returnMap.put("continueTime",Integer.parseInt(rb.getString("xmem.dataLocked.timeout"))/2);
        //取出缓存
        String value= (String) cacheService.getByNS(key,rb.getString("xmem.dataLocked.namespace"));
        /*
         * 判断缓存中是否存在内容,或者缓存数据是否=当前用 session
         * true, 继续锁定,并且返回 true
         * false,数据被其他用户锁定,不能继续访问
         */
        if(StringHelper.isEmpty(value)||value.equals(ProjectContext.getUser().getLoginName())){
            //更新缓存数据
            //取出缓存
            cacheService.setByNS(key, ProjectContext.getUser().getLoginName(),Integer.parseInt(rb.getString("xmem.dataLocked.timeout")),rb.getString("xmem.dataLocked.namespace"));
            returnMap.put("state",1);
            return returnMap;
        }else{
            returnMap.put("state",0);
            returnMap.put("lockedUser",value);
            return returnMap;
        }

    }


    /**
     * 根据父id获取栏目列表
     *
     * @param request the request
     * @param id      父栏目ID
     * @return list
     * @version 2014    -07-23	chenchen	create 	2014-09-17	chenchen	update 		修改83行代码，去除返回a标签中的onclick 		修改86行代码，替换返回a标签中的onclick=open1(...)——》onclick=loadCenterPanel(...)
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value="/userChannel",method=RequestMethod.POST) 
	public @ResponseBody List<Map<String, Object>> userChannel(HttpServletRequest request,
		@RequestParam(defaultValue="268f74864f4a11e6b2be0242ac110003",required=false) String id){
        //获取当前请求的栏目对象
        MChannelConfig channelConfig=channelService.getAllChannelTree().get(id);
        List<Map<String, Object>> list=new ArrayList<>();
        /*
         * 判断当前栏目对象是不存在,
         * true,返回 空集合
         */
        if (channelConfig==null){
            return list;
        }
        List<MChannelConfig> channelList=channelService.getAllChannelTree().get(id).getChildren();
		//获取session中的用户权限map
		Map<Long, Boolean> userMap =(Map<Long, Boolean>)request.getSession().getAttribute("channelMap");
		//获取当前项目根 url
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();;
		/*
		 * 循环子栏目集合,并根据当前用户的权限,设置其可访问的栏目对象
		 */
        for (MChannelConfig mChannelConfig : channelList) {
			if (ProjectContext.getUser().getLoginName().equals("admin") ||(userMap!=null&& userMap.get(mChannelConfig.getId())!=null &&userMap.get(mChannelConfig.getId()))) {
				Map<String, Object> map= new HashMap<>();
				map.put("id", mChannelConfig.getId());
				if (mChannelConfig.getChannelUrl()==null || mChannelConfig.getChannelUrl().equals("")) {
					map.put("text","<a style='font-size: 20px;color:#205CB2;'>"
							+mChannelConfig.getChannelName()+"</a>");
				}else {
					map.put("text","<a style='font-size: 20px;color:#205CB2;' onclick=\"loadCenterPanel('"
							+mChannelConfig.getChannelName()+"','"+basePath+mChannelConfig.getChannelUrl()+"')\">"
							+mChannelConfig.getChannelName()+"</a>");
				}
				if (mChannelConfig.getIsEnd()==1) {
					map.put("state", "open");
				}else {
					map.put("state", "closed");
				}
				list.add(map);
			}
		}
		return list;
		
	}
		//======================获取用户栏目===========================================================
}
