package com.publiccms.common.base;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import com.publiccms.common.directive.BaseTemplateDirective;
import com.publiccms.common.handler.HttpParameterHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.controller.api.ApiController;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysAppToken;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.sys.SysAppService;
import com.publiccms.logic.service.sys.SysAppTokenService;

/**
 * 
 * AbstractTemplateDirective 自定义模板指令基类
 *
 */
public abstract class AbstractTaskDirective extends BaseTemplateDirective implements Base {
    /**
     * @param handler
     * @return site
     * @throws Exception
     */
    public SysSite getSite(RenderHandler handler) throws Exception {
        return (SysSite) handler.getAttribute(AbstractFreemarkerView.CONTEXT_SITE);
    }

    @Override
    public void execute(HttpMessageConverter<Object> httpMessageConverter, MediaType mediaType, HttpServletRequest request,
            String callback, HttpServletResponse response) throws IOException, Exception {
        HttpParameterHandler handler = new HttpParameterHandler(httpMessageConverter, mediaType, request, callback, response);
        SysApp app = null;
        if (null == (app = getApp(handler))) {
            handler.put("error", ApiController.NEED_APP_TOKEN).render();
        } else if (CommonUtils.empty(app.getAuthorizedApis())
                || !ArrayUtils.contains(StringUtils.split(app.getAuthorizedApis(), COMMA_DELIMITED), getName())) {
            handler.put("error", ApiController.UN_AUTHORIZED).render();
        } else {
            execute(handler);
            handler.render();
        }
    }

    protected SysApp getApp(RenderHandler handler) throws Exception {
        SysAppToken appToken = appTokenService.getEntity(handler.getString("appToken"));
        if (null != appToken) {
            return appService.getEntity(appToken.getAppId());
        }
        return null;
    }

    @Autowired
    private SysAppTokenService appTokenService;
    @Autowired
    private SysAppService appService;
    @Autowired
    protected SiteComponent siteComponent;
}