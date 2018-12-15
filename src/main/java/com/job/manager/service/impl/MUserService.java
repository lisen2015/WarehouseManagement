/*
 * FileName：MUserService.java
 *
 * Description： 用户service 实现类
 *
 * History：
 * 版本号 		作者 			日期       			简介
 * 1.0        	chenchen    	2016-07-05  		Create
 * */
package com.job.manager.service.impl;

import com.job.manager.daoManager.BaseDaoManager;
import com.job.manager.daoManager.HQLBuilder;
import com.job.manager.daoManager.PagedList;
import com.job.manager.daoManager.SearchFilter;
import com.job.manager.model.MUser;
import com.job.manager.service.ICodeTableService;
import com.job.manager.service.IMUserService;
import com.job.manager.util.DigestUtil;
import com.job.manager.util.StringHelper;
import com.job.manager.vo.VMUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MUserService implements IMUserService {
    @Resource
    private ICodeTableService codeTableService;
    @Resource
    private BaseDaoManager<MUser> mUserManager;


    @Override
    public MUser add(MUser user) {
        //密码加密
        user.setLoginPassword(encrypt(user.getLoginPassword()));
        String i = (String) mUserManager.insert(user);
        return StringHelper.isNotEmpty(i) ? user:null;
    }

    @Override
    public MUser update(MUser user) {
        MUser oldUser = mUserManager.getObject(user.getId());
        if (oldUser != null) {
            oldUser.setLoginName(user.getLoginName()==null?"":user.getLoginName().trim());
            oldUser.setUserName(user.getUserName()==null?"":user.getUserName().trim());
            oldUser.setEmail(user.getEmail()==null?"":user.getEmail().trim());
            if (user.getIsValidate()!=null) {
                oldUser.setIsValidate(user.getIsValidate());
            }
            mUserManager.update(oldUser);
            return oldUser;
        } else {
            return null;
        }
    }


    @Override
    public boolean delete(String[] id) {
        HQLBuilder hqb= HQLBuilder.LOGICDELLETE(MUser.class);
        hqb.addInCondition("id",id);
        mUserManager.batchDeleteByLogic(hqb);
        return true;
    }

    @Override
    public boolean deleteReal(String[] id) {
        HQLBuilder hqb= HQLBuilder.DELETE(MUser.class);
        hqb.addInCondition("id",id);
        mUserManager.batchDelete(hqb);
        return true;
    }

    @Override
    public boolean batchRecover(String[] id) {
        HQLBuilder hqb= HQLBuilder.UPDATE(MUser.class);
        hqb.setNewValue("isVaildate",1);
        hqb.addInCondition("id",id);
        mUserManager.batchUpdate(hqb);
        return true;
    }

    @Override
    public MUser get(String id) {
        return mUserManager.getObject(id);
    }

    @Override
    public Map<String, Object> findUserNotBy(String table, String id, String value, int pageNo, int pageSize) {
        HashMap<String, Object> map =new HashMap<String	,Object>();
        String hql=null;//查询语句
        String totalHql=null;//查询总数
        String searchHql=" and concat(loginName,'-',userName) like ? ";//搜索语句
        totalHql="select count(*) from MUser where id not in (select userId from MUserRoleConfig where roleId=?) and loginName <>'admin' " +
                    "and isValidate=1";
        hql="select new com.job.manager.vo.VMUser(id,loginName,userName) " +
                    "from MUser where id not in(select userId from MUserRoleConfig where roleId=?) " +
                    "and isValidate=1 and loginName <>'admin'";

        ArrayList<Object> list=new ArrayList<Object>();
        list.add(id);
		/*
		 * 判断是否需要增加搜索语句、参数
		 */
        if (value!=null && value.length()>0) {
            totalHql+=searchHql;
            hql+=searchHql;
            list.add("%"+value+"%");
        }
        hql+=" order by id desc";
        map.put("total", mUserManager.totalByQuery(totalHql,list));
        map.put("rows", mUserManager.executeQuery(hql,(pageNo-1)*pageSize,pageSize ,list));
        return map;
    }

    @Override
    public Map<String, Object> findUserBy(String table, String id, String value, int pageNo, int pageSize) {
        Map<String, Object> map =new HashMap<String	,Object>();
        String hql=null;//查询语句
        String searchHql=" and concat(A.loginName,'-',A.userName) like ? ";//搜索语句
        String totalHql=null;//查询总数
        totalHql="select count(*) from MUser A,MUserRoleConfig " +
                "B where A.id=B.userId and A.loginName <>'admin' and A.isValidate=1 and B.roleId=?";
        hql="select new com.job.manager.vo.VMUser(B.id,A.loginName,A.userName) " +
                "from MUser A,MUserRoleConfig B where A.id=B.userId and A.loginName <>'admin' and A.isValidate=1 and B.roleId=? ";
        ArrayList<Object> list=new ArrayList<Object>();
        list.add(id);
		/*
		 * 判断是否需要增加搜索语句、参数
		 */
        if (value!=null && value.length()>0) {
            totalHql+=searchHql;
            hql+=searchHql;
            list.add("%"+value+"%");
        }
        hql+=" order by B.inputTime desc, A.id desc";
        map.put("total", mUserManager.totalByQuery(totalHql,list));
        map.put("rows", mUserManager.executeQuery(hql,(pageNo-1)*pageSize,pageSize ,list));
        return map;
    }

    @Override
    public MUser loginByName(String loginName, String password) {
        SearchFilter esf= SearchFilter.getDefault();
        esf.addEqCondition("loginName", loginName);
        esf.addEqCondition("loginPassword", encrypt(password));
        return mUserManager.findUnique(esf);
    }

    @Override
    public MUser loginByEmail(String email, String password) {
        return null;
    }

    @Override
    public MUser loginByTel(String tel, String password) {
        return null;
    }

    @Override
    public boolean exist(String field, Object value, String excludedId) {
        SearchFilter esf= SearchFilter.getDefault();
        return mUserManager.exists(field, value, excludedId,esf);
    }

    @Override
    public boolean resetPass(String[] ids) {
        HQLBuilder hqb= HQLBuilder.UPDATE(MUser.class);
        hqb.setNewValue("loginPassword",encrypt(codeTableService.get("1001").getCodeValue1()));
        hqb.addInCondition("id",ids);
        mUserManager.batchUpdate(hqb);
        return true;
    }

    @Override
    public String encrypt(String password) {
        return DigestUtil.md5Hex(password);
    }

    @Override
    public boolean resetPass(String id, String newLoginPassword) {
        HQLBuilder hqb= HQLBuilder.UPDATE(MUser.class);
        hqb.setNewValue("loginPassword",newLoginPassword);
        hqb.addEqCondition("id", id);
        mUserManager.batchUpdate(hqb);
        return true;
    }

    @Override
    public PagedList<VMUser> findByType(String type, String value, int page, int rows, int flag) {
        if (type.equals("all")) {
            SearchFilter sf= SearchFilter.getSearchFilter(page,rows);
            sf.setSelectFields("id,loginName,userName,email,isValidate,inputTime,deleteTime");
            sf.setViewClassType(VMUser.class);
            sf.addEqCondition("isValidate",flag);
            sf.addNotEqCondition("loginName", "admin");
            sf.addLike("concat(loginName,'-',userName,'-',email)",value);
            sf.addLike("loginName",value);
            sf.setOrderBy("inputTime desc,id desc");
            return mUserManager.pagedObjects(sf);
        }else {
            SearchFilter esf= SearchFilter.getSearchFilter(page, rows);
            esf.setSelectFields("id,loginName,userName,email,isValidate,inputTime,deleteTime");
            esf.setViewClassType(VMUser.class);
            //判断查询的是普通用户还是已删除的用户
            esf.addEqCondition("isValidate", flag);
            esf.addLike(type, value);
            esf.addNotEqCondition("loginName", "admin");
            esf.setOrderBy("inputTime desc,id desc");
            return mUserManager.pagedObjects(esf);
        }
    }

    @Override
    public PagedList<VMUser> getallUserByPage(int page, int rows, int flag) {
        SearchFilter esf= SearchFilter.getSearchFilter(page,rows);
        esf.setSelectFields("id,loginName,userName,email,isValidate,inputTime,deleteTime");
        esf.setViewClassType(VMUser.class);
        esf.addNotEqCondition("loginName", "admin");
        //判断查询的是普通用户还是已删除的用户
        if (flag < 999) {
            esf.addEqCondition("isValidate", flag);
        }
        esf.setOrderBy("inputTime desc,id");
        return mUserManager.pagedObjects(esf);
    }

    @Override
    public List<MUser> getall(String value) {
        return null;
    }

    @Override
    public List<MUser> getall(Object[] userList) {
        return null;
    }


    @Override
    public boolean exist(String field, Object value) {
        return mUserManager.exists(field, value, SearchFilter.getDefault());
    }
}
