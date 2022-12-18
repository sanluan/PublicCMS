package com.publiccms.controller.web.oauth;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.api.Config;
import com.publiccms.common.api.oauth.OauthGateway;
import com.publiccms.common.base.oauth.AbstractOauth;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.controller.web.LoginController;
import com.publiccms.entities.log.LogLogin;
import com.publiccms.entities.sys.SysAppClient;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.SiteConfigComponent;
import com.publiccms.logic.component.oauth.OauthComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysAppClientService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;
import com.publiccms.views.pojo.oauth.OauthAccess;
import com.publiccms.views.pojo.oauth.OauthUser;

@Controller
@RequestMapping("oauth")
public class OauthController {
    protected final Log log = LogFactory.getLog(getClass());
    /**
     * 
     */
    public final static String STATE_COOKIE_NAME = "oauth_state";
    /**
     * 
     */
    public final static String RETURN_URL = "oauth_return_url";

    @Resource
    private OauthComponent oauthComponent;
    @Resource
    private ConfigComponent configComponent;
    @Resource
    protected SiteConfigComponent siteConfigComponent;
    @Resource
    private SysAppClientService appClientService;
    @Resource
    private SysUserTokenService sysUserTokenService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private LogLoginService logLoginService;
    @Resource
    protected SiteComponent siteComponent;

    /**
     * @param channel
     * @param site
     * @param returnUrl
     * @param request
     * @param response
     * @return view name
     */
    @RequestMapping(value = "login/{channel}")
    public String login(@PathVariable("channel") String channel, @RequestAttribute SysSite site, String returnUrl,
            HttpServletRequest request, HttpServletResponse response) {
        OauthGateway oauthGateway = oauthComponent.get(channel);
        if (null != oauthGateway && oauthGateway.enabled(site.getId())) {
            String state = UUID.randomUUID().toString();
            RequestUtils.addCookie(request.getContextPath(), request.getScheme(), response, STATE_COOKIE_NAME, state, null, null);
            returnUrl = siteConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
            RequestUtils.addCookie(request.getContextPath(), request.getScheme(), response, RETURN_URL, returnUrl, null, null);
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + oauthGateway.getAuthorizeUrl(site.getId(), state);
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + site.getDynamicPath();
    }

    /**
     * @param channel
     * @param site
     * @param state
     * @param code
     * @param request
     * @param session
     * @param response
     * @param model
     * @return view name
     */
    @RequestMapping(value = "callback/{channel}")
    public String callback(@PathVariable("channel") String channel, @RequestAttribute SysSite site, String state, String code,
            HttpServletRequest request, HttpSession session, HttpServletResponse response, ModelMap model) {
        OauthGateway oauthGateway = oauthComponent.get(channel);
        Cookie cookie = RequestUtils.getCookie(request.getCookies(), RETURN_URL);
        RequestUtils.cancleCookie(request.getContextPath(), request.getScheme(), response, RETURN_URL, null);
        String returnUrl;
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String safeReturnUrl = config.get(SiteConfigComponent.CONFIG_RETURN_URL);
        if (null != cookie && CommonUtils.notEmpty(cookie.getValue())
                && !SiteConfigComponent.isUnSafeUrl(cookie.getValue(), site, safeReturnUrl, request.getContextPath())) {
            returnUrl = cookie.getValue();
        } else {
            returnUrl = site.isUseStatic() ? site.getSitePath() : site.getDynamicPath();
        }

        Cookie stateCookie = RequestUtils.getCookie(request.getCookies(), STATE_COOKIE_NAME);
        RequestUtils.cancleCookie(request.getContextPath(), request.getScheme(), response, STATE_COOKIE_NAME, null);
        if (null != oauthGateway && oauthGateway.enabled(site.getId()) && null != stateCookie && null != state
                && state.equals(stateCookie.getValue())) {
            try {
                OauthAccess oauthAccess = oauthGateway.getOpenId(site.getId(), code);
                if (null != oauthAccess && null != oauthAccess.getOpenId()) {
                    SysAppClient appClient = appClientService.getEntity(site.getId(), channel, oauthAccess.getOpenId());
                    String ip = RequestUtils.getIpAddress(request);
                    SysUser user = ControllerUtils.getUserFromSession(session);
                    if (null == user) {
                        Date now = CommonUtils.getDate();
                        if (null == appClient) {
                            OauthUser oauthUser = oauthGateway.getUserInfo(site.getId(), oauthAccess);
                            Map<String, String> oauthConfig = configComponent.getConfigData(site.getId(),
                                    AbstractOauth.CONFIG_CODE);
                            if (null != oauthUser && CommonUtils.notEmpty(oauthConfig)
                                    && CommonUtils.notEmpty(config.get(SiteConfigComponent.CONFIG_REGISTER_URL))) {
                                appClient = new SysAppClient(site.getId(), channel, oauthAccess.getOpenId(),
                                        CommonUtils.getDate(), false);
                                appClient.setClientVersion(CmsVersion.getVersion());
                                appClient.setLastLoginIp(ip);
                                appClientService.save(appClient);
                                model.addAttribute("nickname", oauthUser.getNickname());
                                model.addAttribute("clientId", appClient.getId());
                                model.addAttribute("uuid", oauthAccess.getOpenId());
                                model.addAttribute("returnUrl", returnUrl);
                                return UrlBasedViewResolver.REDIRECT_URL_PREFIX
                                        + config.get(SiteConfigComponent.CONFIG_REGISTER_URL);
                            }
                        } else if (null != appClient.getUserId() && !appClient.isDisabled()) {// 有授权则登录
                            appClientService.updateLastLogin(appClient.getId(), CmsVersion.getVersion(), ip);
                            int expiryMinutes = ConfigComponent.getInt(config.get(SiteConfigComponent.CONFIG_EXPIRY_MINUTES_WEB),
                                    SiteConfigComponent.DEFAULT_EXPIRY_MINUTES);
                            user = sysUserService.getEntity(appClient.getUserId());
                            if (null != user && !user.isDisabled()) {
                                String loginToken = UUID.randomUUID().toString();
                                sysUserTokenService.save(new SysUserToken(loginToken, site.getId(), user.getId(),
                                        LogLoginService.CHANNEL_WEB, now, DateUtils.addMinutes(now, expiryMinutes), ip));
                                LoginController.addLoginStatus(user, loginToken, request, response, expiryMinutes);
                                sysUserService.updateLoginStatus(user.getId(), ip);
                                logLoginService.save(
                                        new LogLogin(site.getId(), user.getName(), user.getId(), ip, channel, true, now, null));
                            }
                        }
                    } else {
                        if (null == appClient) {
                            appClient = new SysAppClient(site.getId(), channel, oauthAccess.getOpenId(), CommonUtils.getDate(),
                                    false);
                            appClient.setClientVersion(CmsVersion.getVersion());
                            appClient.setLastLoginIp(ip);
                            appClient.setUserId(user.getId());
                            appClientService.save(appClient);
                        } else if (null == appClient.getUserId() || !appClient.getUserId().equals(user.getId())) {// 有授权则登录
                            appClientService.updateUser(appClient.getId(), user.getId());
                        }
                    }
                }
            } catch (IOException e) {
                log.error(e);
            }
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
    }
}
