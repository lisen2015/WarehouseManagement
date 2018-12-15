/*															
 * FileName：BaseDaoManager.java						
 *			
 * Description：用于封装dao操作，提供数据库基础增、删、改、查、分页查询功能、
 * hql查询、sql查询、存储过程执行等功能。并不进行事务操作，事务请自行在service层完成。				
 * 																	
 * History：
 * 版本号 		作者 			日期       			简介
 *  1.0   	    chenchen	    2013-10-24  		Create
 *  2.0   	    chenchen	    2016-07-05  		update 新增逻辑删除、修改多个新增删除方法
 */
package com.job.manager.daoManager;

import com.job.manager.exception.DAOException;
import com.job.manager.util.AssertUtil;
import com.job.manager.util.ProjectContext;
import com.job.manager.util.StringHelper;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.hibernate.transform.Transformers;
import org.hibernate.tuple.entity.EntityMetamodel;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;


/**
 * 用于封装dao操作，提供数据库基础增、删、改、查、分页查询功能、
 * hql查询、sql查询、存储过程执行等功能。并不进行事务操作，事务请自行在service层完成。
 *
 */
public class BaseDaoManager<T> {
    private static final Logger LOG = Logger.getLogger("DaoLogger");
    @Resource
    private SessionFactory sessionFactory;
    private Class<T> classType;


    public BaseDaoManager(Class<T> classType) {
        this.classType = classType;
    }

    /**
     * 用于绑定参数
     *
     * @param query      语句
     * @param parameters 参数
     * @version 2014-11-27	chenchen	create
     */
    @SuppressWarnings("rawtypes")
    static void bindParameters(Query query, List<HQLBuilder.Condition> parameters) {
        for (int i = 0; i < parameters.size(); i++) {
            HQLBuilder.Condition condition = parameters.get(i);
            if (condition.getParameterName() == null) {
                continue;
            }
            if (condition.isNoNeedBindParam()) {
                continue;
            }
            Object value = condition.getValue();
            if (value == null) {
                query.setParameter(condition.getParameterName(), value);
            } else if (value.getClass().isArray())
                query.setParameterList(condition.getParameterName(), (Object[]) value);
            else if (Collection.class.isAssignableFrom(value.getClass()))
                query.setParameterList(condition.getParameterName(), (Collection) value);
            else
                query.setParameter(condition.getParameterName(), value);
        }
    }

    /**
     * 数据库插入对象
     *
     * @param object 需要进行操作的对象
     * @return Serializable 主键对象
     * @eg User user=new User();<br/>
     * user.setName("aaaa");<br/>
     * int id = (Integer)baseDaoManager.insert(user);
     * @version
     * 2013-10-24 	chenchen	create
     * 2016-07-05   chenchen    增加默认插入创建者和修改者
     *
     */
    public Serializable insert(T object) {
        AssertUtil.notNull(object, "cannot insert null object!");
        Serializable serialedId = null;
        QueryTimer timer = new QueryTimer(classType);
        /*
         * 设置用户的创建用户、最后修改用户
         */
        try {
            Method setCreateUser = classType.getMethod("setCreateUser",String.class);
            if (ProjectContext.getUser()!=null){
                setCreateUser.invoke(object, ProjectContext.getUser().getId());
            }else{
                setCreateUser.invoke(object,"6ad47ab04f4c11e6b2be0242ac110003");
            }
        } catch (Exception e) {}
        try {
            Method setLastUpdateUser =  classType.getMethod("setLastUpdateUser",String.class);
            if (ProjectContext.getUser()!=null){
                setLastUpdateUser.invoke(object, ProjectContext.getUser().getId());
            }else{
                setLastUpdateUser.invoke(object,"6ad47ab04f4c11e6b2be0242ac110003");
            }
        } catch (Exception e) {}
        serialedId = sessionFactory.getCurrentSession().save(object);
        timer.stopWatch(object, "insert");
        return serialedId;
    }

    /**
     * 数据库逻辑删除对象
     *
     * @param object 需要进行操作的对象
     * @eg User user=userDaoManager.get(10L);<br/>
     * userDaoManager.delete(user);
     * @version
     * 2016-07-05   chenchen    create
     */
    public void deleteByLogic(T object) {
        if (object == null) {
            throw new DAOException("cannot delete null object!");
        }

        /*
         * 设置最后修改用户
         */
        try {
            Method setLastUpdateUser =  classType.getMethod("setLastUpdateUser",String.class);
            if (ProjectContext.getUser()!=null){
                setLastUpdateUser.invoke(object, ProjectContext.getUser().getId());
            }else{
                setLastUpdateUser.invoke(object,"6ad47ab04f4c11e6b2be0242ac110003");
            }
        } catch (Exception e) {}
        /*
         * 设置用户isValidate=0,
         */
        try {
            Method setIsValidate =  classType.getMethod("setIsValidate",Integer.class);
            setIsValidate.invoke(object,0);
        } catch (Exception e) {}
        /*
         * 设置deleteTime=now
         */
        try {
            Method setDeleteTime =  classType.getMethod("setDeleteTime",Timestamp.class);
            setDeleteTime.invoke(object,new Timestamp(System.currentTimeMillis()));
        } catch (Exception e) {}
        QueryTimer timer = new QueryTimer(classType);
        sessionFactory.getCurrentSession().delete(object);
        timer.stopWatch(object, "delete");
    }
    /**
     * 数据库删除对象
     *
     * @param object 需要进行操作的对象
     * @eg User user=userDaoManager.get(10L);<br/>
     * userDaoManager.delete(user);
     * @version 2013-10-24	chenchen	create
     */
    public void delete(T object) {
        if (object == null) {
            throw new DAOException("cannot delete null object!");
        }
        QueryTimer timer = new QueryTimer(classType);
        sessionFactory.getCurrentSession().delete(object);
        timer.stopWatch(object, "delete");
    }

    /**
     * 用于数据库更新对象
     *
     * @param object 需要进行操作的对象
     * @eg User user=userDaoManager.get(10L);<br/>
     * user.serLoginName("xxx");<br/>
     * userDaoManager.update(user);
     * @version
     * 2013-10-24 	chenchen	create
     * 2016-07-05   chenchen    增加默认插入修改者 修改时间
     */
    public void update(T object) {
        if (object == null) {
            throw new DAOException("cannot update null object!");
        }
        /*
         * 设置updateTime=now、最后修改用户
         */
        try {
            Method setLastUpdateUser =  classType.getMethod("setLastUpdateUser",String.class);
            if (ProjectContext.getUser()!=null){
                setLastUpdateUser.invoke(object, ProjectContext.getUser().getId());
            }else{
                setLastUpdateUser.invoke(object,"6ad47ab04f4c11e6b2be0242ac110003");
            }
        } catch (Exception e) {}
         /*
         * 设置updateTime=now、最后修改用户
         */
        try {
            Method setUpdateTime =  classType.getMethod("setUpdateTime",Timestamp.class);
            setUpdateTime.invoke(object,new Timestamp(System.currentTimeMillis()));
        } catch (Exception e) {}
        QueryTimer timer = new QueryTimer(classType);
        sessionFactory.getCurrentSession().update(object);
        timer.stopWatch(object, "saveOrUpdate");
    }

