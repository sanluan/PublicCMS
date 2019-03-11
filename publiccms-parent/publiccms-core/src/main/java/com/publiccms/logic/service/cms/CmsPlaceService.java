package com.publiccms.logic.service.cms;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.logic.dao.cms.CmsPlaceDao;
import com.publiccms.views.pojo.entities.ClickStatistics;

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
    public static final int STATUS_DRAFT = 0;
    /**
     * 
     */
    public static final int STATUS_NORMAL = 1;
    /**
     * 
     */
    public static final Integer[] STATUS_NORMAL_ARRAY = new Integer[] { STATUS_NORMAL };
    /**
     * 
     */
    public static final int STATUS_PEND = 2;
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
     * @param expiryDate
     * @param status
     * @param disabled
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Long userId, String path, String itemType, Long itemId, Date startPublishDate,
            Date endPublishDate, Date expiryDate, Integer[] status, Boolean disabled, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, path, itemType, itemId, startPublishDate, endPublishDate, expiryDate, status, disabled,
                orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param entitys
     */
    public void updateStatistics(Collection<ClickStatistics> entitys) {
        for (ClickStatistics entityStatistics : entitys) {
            CmsPlace entity = getEntity(entityStatistics.getId());
            if (null != entity) {
                entity.setClicks(entity.getClicks() + entityStatistics.getClicks());
            }
        }
    }

    /**
     * @param id
     * @param userId
     */
    public void check(Serializable id, Long userId) {
        CmsPlace entity = getEntity(id);
        if (null != entity && STATUS_PEND == entity.getStatus()) {
            entity.setStatus(STATUS_NORMAL);
            entity.setCheckUserId(userId);
        }
    }

    /**
     * @param id
     */
    public void uncheck(Serializable id) {
        CmsPlace entity = getEntity(id);
        if (null != entity && STATUS_NORMAL == entity.getStatus()) {
            entity.setStatus(STATUS_PEND);
        }
    }

    /**
     * @param siteId
     * @param userId
     * @param ids
     * @param path
     */
    public void check(short siteId, Long userId, Serializable[] ids, String path) {
        for (CmsPlace entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && STATUS_PEND == entity.getStatus() && path.equals(entity.getPath())) {
                entity.setStatus(STATUS_NORMAL);
                entity.setCheckUserId(userId);
            }
        }
    }

    /**
     * @param siteId
     * @param ids
     * @param path
     */
    public void uncheck(short siteId, Serializable[] ids, String path) {
        for (CmsPlace entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && STATUS_NORMAL == entity.getStatus() && path.equals(entity.getPath())) {
                entity.setStatus(STATUS_PEND);
            }
        }
    }

    /**
     * @param siteId
     * @param ids
     * @param path
     */
    public void refresh(short siteId, Serializable[] ids, String path) {
        Date now = CommonUtils.getDate();
        for (CmsPlace entity : getEntitys(ids)) {
            if (null != entity && siteId == entity.getSiteId() && path.equals(entity.getPath())) {
                if (now.after(entity.getPublishDate())) {
                    entity.setPublishDate(now);
                }
            }
        }
    }

    @Override
    public void delete(Serializable id) {
        CmsPlace entity = getEntity(id);
        if (null != entity) {
            entity.setDisabled(true);
        }
    }

    /**
     * @param siteId
     * @param ids
     * @param path
     */
    public void delete(short siteId, Serializable[] ids, String path) {
        for (CmsPlace entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && !entity.isDisabled() && path.equals(entity.getPath())) {
                entity.setDisabled(true);
            }
        }
    }

    /**
     * @param siteId
     * @param path
     * @return number of data deleted
     */
    public int delete(short siteId, String path) {
        return dao.delete(siteId, path);
    }

    @Autowired
    private CmsPlaceDao dao;

}