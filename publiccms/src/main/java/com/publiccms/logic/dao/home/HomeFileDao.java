package com.publiccms.logic.dao.home;

// Generated 2016-11-13 11:38:14 by com.sanluan.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.entities.home.HomeFile;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class HomeFileDao extends BaseDao<HomeFile> {
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
        if ("asc".equalsIgnoreCase(orderType)) {
            orderType = "asc";
        } else {
            orderType = "desc";
        }
        if (null == orderField) {
            orderField = "";
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