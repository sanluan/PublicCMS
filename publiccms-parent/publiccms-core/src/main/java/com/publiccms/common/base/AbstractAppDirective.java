package com.publiccms.common.base;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.directive.BaseHttpDirective;
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
 * BaseDirective 自定义接口指令基类
 *
 */
public abstract class AbstractAppDirective extends BaseHttpDirective {
    /**
     * @param handler
     * @return site
     * @throws IOException
     * @throws Exception
     */
    public SysSite getSite(RenderHandler handler) throws IOException, Exception {
        HttpServletRequest request = handler.getRequest();
        return siteComponent.getSite(request.getServerName());
    }

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        SysApp app = null;
        SysUser user = null;
        if (needAppToken() && (null == (app = getApp(handler)) || CommonUtils.empty(app.getAuthorizedApis()) || !ArrayUtils
                .contains(StringUtils.split(app.getAuthorizedApis(), CommonConstants.COMMA_DELIMITED), getName()))) {
            if (null == app) {
                handler.put("error", ApiController.NEED_APP_TOKEN).render();
            } else {
                handler.put("error", ApiController.UN_AUTHORIZED).render();
            }
        } else if (needUserToken() && null == (user = getUser(handler))) {
            handler.put("error", ApiController.NEED_LOGIN).render();
        } else {
            execute(handler, app, user);
            handler.render();
        }
    }

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

    protected SysUser getUser(RenderHandler handler) throws Exception {
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
     * @param handler
     * @param app
     * @param user
     * @throws IOException
     * @throws Exception
     */
    public abstract void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception;

    /**
     * @return whether need the app token
     */
    public abstract boolean needAppToken();

    /**
     * @return whether need the user token
     */
    public abstract boolean needUserToken();

    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysAppTokenService appTokenService;
    @Autowired
    private SysAppService appService;
    @Autowired
    private SiteComponent siteComponent;
}
