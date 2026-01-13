package com.publiccms.logic.service.cms;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.entities.cms.CmsLanguage;
import com.publiccms.logic.dao.cms.CmsLanguageDao;

import jakarta.annotation.Resource;

/**
 *
 * CmsLanguageService
 * 
 */
@Service
@Transactional
public class CmsLanguageService extends BaseService<CmsLanguage> {

    /**
     * @param siteId
     * @return data list
     */
    @Transactional(readOnly = true)
    public List<CmsLanguage> getList(short siteId) {
        return dao.getList(siteId);
    }

    @Resource
    private CmsLanguageDao dao;
}