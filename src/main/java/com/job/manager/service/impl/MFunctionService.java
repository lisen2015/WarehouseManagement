/*															
 * FileName：MFunctionService.java						
 *			
 * Description：IMFunctionService的实现类，用于完成栏目功能配置的业务逻辑						
 * 																	
 * History：
 * 版本号 作者 		日期       	简介
 *  1.0   chenchen	2014-7-16		Create		
 */
package com.job.manager.service.impl;

import com.job.manager.daoManager.BaseDaoManager;
import com.job.manager.daoManager.ExtendSearchFilter;
import com.job.manager.daoManager.HQLBuilder;
import com.job.manager.daoManager.SearchFilter;
import com.job.manager.model.MFunctionConfig;
import com.job.manager.service.IMFunctionService;
import com.job.manager.util.StringHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MFunctionService implements IMFunctionService {
    @Resource
    private BaseDaoManager<MFunctionConfig> mFunctionManager;

    @Override
    public List<MFunctionConfig> getListByChannelId(String channelId) {
        SearchFilter sf = SearchFilter.getDefault();
        sf.addEqCondition("channelId", channelId);
        sf.setOrderBy("functionIndex,id desc");
        return mFunctionManager.listObjects(sf);
    }

    @Override
    public boolean addFunction(MFunctionConfig function) {
        function.setFunctionIndex(getCurrentIndex(function.getChannelId()));
        String i = (String) mFunctionManager.insert(function);
        return StringHelper.isNotEmpty(i);
    }

    @Override

    public Map<String, MFunctionConfig> getAllFunctionMap() {
        Map<String, MFunctionConfig> map = new HashMap<>();
        ExtendSearchFilter esf = ExtendSearchFilter.getDefault();
        mFunctionManager.listObjects(esf).stream().forEach(
                m -> {
                    Arrays.stream(m.getHtmlUrl().split(";")).forEach(url -> map.put(url, m));
                });
        return map;
    }

    @Override
    public boolean updateFunction(MFunctionConfig function) {
        MFunctionConfig oldFunction=mFunctionManager.getObject(function.getId());
        if (oldFunction!=null) {
            oldFunction.setMFunctionConfig(function.getFunctionName(),function.getHtmlId(), function.getHtmlUrl(),
                    function.getFunctionIndex(), new Timestamp(System.currentTimeMillis()));
            oldFunction.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            mFunctionManager.update(oldFunction);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean delete(String[] ids) {
        HQLBuilder hqb = HQLBuilder.DELETE(MFunctionConfig.class);
        hqb.addInCondition("id", ids);
        mFunctionManager.batchDelete(hqb);
        return true;
    }

    /* (non-Javadoc)
     * @see com.trs.lab.service.IMFunctionService#exist(java.lang.String, java.lang.Object, java.lang.Long)
	 */
    @Override
    public boolean exist(String field, Object value, String excludedId) {
        SearchFilter esf = SearchFilter.getDefault();
        return mFunctionManager.exists(field, value, excludedId, esf);
    }
    private Integer getCurrentIndex(String channelId){
        //仅获取一条数据
        SearchFilter sf= SearchFilter.getSearchFilter(1,1);
        sf.addEqCondition("channelId",channelId);
        sf.setOrderBy("functionIndex");
        MFunctionConfig functionConfig=mFunctionManager.findFirst(sf);
        /*
         * 如果无查询对象,获取对象的索引为空
         * true,返回0,
         * false,返回当前索引-1
         */
        if (functionConfig==null || functionConfig.getFunctionIndex()==null){
            return 0;
        }else {
            return functionConfig.getFunctionIndex()-1;
        }

    }
    @Override
    public void updateIndex(String sId, Integer sIndex, Integer tIndex, String point) {
        MFunctionConfig functionConfig=mFunctionManager.getObject(sId);
        Map<String, Object> paramMap = new HashMap<String, Object>();
		/*
         * 将所有大于目标索引的索引+1，排除目标
		 */
        HQLBuilder hqb = HQLBuilder.UPDATE(MFunctionConfig.class);
        hqb.setNewValueField("functionIndex", "functionIndex+1");
        hqb.addEqCondition("channelId",functionConfig.getChannelId());
		/*
		 * 判断移动后是在目标上方还是下方
		 */
        if (point.equals("top")) {
            //上方：替换目标index，并从目标开始index+1
            hqb.addGreaterThanEquals("functionIndex", tIndex);
            paramMap.put("functionIndex", tIndex);
        } else {
            //上方：替换目标index+1，并从目标后一下开始index+1
            hqb.addGreaterThan("functionIndex", tIndex);
            paramMap.put("functionIndex", tIndex + 1);
        }
        hqb.addNotEqCondition("id", sId);
        mFunctionManager.batchUpdate(hqb);
        //更新被移动数据的索引，并判断是否成功
        mFunctionManager.update(sId, paramMap);
    }
}
