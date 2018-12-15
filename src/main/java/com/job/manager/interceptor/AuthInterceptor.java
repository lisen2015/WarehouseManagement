package com.job.manager.interceptor;

import com.job.manager.model.MFunctionConfig;
import com.job.manager.service.IMFunctionService;
import com.job.manager.util.ProjectContext;
import com.job.manager.util.StringHelper;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by cc on 16/7/5.
 */
public class AuthInterceptor implements HandlerInterceptor {
    @Resource
    private IMFunctionService functionService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        //获取用户功能操作权限map，key=栏目ID，value=栏目权限（true=有权限，false=没有权限）
        Map<Long,Boolean> userAuthMap =(Map<Long, Boolean>) request.getSession().getAttribute("functionMap");

        //系统默认admin用户拥有所有栏目权限
        if ( ProjectContext.getUser().getLoginName().equals("admin")) {
            return true;
        }
        //获取系统所有功能 map,key=htmlUrl,value=function
        Map<String, MFunctionConfig> functionMap=functionService.getAllFunctionMap();
        MFunctionConfig functionConig=functionMap.get(request.getServletPath());
        String path = request.getServletPath();
        if (functionConig==null) {
            dispatcher(request, response, "/mLogin/noAuthor","您没有访问/操作权限！！！",997);
            return false;
        }
        ProjectContext.set("curChannelId",functionConig.getChannelId());//当前用户所在栏目id

        if (userAuthMap == null){
            dispatcher(request, response, "/mLogin", "您尚未登录或您的登录已过期，请先登录！！！", 999);
            return true;
        }

        if (StringHelper.isNotEmpty(functionConig.getHtmlId())) {
            Boolean flag=userAuthMap.get(functionConig.getHtmlId());
            if (flag==null) {
                dispatcher(request, response, "/mLogin/noAuthor","您没有访问/操作权限！！！",997);
                return false;
            }else if (flag) {
                return true;
            }else {
                dispatcher(request, response, "/mLogin/noAuthor","您没有访问/操作权限！！！",997);
                return false;
            }
        }else {
            dispatcher(request, response, "/mLogin/noAuthor","您没有访问/操作权限！！！",997);
            return false;
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
