/*															
 * FileName：MRoleService.java
 *			
 * Description：角色管理的业务层实现类，用于实现角色的所有业务方法
 * 																	
 * History：
 * 版本号 		作者 			日期       			简介
 *  1.0   	chenchen	2014-7-16		Create
 *  1.0.1	nijiaqi		2014-10-10		新增方法exist判断角色ID是否重复
 *  1.0.2	nijiaqi		2014-10-10		修改方法update，添加了roleNo字段
 *  1.0.3	chenchen	2014-10-22		删除批量delete方法，修改delete(id)方法
 *  1.0.4	chenchen	2014-10-22		删除方法getAllRoleUser、updateAllRoleUser
 *  1.0.5	chenchen	2014-10-22		修改方法getAllRoleGPS、updateAllRoleGPS
 */
package com.job.manager.service.impl;

import com.job.manager.daoManager.BaseDaoManager;
import com.job.manager.daoManager.HQLBuilder;
import com.job.manager.daoManager.SearchFilter;
import com.job.manager.model.MUserRole;
import com.job.manager.model.MUserRoleConfig;
import com.job.manager.service.ICacheService;
import com.job.manager.service.IMRoleService;
import com.job.manager.util.StringHelper;
import com.job.manager.vo.VMRole;
import com.job.manager.vo.VMRoleUserConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class MRoleService implements IMRoleService {
    private static ResourceBundle rb=ResourceBundle.getBundle("globalConfig");
    @Resource
    private BaseDaoManager<MUserRole> mRoleManager;
    @Resource
    private BaseDaoManager<MUserRoleConfig> mRoleUserManager;
    @Resource
    private ICacheService cacheService;


    @Override
    public List<VMRoleUserConfig> findRoleByUser(String id) {
        ArrayList<Object> list = new ArrayList<>();
        String hql = "select new com.job.manager.vo.VMRoleUserConfig(b.id,a.id,a.roleName,a.inputTime) " +
                "from MUserRole a,MUserRoleConfig b where a.id=b.roleId and b.userId=? " +
                "and a.isValidate=1 ";
        list.add(id);
        return (List<VMRoleUserConfig>) mRoleManager.executeQuery(hql + " order by a.roleIndex,b.id", list);
    }


    @Override
    public boolean addRoleUser(String roleId, String[] ids) {
        MUserRoleConfig[] roleConfigs = new MUserRoleConfig[ids.length];
        for (int i = 0; i < ids.length; i++) {
            roleConfigs[i] = new MUserRoleConfig();
            roleConfigs[i].setRoleId(roleId);
            roleConfigs[i].setUserId(ids[i]);
        }
        mRoleUserManager.batchInsert(roleConfigs);
        return true;
    }

    @Override
    public boolean deleteRoleUser(String[] ids) {
        mRoleUserManager.batchDelete(HQLBuilder.DELETE(MUserRoleConfig.class).addInCondition("id", ids));
        return true;
    }

    @Override
    public List<MUserRole> findByType(String type, String value) {
        SearchFilter sf = SearchFilter.getDefault();
        if ("all".equals(type)) {
            sf.addLike("concat(roleName,'-',roleDesc)", value);
        } else {
            sf.addLike(type, value);
        }
        sf.setOrderBy("roleIndex,id desc");
        return mRoleManager.listObjects(sf);
    }

    @Override
    public List<MUserRole> findRoleTreeListByParentId(String id, boolean b) {
        return null;
    }

    @Override
    public boolean add(MUserRole role) {
        role.setRoleIndex(getCurrentIndex());
        return StringHelper.isNotEmpty((String) mRoleManager.insert(role));
    }

    @Override
    public boolean update(MUserRole role) {
        MUserRole oldRole = mRoleManager.getObject(role.getId());
        if (oldRole != null) {
            oldRole.setRoleName(role.getRoleName());
            oldRole.setRoleDesc(role.getRoleDesc());
            oldRole.setRoleIndex(role.getRoleIndex());
            mRoleManager.update(oldRole);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(String[] id) {
        HQLBuilder hqb = HQLBuilder.DELETE(MUserRole.class);
        hqb.addInCondition("id", id);
        mRoleManager.batchDelete(hqb);
        cacheService.clearCached(rb.getString("xmem.roleChannelAuth.key"));
        cacheService.clearCached(rb.getString("xmem.roleFunctionAuth.key"));
        return true;
    }

    @Override
    public List<MUserRole> getAll() {
        SearchFilter sf = SearchFilter.getDefault();
        sf.setOrderBy("roleIndex,id desc");
        return mRoleManager.listObjects(sf);
    }


    @Override
    public List<VMRole> findRoleNotByUser(String id) {
        String hql = null;
        hql = "select new com.job.manager.vo.VMRole(id,roleName,inputTime) " +
                "from MUserRole where id not in(select roleId from MUserRoleConfig where userId=?) " +
                "and isValidate=?";
        ArrayList<Object> list = new ArrayList<>();
        list.add(id);
        list.add(1);
        return (List<VMRole>) mRoleUserManager.executeQuery(hql + " order by id desc", list);

    }

    @Override
    public boolean addRoleByUser(String userId, String[] ids) {
        MUserRoleConfig[] roleConfigs = new MUserRoleConfig[ids.length];
        for (int i = 0; i < ids.length; i++) {
            roleConfigs[i] = new MUserRoleConfig();
            roleConfigs[i].setRoleId(ids[i]);
            roleConfigs[i].setUserId(userId);
        }
        mRoleUserManager.batchInsert(roleConfigs);
        return true;
    }

    @Override
    public void updateIndex(String sId, Integer sIndex, Integer tIndex, String point) {
        Map<String, Object> paramMap = new HashMap<String, Object>();

		/*
         * 将所有大于目标索引的索引+1，排除目标
		 */
        HQLBuilder hqb = HQLBuilder.UPDATE(MUserRole.class);
        hqb.setNewValueField("roleIndex", "roleIndex+1");
		/*
		 * 判断移动后是在目标上方还是下方
		 */
        if (point.equals("top")) {
            //上方：替换目标index，并从目标开始index+1
            hqb.addGreaterThanEquals("roleIndex", tIndex);
            paramMap.put("roleIndex", tIndex);
        } else {
            //上方：替换目标index+1，并从目标后一下开始index+1
            hqb.addGreaterThan("roleIndex", tIndex);
            paramMap.put("roleIndex", tIndex + 1);
        }
        hqb.addNotEqCondition("id", sId);
        mRoleManager.batchUpdate(hqb);
        //更新被移动数据的索引，并判断是否成功
        mRoleManager.update(sId, paramMap);
    }

    private Integer getCurrentIndex(){
        //仅获取一条数据
        SearchFilter sf= SearchFilter.getSearchFilter(1,1);
        sf.setOrderBy("roleIndex");
        MUserRole role=mRoleManager.findFirst(sf);
        /*
         * 如果无查询对象,获取对象的索引为空
         * true,返回0,
         * false,返回当前索引-1
         */
        if (role==null || role.getRoleIndex()==null){
            return 0;
        }else {
            return role.getRoleIndex()-1;
        }

    }

}
