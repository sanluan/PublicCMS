package com.publiccms.views.api;

//Generated 2015-5-10 17:54:56 by com.sanluan.common.source.SourceMaker

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppV1Directive;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.StatisticsComponent;
import com.publiccms.views.pojo.CmsContentStatistics;
import com.sanluan.common.handler.RenderHandler;

@Component
public class ContentClickDirective extends AbstractAppV1Directive {
    @Autowired
    private StatisticsComponent statisticsComponent;

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        Long id = handler.getLong("id");
        CmsContentStatistics contentStatistics = statisticsComponent.clicks(id);
        if (notEmpty(contentStatistics) && notEmpty(contentStatistics.getEntity())) {
            handler.put("clicks", contentStatistics.getEntity().getClicks() + contentStatistics.getClicks()).render();
        } else {
            handler.put("error", REQUIRED_PARAMTER + "id").render();
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