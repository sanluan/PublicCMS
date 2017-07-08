package org.publiccms.logic.service.cms;

import org.publiccms.entities.cms.CmsDictionary;
import org.publiccms.logic.dao.cms.CmsDictionaryDao;

// Generated 2016-11-20 14:50:37 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * CmsDictionaryService
 * 
 */
@Service
@Transactional
public class CmsDictionaryService extends BaseService<CmsDictionary> {

    /**
     * @param multiple
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Boolean multiple, Integer pageIndex, Integer pageSize) {
        return dao.getPage(multiple, pageIndex, pageSize);
    }

    @Autowired
    private CmsDictionaryDao dao;

}