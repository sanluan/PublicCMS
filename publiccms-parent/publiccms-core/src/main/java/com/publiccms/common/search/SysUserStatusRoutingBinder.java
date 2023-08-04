package com.publiccms.common.search;

import org.hibernate.search.mapper.pojo.bridge.binding.RoutingBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.RoutingBinder;

import com.publiccms.entities.sys.SysUser;

public class SysUserStatusRoutingBinder implements RoutingBinder {

    @Override
    public void bind(RoutingBindingContext context) {
        context.dependencies().use("disabled");
        context.bridge(SysUser.class, new SysUserStatusRoutingBridge());
    }
}
