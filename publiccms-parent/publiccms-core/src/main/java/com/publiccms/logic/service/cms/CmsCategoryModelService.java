package com.publiccms.logic.service.cms;

import java.util.List;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.logic.dao.cms.CmsCategoryModelDao;

/**
 *
 * CmsCategoryModelService
 * 
 */
@Service
@Transactional
public class CmsCategoryModelService extends BaseService<CmsCategoryModel> {

    /**
     * @param siteId
     * @param modelId
     * @param categoryId
     * @return results page
     */
    @Transactional(readOnly = true)
    public List<CmsCategoryModel> getList(short siteId, String modelId, Integer categoryId) {
        return dao.getList(siteId, modelId, categoryId);
    }
    
    /**
     * @param siteId
     * @param modelId
     * @param categoryId
     * @return number of data deleted
     */
    public int delete(short siteId, String modelId, Integer categoryId) {
        return dao.delete(siteId, modelId, categoryId);
    }

    @Resource
    private CmsCategoryModelDao dao;

}