package com.publiccms.common.base;

import static com.publiccms.logic.component.SiteComponent.CONTEXT_SITE;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysAppToken;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.SiteComponent;
import com.publiccms.logic.service.sys.SysAppService;
import com.publiccms.logic.service.sys.SysAppTokenService;
import com.sanluan.common.directive.BaseTemplateDirective;
import com.sanluan.common.directive.HttpDirective;
import com.sanluan.common.handler.HttpParameterHandler;
import com.sanluan.common.handler.RenderHandler;

/**
 * 
 * AbstractTemplateDirective 自定义模板指令基类
 *
 */
public abstract class AbstractTemplateDirective extends BaseTemplateDirective implements HttpDirective {
    public SysSite getSite(RenderHandler handler) throws Exception {
        return (SysSite) handler.getAttribute(CONTEXT_SITE);
    }

    @Override
    public void execute(HttpMessageConverter<Object> httpMessageConverter, MediaType mediaType, HttpServletRequest request,
            String callback, HttpServletResponse response) throws IOException, Exception {
        HttpParameterHandler handler = new HttpParameterHandler(httpMessageConverter, mediaType, request, callback, response);
        if (needAppToken() && empty(getApp(handler))) {
            handler.put("error", "needAppToken").render();
        } else {
            execute(handler);
            handler.render();
        }
    }

    private SysApp getApp(RenderHandler handler) throws Exception {
        SysAppToken appToken = appTokenService.getEntity(handler.getString("appToken"));
        if (notEmpty(appToken)) {
            return appService.getEntity(appToken.getAppId());
        }
        return null;
    }

    public boolean needAppToken() {
        return false;
    }

    @Autowired
    private SysAppTokenService appTokenService;
    @Autowired
    private SysAppService appService;
    @Autowired
    protected SiteComponent siteComponent;
}