    /**
     * 数据库更新对象
     *
     * @param id            更新对象的id
     * @param updatedFields 更新对象map
     * @return int 受影响的行数
     * @eg Map<String, Object> updatedFields =new HashMap<String, Object>();<br/>
     * updatedFields.put("loginName", "aaa");<br/>
     * updatedFields.put("inputTime", new Timestamp(System.currentTimeMillis()));<br/>
     * userManager.update(1001L, updatedFields);<br/>
     * @version
     * 2013-10-24  chenchen	create
     * 2016-07-05   chenchen    增加默认插入创建者和修改者
     */
    public int update(Object id, Map<String, Object> updatedFields) {
        if ((updatedFields == null) || (updatedFields.size() == 0)) {
            return 0;
        }
        HQLBuilder hqlBuilder = HQLBuilder.UPDATE(classType);
        for (String field : updatedFields.keySet()) {
            hqlBuilder.setNewValue(field, updatedFields.get(field));
        }
        /*
         置updateTime=now、最后修改用户
         */
        try {
            classType.getMethod("setLastUpdateUser",String.class);
            if (ProjectContext.getUser()!=null){
                hqlBuilder.setNewValue("lastUpdateUser", ProjectContext.getUser().getId());
            }else{
                hqlBuilder.setNewValue("lastUpdateUser", "6ad47ab04f4c11e6b2be0242ac110003");
            }
        } catch (Exception e) {}
        /*
         置updateTime=now、最后修改用户
         */
        try {
            classType.getMethod("setUpdateTime",Timestamp.class);
            hqlBuilder.setNewValue("updateTime", new Timestamp(System.currentTimeMillis()));
        } catch (Exception e) {}
        hqlBuilder.addEqCondition("id", id);
        return batchWithBuilder(hqlBuilder);
    }

    /**
     * 用于数据库更新对象
     *
     * @param id    主键对象
     * @param field 字段名称
     * @param value 更新值
     * @return int 受影响的行数
     * @eg userManager.update(1001L, "loginName", "aaa");
     * @version 2013-10-25 	chenchen	create
     */
    public int update(Object id, String field, Object value) {
        Map<String, Object> updatedFields = new HashMap<String, Object>();
        updatedFields.put(field, value);
        return update(id, updatedFields);
    }

    /**
     * 获取数据库对象
     *
     * @param objectId 主键ID（可序列化）
     * @return T 对象
     * @eg User user=mUserManager.getObject(1001L);
     * @version 2013-10-25 	chenchen	create
     */
    @SuppressWarnings("unchecked")
    public T getObject(Object objectId) {
        if (objectId == null) {
            return null;
        }
        QueryTimer timer = new QueryTimer(classType);
        try {
            return (T) sessionFactory.getCurrentSession().get(classType, (Serializable) objectId);
        } finally {
            timer.stopWatch(objectId);
        }
    }

    /**
     * 获取数据对象集合
     *
     * @return List<T>
     * @eg List<User> list=userManager.listObjects();
     * @version 2013-10-24 	chenchen	create
     */
    public List<T> listObjects() {
        SearchFilter sf = SearchFilter.getNoPagedFilter();
        return listObjects(sf);
    }

    /**
     * 根据字段内容获取数据对象集合
     *
     * @param fieldName 条件字段名称
     * @param value     条件字段值
     * @return List<T>
     * @eg userManager.listObjects("loginName","aaa");<br/>
     * 等同于"select * from User where loginName='aaa'"
     * @version 2013-10-24 	chenchen	create
     */
    public List<T> listObjects(String fieldName, Object value) {
        SearchFilter sf = SearchFilter.getNoPagedFilter();
        sf.addEqCondition(fieldName, value);
        return listObjects(sf);
    }

    /**
     * 据自定义筛选器获取数据对象集合
     *
     * @param sf 筛选器
     * @return List<T>
     * @eg SearchFilter sf=SearchFilter.getDefault();<br/>
     * sf.addEqCondition("loginName", "aaa");<br/>
     * List<User> list=userManager.listObjects(sf);<br/>
     * @version 2013-10-24	chenchen	create
     */
    @SuppressWarnings("unchecked")
    public List<T> listObjects(SearchFilter sf) {
        StringBuilder sb = buildFromAndWhere(sf);
        sb = appendOrderbyClause(sf, sb);
        Query query = null;
        QueryTimer timer = new QueryTimer(classType);
        query = sessionFactory.getCurrentSession().createQuery(sb.toString());
        HqlGenerator.bindParameters(query, sf);
        if (sf.getMaxResults() > 0) {
            query.setMaxResults(sf.getMaxResults());
        }
        List<T> list = query.list();
        timer.stopWatch(sf);
        return list;
    }

    /**
     * 根据自定义视图对象返回筛选器获取数据对象集合
     *
     * @param sf 筛选器
     * @return List
     * @eg SearchFilter sf=SearchFilter.getDefault();<br/>
     * sf.setViewClassType(VMUser.class);<br/>
     * sf.addEqCondition("loginName", "aaa");<br/>
     * List<VMUser> list=userManager.listObjectsByView(sf);<br/>
     * @version 2013-10-24 	chenchen	create
     */
    public List<?> listObjectsByView(SearchFilter sf) {
        StringBuilder sb = buildFromAndWhere(sf);
        sb = appendOrderbyClause(sf, sb);
        Query query = null;
        QueryTimer timer = new QueryTimer(classType);
        query = sessionFactory.getCurrentSession().createQuery(sb.toString());
        HqlGenerator.bindParameters(query, sf);
        if (sf.getMaxResults() > 0) {
            query.setMaxResults(sf.getMaxResults());
        }
        List<?> list = query.list();
        timer.stopWatch(sf);
        return list;
    }

    /**
     * 通过分页对象返回所有数据（无条件、无分页）
     *
     * @return PagedList
     * @eg PagedList<User> list=userManager.pagedAll();
     * @version 2013-10-24 	chenchen	create
     */
    @SuppressWarnings("rawtypes")
    public PagedList pagedAll() {
        return pagedObjects(SearchFilter.getDefault());
    }

