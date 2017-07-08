package org.publiccms.logic.service.sys;

// Generated 2016-1-19 11:41:45 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.ArrayUtils.removeElement;

import java.util.List;

import org.publiccms.entities.sys.SysDeptCategory;
import org.publiccms.entities.sys.SysDeptCategoryId;
import org.publiccms.logic.dao.sys.SysDeptCategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

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
    
    /**
     * @param deptId
     * @param categoryId
     */
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