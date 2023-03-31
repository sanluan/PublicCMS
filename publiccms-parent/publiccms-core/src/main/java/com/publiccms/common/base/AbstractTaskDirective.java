package com.publiccms.common.base;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
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

import freemarker.template.TemplateException;

/**
 * 
 * AbstractTemplateDirective 自定义模板指令基类
 *
 */
public abstract class AbstractTaskDirective extends BaseTemplateDirective {
    /**
     * @param handler
     * @return site
     * @throws TemplateException 
     */
    public SysSite getSite(RenderHandler handler) throws TemplateException {
        return (SysSite) handler.getAttribute(CommonConstants.getAttributeSite());
    }

    @Override
    public void execute(HttpMessageConverter<Object> httpMessageConverter, MediaType mediaType, HttpServletRequest request,
            HttpServletResponse response) throws IOException, TemplateException {
        HttpParameterHandler handler = new HttpParameterHandler(httpMessageConverter, mediaType, request, response);
        SysApp app = null;
        if (null == (app = getApp(handler))) {
            handler.put(CommonConstants.ERROR, ApiController.NEED_APP_TOKEN).render();
        } else if (CommonUtils.empty(app.getAuthorizedApis())
                || !ArrayUtils.contains(StringUtils.split(app.getAuthorizedApis(), Constants.COMMA), getName())) {
            handler.put(CommonConstants.ERROR, ApiController.UN_AUTHORIZED).render();
        } else {
            execute(handler);
            if (!handler.getRenderd()) {
                handler.render();
            }
        }
    }

    protected SysApp getApp(RenderHandler handler) throws TemplateException {
        SysAppToken appToken = appTokenService.getEntity(handler.getString("appToken"));
        if (null != appToken && (null == appToken.getExpiryDate() || CommonUtils.getDate().before(appToken.getExpiryDate()))) {
            SysApp app = appService.getEntity(appToken.getAppId());
            if (app.getSiteId() == getSite(handler).getId()) {
                return app;
            }
        }
        return null;
    }

    @Resource
    private SysAppTokenService appTokenService;
    @Resource
    private SysAppService appService;
    @Resource
    protected SiteComponent siteComponent;
}