    /**
     * 返回分页数据
     *
     * @param sf 筛选器
     * @return PagedList
     * @eg SearchFilter sf=SearchFilter.getSearchFilter(1, 10);<br/>
     * PagedList<User> list=userManager.pagedObjects(sf);<br/>
     * 等同于Select * from User,并获取第1条-第10条数据
     * @version 2013-10-24 	chenchen	create
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public PagedList pagedObjects(SearchFilter sf) {
        QueryTimer timer = new QueryTimer(classType);
        try {
            StringBuilder sb = buildFromAndWhere(sf);
            LOG.debug("query count for " + classType + " [" + sb + "]");
            int total = total(sf);
            sb = appendOrderbyClause(sf, sb);
            Query query = sessionFactory.getCurrentSession().createQuery(sb.toString());
            HqlGenerator.bindParameters(query, sf);
            query.setFirstResult(sf.getStartPos());
            if (sf.getMaxResults() > 0) {
                query.setMaxResults(sf.getMaxResults());
            }
            LOG.debug("query " + classType + " [" + sb + "]");
            return new PagedList(query.list(), sf.getStartPos()
                    / sf.getMaxResults(), sf.getMaxResults(), total);
        } finally {
            timer.stopWatch();
        }
    }

    /**
     * 回分页数据
     *
     * @param hql       hql语句
     * @param startPos  返回的第一条记录位置
     * @param maxResult 最大返回结果数
     * @param paramList  参数List
     * @return PagedList
     * @eg String hql="from User where loginName=?";<br/>
     * List<Object> paramList=new ArrayList<Object>();<br/>
     * list.add("aaa");<br/>
     * PagedList<User> list=userManager.pagedObjects(hql,0,10,paramList);<br/>
     * 等同于Select * from User where loginName='aaa',并获取第1条-第10条数据
     * @version 2013-10-24 	chenchen	create
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public PagedList pagedObjects(String hql, int startPos, int maxResult, List<? extends Object> paramList) {
        QueryTimer timer = new QueryTimer(classType);
        try {
            int total = totalByQuery(hql, paramList);
            return new PagedList(executeQuery(hql, startPos, maxResult, paramList), startPos
                    / maxResult, maxResult, total);
        } finally {
            timer.stopWatch();
        }
    }

    /**
     * 返回分页数据
     *
     * @param hql       hql语句
     * @param startPos  返回的第一条记录位置
     * @param maxResult 最大返回结果数
     * @param paramMap  参数Map
     * @return PagedList
     * @eg String hql="from User where loginName=:loginName";<br/>
     * Map<String,Object> paramMap=new HashMap<String,Object>();<br/>
     * paramMap.put("loginName","aaa");<br/>
     * PagedList<User> list=userManager.pagedObjects(hql,0,10,paramMap);<br/>
     * 等同于Select * from User where loginName='aaa',并获取第1条-第10条数据
     * @version 2013-10-24 	chenchen	create
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public PagedList pagedObjects(String hql, int startPos, int maxResult, Map<String, ?> paramMap) {
        QueryTimer timer = new QueryTimer(classType);
        try {
            int total = totalByQuery(hql, paramMap);
            return new PagedList(executeQueryMapByMap(hql, startPos, maxResult, paramMap), startPos
                    / maxResult, maxResult, total);
        } finally {
            timer.stopWatch();
        }
    }

    /**
     * 批量新增
     *
     * @param objects
     * @return
     * @version
     * 2013-10-24	chenchen	create
     * 2016-07-05   chenchen    update 新增默认插入 createuser lastupdateuser
     */
    public void batchInsert(T[] objects) {
        if (objects == null) {
            return ;
        }
        Arrays.stream(objects).forEach(o -> {
            /*
             * 设置用户的创建用户、最后修改用户
             */
            try {
                Method setCreateUser = classType.getMethod("setCreateUser",String.class);
                if (ProjectContext.getUser()!=null){
                    setCreateUser.invoke(o, ProjectContext.getUser().getId());
                }else{
                    setCreateUser.invoke(o,"6ad47ab04f4c11e6b2be0242ac110003");
                }
            } catch (Exception e) {
            }
            /*
             * 设置用户的创建用户、最后修改用户
             */
            try {
                Method setLastUpdateUser =  classType.getMethod("setLastUpdateUser",String.class);
                if (ProjectContext.getUser()!=null){
                    setLastUpdateUser.invoke(o, ProjectContext.getUser().getId());
                }else{
                    setLastUpdateUser.invoke(o,"6ad47ab04f4c11e6b2be0242ac110003");
                }
            } catch (Exception e) {
            }
            sessionFactory.getCurrentSession().save(o);
        });
    }

    /**
     * 批量更新
     *
     * @param objects
     * @return int
     * @version
     * 2013-10-24	chenchen	create
     * 2016-07-05   chenchen    update 新增默认插入 createuser lastupdateuser
     */
    public void batchInsert(List<T> objects) {
        if (objects == null) {
            return;
        }
        objects.stream().forEach(o -> {
            /*
             * 设置用户的创建用户、最后修改用户
             */
            try {
                Method setCreateUser = classType.getMethod("setCreateUser",String.class);
                if (ProjectContext.getUser()!=null){
                    setCreateUser.invoke(o, ProjectContext.getUser().getId());
                }else{
                    setCreateUser.invoke(o,"6ad47ab04f4c11e6b2be0242ac110003");
                }
            } catch (Exception e) {}
             /*
             * 设置用户的创建用户、最后修改用户
             */
            try {
                Method setLastUpdateUser =  classType.getMethod("setLastUpdateUser",String.class);
                if (ProjectContext.getUser()!=null){
                    setLastUpdateUser.invoke(o, ProjectContext.getUser().getId());
                }else{
                    setLastUpdateUser.invoke(o,"6ad47ab04f4c11e6b2be0242ac110003");
                }
            } catch (Exception e) {}



            sessionFactory.getCurrentSession().save(o);
        });
    }

    /**
     * 根据条件批量更新
     *
     * @param builder
     * @return int
     * @eg HQLBuilder builder2=HQLBuilder.UPDATE(Object.class);<br/>
     * builder.setNewValue("id","10"); <br/>
     * builder.setNewValue("value","xxxx");<br/>
     * builder.addEqCondition("id", "11"); <br/>
     * batchUpdate(builder);<br/>
     * 等同于"update xxx set id=10,value='xxxx' where id=11"
     * @version 2013-10-24	chenchen	create
     */
    public void batchUpdate(HQLBuilder builder) {
        /*
         * 设置最后修改用户,修改时间
         */
        try {
            classType.getMethod("setUpdateTime", Timestamp.class);
            builder.setNewValue("updateTime",new Timestamp(System.currentTimeMillis()));
        } catch (Exception e) {}
        /*
         * 设置最后修改用户,修改时间
         */
        try {
             classType.getMethod("setLastUpdateUser",String.class);
            if (ProjectContext.getUser()!=null){
                builder.setNewValue("lastUpdateUser", ProjectContext.getUser().getId());
            }else{
                builder.setNewValue("lastUpdateUser","6ad47ab04f4c11e6b2be0242ac110003");
            }
        } catch (Exception e) {}
         batchWithBuilder(builder);
    }

