/*															
 * FileName：MUserLoginAction.java						
 *			
 * Description：用户登录controller层，用于处理用户登录、权限赋值、注销						
 * 																	
 * History：
 * 版本号 		作者 			日期       			简介
 *  1.0   	    chenchen	    2016-07-05  		Create
 */
package com.job.manager.controller;

import com.job.manager.model.MUser;
import com.job.manager.service.ICacheService;
import com.job.manager.service.IMAuthorityService;
import com.job.manager.service.IMUserService;
import com.job.manager.util.JCryptionUtil;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;

@Controller
@RequestMapping("/mLogin")
public class MUserLoginAction {
    private static ResourceBundle rb=ResourceBundle.getBundle("globalConfig");
    private static Logger log = Logger.getLogger(MUserLoginAction.class);
	@Resource
	private IMUserService userService;
	@Resource
	private IMAuthorityService authService;
    @Resource
    private ICacheService cacheService;
	//======================登录管理开始============================================================

    /**
     * 用户登录页跳转控制
     * 用于跳转用户登录页,对于已登录用户访问则直接跳转到管理页面
     * @param request
     * @return
     * @throws IOException
     */
	@RequestMapping("")
	public String loginJsp(HttpServletRequest request) throws Exception{
	    MUser user=(MUser)request.getSession().getAttribute("user");//从 session 中获取用户
        /*
         * 判断用户是否为空
         * true,用户已登录,继续判断用户是否异地登录
         * false,跳转登录页面
         */
		if (user!=null ) {
			//从缓存中获取当前登录用户的 sessionID
            Object  sessionId = cacheService.getByNS(user.getId(),rb.getString("xmem.userSessionMap.namespace"));
            /*
             * 判断当前登录用户的 sessionId 和当前请求的 sessionId 是否相同
             * true,跳转管理界面
             * false,跳转登录界面
             */
            if (sessionId!=null && sessionId.equals(request.getSession().getId())) {
				return "redirect:/manager";
			}else {
				return "mLogin";
			}
		}else {	
			return "mLogin";
		}
		
	}
	/**
	 * 根据用户名密码完成用户登录功能
	 * @param loginName 用户名
	 * @param password 密码
	 * @param request
	 * @return
	 * @throws Exception 
	 * @version
	 * 	2016-07-05	chenchen	create
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/login")
	public  @ResponseBody Map<String, Object> login(@RequestParam(required=false) String loginName,
			@RequestParam(required=false) String password,@RequestParam(required=false) String vcode,
			HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>();
		/*
		 * 由于springmvc如果在参数必填的情况下，如果调用url且并不传入参数，将直接跳转400错误页面，这里为了
		 * 使界面展现更友好，接受空参数传入，并判断是否参数为空，如果用户名、密码为空则前台弹出提示
		 */
		if (loginName==null||loginName.length()==0||password==null||password.length()==0) {
			map.put("state", 2);
			map.put("message", "请输入用户名或密码！！！");
			return map;
		}
		/*
		 * 判断是否获取服务端验证码
		 */
		String servceVcode=(String) request.getSession().getAttribute("vCode");
		if (servceVcode==null) {
			map.put("state", 2);
			map.put("message", "验证码获取错误，请刷新验证码！");
			return map;
		}
		/*
		 * 判断验证码,不管是否验证成功，都清除session中的验证码
		 */
		if(vcode==null || !vcode.equalsIgnoreCase(servceVcode)){
			map.put("state", 2);
			map.put("message", "验证码输入错误！！！");
			request.getSession().removeAttribute("vCode");
			return map;
		}else {
			request.getSession().removeAttribute("vCode");
		}
		
		//RSA解密
//		System.out.println(password);
		KeyPair keys = (KeyPair) request.getSession().getAttribute("keys");
		if (keys==null) {
			map.put("state", 2);
			map.put("message", "操作失败，请刷新页面后重试！！！");
			return map;
		}
		password = JCryptionUtil.decrypt(password, keys);
		MUser user=userService.loginByName(loginName, password);
		/*
		 * 如果用户登录成功
		 * 将muser（用户信息）存入session
		 * 将channelMap（用户栏目访问权限）存入session
		 * 将sessionID存入mUserSessionMap(key=userID,value=sessionId),用于判断异地登录
		 * userChannel（用户栏目访问权限）存入session
		 */
		if (user!=null) {
			//获取用户访问权限
			Map<String, Boolean> userMap=authService.findAllUserChannelAuthorities(user.getId());
			if (user.getIsValidate()==0){
                map.put("state", 2);
                map.put("message", "当前账号已被删除，如需恢复请及时联系管理员！！！");
                return map;
            }
            if (user.getIsValidate()==2){
                map.put("state", 2);
                map.put("message", "当前账号尚未通过审核，请及时联系管理员！！！");
                return map;
            }
            /*
			 * 判断有没有查看系统的权限
			 */
			if(!loginName.equals("admin")&&(userMap==null||userMap.get("268f74864f4a11e6b2be0242ac110003")==null ||!userMap.get("268f74864f4a11e6b2be0242ac110003"))){
				map.put("state", 2);
				map.put("message", "当前账号没有权限进入系统，请及时联系管理员！！！");
				return map;
			}
            request.getSession().setAttribute("user",user);
            //用户栏目访问权限
            request.getSession().setAttribute("channelMap", userMap);
            //用户功能访问权限
            request.getSession().setAttribute("functionMap", authService.findUserAllFunctionAuthorities(user.getId()));
            //将用户 sessionID 存入缓存,用于后续进行异地登录判断
            cacheService.setByNS(user.getId(),request.getSession().getId(),Integer.parseInt(rb.getString("xmem.userSessionMap.timeout")),rb.getString("xmem.userSessionMap.namespace"));
            map.put("state", 1);
            return map;
		}else {
			map.put("state", 2);
			map.put("message", "用户名或密码错误！！！");
			return map;
		}
	}

	@RequestMapping("/loginOut")
	public String loginOut(HttpServletRequest request) throws IOException, InterruptedException, MemcachedException, TimeoutException {
		request.getSession().removeAttribute("user");
		request.getSession().removeAttribute("userMap");
        cacheService. clearByNS(request.getSession().getId(),rb.getString("xmem.userSessionMap.timeout"));
		return "mLogin";
	}
	//======================登录管理结束============================================================
	//======================没有权限跳转============================================================
	@RequestMapping("/noAuthor")
	public String noAuth(){
		return "noAuthor";
	}

	@RequestMapping("/loginRAS") 
	public @ResponseBody Map<String, String>  loginRAS(HttpServletRequest request) throws Exception{
			Map<String, String> map= new HashMap<>();
			JCryptionUtil jCryptionUtil = new JCryptionUtil();
			KeyPair keys;
			if (request.getSession().getAttribute("keys") == null) {
				keys = jCryptionUtil.generateKeypair(512);
				request.getSession().setAttribute("keys", keys);
			}else {
				keys=(KeyPair) request.getSession().getAttribute("keys");
			}
			map.put("e", JCryptionUtil.getPublicKeyExponent(keys));
			map.put("n", JCryptionUtil.getPublicKeyModulus(keys));
			map.put("maxdigits", String.valueOf(JCryptionUtil.getMaxDigits(512)));
			return map;
		
	}

}
