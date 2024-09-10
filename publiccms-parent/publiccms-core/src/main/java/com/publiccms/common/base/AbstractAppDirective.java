package com.publiccms.common.base;

import static com.publiccms.common.base.AbstractTemplateDirective.APP_TOKEN;
import static com.publiccms.common.base.AbstractTemplateDirective.AUTH_TOKEN;
import static com.publiccms.common.base.AbstractTemplateDirective.AUTH_USER_ID;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.directive.BaseHttpDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.controller.api.ApiController;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysAppToken;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.service.sys.SysAppService;
import com.publiccms.logic.service.sys.SysAppTokenService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;

import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

/**
 * 
 * BaseDirective 自定义接口指令基类
 *
 */
public abstract class AbstractAppDirective extends BaseHttpDirective {
    /**
     * @param handler
     * @return site
     * @throws TemplateModelException
     */
    public SysSite getSite(RenderHandler handler) throws TemplateModelException {
        return (SysSite) handler.getAttribute(CommonConstants.getAttributeSite());
    }

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        SysApp app = null;
        SysUser user = null;
        if (needAppToken() && (null == (app = getApp(handler)) || CommonUtils.empty(app.getAuthorizedApis())
                || !ArrayUtils.contains(StringUtils.split(app.getAuthorizedApis(), Constants.COMMA), getName()))) {
            if (null == app) {
                handler.put(CommonConstants.ERROR, ApiController.NEED_APP_TOKEN).render();
            } else {
                handler.put(CommonConstants.ERROR, ApiController.UN_AUTHORIZED).render();
            }
        } else if (needUserToken() && null == (user = getUser(handler))) {
            handler.put(CommonConstants.ERROR, ApiController.NEED_LOGIN).render();
        } else {
            execute(handler, app, user);
            if (!handler.getRenderd()) {
                handler.render();
            }
        }
    }

    protected SysApp getApp(RenderHandler handler) throws TemplateModelException {
        String appToken = (String) handler.getAttribute(APP_TOKEN);
        if (null == appToken) {
            appToken = handler.getString(APP_TOKEN);
        }
        SysAppToken token = appTokenService.getEntity(appToken);
        if (null != token && (null == token.getExpiryDate() || CommonUtils.getDate().before(token.getExpiryDate()))) {
            SysApp app = appService.getEntity(token.getAppId());
            if (app.getSiteId() == getSite(handler).getId()) {
                return app;
            }
        }
        return null;
    }

    protected SysUser getUser(RenderHandler handler) throws TemplateModelException {
        String authToken = (String) handler.getAttribute(AUTH_TOKEN);
        Long authUserId = (Long) handler.getAttribute(AUTH_USER_ID);
        if (null == authToken && null == authUserId) {
            authToken = handler.getString(AUTH_TOKEN);
            authUserId = handler.getLong(AUTH_USER_ID);
        }
        if (CommonUtils.notEmpty(authToken) && null != authUserId) {
            SysUserToken sysUserToken = sysUserTokenService.getEntity(authToken);
            if (null != sysUserToken
                    && (null == sysUserToken.getExpiryDate() || CommonUtils.getDate().before(sysUserToken.getExpiryDate()))
                    && authUserId.equals(sysUserToken.getUserId())) {
                SysUser user = sysUserService.getEntity(sysUserToken.getUserId());
                if (user.getSiteId() == getSite(handler).getId() && !user.isDisabled()) {
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * @param handler
     * @param app
     * @param user
     * @throws IOException
     * @throws TemplateException
     */
    public abstract void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, TemplateException;

    /**
     * @return whether need the app token
     */
    public abstract boolean needAppToken();

    /**
     * @return whether need the user token
     */
    public abstract boolean needUserToken();

    @Resource
    private SysUserTokenService sysUserTokenService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysAppTokenService appTokenService;
    @Resource
    private SysAppService appService;
}