    /**
     * 根据条件对数据进行批量删除
     *
     * @param builder
     * @return int
     * @eg HQLBuilder builder2=HQLBuilder.DELETE(Object.class);<br/>
     * builder.addEqCondition("id", "10");<br/>
     * batchUpdate(builder);<br/>
     * 等同于"delete xxx where id=10"
     * @version 2013-10-24	chenchen	create
     */
    public void batchDelete(HQLBuilder builder) {
         batchWithBuilder(builder);
    }
    /**
     * 根据条件对数据进行批量逻辑删除
     *
     * @param builder
     * @return int
     * @eg HQLBuilder builder2=HQLBuilder.DELETE(Object.class);<br/>
     * builder.addEqCondition("id", "10");<br/>
     * batchUpdate(builder);<br/>
     * 等同于"delete xxx where id=10"
     * @version
     * 2016-07-05	chenchen	create
     */
    public void batchDeleteByLogic(HQLBuilder builder) {
        batchWithBuilder(builder);
    }
    /**
     * 对数据进行批量删除
     *
     * @param objects
     * @return int
     * @version 2013-10-24	chenchen	create
     */
    public void batchDeleteByLogic(List<T> objects) {
        objects.stream().forEach(o->{
            /*
             * 通过获取 setIsVaildate setDeleteTime setLastUpdateUser处理是否可以逻辑删除
             */
            try {
                Method setDeleteTime=classType.getMethod("setDeleteTime", Timestamp.class);
                setDeleteTime.invoke(o,new Timestamp(System.currentTimeMillis()));
            } catch (Exception e) {}
             /*
             * 通过获取 setIsVaildate setDeleteTime setLastUpdateUser处理是否可以逻辑删除
             */
            try {
                Method setIsValidate=classType.getMethod("setIsValidate", Integer.class);
                setIsValidate.invoke(o,0);
            } catch (Exception e) {}
             /*
             * 通过获取 setIsVaildate setDeleteTime setLastUpdateUser处理是否可以逻辑删除
             */
            try {
                Method setLastUpdateUser=classType.getMethod("setLastUpdateUser",String.class);
                if (ProjectContext.getUser()!=null){
                    setLastUpdateUser.invoke(o, ProjectContext.getUser().getId());
                }else{
                    setLastUpdateUser.invoke(o, "6ad47ab04f4c11e6b2be0242ac110003");
                }
            } catch (Exception e) {}
            sessionFactory.getCurrentSession().save(o);
        });
    }
    /**
     * 对数据进行批量删除
     *
     * @param objects
     * @return int
     * @version 2013-10-24	chenchen	create
     */
    public void batchDelete(List<T> objects) {
        if (objects == null) {
            return ;
        }
        objects.stream().forEach(o->sessionFactory.getCurrentSession().delete(o));
    }

    /**
     * 对数据进行批量更新
     *
     * @param objects
     * @version 2013-10-24	chenchen	create
     */
    public void batchUpdate(T[] objects) {
        batchUpdate(Arrays.asList(objects));
    }

    /**
     * 对数据进行批量更新
     *
     * @param objects
     * @version
     * 2013-10-24 chenchen create
     * 2016-07-06   chenchen    update 增加默认插入修改时间、修改用户
     */
    public void batchUpdate(List<T> objects) {
        if (objects == null) {
            throw new DAOException("cannot update null object!");
        }
        QueryTimer timer = new QueryTimer(classType);

        objects.stream().forEach(o-> {
            /*
             * 通过获取  setUpdateTime处理是否可以逻辑删除
             */
            try {
                Method setUpdateTime = classType.getMethod("setUpdateTime", Timestamp.class);
                setUpdateTime.invoke(o, new Timestamp(System.currentTimeMillis()));
            } catch (Exception e) {
            }
            /*
             * 通过获取 setLastUpdateUser处理是否可以逻辑删除
             */
            try {
                Method setLastUpdateUser = classType.getMethod("setLastUpdateUser", String.class);
                if (ProjectContext.getUser() != null) {
                    setLastUpdateUser.invoke(o, ProjectContext.getUser().getId());
                } else {
                    setLastUpdateUser.invoke(o, "");
                }
            } catch (Exception e) {
            }
            /*
             * 通过获取  getCreateUser判断当前是否已经插入 createuser,如果没有则插入
             */
            try {
                Method getCreateUser = classType.getMethod("getCreateUser");
                if (getCreateUser.invoke(o)==null){
                    Method setCreateUser = classType.getMethod("setCreateUser",String.class);
                    if (ProjectContext.getUser() != null) {
                        setCreateUser.invoke(o, ProjectContext.getUser().getId());
                    } else {
                        setCreateUser.invoke(o, "6ad47ab04f4c11e6b2be0242ac110003");
                    }
                }
            } catch (Exception e) {
            }
            sessionFactory.getCurrentSession().saveOrUpdate(o);
        });

        timer.stopWatch(objects, "batchUpdate");
    }

    /**
     * 执行TRUNCATE操作
     *
     * @return int
     * @version 2013-10-24	chenchen	create
     */
    public int truncate() {
        return executeNativeUpdateSQL("TRUNCATE TABLE " + getTableName());
    }

    /**
     * 根据旧数据更新新数据
     *
     * @param field    字段名称
     * @param oldValue 旧值
     * @param newValue 新值
     * @return int 受影响的记录条数
     * @eg bulkUpdate("loginName","aaa","bbb");
     * @version 2013-10-24	chenchen	create
     */
    public void bulkUpdate(String field, Object oldValue, Object newValue) {
        HQLBuilder hqlBuilder = HQLBuilder.UPDATE(classType);
        hqlBuilder.setNewValue(field, newValue).addEqCondition(field, oldValue);
        batchUpdate(hqlBuilder);
    }

    String getGenericClassName() {
        return classType == null ? "Unknown" : classType.getName();
    }

