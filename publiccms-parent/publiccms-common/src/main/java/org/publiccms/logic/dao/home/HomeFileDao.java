package org.publiccms.logic.dao.home;

// Generated 2016-11-13 11:38:14 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import org.publiccms.entities.home.HomeFile;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * HomeFileDao
 * 
 */
@Repository
public class HomeFileDao extends BaseDao<HomeFile> {

    /**
     * @param siteId
     * @param userId
     * @param directoryId
     * @param title
     * @param filePath
     * @param image
     * @param disabled
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Integer siteId, Long userId, Long directoryId, String title, String filePath, Boolean image,
            Boolean disabled, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeFile bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(directoryId)) {
            queryHandler.condition("bean.directoryId = :directoryId").setParameter("directoryId", directoryId);
        }
        if (notEmpty(title)) {
            queryHandler.condition("bean.title like :title").setParameter("title", like(title));
        }
        if (notEmpty(filePath)) {
            queryHandler.condition("bean.filePath = :filePath").setParameter("filePath", filePath);
        }
        if (notEmpty(image)) {
            queryHandler.condition("bean.image = :image").setParameter("image", image);
        }
        if (notEmpty(disabled)) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = BLANK;
        }
        switch (orderField) {
        case "fileSize":
            queryHandler.order("bean.fileSize " + orderType);
            break;
        case "scores":
            queryHandler.order("bean.scores " + orderType);
            break;
        case "comments":
            queryHandler.order("bean.comments " + orderType);
            break;
        case "createDate":
            queryHandler.order("bean.createDate " + orderType);
            break;
        default:
            queryHandler.order("bean.id " + orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeFile init(HomeFile entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}