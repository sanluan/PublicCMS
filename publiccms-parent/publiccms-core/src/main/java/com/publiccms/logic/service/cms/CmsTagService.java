package com.publiccms.logic.service.cms;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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
    private CmsTagTypeService tagTypeService;

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
                if (null != entity.getTypeId()) {
                    tagTypeService.updateCount(siteId, entity.getTypeId(), dao.getCount(siteId, entity.getTypeId()));
                }
            }
        }
    }

    /**
     * @param entitys
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateStatistics(Collection<ClickStatistics> entitys) {
        for (ClickStatistics entityStatistics : entitys) {
            CmsTag entity = getEntity(entityStatistics.getId());
            if (null != entity) {
                entity.setSearchCount(entity.getSearchCount() + entityStatistics.getClicks());
            }
        }
    }

    /**
     * @param id
     * @param newEntity
     * @return entity
     */
    @Override
    public CmsTag update(Serializable id, CmsTag newEntity) {
        CmsTag entity = getEntity(id);
        if (null != entity) {
            Integer oldTypeId = entity.getTypeId();
            entity = super.update(id, newEntity);
            if ((null != oldTypeId || null != entity.getTypeId()) && entity.getTypeId() != oldTypeId) {
                if (null != oldTypeId) {
                    tagTypeService.updateCount(entity.getSiteId(), oldTypeId, dao.getCount(entity.getSiteId(), oldTypeId));
                }
                if (null != entity.getTypeId()) {
                    tagTypeService.updateCount(entity.getSiteId(), entity.getTypeId(),
                            dao.getCount(entity.getSiteId(), entity.getTypeId()));
                }
            }
        }
        return entity;
    }

    /**
     * @param entity
     */
    @Override
    public void save(CmsTag entity) {
        super.save(entity);
        if (null != entity.getTypeId()) {
            tagTypeService.updateCount(entity.getSiteId(), entity.getTypeId(),
                    dao.getCount(entity.getSiteId(), entity.getTypeId()));
        }
    }

    /**
     * @param siteId
     * @param entitys
     * @return
     */
    public Set<Serializable> update(short siteId, List<CmsTag> entitys) {
        Set<Serializable> idList = new HashSet<>();
        if (CommonUtils.notEmpty(entitys)) {
            for (CmsTag entity : entitys) {
                if (null != entity.getId()) {
                    idList.add(entity.getId());
                } else {
                    entity.setSiteId(siteId);
                    save(entity);
                    if (null != entity.getTypeId()) {
                        tagTypeService.updateCount(siteId, entity.getTypeId(), dao.getCount(siteId, entity.getTypeId()));
                    }
                    idList.add(entity.getId());
                }
            }
        }
        return idList;
    }

    @Resource
    private CmsTagDao dao;

}