package org.publiccms.logic.service.cms;

// Generated 2015-7-10 16:36:23 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.publiccms.entities.cms.CmsTag;
import org.publiccms.logic.dao.cms.CmsTagDao;
import org.publiccms.views.pojo.CmsTagStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

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
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Integer typeId, String name, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, typeId, name, orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param ids
     */
    public void delete(int siteId, Serializable[] ids) {
        for (CmsTag entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId()) {
                delete(entity.getId());
            }
        }
    }

    /**
     * @param entitys
     */
    public void updateStatistics(Collection<CmsTagStatistics> entitys) {
        for (CmsTagStatistics entityStatistics : entitys) {
            CmsTag entity = getEntity(entityStatistics.getId());
            if (null != entity) {
                entity.setSearchCount(entity.getSearchCount() + entityStatistics.getSearchCounts());
            }
        }
    }

    /**
     * @param siteId
     * @param entitys
     * @return
     */
    public Long[] update(int siteId, List<CmsTag> entitys) {
        Set<Long> idList = new HashSet<Long>();
        if (notEmpty(entitys)) {
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