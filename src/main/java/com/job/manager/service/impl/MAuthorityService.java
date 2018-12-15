/*															
 * FileName：MAuthorityService.java						
 *			
 * Description：权限操作的业务类实现类，用于实现权限操作的方法						
 * 																	
 * History：
 * 版本号 作者 		日期       	简介
 *  1.0   chenchen	2014-7-16		Create		
 */
package com.job.manager.service.impl;

import com.job.manager.daoManager.BaseDaoManager;
import com.job.manager.daoManager.SearchFilter;
import com.job.manager.model.MAuthFunctionRole;
import com.job.manager.model.MAuthRole;
import com.job.manager.model.MUserRoleConfig;
import com.job.manager.service.ICacheService;
import com.job.manager.service.IMAuthorityService;
import com.job.manager.service.IMRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class MAuthorityService implements IMAuthorityService {
    private static ResourceBundle rb=ResourceBundle.getBundle("globalConfig");
    @Resource
    private BaseDaoManager<MAuthFunctionRole> authFunctionRoleManager;
    @Resource
    private BaseDaoManager<MAuthRole> authRoleManager;
    @Resource
    private BaseDaoManager<MUserRoleConfig> mRoleUserManager;
    @Resource
    private IMRoleService roleService;
    @Resource
    private ICacheService cacheService;
    @Override
    public Map<String, Boolean> findAllUserChannelAuthorities(String userId) {
        //1、定义用于返回的authMap
        Map<String, Boolean> authMap = new HashMap<>();
        //2、获取用户组织列表
        SearchFilter sf = new SearchFilter(1, -1);
        sf.addEqCondition("userId", userId);
        List<MUserRoleConfig> userRoleList = mRoleUserManager.listObjects(sf);
        /*
         * 3、判断用户组是否非空
         * true,继续查询权限
         */
        if (userRoleList != null && userRoleList.size() > 0) {
            /*
             * 循环权限map
             */
            for (MUserRoleConfig orgConfig : userRoleList) {
               Map<String,Integer> orgAuth= getRoleChannel().get(orgConfig.getRoleId());
                if(orgAuth!=null){
                    orgAuth.forEach((key,value)->{
                        if(value!=null &&value==1){
                            authMap.put(key,true);
                        }
                    });
                }
            }
        }
        return authMap;
    }

    @Override
    public Map<String, Boolean> findUserAllFunctionAuthorities(String userId) {
        //1、定义用于返回的authMap
        Map<String, Boolean> authMap = new HashMap<>();
        //2、获取用户组织列表
        SearchFilter sf = new SearchFilter(1, -1);
        sf.addEqCondition("userId", userId);
        List<MUserRoleConfig> userRoleList = mRoleUserManager.listObjects(sf);
        /*
         * 3、判断用户组是否非空
         * true,继续查询权限
         */
        if (userRoleList != null && userRoleList.size() > 0) {
            /*
             * 循环权限map
             */
            for (MUserRoleConfig orgConfig : userRoleList) {
                Map<String,Integer> orgAuth= getRoleFunction().get(orgConfig.getRoleId());
                if(orgAuth!=null){
                    orgAuth.forEach((key,value)->{
                        if(value!=null &&value==1){
                            authMap.put(key,true);
                        }
                    });
                }
            }
        }
        return authMap;
    }

    @Override
    public boolean addRoleAuthority(List<MAuthRole> list) {
        authRoleManager.batchUpdate(list);
        //清除缓存
        cacheService.clearCached(rb.getString("xmem.roleChannelAuth.key"));
        return true;
    }

    @Override
    public List<Map<String, Object>> findRoleChannelAuthoritiesBy(String channelParentId, String orgId) {
        String sql = "select A.ID as CHANNELID,B.id as ID,B.ROLE_ID as AUTHORITYID,A.CHANNEL_NAME as CHANNELNAME" +
                ",A.PARENT_ID as CHANNELPARENTID,B.IS_AUTH as ISAUTH ,A.IS_END as ISEND from M_CHANNEL_CONFIG A " +
                "left join M_AUTH_ROLE B on A.Id=B.CHANNEL_ID and B.ROLE_ID=? " +
                "where  A.IS_VALIDATE=1 and A.PARENT_ID=?  order by A.CHANNEL_INDEX";
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(orgId);
        list.add(channelParentId);
        List<Map<String, Object>> authList = (List<Map<String, Object>>) authRoleManager.executeNativeQuerySQL(sql, list);
        /*
		 * 循环结果根据IS_END获取树结果state（是否包含子节点）
		 */
        for (Map<String, Object> map : authList) {
            if (map.get("ISEND") instanceof Integer) {
                if ((Integer) map.get("ISEND") == 0) {
                    map.put("state", "closed");
                } else {
                    map.put("state", "open");
                }
            } else {
                if (Integer.parseInt(map.get("ISEND").toString()) == 0) {
                    map.put("state", "closed");
                } else {
                    map.put("state", "open");
                }
            }
        }
        return authList;
    }


    @Override
    public List<Map<String, Object>> findRoleFunctionAuthoritiesBy(String channelId, String orgId) {
        String sql = "SELECT B.ID ID,A.ID FUNCTIONID,A.FUNCTION_NAME FUNCTIONNAME,B.IS_AUTH ISAUTH " +
                "FROM M_FUNCTION_CONFIG A  LEFT JOIN M_AUTH_FUNCTION_ROLE B " +
                "ON A.ID=B.FUNCTION_ID AND B.ROLE_ID=? " +
                "WHERE A.CHANNEL_ID=? AND  A.IS_VALIDATE=1 " +
                "ORDER BY FUNCTION_INDEX";
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(orgId);
        list.add(channelId);
        return (List<Map<String, Object>>) authFunctionRoleManager.executeNativeQuerySQL(sql, list);
    }

    @Override
    public boolean addRoleFunctionAuthority(List<MAuthFunctionRole> list) {
        authFunctionRoleManager.batchUpdate(list);
        //更新缓存
        cacheService.clearCached(rb.getString("xmem.roleFunctionAuth.key"));
        return true;
    }

    private Map<String, Map<String, Integer>> getRoleChannel() {
        //从缓存中获取数据
        Map<String, Map<String, Integer>>  roleChannelAuth = (Map<String, Map<String, Integer>>) cacheService.get(rb.getString("xmem.roleChannelAuth.key"));
        if (roleChannelAuth!=null){
            return roleChannelAuth;
        }
        SearchFilter sf = SearchFilter.getSearchFilter(1, -1);
        //获取角色权限集合
        List<MAuthRole> list = authRoleManager.listObjects(sf);
        //定义用于返回的map
        Map<String, Map<String, Integer>> map = new HashMap<>();
		/*
		 * 循环角色权限集合，将结果装入map
		 * key=角色id，value=栏目权限HashMap（key=栏目Id，value=权限）
		 */
        for (MAuthRole mAuthorityRole : list) {
            Map<String, Integer> roleMap = map.get(mAuthorityRole.getRoleId());
            if (roleMap == null) {
                roleMap = new HashMap<String, Integer>();
                map.put(mAuthorityRole.getRoleId(), roleMap);
            }
            roleMap.put(mAuthorityRole.getChannelId(), mAuthorityRole.getIsAuth());
        }
        cacheService.set(rb.getString("xmem.roleChannelAuth.key"),map,Integer.parseInt(rb.getString("xmem.roleChannelAuth.timeout")));
        return map;
    }

    private Map<String, Map<String, Integer>> getRoleFunction() {
        //从缓存中获取数据
        Map<String,Map<String, Integer>>  roleFunctionAuth = (Map<String, Map<String, Integer>>) cacheService.get(rb.getString("xmem.roleFunctionAuth.key"));
        if (roleFunctionAuth!=null){
            return roleFunctionAuth;
        }
        SearchFilter sf = SearchFilter.getSearchFilter(1, -1);
        //获取角色权限集合
        List<MAuthFunctionRole> list = authFunctionRoleManager.listObjects(sf);
        //定义用于返回的map
        Map<String, Map<String, Integer>> map = new HashMap<>();
		/*
		 * 循环角色权限集合，将结果装入map
		 * key=角色id，value=栏目权限HashMap（key=栏目Id，value=权限）
		 */
        for (MAuthFunctionRole mAuthorityRole : list) {
            Map<String, Integer> roleMap = map.get(mAuthorityRole.getRoleId());
            if (roleMap == null) {
                roleMap = new HashMap<String, Integer>();
                map.put(mAuthorityRole.getRoleId(), roleMap);
            }
            roleMap.put(mAuthorityRole.getFunction().getHtmlId(), mAuthorityRole.getIsAuth());
        }
        //将数据存入缓存
        cacheService.set(rb.getString("xmem.roleFunctionAuth.key"),map,Integer.parseInt(rb.getString("xmem.roleFunctionAuth.timeout")));
        return map;
    }
}
