/*
 * FileName：LoginInterceptor.java
 *
 * Description：登录拦截器,用于判断用户是否已登录,或是否为已登录
 *
 * History：
 * 版本号 		作者 			日期       			简介
 *  1.0   	    chenchen	    2016-07-05		    Create
 */
package com.job.manager.interceptor;

import com.job.manager.controller.MUserLoginAction;
import com.job.manager.model.MUser;
import com.job.manager.service.ICacheService;
import com.job.manager.util.ProjectContext;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginInterceptor implements HandlerInterceptor {
    private static ResourceBundle rb=ResourceBundle.getBundle("globalConfig");
    private static Logger log = Logger.getLogger(MUserLoginAction.class);
    @Resource
    private ICacheService cacheService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        String userAgent = request.getHeader("USER-AGENT");
        Pattern p = Pattern.compile(".*(MSIE 5.0|MSIE 6.0|MSIE 7.0|MSIE 8.0).*");
        Matcher m = p.matcher(userAgent);
        if (m.find()) {
            dispatcher(request, response, "/browserError.html", "版本错误", 996);
            return false;
        }
        MUser user = (MUser) request.getSession().getAttribute("user");//获取 session 中的用户

            /*    判断用户是否存在,
                 false,返回登录页面
                 true,继续操作
            */
        if (user == null) {
            dispatcher(request, response, "/mLogin", "您尚未登录或您的登录已过期，请先登录！！！", 999);
            return true;
        } else {
            //获取 sessionid
            Object  sessionId = cacheService.getByNS(user.getId(),rb.getString("xmem.userSessionMap.namespace"));

            /*     判断缓存中存储的 sessionId 是否等于当前 sessionId
              true,将用户存入上下文对象,并返回 true
              false,删除用户登录状态,并返回登录页面*/

            if (sessionId != null && sessionId.equals(request.getSession().getId())) {
                //初始化上下文
                ProjectContext.init();
                //将用户存入上下文
                ProjectContext.setUser(user);
                return true;
            } else {
                request.getSession().removeAttribute("user");
                dispatcher(request, response, "/mLogin", "您的帐号已在其它地方登录！！！", 998);
                return false;
            }
        }


    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    /**
     * 用于页面跳转或返回 errorcode
     * @param request
     * @param response
     * @param page
     * @param error
     * @param code
     * @throws ServletException
     * @throws IOException
     * @version
     *  chenchen 2016-07-05 create
     */
    private void dispatcher(HttpServletRequest request,HttpServletResponse response,String page,String error,int code) throws ServletException, IOException{
        if (request.getHeader("x-requested-with")!=null) {
            response.sendError(code);
        }else {
            request.setAttribute("error", error);
            request.getRequestDispatcher(page).forward(request, response);
        }
    }
}
