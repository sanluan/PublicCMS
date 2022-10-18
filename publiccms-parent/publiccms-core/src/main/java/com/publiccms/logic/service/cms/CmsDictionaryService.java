package com.publiccms.logic.service.cms;

import java.util.List;
import java.util.function.BiConsumer;

// Generated 2016-11-20 14:50:37 by com.publiccms.common.generator.SourceGenerator

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.cms.CmsDictionary;
import com.publiccms.logic.dao.cms.CmsDictionaryDao;

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
     * @param worker
     * @param batchSize
     */
    @Transactional(readOnly = true)
    public void batchWork(short siteId, BiConsumer<List<CmsDictionary>, Integer> worker, int batchSize) {
        dao.batchWork(siteId, worker, batchSize);
    }

    /**
     * @param siteId
     * @param name
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, String name, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, name, pageIndex, pageSize);
    }

    @Resource
    private CmsDictionaryDao dao;

}