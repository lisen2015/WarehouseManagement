package com.job.manager.service.impl;

import com.job.manager.daoManager.BaseDaoManager;
import com.job.manager.daoManager.ExtendSearchFilter;
import com.job.manager.model.MChannelConfig;
import com.job.manager.service.ICacheService;
import com.job.manager.service.IMChannelService;
import com.job.manager.util.StringHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * The type M channel service.
 */
@Service
public class MChannelService implements IMChannelService {

    private static ResourceBundle rb=ResourceBundle.getBundle("globalConfig");
    @Resource
    private ICacheService cacheService;
    @Resource
    private BaseDaoManager<MChannelConfig> mChannelManager;



    @Override
    public boolean addChannel(MChannelConfig mChannelConfig) {
        String i = (String) mChannelManager.insert(mChannelConfig);
        if (StringHelper.isNotEmpty(i)) {
            String parentId=mChannelConfig.getParentId();
            if (StringHelper.isNotEmpty(parentId)) {
                MChannelConfig parentChannel=get(parentId);
                if (parentChannel!=null ) {
                    parentChannel.setIsEnd(0);
                    updateChannel(parentChannel);
                }
            }
            cacheService.clearCached(rb.getString("xmem.allChannelTree.key"));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateChannel(MChannelConfig mChannelConfig) {
        MChannelConfig oldChannel=mChannelManager.getObject(mChannelConfig.getId());
        if (oldChannel!=null) {
            oldChannel.setChannelName(mChannelConfig.getChannelName());
            oldChannel.setChannelUrl(mChannelConfig.getChannelUrl());
            oldChannel.setChannelUrl2(mChannelConfig.getChannelUrl2());
            oldChannel.setChannelIndex(mChannelConfig.getChannelIndex());
            oldChannel.setParentId(mChannelConfig.getParentId());
            oldChannel.setChannelLevel(mChannelConfig.getChannelLevel());
            mChannelManager.update(oldChannel);
            cacheService.clearCached(rb.getString("xmem.allChannelTree.key"));
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        mChannelManager.deleteByLogic(get(id));
        cacheService.clearCached(rb.getString("xmem.roleFunctionAuth.key"));
        cacheService.clearCached(rb.getString("xmem.roleChannelAuth.key"));
        cacheService.clearCached(rb.getString("xmem.allChannelTree.key"));
        return true;
    }

    @Override
    public MChannelConfig get(String id) {
        return mChannelManager.getObject(id);
    }

    @Override
    public boolean delete(String[] ids) {
        for (String id : ids) {
            MChannelConfig channel=get(id);
            if (channel!=null) {
                if (channel.getIsEnd()==0) {
                    MChannelConfig parentChannel=getAllChannelTree().get(channel.getId());
                    if (parentChannel!=null){
                        List<MChannelConfig> list=parentChannel.getChildren();
                        delete(list);
                    }
                }
                mChannelManager.delete(channel);
                //查询删除的组织的兄弟组织集合()
                MChannelConfig parentChannel=getAllChannelTree().get(channel.getParentId());
                if (parentChannel!=null){
                    List<MChannelConfig> list=parentChannel.getChildren();
                    if (list==null || list.size()==0) {
                        Map<String, Object> map=new HashMap<String, Object>();
                        map.put("isEnd", 1);
                        mChannelManager.update(channel.getParentId(),map);
                    }
                }

            }
        }
        cacheService.clearCached(rb.getString("xmem.roleFunctionAuth.key"));
        cacheService.clearCached(rb.getString("xmem.roleChannelAuth.key"));
        cacheService.clearCached(rb.getString("xmem.allChannelTree.key"));
        return true;
    }

    /**
     * Delete.
     *
     * @param list the list
     * @version * 2016-07-05 chenchen create
     */
    private void delete(List<MChannelConfig> list){
        if (list==null){
            return;
        }
            list.stream().forEach(channel->{
                if (channel!=null) {
                    if (channel.getIsEnd()==0) {//0表示下边含有子目录
                        MChannelConfig parentChannel=getAllChannelTree().get(channel.getId());
                        if (parentChannel!=null){
                            List<MChannelConfig> childList=parentChannel.getChildren();
                            delete(childList);
                        }
                    }
                    mChannelManager.delete(channel);
                }
            });
    }
    @Override
    public boolean exist(String field, Object value, String excludedId) {
        ExtendSearchFilter esf= ExtendSearchFilter.getDefault();
        return mChannelManager.exists(field, value, excludedId,esf);
    }

    @Override
    public List<MChannelConfig> findByType(String type, String value) {
        ExtendSearchFilter esf= ExtendSearchFilter.getDefault();
        if (type.equals("all")) {
            esf.addLike("concat_ws('-',channelName,channelUrl2,channelUrl,channelLevel,channelIndex)", value);
        }else {
            esf.addLike(type, value);
        }
        esf.addNotEqCondition("id",1);
        esf.setOrderBy("channelIndex desc,id desc");
        return mChannelManager.listObjects(esf);
    }

    @Override

    public Map<String, MChannelConfig> getAllChannelTree() {
        //1、判断缓存中是否存在,如果存在直接返回缓存中的内容
        Map<String,MChannelConfig> allChannelTree= (Map<String, MChannelConfig>) cacheService.get(rb.getString("xmem.allChannelTree.key"));
        if (allChannelTree!=null){
            return allChannelTree;
        }
        //2、获取数据
        Map<String,MChannelConfig> map=new HashMap<String,MChannelConfig>();
        ExtendSearchFilter esf= ExtendSearchFilter.getDefault();
        esf.setOrderBy("channelLevel,parentId,channelIndex,id");
        mChannelManager.listObjects(esf).stream().forEach(channel->{
            //1:将数据存入缓存
            map.put(channel.getId(),channel);
            //2:获取父栏目对象,并将当前对象存入父栏目的 children 中
            MChannelConfig parentChannel=map.get(channel.getParentId());
            if (parentChannel!=null) {
                List<MChannelConfig> childrenList=parentChannel.getChildren();
                if (childrenList==null) {
                    childrenList= new ArrayList<>();
                    childrenList.add(channel);
                    parentChannel.setChildren(childrenList);
                }else{
                    childrenList.add(channel);
                }
            }
        });
        //3、存入缓存
        cacheService.set(rb.getString("xmem.allChannelTree.key"),map,Integer.parseInt(rb.getString("xmem.allChannelTree.timeout")));
        return map;
    }
}
