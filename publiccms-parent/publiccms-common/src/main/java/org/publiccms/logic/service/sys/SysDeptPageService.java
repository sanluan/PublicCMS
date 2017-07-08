package org.publiccms.logic.service.sys;

// Generated 2016-1-19 11:41:45 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.ArrayUtils.removeElement;

import java.util.List;

import org.publiccms.entities.sys.SysDeptCategory;
import org.publiccms.entities.sys.SysDeptPage;
import org.publiccms.entities.sys.SysDeptPageId;
import org.publiccms.logic.dao.sys.SysDeptPageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * SysDeptPageService
 * 
 */
@Service
@Transactional
public class SysDeptPageService extends BaseService<SysDeptPage> {

    /**
     * @param deptId
     * @param page
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer deptId, String page, Integer pageIndex, Integer pageSize) {
        return dao.getPage(deptId, page, pageIndex, pageSize);
    }

    /**
     * @param deptId
     * @param pages
     */
    public void updateDeptPages(Integer deptId, String[] pages) {
        if (notEmpty(deptId)) {
            @SuppressWarnings("unchecked")
            List<SysDeptPage> list = (List<SysDeptPage>) getPage(deptId, null, null, null).getList();
            for (SysDeptPage deptPage : list) {
                if (contains(pages, deptPage.getId().getPage())) {
                    pages = removeElement(pages, deptPage.getId().getPage());
                } else {
                    delete(deptPage.getId());
                }
            }
            if (notEmpty(pages)) {
                for (String page : pages) {
                    save(new SysDeptPage(new SysDeptPageId(deptId, page)));
                }
            }

        }
    }
    
    /**
     * @param deptId
     * @param page
     */
    public void delete(Integer deptId, String page) {
        if (notEmpty(page) || notEmpty(deptId)) {
            @SuppressWarnings("unchecked")
            List<SysDeptCategory> list = (List<SysDeptCategory>) getPage(deptId, page, null, null).getList();
            for (SysDeptCategory deptCategory : list) {
                delete(deptCategory.getId());
            }
        }
    }

    @Autowired
    private SysDeptPageDao dao;
    
}