package com.publiccms.common.base;

import static com.publiccms.common.base.AbstractFreemarkerView.CONTEXT_SITE;
import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.StringUtils.split;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

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
import com.sanluan.common.directive.BaseTemplateDirective;
import com.sanluan.common.handler.HttpParameterHandler;
import com.sanluan.common.handler.RenderHandler;

/**
 * 
 * AbstractTemplateDirective 自定义模板指令基类
 *
 */
public abstract class AbstractTemplateDirective extends BaseTemplateDirective {
    public SysSite getSite(RenderHandler handler) throws Exception {
        return (SysSite) handler.getAttribute(CONTEXT_SITE);
    }

    @Override
    public void execute(HttpMessageConverter<Object> httpMessageConverter, MediaType mediaType, HttpServletRequest request,
            String callback, HttpServletResponse response) throws IOException, Exception {
        HttpParameterHandler handler = new HttpParameterHandler(httpMessageConverter, mediaType, request, callback, response);
        SysApp app = null;
        if (needAppToken() && (null == (app = getApp(handler)) || empty(app.getAuthorizedApis())
                || !contains(split(app.getAuthorizedApis(), COMMA_DELIMITED), getName()))) {
            if (null == app) {
                handler.put("error", "needAppToken").render();
            } else {
                handler.put("error", "unAuthorized").render();
            }
        } else if (needUserToken() && null == getUser(handler)) {
            handler.put("error", "needLogin").render();
        } else {
            execute(handler);
            handler.render();
        }
    }

    protected SysUser getUser(RenderHandler handler) throws Exception {
        String authToken = handler.getString("authToken");
        Long authUserId = handler.getLong("authUserId");
        if (notEmpty(authToken) && null != authUserId) {
            SysUserToken sysUserToken = sysUserTokenService.getEntity(authToken);
            if (null != sysUserToken && sysUserToken.getUserId() == authUserId) {
                return sysUserService.getEntity(sysUserToken.getUserId());
            }
        }
        return null;
    }

    public boolean needAppToken() {
        return false;
    }

    public boolean needUserToken() {
        return false;
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
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    protected SiteComponent siteComponent;
}