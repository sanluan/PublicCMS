package com.publiccms.logic.service.cms;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.entities.cms.CmsDictionaryExcludeId;
import com.publiccms.entities.cms.CmsDictionaryExcludeValue;
import com.publiccms.logic.dao.cms.CmsDictionaryExcludeValueDao;

/**
 *
 * CmsDictionaryExcludeValueService
 * 
 */
@Service
@Transactional
public class CmsDictionaryExcludeValueService extends BaseService<CmsDictionaryExcludeValue> {

    /**
     * @param siteId
     * @param excludeId
     * @return the number of entities deleted
     */
    public int delete(short siteId, CmsDictionaryExcludeId excludeId) {
        return dao.delete(siteId, excludeId);
    }

    /**
     * @param siteId
     * @param dictionaryIds
     * @return the number of entities deleted
     */
    public int delete(short siteId, String[] dictionaryIds) {
        return dao.delete(siteId, dictionaryIds);
    }

    /**
     * @param siteId
     * @param dictionaryId
     * @param value
     * @return the number of entities deleted
     */
    public int deleteByValue(short siteId, String dictionaryId, String value) {
        return dao.deleteByValue(siteId, dictionaryId, value);
    }

    @Resource
    private CmsDictionaryExcludeValueDao dao;

}