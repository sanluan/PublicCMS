package com.publiccms.views.directive.tools;

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 *
 * masterSite 管理站点判断指令
 * <p>
 * 使用示例
 * <p>
 * &lt;@tools.masterSite&gt;this is a master site&lt;/@tools.masterSite&gt;
 */
@Component
public class MasterSiteDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        if (siteComponent.isMaster(getSite(handler).getId())) {
            handler.render();
        }
    }
}