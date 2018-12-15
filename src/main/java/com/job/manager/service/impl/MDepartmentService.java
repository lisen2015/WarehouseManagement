package com.job.manager.service.impl;

import com.job.manager.daoManager.BaseDaoManager;
import com.job.manager.daoManager.HQLBuilder;
import com.job.manager.daoManager.SearchFilter;
import com.job.manager.model.MDepartment;
import com.job.manager.service.IMDepartmentService;
import com.job.manager.util.StringHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Li.chen on 2018/12/03 14:25.
 */
@Service
public class MDepartmentService implements IMDepartmentService {

    @Resource
    private BaseDaoManager<MDepartment> mDepartmentManager;

    @Override
    public List<MDepartment> findDepartmentById(String id) {
        return null;
    }

    @Override
    public boolean delete(String[] ids) {
//        HQLBuilder hqb=HQLBuilder.LOGICDELLETE(MDepartment.class);
//        hqb.addInCondition("id",ids);
//        mDepartmentManager.batchDeleteByLogic(hqb);
        HQLBuilder hqb= HQLBuilder.DELETE(MDepartment.class);
        hqb.addInCondition("id",ids);
        mDepartmentManager.batchDelete(hqb);
        return true;
    }

    @Override
    public boolean add(MDepartment department) {
        return StringHelper.isNotEmpty((String) mDepartmentManager.insert(department));
    }

    @Override
    public boolean update(MDepartment department) {
        MDepartment mDepartment = mDepartmentManager.getObject(department.getId());
        if (mDepartment != null) {
            mDepartment.setDepartmentName(department.getDepartmentName());
            mDepartment.setDepartmentDesc(department.getDepartmentDesc());
            mDepartmentManager.update(mDepartment);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<MDepartment> getAll() {
        SearchFilter sf = SearchFilter.getDefault();
//        sf.setOrderBy("id desc");
        return mDepartmentManager.listObjects(sf);
    }
}
