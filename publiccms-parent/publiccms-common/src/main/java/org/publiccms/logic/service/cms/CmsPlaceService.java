package org.publiccms.logic.service.cms;

// Generated 2015-12-24 10:49:03 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.getDate;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import org.publiccms.entities.cms.CmsPlace;
import org.publiccms.logic.dao.cms.CmsPlaceDao;
import org.publiccms.views.pojo.CmsPlaceStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * CmsPlaceService
 * 
 */
@Service
@Transactional
public class CmsPlaceService extends BaseService<CmsPlace> {

    /**
     * 
     */
    public static final int STATUS_CONTRIBUTE = 0;
    /**
     * 
     */
    public static final int STATUS_NORMAL = 1;
    /**
     * 
     */
    public static final String ITEM_TYPE_CONTENT = "content";
    /**
     * 
     */
    public static final String ITEM_TYPE_CATEGORY = "category";
    /**
     * 
     */
    public static final String ITEM_TYPE_CUSTOM = "custom";

    /**
     * @param siteId
     * @param userId
     * @param path
     * @param itemType
     * @param itemId
     * @param startPublishDate
     * @param endPublishDate
     * @param status
     * @param disabled
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Long userId, String path, String itemType, Integer itemId, Date startPublishDate,
            Date endPublishDate, Integer status, Boolean disabled, String orderField, String orderType, Integer pageIndex,
            Integer pageSize) {
        return dao.getPage(siteId, userId, path, itemType, itemId, startPublishDate, endPublishDate, status, disabled, orderField,
                orderType, pageIndex, pageSize);
    }

    /**
     * @param entitys
     */
    public void updateStatistics(Collection<CmsPlaceStatistics> entitys) {
        for (CmsPlaceStatistics entityStatistics : entitys) {
            CmsPlace entity = getEntity(entityStatistics.getId());
            if (null != entity) {
                entity.setClicks(entity.getClicks() + entityStatistics.getClicks());
            }
        }
    }

    /**
     * @param id
     */
    public void check(Serializable id) {
        CmsPlace entity = getEntity(id);
        if (null != entity) {
            entity.setStatus(STATUS_NORMAL);
            Date now = getDate();
            if (now.after(entity.getPublishDate())) {
                entity.setPublishDate(now);
            }
        }
    }

    /**
     * @param siteId
     * @param ids
     */
    public void check(int siteId, Serializable[] ids) {
        Date now = getDate();
        for (CmsPlace entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && STATUS_CONTRIBUTE == entity.getStatus()) {
                entity.setStatus(STATUS_NORMAL);
                if (now.after(entity.getPublishDate())) {
                    entity.setPublishDate(now);
                }
            }
        }
    }

    /**
     * @param siteId
     * @param ids
     */
    public void refresh(int siteId, Serializable[] ids) {
        Date now = getDate();
        for (CmsPlace entity : getEntitys(ids)) {
            if (null != entity && STATUS_NORMAL == entity.getStatus() && siteId == entity.getSiteId()) {
                if (now.after(entity.getPublishDate())) {
                    entity.setPublishDate(now);
                }
            }
        }
    }

    public void delete(Serializable id) {
        CmsPlace entity = getEntity(id);
        if (null != entity) {
            entity.setDisabled(true);
        }
    }

    /**
     * @param siteId
     * @param ids
     */
    public void delete(int siteId, Serializable[] ids) {
        for (CmsPlace entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && !entity.isDisabled()) {
                entity.setDisabled(true);
            }
        }
    }

    /**
     * @param siteId
     * @param path
     * @return
     */
    public int delete(int siteId, String path) {
        return dao.delete(siteId, path);
    }

    @Autowired
    private CmsPlaceDao dao;

}