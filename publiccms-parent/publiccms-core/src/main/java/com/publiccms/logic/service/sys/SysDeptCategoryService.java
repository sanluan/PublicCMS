package com.publiccms.logic.service.sys;

// Generated 2016-1-19 11:41:45 by com.publiccms.common.source.SourceGenerator

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDeptCategory;
import com.publiccms.entities.sys.SysDeptCategoryId;
import com.publiccms.logic.dao.sys.SysDeptCategoryDao;

/**
 *
 * SysDeptCategoryService
 * 
 */
@Service
@Transactional
public class SysDeptCategoryService extends BaseService<SysDeptCategory> {

    /**
     * @param deptId
     * @param categoryId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer deptId, Integer categoryId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(deptId, categoryId, pageIndex, pageSize);
    }

    /**
     * @param deptId
     * @param categoryIds
     */
    public void updateDeptCategorys(Integer deptId, Integer[] categoryIds) {
        if (CommonUtils.notEmpty(deptId)) {
            @SuppressWarnings("unchecked")
            List<SysDeptCategory> list = (List<SysDeptCategory>) getPage(deptId, null, null, null).getList();
            for (SysDeptCategory deptCategory : list) {
                if (ArrayUtils.contains(categoryIds, deptCategory.getId().getCategoryId())) {
                    categoryIds = ArrayUtils.removeElement(categoryIds, deptCategory.getId().getCategoryId());
                } else {
                    delete(deptCategory.getId());
                }
            }
            if (CommonUtils.notEmpty(categoryIds)) {
                for (Integer categoryId : categoryIds) {
                    save(new SysDeptCategory(new SysDeptCategoryId(deptId, categoryId)));
                }
            }
        }
    }
    
    /**
     * @param deptId
     * @param categoryId
     */
    public void delete(Integer deptId, Integer categoryId) {
        if (CommonUtils.notEmpty(categoryId) || CommonUtils.notEmpty(deptId)) {
            @SuppressWarnings("unchecked")
            List<SysDeptCategory> list = (List<SysDeptCategory>) getPage(deptId, categoryId, null, null).getList();
            for (SysDeptCategory deptCategory : list) {
                delete(deptCategory.getId());
            }
        }
    }

    @Autowired
    private SysDeptCategoryDao dao;
    
}