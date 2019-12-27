package com.publiccms.common.base;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.directive.BaseTemplateDirective;
import com.publiccms.common.handler.HttpParameterHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.controller.api.ApiController;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysAppToken;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.sys.SysAppService;
import com.publiccms.logic.service.sys.SysAppTokenService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;

/**
 * 
 * AbstractTemplateDirective 自定义模板指令基类
 *
 */
public abstract class AbstractTemplateDirective extends BaseTemplateDirective {
    /**
     * @param handler
     * @return site
     * @throws Exception
     */
    protected SysSite getSite(RenderHandler handler) throws Exception {
        return (SysSite) handler.getAttribute(AbstractFreemarkerView.CONTEXT_SITE);
    }

    /**
     * @param model
     * @param handler
     * @throws IOException
     * @throws Exception
     */
    protected void expose(RenderHandler handler, Map<String, Object> model) throws IOException, Exception {
        HttpServletRequest request = handler.getRequest();
        if (null != request) {
            Enumeration<String> parameters = request.getParameterNames();
            while (parameters.hasMoreElements()) {
                String parameterName = parameters.nextElement();
                String[] values = request.getParameterValues(parameterName);
                if (CommonUtils.notEmpty(values)) {
                    if (1 < values.length) {
                        model.put(parameterName, values);
                    } else {
                        model.put(parameterName, values[0]);
                    }
                }
            }
            AbstractFreemarkerView.exposeAttribute(model, request);
        } else {
            AbstractFreemarkerView.exposeSite(model, getSite(handler));
        }
    }

    @Override
    public void execute(HttpMessageConverter<Object> httpMessageConverter, MediaType mediaType, HttpServletRequest request,
            HttpServletResponse response) throws IOException, Exception {
        HttpParameterHandler handler = new HttpParameterHandler(httpMessageConverter, mediaType, request, response);
        SysApp app = null;
        if (needAppToken() && (null == (app = getApp(handler)) || CommonUtils.empty(app.getAuthorizedApis()) || !ArrayUtils
                .contains(StringUtils.split(app.getAuthorizedApis(), CommonConstants.COMMA_DELIMITED), getName()))) {
            if (null == app) {
                handler.put("error", ApiController.NEED_APP_TOKEN).render();
            } else {
                handler.put("error", ApiController.UN_AUTHORIZED).render();
            }
        } else if (needUserToken() && null == getUser(handler)) {
            handler.put("error", ApiController.NEED_LOGIN).render();
        } else {
            execute(handler);
            handler.render();
        }
    }

    /**
     * @param handler
     * @return user
     * @throws Exception
     */
    private SysUser getUser(RenderHandler handler) throws Exception {
        String authToken = handler.getString("authToken");
        Long authUserId = handler.getLong("authUserId");
        if (CommonUtils.notEmpty(authToken) && null != authUserId) {
            SysUserToken sysUserToken = sysUserTokenService.getEntity(authToken);
            if (null != sysUserToken
                    && (null == sysUserToken.getExpiryDate() || CommonUtils.getDate().before(sysUserToken.getExpiryDate()))
                    && authUserId.equals(sysUserToken.getUserId())) {
                return sysUserService.getEntity(sysUserToken.getUserId());
            }
        }
        return null;
    }

    /**
     * @return whether to enable http
     */
    public boolean httpEnabled() {
        return true;
    }

    /**
     * @return whether need the app token
     */
    public boolean needAppToken() {
        return false;
    }

    /**
     * @return whether need the user token
     */
    public boolean needUserToken() {
        return false;
    }

    /**
     * @param handler
     * @return
     * @throws Exception
     */
    protected SysApp getApp(RenderHandler handler) throws Exception {
        SysAppToken appToken = appTokenService.getEntity(handler.getString("appToken"));
        if (null != appToken && (null == appToken.getExpiryDate() || CommonUtils.getDate().before(appToken.getExpiryDate()))) {
            SysApp app = appService.getEntity(appToken.getAppId());
            if (app.getSiteId() == getSite(handler).getId()) {
                return app;
            }
        }
        return null;
    }

    @Autowired
    private SysAppTokenService appTokenService;
    @Autowired
    private SysAppService appService;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    protected SiteComponent siteComponent;
}