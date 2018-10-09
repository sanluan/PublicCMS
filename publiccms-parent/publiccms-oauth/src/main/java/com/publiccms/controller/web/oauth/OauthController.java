package com.publiccms.controller.web.oauth;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.api.Config;
import com.publiccms.common.api.oauth.Oauth;
import com.publiccms.common.base.AbstractController;
import com.publiccms.common.base.oauth.AbstractOauth;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.controller.web.LoginController;
import com.publiccms.entities.log.LogLogin;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.LoginConfigComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;
import com.publiccms.view.pojo.oauth.OauthAccess;
import com.publiccms.view.pojo.oauth.OauthUser;

@Controller
@RequestMapping("oauth")
public class OauthController extends AbstractController {
    /**
     * 
     */
    public final static String STATE_COOKIE_NAME = "oauth_state";
    /**
     * 
     */
    public final static String RETURN_URL = "oauth_return_url";

    private Map<String, Oauth> oauthChannelMap = new HashMap<>();

    @Autowired
    private ConfigComponent configComponent;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private LogLoginService logLoginService;

    /**
     * @param channel
     * @param returnUrl
     * @param request
     * @param response
     * @return view name
     */
    @RequestMapping(value = "login/{channel}")
    public String login(@PathVariable("channel") String channel, String returnUrl, HttpServletRequest request,
            HttpServletResponse response) {
        Oauth oauthComponent = oauthChannelMap.get(channel);
        SysSite site = getSite(request);
        if (null != oauthComponent && oauthComponent.enabled(site.getId())) {
            String state = UUID.randomUUID().toString();
            RequestUtils.addCookie(request.getContextPath(), response, STATE_COOKIE_NAME, state, null, null);
            RequestUtils.addCookie(request.getContextPath(), response, RETURN_URL, returnUrl, null, null);
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + oauthComponent.getAuthorizeUrl(site.getId(), state);
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + site.getDynamicPath();
    }

    /**
     * @param channel
     * @param state
     * @param code
     * @param request
     * @param session
     * @param response 
     * @param model
     * @return view name
     */
    @RequestMapping(value = "callback/{channel}")
    public String callback(@PathVariable("channel") String channel, String state, String code, HttpServletRequest request,
            HttpSession session, HttpServletResponse response, ModelMap model) {
        Oauth oauthComponent = oauthChannelMap.get(channel);
        SysSite site = getSite(request);
        Cookie cookie = RequestUtils.getCookie(request.getCookies(), RETURN_URL);
        RequestUtils.cancleCookie(request.getContextPath(), response, RETURN_URL, null);
        String returnUrl;
        if (null != cookie && CommonUtils.notEmpty(cookie.getValue())) {
            returnUrl = cookie.getValue();
        } else {
            returnUrl = site.getDynamicPath();
        }

        Cookie stateCookie = RequestUtils.getCookie(request.getCookies(), STATE_COOKIE_NAME);
        RequestUtils.cancleCookie(request.getContextPath(), response, STATE_COOKIE_NAME, null);
        if (null != oauthComponent && oauthComponent.enabled(site.getId()) && null != stateCookie && null != state
                && state.equals(stateCookie.getValue())) {
            try {
                OauthAccess oauthAccess = oauthComponent.getOpenId(site.getId(), code);
                if (null != oauthAccess && null != oauthAccess.getOpenId()) {
                    String authToken = new StringBuilder(channel).append(CommonConstants.DOT).append(oauthAccess.getOpenId())
                            .toString();
                    Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
                    int expiryMinutes = ConfigComponent.getInt(config.get(LoginConfigComponent.CONFIG_EXPIRY_MINUTES_WEB),
                            LoginConfigComponent.DEFAULT_EXPIRY_MINUTES);
                    String ip = RequestUtils.getIpAddress(request);
                    Date now = CommonUtils.getDate();
                    SysUserToken entity = sysUserTokenService.getEntity(authToken);
                    if (null == entity) {
                        SysUser user = ControllerUtils.getUserFromSession(session);
                        if (null == user) { // 未登录则注册用户
                            OauthUser oauthUser = oauthComponent.getUserInfo(site.getId(), oauthAccess);
                            Map<String, String> oauthConfig = configComponent.getConfigData(site.getId(),
                                    AbstractOauth.CONFIG_CODE);
                            if (null != oauthUser && CommonUtils.notEmpty(oauthConfig)
                                    && CommonUtils.notEmpty(config.get(LoginConfigComponent.CONFIG_REGISTER_URL))) {
                                model.addAttribute("nickname", oauthUser.getNickname());
                                model.addAttribute("openId", oauthUser.getOpenId());
                                model.addAttribute("channel", channel);
                                model.addAttribute("returnUrl", returnUrl);
                                return UrlBasedViewResolver.REDIRECT_URL_PREFIX
                                        + config.get(LoginConfigComponent.CONFIG_REGISTER_URL);
                            }
                        } else { // 如果用户已经登录,则绑定用户
                            entity = new SysUserToken(authToken, site.getId(), user.getId(), channel, now, ip);
                            sysUserTokenService.save(entity);
                            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
                        }
                    } else if (entity.getChannel().equals(channel)) {// 有授权则登录
                        SysUser user = sysUserService.getEntity(entity.getUserId());
                        String loginToken = UUID.randomUUID().toString();
                        sysUserTokenService.save(new SysUserToken(loginToken, site.getId(), user.getId(),
                                LogLoginService.CHANNEL_WEB, now, DateUtils.addMinutes(now, expiryMinutes), ip));
                        LoginController.addLoginStatus(user, loginToken, request, response, expiryMinutes);
                        sysUserService.updateLoginStatus(user.getId(), ip);
                        logLoginService
                                .save(new LogLogin(site.getId(), user.getName(), user.getId(), ip, channel, true, now, null));
                        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
                    }
                }
            } catch (IOException e) {
                log.error(e);
            }
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
    }

    @Autowired(required = false)
    public void init(List<Oauth> oauthList) {
        if (null != oauthList) {
            for (Oauth oauth : oauthList) {
                oauthChannelMap.put(oauth.getChannel(), oauth);
            }
        }
    }
}