    /**
     * 根据筛选器对数据进行删除
     *
     * @param searchFilter 筛选器
     * @return int 受影响的记录条数
     * @eg SearchFilter sf=SearchFilter.getDefault();
     * sf.addEqCondition("loginName","aaa");
     * userDaoManager.delete(sf);
     * @version 2013-10-24	chenchen	create
     */
    public int delete(SearchFilter searchFilter) {
        QueryTimer timer = new QueryTimer(classType);
        StringBuilder sb = new StringBuilder("delete ");
        sb.append(buildFromAndWhere(searchFilter));
        Query query = sessionFactory.getCurrentSession().createQuery(sb.toString());
        HqlGenerator.bindParameters(query, searchFilter);
        int count = query.executeUpdate();
        timer.stopWatch(searchFilter);
        return count;
    }

    /**
     * 根据sql语句更新数据
     *
     * @param sql
     * @return
     * @version 2013-10-24	chenchen	create
     */
    public int executeNativeUpdateSQL(String sql) {
        QueryTimer timer = new QueryTimer(classType);
        int result = 0;
        result = sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
        timer.stopWatch(sql);
        return result;
    }

    /**
     * 根据sql语句分页查询数据
     *
     * @param sql       sql语句
     * @param startPos  开始查询的记录索引
     * @param maxResult maxResult
     * @param paramlist 参数集合
     * @return
     * @version 2013-10-24 	chenchen	create
     */
    public List<? extends Object> executeNativeQuerySQL(String sql,
                                                        int startPos, int maxResult, List<? extends Object> paramlist) {
        if (startPos < 0) {
            startPos = 0;
        }
        QueryTimer timer = new QueryTimer(classType);
        SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
        if (maxResult > 0) {
            query.setMaxResults(maxResult);
            query.setFirstResult(startPos);
        }
        if (paramlist != null && paramlist.size() > 0) {
            for (int i = 0; i < paramlist.size(); i++) {
                query.setParameter(i, paramlist.get(i));
            }
        }
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        timer.stopWatch(sql);
        return query.list();
    }
    /**
     * 根据sql语句分页查询数据
     *
     * @param sql       sql语句
     * @param startPos  开始查询的记录索引
     * @param maxResult maxResult
     * @param paramMap 参数集合
     * @return
     * @version 2013-10-24 	chenchen	create
     */
    public List<? extends Object> executeNativeQuerySQL(String sql,
                                                        int startPos, int maxResult,Map<String,?> paramMap) {
        if (startPos < 0) {
            startPos = 0;
        }
        QueryTimer timer = new QueryTimer(classType);
        SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
        if (maxResult > 0) {
            query.setMaxResults(maxResult);
            query.setFirstResult(startPos);
        }

        if (paramMap != null && paramMap.size() > 0) {
            paramMap.forEach((s, o) ->{
                if (o instanceof  Object[]){
                    query.setParameterList(s, (Object[]) o);
                }else {
                    query.setParameter(s,o);
                }
            } );
        }
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        timer.stopWatch(sql);
        return query.list();
    }
    /**
     * 根据sql语句查询数据
     *
     * @param sql       sql语句
     * @param paramlist 参数几何
     * @return List<? extends Object>
     * @version 2013-10-24 	chenchen	create
     */
    public List<? extends Object> executeNativeQuerySQL(String sql,
                                                        List<? extends Object> paramlist) {
        return executeNativeQuerySQL(sql, 0, -1, paramlist);
    }
    /**
     * 根据sql语句查询数据
     *
     * @param sql       sql语句
     * @param paramMap 参数几何
     * @return List<? extends Object>
     * @version 2013-10-24 	chenchen	create
     */
    public List<? extends Object> executeNativeQuerySQL(String sql,Map<String,?> paramMap) {
        return executeNativeQuerySQL(sql, 0, -1, paramMap);
    }

    /**
     * 根据sql语句查询数据
     *
     * @param sql sql语句
     * @return List<? extends Object>
     * @version 2013-10-24 	chenchen	create
     */
    public List<? extends Object> executeNativeQuerySQL(String sql) {
        return executeNativeQuerySQL(sql, 0, -1, new ArrayList<>());
    }


    /**
     * 执行多条sql语句
     *
     * @param statements sql语句集合
     * @return int 受影响的记录条数
     * @version 2013-10-24 	chenchen	create
     */
    public int executeNativeUpdateSQL(String[] statements) {
        int result = 0;
        for (int i = 0; i < statements.length; i++) {
            result += sessionFactory.getCurrentSession().createSQLQuery(statements[i]).executeUpdate();
        }
        return result;
    }

    /**
     * 批量操作
     *
     * @param builder hql构建器
     * @return int 受影响的记录条数
     * @version 2013-10-24 	chenchen	create
     */
    private int batchWithBuilder(HQLBuilder builder) {
        QueryTimer timer = new QueryTimer(classType);
        String preparedHQL = "";
        preparedHQL = builder.getPreparedHQL();
        Query query = sessionFactory.getCurrentSession().createQuery(preparedHQL);
        bindParameters(query, builder.getParameterValues());
        LOG.debug("query: " + query.getQueryString() + "; params: "
                + Arrays.toString(query.getNamedParameters()));
        int count = query.executeUpdate();
        timer.stopWatch(preparedHQL);
        return count;
    }

    /**
     * 返回所查询记录的第一个匹配结果
     *
     * @param field 要判断的记录字段
     * @param value 内容
     * @return
     * @version 2013-10-24 	chenchen	create
     */
    public T findFirst(String field, Object value) {
        List<T> objects = listObjects(field, value);
        if (objects.size() == 0) {
            return null;
        }
        return objects.get(0);
    }

    /**
     * 根据筛选器返回唯一记录（如果记录不唯一将返回null）
     *
     * @param sf
     * @return
     * @version 2013-10-24 	chenchen	create
     */
    @SuppressWarnings("unchecked")
    public T findUnique(SearchFilter sf) {
        if (sf == null) {
            sf = SearchFilter.getDefault();
        }
        PagedList<T> entities = pagedObjects(sf);
        if (entities.getRows().size() > 1) {
            return null;
        }
        return entities.getRows().size() > 0 ? entities.get(0) : null;
    }

    /**
     * 返回所查询记录的第一个匹配结果
     *
     * @param sf
     * @return
     * @version 2013-10-24 	chenchen	create
     */
    @SuppressWarnings("unchecked")
    public T findFirst(SearchFilter sf) {
        if (sf == null) {
            sf = SearchFilter.getDefault();
        }
        PagedList<T> entities = pagedObjects(sf);
        return entities.getRows().size() > 0 ? entities.get(0) : null;
    }

    /**
     * 判断数据是否存在
     *
     * @param field 判断字段名称
     * @param value 数据值
     * @param sf    额外的条件
     * @return
     * @version 2013-10-24 	chenchen	create
     */
    public boolean exists(String field, Object value, SearchFilter sf) {
        return exists(field, value, "-1", sf);
    }

