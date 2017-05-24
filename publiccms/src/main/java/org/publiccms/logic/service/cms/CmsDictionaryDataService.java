package org.publiccms.logic.service.cms;

import org.publiccms.entities.cms.CmsDictionaryData;
import org.publiccms.logic.dao.cms.CmsDictionaryDataDao;

// Generated 2016-11-20 14:50:55 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * CmsDictionaryDataService
 * 
 */
@Service
@Transactional
public class CmsDictionaryDataService extends BaseService<CmsDictionaryData> {

    /**
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer pageIndex, Integer pageSize) {
        return dao.getPage(pageIndex, pageSize);
    }

    @Autowired
    private CmsDictionaryDataDao dao;
    
}