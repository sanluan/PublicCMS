package com.publiccms.common.search;

import org.hibernate.search.mapper.pojo.bridge.RoutingBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.RoutingBridgeRouteContext;
import org.hibernate.search.mapper.pojo.route.DocumentRoutes;

import com.publiccms.common.database.CmsDataSource;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.logic.component.BeanComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.views.pojo.entities.CmsModel;

/**
 *
 * CmsContentStatusRoutingBridge
 * 
 */
public class CmsContentStatusRoutingBridge implements RoutingBridge<CmsContent> {

    @Override
    public void route(DocumentRoutes routes, Object entityIdentifier, CmsContent indexedEntity,
            RoutingBridgeRouteContext context) {
        ModelComponent modelComponent = BeanComponent.getModelComponent();
        SiteComponent siteComponent = BeanComponent.getSiteComponent();
        CmsModel model = modelComponent.getModel(siteComponent.getSiteById(indexedEntity.getSiteId()),
                indexedEntity.getModelId());
        if (CommonUtils.empty(CmsDataSource.getDataSourceName()) && null != model && model.isSearchableModel()
                && CmsContentService.STATUS_NORMAL == indexedEntity.getStatus()
                && (null != indexedEntity.getParentId() || null == indexedEntity.getQuoteContentId())
                && !indexedEntity.isDisabled()) {
            routes.addRoute();
        } else {
            routes.notIndexed();
        }

    }

    @Override
    public void previousRoutes(DocumentRoutes routes, Object entityIdentifier, CmsContent indexedEntity,
            RoutingBridgeRouteContext context) {
        routes.addRoute();
    }
}