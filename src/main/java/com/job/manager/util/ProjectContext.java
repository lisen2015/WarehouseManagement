/*															
 * FileName：ProjectContext.java						
 *			
 * Description：项目上下文对象，用于存储用户信息、组织、栏目等原本存储在applicationContext
 * 	session中的信息数据。提供service层使用						
 * 																	
 * History：
 * 版本号 			作者 			日期       			简介
 *  1.0   		chenchen	2014-7-14		Create	
 *  		
 */
package com.job.manager.util;

import com.job.manager.model.MUser;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Project context.
 */
public class ProjectContext {
    private static final ThreadLocal<Map<String,Object>> CONTEXT_MAP = new ThreadLocal<Map<String,Object>>();

    /**
     * Instantiates a new Project context.
     */
    protected ProjectContext() {
    }

    /**
     * 初始化
     *
     * @version 2015年8月21日 chenchen	create
     */
    public static void init(){
        Map<String,Object> map = CONTEXT_MAP.get();
        if (map!=null) {
            map.clear();
        }
    }

    /**
     * Get object.
     *
     * @param attribute the attribute
     * @return the object
     * @version * 2016-08-03 chenchen create
     */
    public static Object get(String attribute) {
        try {
            Map<String,Object> map = CONTEXT_MAP.get();
            return map.get(attribute);
        } catch (NullPointerException e) {
            return null;
        }

    }


    /**
     * Get t.
     *
     * @param <T>       the type parameter
     * @param attribute the attribute
     * @param clazz     the clazz
     * @return the t
     * @version * 2016-08-03 chenchen create
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String attribute, Class<T> clazz) {
        return (T) get(attribute);
    }


    /**
     * Set.
     *
     * @param attribute the attribute
     * @param value     the value
     * @version * 2016-08-03 chenchen create
     */
    public static void set(String attribute, Object value) {
        Map<String,Object> map = CONTEXT_MAP.get();

        if (map == null) {
            map = new HashMap<String,Object>();
            CONTEXT_MAP.set(map);
        }

        map.put(attribute, value);
    }

    /**
     * Gets user.
     *
     * @return the user
     * @version * 2016-08-03 chenchen create
     */
    public static MUser getUser() {
        return get("user",MUser.class);
    }

    /**
     * Sets user.
     *
     * @param user the user
     * @version * 2016-08-03 chenchen create
     */
    public static void setUser(MUser user) {
        set("user",user);
    }


}
