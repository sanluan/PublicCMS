package com.publiccms.logic.service.cms;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsTag;
import com.publiccms.logic.dao.cms.CmsTagDao;
import com.publiccms.views.pojo.entities.ClickStatistics;

/**
 *
 * CmsTagService
 * 
 */
@Service
@Transactional
public class CmsTagService extends BaseService<CmsTag> {

    /**
     * @param siteId
     * @param typeId
     * @param name
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Integer typeId, String name, String orderField, String orderType, Integer pageIndex,
            Integer pageSize) {
        return dao.getPage(siteId, typeId, name, orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param ids
     */
    public void delete(short siteId, Serializable[] ids) {
        for (CmsTag entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId()) {
                delete(entity.getId());
            }
        }
    }

    /**
     * @param entitys
     */
    public void updateStatistics(Collection<ClickStatistics> entitys) {
        for (ClickStatistics entityStatistics : entitys) {
            CmsTag entity = getEntity(entityStatistics.getId());
            if (null != entity) {
                entity.setSearchCount(entity.getSearchCount() + entityStatistics.getClicks());
            }
        }
    }

    /**
     * @param siteId
     * @param entitys
     * @return
     */
    public Long[] update(short siteId, List<CmsTag> entitys) {
        Set<Long> idList = new HashSet<>();
        if (CommonUtils.notEmpty(entitys)) {
            for (CmsTag entity : entitys) {
                if (null != entity.getId()) {
                    entity = getEntity(entity.getId());
                    if (siteId == entity.getSiteId()) {
                        idList.add(entity.getId());
                    }
                } else {
                    entity.setSiteId(siteId);
                    save(entity);
                    idList.add(entity.getId());
                }
            }
        }
        return idList.toArray(new Long[idList.size()]);
    }

    @Autowired
    private CmsTagDao dao;

}