    /**
     * 判断记录除本身以外是否存在
     *
     * @param field      要判断的记录字段名称
     * @param value      数据值
     * @param excludedId 例外id
     * @param sf
     * @return
     * @version 2013-10-24 	chenchen	create
     */
    public boolean exists(String field, Object value, Object excludedId, SearchFilter sf) {
        sf.addEqCondition(field, value);
        sf.addNotEqCondition("id", excludedId);
        return total(sf) != 0;
    }

    /**
     * 通过hql查询数据
     *
     * @param hql
     * @return List<? extends Object>
     * @version 2013-10-24 	chenchen	create
     */
    public List<? extends Object> executeQuery(String hql) {
        return executeQuery(hql, 0, -1);
    }

    /**
     * 通过hql查询数据 ,返回结果集为Map，key=字段名称
     *
     * @param hql
     * @return List<Map<String, Object>>
     * @eg String hql= "select loginName from User";
     * List<Map<String,Object>> list=userDaoManager.executeQueryMap(hql);
     * for(Map<String,Object> map:list){
     * map.get("loginName);
     * }
     * @version 2013-10-24 	chenchen	create
     */
    public List<Map<String, Object>> executeQueryMap(String hql) {
        return executeQueryMap(hql, 0, -1);
    }

    /**
     * 通过hql查询数据
     *
     * @param hql
     * @param paramList 参数集合
     * @return List<? extends Object>
     * @version 2013-10-24 	chenchen	create
     */
    public List<? extends Object> executeQuery(String hql, List<? extends Object> paramList) {
        return executeQuery(hql, 0, -1, paramList);
    }

    /**
     * 通过hql查询数据 ,返回结果集为Map，key=字段名称
     *
     * @param hql
     * @param paramList 参数集合
     * @return List<Map<String, Object>>
     * @eg String hql= "select loginName from User where sex=?";
     * List<Object> paramList=new ArrayList<Object>();
     * paramList.add(1);
     * List<Map<String,Object>> list=userDaoManager.executeQueryMap(hql,paramList);
     * for(Map<String,Object> map:list){
     * map.get("loginName);
     * }
     * @version 2013-10-24 	chenchen	create
     */
    public List<Map<String, Object>> executeQueryMap(String hql, List<? extends Object> paramList) {
        return executeQueryMap(hql, 0, -1, paramList);
    }

    /**
     * 通过hql查询数据 ,返回结果集为Map，key=字段名称
     *
     * @param hql
     * @param startPos  记录开始索引
     * @param maxResult 最大返回记录数
     * @return List<Map<String, Object>>
     * @version 2013-10-24 	chenchen	create
     */
    public List<? extends Object> executeQuery(String hql, int startPos,
                                               int maxResult) {
        return executeQuery(hql, startPos, maxResult, null);
    }

    /**
     * 通过hql查询数据 ,返回结果集为Map，key=字段名称
     *
     * @param hql
     * @param startPos  记录开始索引
     * @param maxResult 最大返回记录数
     * @return List<Map<String, Object>>
     * @eg String hql= "select loginName from ";
     * List<Map<String,Object>> list=userDaoManager.executeQueryMap(hql,0,10);
     * for(Map<String,Object> map:list){
     * map.get("loginName);
     * }
     * @version 2013-10-24 	chenchen	create
     */
    public List<Map<String, Object>> executeQueryMap(String hql, int startPos,
                                                     int maxResult) {
        return executeQueryMap(hql, startPos, maxResult, null);
    }

