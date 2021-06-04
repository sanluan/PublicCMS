package com.publiccms.common.search;

import org.hibernate.search.mapper.pojo.bridge.binding.RoutingBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.RoutingBinder;

import com.publiccms.entities.cms.CmsContent;

public class CmsContentStatusRoutingBinder implements RoutingBinder {

    @Override
    public void bind(RoutingBindingContext context) {
        context.dependencies().use("status").use("quoteContentId").use("disabled");
        context.bridge(CmsContent.class, new CmsContentStatusRoutingBridge());
    }
}
