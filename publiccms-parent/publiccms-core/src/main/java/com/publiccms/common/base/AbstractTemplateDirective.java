package com.publiccms.common.base;

import java.io.IOException;
import java.util.Map;

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
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.sys.SysAppService;
import com.publiccms.logic.service.sys.SysAppTokenService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;

import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

/**
 * 
 * AbstractTemplateDirective 自定义模板指令基类
 *
 */
public abstract class AbstractTemplateDirective extends BaseTemplateDirective {
    public static final String AUTH_TOKEN = "authToken";
    public static final String AUTH_USER_ID = "authUserId";
    public static final String ADVANCED = "advanced";

    /**
     * @param handler
     * @return site
     * @throws TemplateModelException
     */
    protected SysSite getSite(RenderHandler handler) throws TemplateModelException {
        return (SysSite) handler.getAttribute(CommonConstants.getAttributeSite());
    }

    /**
     * @param model
     * @param handler
     * @throws TemplateModelException
     */
    protected void expose(RenderHandler handler, Map<String, Object> model) throws TemplateModelException {
        AbstractFreemarkerView.exposeSite(model, getSite(handler));
    }

    protected boolean getAdvanced(RenderHandler handler) throws TemplateModelException {
        return handler.getBoolean(ADVANCED, false);
    }

    protected Long getUserId(RenderHandler handler, String name) throws TemplateModelException {
        if (needUserToken()) {
            Long authUserId = handler.getLong(AUTH_USER_ID);
            if (null != authUserId) {
                return authUserId;
            } else {
                return handler.getLong(name);
            }
        } else {
            return handler.getLong(name);
        }
    }

    @Override
    public void execute(HttpMessageConverter<Object> httpMessageConverter, MediaType mediaType, HttpServletRequest request,
            HttpServletResponse response) throws TemplateException, IOException {
        HttpParameterHandler handler = new HttpParameterHandler(httpMessageConverter, mediaType, request, response);
        SysApp app = null;
        if ((needAppToken() || supportAdvanced() && null != handler.getBoolean(ADVANCED))
                && (null == (app = getApp(handler)) || CommonUtils.empty(app.getAuthorizedApis())
                        || !ArrayUtils.contains(StringUtils.split(app.getAuthorizedApis(), Constants.COMMA), getName()))) {
            if (null == app) {
                handler.put(CommonConstants.ERROR, ApiController.NEED_APP_TOKEN).render();
            } else {
                handler.put(CommonConstants.ERROR, ApiController.UN_AUTHORIZED).render();
            }
        } else if (needUserToken() && null == getUser(handler)) {
            handler.put(CommonConstants.ERROR, ApiController.NEED_LOGIN).render();
        } else {
            execute(handler);
            if (!handler.renderd) {
                handler.render();
            }
        }
    }

    /**
     * @param handler
     * @return user
     * @throws TemplateModelException
     */
    private SysUser getUser(RenderHandler handler) throws TemplateModelException {
        String authToken = handler.getString(AUTH_TOKEN);
        Long authUserId = handler.getLong(AUTH_USER_ID);
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
     * @return support advanced parameters
     */
    public boolean supportAdvanced() {
        return false;
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
     * @return app
     * @throws TemplateModelException
     */
    protected SysApp getApp(RenderHandler handler) throws TemplateModelException {
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
    private SysUserTokenService sysUserTokenService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    protected SiteComponent siteComponent;
}