package com.publiccms.views.directive.api;

//Generated 2015-5-10 17:54:56 by com.sanluan.common.source.SourceGenerator

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.views.pojo.CmsContentStatistics;
import com.sanluan.common.handler.RenderHandler;

@Component
public class ContentClickDirective extends AbstractAppDirective {
    @Autowired
    private StatisticsComponent statisticsComponent;

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        Long id = handler.getLong("id");
        CmsContentStatistics contentStatistics = statisticsComponent.clicks(id);
        if (null != contentStatistics && null != contentStatistics.getEntity()) {
            handler.put("clicks", contentStatistics.getEntity().getClicks() + contentStatistics.getClicks());
        }
    }

    @Override
    public boolean needUserToken() {
        return false;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }

}