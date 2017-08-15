package org.publiccms.common.base;

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.StringUtils.split;
import static org.publiccms.controller.api.ApiController.NEED_APP_TOKEN;
import static org.publiccms.controller.api.ApiController.NEED_LOGIN;
import static org.publiccms.controller.api.ApiController.UN_AUTHORIZED;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.publiccms.entities.sys.SysApp;
import org.publiccms.entities.sys.SysAppToken;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.entities.sys.SysUser;
import org.publiccms.entities.sys.SysUserToken;
import org.publiccms.logic.component.site.SiteComponent;
import org.publiccms.logic.service.sys.SysAppService;
import org.publiccms.logic.service.sys.SysAppTokenService;
import org.publiccms.logic.service.sys.SysUserService;
import org.publiccms.logic.service.sys.SysUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.common.base.Base;
import com.publiccms.common.directive.BaseHttpDirective;
import com.publiccms.common.handler.RenderHandler;

/**
 * 
 * BaseDirective 自定义接口指令基类
 *
 */
public abstract class AbstractAppDirective extends BaseHttpDirective implements Base  {
    /**
     * @param handler
     * @return
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
        if (needAppToken() && (null == (app = getApp(handler)) || empty(app.getAuthorizedApis())
                || !contains(split(app.getAuthorizedApis(), COMMA_DELIMITED), getName()))) {
            if (null == app) {
                handler.put("error", NEED_APP_TOKEN).render();
            } else {
                handler.put("error", UN_AUTHORIZED).render();
            }
        } else if (needUserToken() && null == (user = getUser(handler))) {
            handler.put("error", NEED_LOGIN).render();
        } else {
            execute(handler, app, user);
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

    /**
     * @param handler
     * @param app
     * @param user
     * @throws IOException
     * @throws Exception
     */
    public abstract void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception;

    /**
     * @return
     */
    public abstract boolean needAppToken();

    /**
     * @return
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
