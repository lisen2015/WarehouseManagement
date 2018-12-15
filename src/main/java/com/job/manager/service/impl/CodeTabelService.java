/*															
 * FileName：CodeTabelService.java						
 *			
 * Description：字典管理的业务类实现类，用于实现字典管理操作的方法						
 * 																	
 * History：
 * 版本号 作者 		日期       	简介
 *  1.0   chenchen	2014-7-16		Create		
 */
package com.job.manager.service.impl;

import com.job.manager.daoManager.BaseDaoManager;
import com.job.manager.daoManager.ExtendSearchFilter;
import com.job.manager.model.CodeTable;
import com.job.manager.model.NewsClassJson;
import com.job.manager.service.ICacheService;
import com.job.manager.service.ICodeTableService;
import com.job.manager.util.StringHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

@Service
public class CodeTabelService implements ICodeTableService{
	private static List<Map<String, Object>> vCodeMap;//全国省市数据 类变量
	@Resource
	private BaseDaoManager<CodeTable> codeManager;

	private static ResourceBundle rb=ResourceBundle.getBundle("globalConfig");
	@Resource
	private ICacheService cacheService;


	@Override
	public boolean add(CodeTable code) {
		String i = (String) codeManager.insert(code);
		if (StringHelper.isNotEmpty(i)) {
            String parentId=code.getParentId();
			if (StringHelper.isNotEmpty(parentId)) {
				CodeTable parentCode=get(parentId);
				if (parentCode!=null ) {
					parentCode.setIsEnd(0);
					parentCode.setUpdateTime(new Timestamp(System.currentTimeMillis()));
					if(code.getParentId().equals("1005")||code.getCodeValue2().equals("1005")) {//判断是否为消息设置
						cacheService.clearCached(rb.getString("xmem.systemMessageConfig.key"));
					}
					codeManager.update(parentCode);
				}
			}
			return true;
		} else {
			return false;
		}
	}
	@Override
	public boolean update(CodeTable code) {
		CodeTable oldCode=codeManager.getObject(code.getId());
		if (oldCode!=null) {
			oldCode.setCodeDesc(code.getCodeDesc());
			oldCode.setCodeIndex(code.getCodeIndex());
			oldCode.setCodeValue1(code.getCodeValue1());
			oldCode.setCodeValue2(code.getCodeValue2());
			oldCode.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			codeManager.update(oldCode);
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean delete(String[] ids) {
		for (String id : ids) {
			CodeTable code=codeManager.getObject(id);
			if (code!=null) {
				if (code.getIsEnd()==0) {
					List<CodeTable> list=getListByParent(id);
					delete(list);
				}
				if(code.getParentId().equals("1005")||code.getCodeValue2().equals("1005")) {
					cacheService.clearNS(rb.getString("xmem.systemMessageConfig.key"));
				}
				codeManager.delete(code);
				List<CodeTable> list=getListByParent(code.getParentId());
				if (list==null || list.size()==0) {
					Map<String, Object> map= new HashMap<>();
					map.put("isEnd", 1);
					if(code.getParentId().equals("1005")||code.getCodeValue2().equals("1005")) {
						cacheService.clearCached(rb.getString("xmem.systemMessageConfig.key"));
					}
					codeManager.update(code.getParentId(),map);
				}
			}
		}
		return true;
	}
	@Override
	public void delete(List<CodeTable> list){

        list.stream().filter(code -> code != null).forEach(code -> {
            if (code.getIsEnd() == 0) {
                List<CodeTable> codeList = getListByParent(code.getId());
                delete(codeList);
            }
            codeManager.delete(code);

        });
	}

	@Override
	public Map<String, NewsClassJson> getSystemMessageConfig() {
		Map<String,NewsClassJson> map=new HashMap<String,NewsClassJson>();
		ExtendSearchFilter esf=ExtendSearchFilter.getDefault();
		esf.setOrderBy("codeLevel,parentId,codeIndex,id desc");
		codeManager.listObjects(esf).stream().forEach(codeTable->{
			//1:将数据存入缓存
			//使用NewsClassJson代替原来的类，是combotree获取数据，当是父类时，设置attribute为0，表明不能选择
			NewsClassJson newsClassJson =new NewsClassJson();
			newsClassJson.setId(codeTable.getId());
			newsClassJson.setParentId(codeTable.getParentId());
			newsClassJson.setText(codeTable.getCodeValue1());
			newsClassJson.setAttributes(String.valueOf(codeTable.getIsEnd()));
			map.put(newsClassJson.getId(),newsClassJson);
			//2:获取父栏目对象,并将当前对象存入父栏目的 children 中
			NewsClassJson parentCodeTable=map.get(codeTable.getParentId());
			if (parentCodeTable!=null) {
				List<NewsClassJson> childrenList=parentCodeTable.getChildren();
				if (childrenList==null) {
					newsClassJson.setAttributes(String.valueOf(codeTable.getIsEnd()));
					childrenList= new ArrayList<>();
					childrenList.add(newsClassJson);
					parentCodeTable.setChildren(childrenList);
				}else{
					childrenList.add(newsClassJson);
				}
			}
		});
		return map;
	}


	@Override
	public List<CodeTable> getListByParent(String parentId) {
		ExtendSearchFilter esf=ExtendSearchFilter.getDefault();
		esf.addEqCondition("parentId", parentId);
		esf.setOrderBy("codeIndex,id desc");
		try {
			return codeManager.listObjects(esf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	@Override
	public CodeTable get(String id) {
		return codeManager.getObject(id);
	}

	@Override
	public boolean exist(String filed, String id, String ids) {
		return codeManager.exists(filed, id, ids,ExtendSearchFilter.getDefault());
	}





}
