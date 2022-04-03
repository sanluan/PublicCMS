package com.publiccms.logic.service.cms;

import com.publiccms.entities.cms.CmsDictionary;
import com.publiccms.logic.dao.cms.CmsDictionaryDao;

// Generated 2016-11-20 14:50:37 by com.publiccms.common.source.SourceGenerator

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

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
     * @param siteId
     * @param name
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional
    public PageHandler getPage(Short siteId, String name, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, name, pageIndex, pageSize);
    }

    @Resource
    private CmsDictionaryDao dao;

}