    /**
     * 通过hql查询数据
     *
     * @param hql
     * @param startPos  记录开始索引
     * @param maxResult 最大返回记录数
     * @param paramList 参数集合
     * @return List<Map<String, Object>>
     * @version 2013-10-24 	chenchen	create
     */
    @SuppressWarnings("unchecked")
    public List<? extends Object> executeQuery(String hql, int startPos,
                                               int maxResult, List<? extends Object> paramList) {
        if (startPos < 0) {
            startPos = 0;
        }
        QueryTimer timer = new QueryTimer(classType);
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);
            query.setMaxResults(maxResult);
            query.setFirstResult(startPos);
            if (paramList != null && paramList.size() > 0) {
                for (int i = 0; i < paramList.size(); i++) {
                    query.setParameter(i, paramList.get(i));
                }
            }
            return query.list();
        } finally {
            timer.stopWatch(hql);
        }
    }

    /**
     * 通过hql查询数据 ,返回结果集为Map，key=字段名称
     *
     * @param hql
     * @param startPos  记录索引
     * @param startPos  记录开始索引
     * @param maxResult 最大返回记录数
     * @return List<Map<String, Object>>
     * @eg String hql= "select loginName from User where sex=?";
     * List<Object> paramList=new ArrayList<Object>();
     * paramList.add(1);
     * List<Map<String,Object>> list=userDaoManager.executeQueryMap(hql,0,10,paramList);
     * for(Map<String,Object> map:list){
     * map.get("loginName);
     * }
     * @version 2013-10-24 	chenchen	create
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> executeQueryMap(String hql, int startPos,
                                                     int maxResult, List<? extends Object> paramList) {
        if (startPos < 0) {
            startPos = 0;
        }
        QueryTimer timer = new QueryTimer(classType);
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);
            query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            query.setMaxResults(maxResult);
            query.setFirstResult(startPos);
            if (paramList != null && paramList.size() > 0) {
                for (int i = 0; i < paramList.size(); i++) {
                    query.setParameter(i, paramList.get(i));
                }
            }
            return query.list();
        } finally {
            timer.stopWatch(hql);
        }
    }

    /**
     * 通过hql查询数据
     *
     * @param hql
     * @param paramMap 参数map
     * @return List<? extends Object>
     * @eg String hql="select * from User where loginName=:name";
     * Map<String,Object> paramMap=new HashMap<String,Object>();
     * paramMap.put("name","aaa");
     * List<Object> list=userDaoManager.executeQueryByMap(hql,paramMap);
     * @version 2013-10-24 	chenchen	create
     */
    public List<? extends Object> executeQueryByMap(String hql, Map<String, ?> paramMap) {
        return executeQueryByMap(hql, 0, -1, paramMap);
    }

    /**
     * 通过hql查询数据
     *
     * @param hql
     * @param startPos  记录索引
     * @param maxResult 最大记录数
     * @param paramMap  参数集合
     * @return
     * @eg String hql="select * from User where loginName=:name";
     * Map<String,Object> paramMap=new HashMap<String,Object>();
     * paramMap.put("name","aaa");
     * List<User> list=userDaoManager.executeQueryByMap(hql,0,10,paramMap);
     * @version 2013-10-24  	chenchen	create
     */
    @SuppressWarnings("unchecked")
    public List<? extends Object> executeQueryByMap(String hql, int startPos,
                                                    int maxResult, Map<String, ?> paramMap) {
        if (startPos < 0) {
            startPos = 0;
        }
        QueryTimer timer = new QueryTimer(classType);
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);
            query.setMaxResults(maxResult);
            query.setFirstResult(startPos);
            for (String key : paramMap.keySet()) {
                if (paramMap.get(key) instanceof Collection<?>) {
                    query.setParameterList(key, (Collection<?>) paramMap.get(key));
                } else if (paramMap.get(key) instanceof Object[]) {
                    query.setParameterList(key, (Object[]) paramMap.get(key));
                } else {
                    query.setParameter(key, paramMap.get(key));
                }
            }
            return query.list();
        } finally {
            timer.stopWatch(hql);
        }
    }

    /**
     * 通过hql查询数据 ,返回结果集为Map，key=字段名称
     *
     * @param hql
     * @param paramMap 参数集合
     * @return List<Map<String, Object>>
     * @eg String hql="select loginName from User where loginName=:name";
     * Map<String,Object> paramMap=new HashMap<String,Object>();
     * paramMap.put("name","aaa");
     * List<Map<String, Object>> list=userDaoManager.executeQueryByMap(hql,paramMap);
     * for(Map<String, Object> map:list){
     * map.get("loginName");
     * }
     * @version 2013-10-24  	chenchen	create
     */
    public List<Map<String, Object>> executeQueryMapByMap(String hql, Map<String, ?> paramMap) {
        return executeQueryMapByMap(hql, 0, -1, paramMap);
    }

    /**
     * 通过hql查询分页数据 ,,返回结果集为Map，key=字段名称
     *
     * @param hql
     * @param paramMap 参数集合
     * @return List<Map<String, Object>>
     * @eg String hql="select loginName from User where loginName=:name";
     * Map<String,Object> paramMap=new HashMap<String,Object>();
     * paramMap.put("name","aaa");
     * List<Map<String, Object>> list=userDaoManager.executeQueryByMap(hql,0,10,paramMap);
     * for(Map<String, Object> map:list){
     * map.get("loginName");
     * }
     * @version 2013-10-24  	chenchen	create
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> executeQueryMapByMap(String hql, int startPos,
                                                          int maxResult, Map<String, ?> paramMap) {
        if (startPos < 0) {
            startPos = 0;
        }
        QueryTimer timer = new QueryTimer(classType);
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);
            query.setMaxResults(maxResult);
            query.setFirstResult(startPos);
            for (String key : paramMap.keySet()) {
                if (paramMap.get(key) instanceof Collection<?>) {
                    query.setParameterList(key, (Collection<?>) paramMap.get(key));
                } else if (paramMap.get(key) instanceof Object[]) {
                    query.setParameterList(key, (Object[]) paramMap.get(key));
                } else {
                    query.setParameter(key, paramMap.get(key));
                }
            }
            return query.list();
        } finally {
            timer.stopWatch(hql);
        }
    }

    /**
     * 通过hql更新数据
     *
     * @param hql
     * @param paramList 参数集合
     * @return int 受影响的记录数
     * @version 2014-05-22 	chenchen	create
     */
    public int executeUpdateQuery(String hql, List<? extends Object> paramList) {
        QueryTimer timer = new QueryTimer(classType);
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);
            if (paramList != null && paramList.size() > 0) {
                for (int i = 0; i < paramList.size(); i++) {
                    query.setParameter(i, paramList.get(i));
                }
            }
            return query.executeUpdate();
        } finally {
            timer.stopWatch(hql);
        }
    }

    /**
     * 通过hql更新数据
     *
     * @param hql
     * @param paramMap 参数map
     * @return int 受影响的记录数
     * @version 2014-05-22 	chenchen	create
     */
    public int executeUpdateQueryByMap(String hql, Map<String, ?> paramMap) {
        QueryTimer timer = new QueryTimer(classType);
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);
            for (String key : paramMap.keySet()) {
                if (paramMap.get(key) instanceof Object[]) {
                    query.setParameterList(key, (Object[]) paramMap.get(key));
                } else {
                    query.setParameter(key, paramMap.get(key));
                }
            }
            int count = query.executeUpdate();
            return count;
        } finally {
            timer.stopWatch(hql);
        }
    }

    /**
     * 通过hql更新数据
     *
     * @param hql
     * @return int 受影响的记录条数
     * @version 2014-05-22	chenchen	create
     */
    public int executeUpdateQuery(String hql) {
        return executeUpdateQuery(hql, null);
    }

    /**
     * 返回总记录数
     *
     * @return int 记录总数
     * @version 2013-10-22	chenchen	create
     */
    public int total() {
        Query countQry = sessionFactory.getCurrentSession().createQuery("select count(*) from "
                + classType.getName());
        return HbUtil.uniqueResultAsInt(countQry);
    }

    /**
     * 返回总记录数
     *
     * @param sf 筛选器
     * @return int 记录总数
     * @version 2013-10-22	chenchen	create
     */
    public int total(SearchFilter sf) {
        String countHQL = buildCountSQL(sf);
        Query countQry = sessionFactory.getCurrentSession().createQuery(countHQL);
        HqlGenerator.bindParameters(countQry, sf);
        return HbUtil.uniqueResultAsInt(countQry);
    }

    /**
     * 返回总记录数
     *
     * @param hql
     * @return int 记录总数
     * @version 2013-10-22	chenchen	create
     */
    public int totalByQuery(String hql) {
        Query countQry = sessionFactory.getCurrentSession().createQuery(hql);
        return HbUtil.uniqueResultAsInt(countQry);
    }

    /**
     * 返回总记录数
     *
     * @param hql
     * @param paramList 参数集合
     * @return int 记录总数
     * @version 2013-10-22	chenchen	create
     */
    public int totalByQuery(String hql, List<? extends Object> paramList) {
        hql = hql.substring(hql.indexOf("from"));
        hql = "select count(*) " + hql;
        Query countQry = sessionFactory.getCurrentSession().createQuery(hql);
        if (paramList != null && paramList.size() > 0) {
            for (int i = 0; i < paramList.size(); i++) {
                countQry.setParameter(i, paramList.get(i));
            }
        }
        return HbUtil.uniqueResultAsInt(countQry);
    }

    /**
     * 返回总记录数
     *
     * @param hql
     * @param paramMap 参数Map
     * @return int 记录总数
     * @version 2013-10-22	chenchen	create
     */
    public int totalByQuery(String hql, Map<String, ?> paramMap) {
        hql = hql.substring(hql.indexOf("from"));
        hql = "select count(*) " + hql;
        Query countQry = sessionFactory.getCurrentSession().createQuery(hql);
        if (paramMap != null && paramMap.size() > 0) {
            for (String key : paramMap.keySet()) {
                if (paramMap.get(key) instanceof Collection<?>) {
                    countQry.setParameterList(key, (Collection<?>) paramMap.get(key));
                } else if (paramMap.get(key) instanceof Object[]) {
                    countQry.setParameterList(key, (Object[]) paramMap.get(key));
                } else {
                    countQry.setParameter(key, paramMap.get(key));
                }
            }

        }
        return HbUtil.uniqueResultAsInt(countQry);
    }

    /**
     * 返回总记录数
     *
     * @param sql
     * @return int 记录总数
     * @version 2013-10-22	chenchen	create
     */
    public int totalBySQLQuery(String sql) {
        sql = sql.substring(sql.indexOf("from"));
        sql = "select count(*) " + sql;
        Query countQry = sessionFactory.getCurrentSession().createSQLQuery(sql);
        return HbUtil.uniqueResultAsInt(countQry);
    }
    /**
     * 返回总记录数
     *
     * @param sql
     * @param paramList
     * @return int 记录总数
     * @version 2013-10-22	chenchen	create
     */
    public int totalBySQLQuery(String sql,List<? extends Object> paramList) {
        sql = sql.substring(sql.indexOf("from"));
        sql = "select count(*) " + sql;
        Query countQry = sessionFactory.getCurrentSession().createSQLQuery(sql);
        for (int i = 0; i < paramList.size(); i++) {
            countQry.setParameter(i, paramList.get(i));
        }
        return HbUtil.uniqueResultAsInt(countQry);
    }
    /**
     * 返回总记录数
     *
     * @param sql
     * @param paramMap
     * @return int 记录总数
     * @version 2013-10-22	chenchen	create
     */
    public int totalBySQLQuery(String sql,Map<String,?> paramMap) {
        sql = sql.substring(sql.indexOf("from"));
        sql = "select count(*) " + sql;
        Query countQry = sessionFactory.getCurrentSession().createSQLQuery(sql);
        if (paramMap!=null&&paramMap.size()>0){
            paramMap.forEach((key,value)->{
                if (value instanceof Object[]){
                    countQry.setParameterList(key, (Object[]) value);
                }else{
                    countQry.setParameter(key,value);
                }
            });
        }
        return HbUtil.uniqueResultAsInt(countQry);
    }

    /**
     * 构建统计总数语句
     *
     * @param sf 筛选器
     * @return String
     * @version 2013-10-24 	chenchen	create
     */
    private String buildCountSQL(SearchFilter sf) {
        StringBuilder sCountExp = new StringBuilder(256);
        sCountExp.append("select count(*) ");
        sCountExp.append("from ").append(classType.getName());
        sCountExp.append(sf.buildWhere());
        return sCountExp.toString();
    }

    /**
     * 构建sql orderby
     *
     * @param sf 筛选器
     * @param sb 排序条件
     * @return StringBuilder
     * @version 2015年2月26日    chenchen	create
     */
    private StringBuilder appendOrderbyClause(SearchFilter sf, StringBuilder sb) {
        if (sf.hasSetOrderBy()) {
            sb.append(" order by ").append(sf.getOrderBy());
        } else if (classType != null) {
            sb.append(" order by ").append("id desc");
        }

        return sb;
    }


    /**
     * 构建 from where
     *
     * @param sf 筛选器
     * @return StringBuilder
     * @version 2015年2月26日    chenchen	create
     */
    private StringBuilder buildFromAndWhere(SearchFilter sf) {
        StringBuilder sb = new StringBuilder(160);
        /*
		 * true，用户设置了select的低端
		 */
        if (!StringHelper.isEmpty(sf.getSelectFields())) {
			/*
			 * true ,用户设置了新的model类
			 * false,用户未设置新的model类
			 */
            if (sf.getViewClassType() != null) {
                sb.append("select new ")
                        .append(sf.getViewClassType().getName()).append("(")
                        .append(sf.getSelectFields()).append(")");

            } else {
                sb.append("select new ").append(classType.getName())
                        .append("(").append(sf.getSelectFields()).append(")");

            }
        }
        sb.append("from ").append(classType.getName());
        sb.append(sf.buildWhere());
        return sb;
    }

    /**
     * 获取当前实体对象所对应的表名
     *
     * @return String
     * @version 2013-10-24	chenchen	create
     */
    public String getTableName() {
        ClassMetadata metadata = getHibernateClassMetadata();
        if ((metadata instanceof SingleTableEntityPersister)) {
            return ((SingleTableEntityPersister) metadata).getTableName();
        }
        throw new DAOException("ClassMetadata of " + getGenericClassName()
                + " is not a SingleTableEntityPersister, it's " + metadata);
    }

    /**
     * 获取当前实体对象对应的主键字段名
     *
     * @return String
     * @version 2013-10-24	chenchen	create
     */
    public String getIdFieldName() {
        ClassMetadata metadata = getHibernateClassMetadata();
        return metadata.getIdentifierPropertyName();
    }

    /**
     * 获取当前实体对象
     *
     * @return Map<String, ClassMetadata>
     * @version 2013-10-24	chenchen	create
     */
    public Map<String, ClassMetadata> getAllEntityClassMetadata() {
        return HbUtil.getAllEntityClassMetadata(sessionFactory);
    }

    /**
     * 返回当前对象的的主键
     *
     * @return IdentifierGenerator
     * @version 2013-10-24	chenchen	create
     */
    public IdentifierGenerator getHibernateIdGenerator() {
        return HbUtil.getIdGenerator(sessionFactory, classType.getName());
    }

    /**
     * 返回hibernate元数据
     *
     * @return ClassMetadata
     * @version 2013-10-24	chenchen	create
     */
    public ClassMetadata getHibernateClassMetadata() {
        Map<?, ?> maps = getAllEntityClassMetadata();
        ClassMetadata metadata = (ClassMetadata) maps.get(classType.getName());
        AssertUtil.notNull(metadata, "ClassMetadata of " + classType
                + " not exist.");
        return metadata;
    }

    /**
     * 获取hibernate EntityMetamodel
     *
     * @return EntityMetamodel
     * @version 2013-10-24 chenchen	create
     */
    public EntityMetamodel getHibernateEntityMetamodel() {
        ClassMetadata metadata = getHibernateClassMetadata();
        if (!(metadata instanceof AbstractEntityPersister)) {
            throw new DAOException("not a AbstractEntityPersister!");
        }
        AbstractEntityPersister persister = (AbstractEntityPersister) metadata;
        return persister.getEntityMetamodel();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString()).append(" (T=");
        builder.append(classType.getName());
        builder.append(")");
        return builder.toString();
    }
}
