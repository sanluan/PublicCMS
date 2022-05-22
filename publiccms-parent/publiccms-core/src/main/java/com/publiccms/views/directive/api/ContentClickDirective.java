package com.publiccms.views.directive.api;

//Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.views.pojo.entities.ClickStatistics;

/**
 *
 * ContentClickDirective
 * 
 */
@Component
public class ContentClickDirective extends AbstractAppDirective {

    @Resource
    private StatisticsComponent statisticsComponent;

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        Long id = handler.getLong("id");
        ClickStatistics contentStatistics = statisticsComponent.contentClicks(getSite(handler), id);
        if (null != contentStatistics) {
            handler.put("clicks", contentStatistics.getOldClicks() + contentStatistics.getClicks());
        }
        handler.render();
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