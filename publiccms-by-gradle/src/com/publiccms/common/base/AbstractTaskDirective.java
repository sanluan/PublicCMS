package com.publiccms.common.base;

import static com.publiccms.logic.component.SiteComponent.CONTEXT_SITE;

import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.SiteComponent;
import com.sanluan.common.directive.BaseTemplateDirective;
import com.sanluan.common.handler.RenderHandler;

/**
 * 
 * AbstractTemplateDirective 自定义模板指令基类
 *
 */
public abstract class AbstractTaskDirective extends BaseTemplateDirective {
    public SysSite getSite(RenderHandler handler) throws Exception {
        return (SysSite) handler.getAttribute(CONTEXT_SITE);
    }

    @Autowired
    protected SiteComponent siteComponent;
}