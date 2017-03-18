package com.publiccms.logic.service.sys;

// Generated 2016-1-19 11:41:45 by com.sanluan.common.source.SourceGenerator

import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.ArrayUtils.removeElement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.sys.SysDeptCategory;
import com.publiccms.entities.sys.SysDeptCategoryId;
import com.publiccms.logic.dao.sys.SysDeptCategoryDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class SysDeptCategoryService extends BaseService<SysDeptCategory> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer deptId, Integer categoryId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(deptId, categoryId, pageIndex, pageSize);
    }

    public void updateDeptCategorys(Integer deptId, Integer[] categoryIds) {
        if (notEmpty(deptId)) {
            @SuppressWarnings("unchecked")
            List<SysDeptCategory> list = (List<SysDeptCategory>) getPage(deptId, null, null, null).getList();
            for (SysDeptCategory deptCategory : list) {
                if (contains(categoryIds, deptCategory.getId().getCategoryId())) {
                    categoryIds = removeElement(categoryIds, deptCategory.getId().getCategoryId());
                } else {
                    delete(deptCategory.getId());
                }
            }
            if (notEmpty(categoryIds)) {
                for (Integer categoryId : categoryIds) {
                    save(new SysDeptCategory(new SysDeptCategoryId(deptId, categoryId)));
                }
            }
        }
    }
    
    public void delete(Integer deptId, Integer categoryId) {
        if (notEmpty(categoryId) || notEmpty(deptId)) {
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