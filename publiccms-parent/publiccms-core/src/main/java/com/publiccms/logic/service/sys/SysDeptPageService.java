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
import com.publiccms.entities.sys.SysDeptPage;
import com.publiccms.entities.sys.SysDeptPageId;
import com.publiccms.logic.dao.sys.SysDeptPageDao;

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
        if (CommonUtils.notEmpty(deptId)) {
            @SuppressWarnings("unchecked")
            List<SysDeptPage> list = (List<SysDeptPage>) getPage(deptId, null, null, null).getList();
            for (SysDeptPage deptPage : list) {
                if (ArrayUtils.contains(pages, deptPage.getId().getPage())) {
                    pages = ArrayUtils.removeElement(pages, deptPage.getId().getPage());
                } else {
                    delete(deptPage.getId());
                }
            }
            if (CommonUtils.notEmpty(pages)) {
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
        if (CommonUtils.notEmpty(page) || CommonUtils.notEmpty(deptId)) {
            @SuppressWarnings("unchecked")
            List<SysDeptPage> list = (List<SysDeptPage>) getPage(deptId, page, null, null).getList();
            for (SysDeptPage deptPage : list) {
                delete(deptPage.getId());
            }
        }
    }

    @Autowired
    private SysDeptPageDao dao;
    
}