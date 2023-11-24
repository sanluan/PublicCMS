package com.publiccms.common.search;

import org.hibernate.search.mapper.pojo.bridge.RoutingBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.RoutingBridgeRouteContext;
import org.hibernate.search.mapper.pojo.route.DocumentRoutes;

import com.publiccms.common.database.CmsDataSource;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysUser;

/**
 *
 * SysUserStatusRoutingBridge
 * 
 */
public class SysUserStatusRoutingBridge implements RoutingBridge<SysUser> {

    @Override
    public void route(DocumentRoutes routes, Object entityIdentifier, SysUser indexedEntity, RoutingBridgeRouteContext context) {
        if (CommonUtils.empty(CmsDataSource.getDataSourceName()) && !indexedEntity.isDisabled()) {
            routes.addRoute();
        } else {
            routes.notIndexed();
        }

    }

    @Override
    public void previousRoutes(DocumentRoutes routes, Object entityIdentifier, SysUser indexedEntity,
            RoutingBridgeRouteContext context) {
        routes.addRoute();
    }
}