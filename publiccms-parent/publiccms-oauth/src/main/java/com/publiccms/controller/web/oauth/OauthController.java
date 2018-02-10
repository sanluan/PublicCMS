package com.publiccms.controller.web.oauth;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.api.oauth.Oauth;
import com.publiccms.common.base.AbstractController;
import com.publiccms.common.base.oauth.AbstractOauth;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.LoginConfigComponent;
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
            return REDIRECT + oauthComponent.getAuthorizeUrl(site.getId(), state);
        }
        return REDIRECT + site.getDynamicPath();
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
        Cookie stateCookie = RequestUtils.getCookie(request.getCookies(), STATE_COOKIE_NAME);
        if (null != oauthComponent && oauthComponent.enabled(site.getId()) && null != stateCookie && null != state
                && state.equals(stateCookie.getValue())) {
            try {
                OauthAccess oauthAccess = oauthComponent.getOpenId(site.getId(), code);
                if (null != oauthAccess && null != oauthAccess.getOpenId()) {
                    Cookie cookie = RequestUtils.getCookie(request.getCookies(), RETURN_URL);
                    String returnUrl = site.getDynamicPath();
                    if (null != cookie && null != cookie.getValue()) {
                        returnUrl = cookie.getValue();
                    }
                    SysUserToken entity = sysUserTokenService.getEntity(oauthAccess.getOpenId());
                    if (null != entity) {
                        if (entity.getChannel().equals(channel)) {
                            setUserToSession(session, sysUserService.getEntity(entity.getUserId()));
                            return REDIRECT + returnUrl;
                        }
                    } else {
                        SysUser user = getUserFromSession(session);
                        if (null == user) {
                            OauthUser oauthUser = oauthComponent.getUserInfo(site.getId(), oauthAccess);
                            Map<String, String> config = configComponent.getConfigData(site.getId(), AbstractOauth.CONFIG_CODE);
                            if (null != oauthUser && CommonUtils.notEmpty(config)
                                    && CommonUtils.notEmpty(config.get(LoginConfigComponent.CONFIG_REGISTER_URL))) {
                                model.addAttribute("nickname", oauthUser.getNickname());
                                model.addAttribute("openId", oauthUser.getOpenId());
                                model.addAttribute("avatar", oauthUser.getAvatar());
                                model.addAttribute("gender", oauthUser.getGender());
                                model.addAttribute("channel", channel);
                                model.addAttribute("returnUrl", returnUrl);
                                return REDIRECT + config.get(LoginConfigComponent.CONFIG_REGISTER_URL);
                            }
                        } else {
                            String authToken = new StringBuilder(channel).append(DOT).append(site.getId()).append(DOT)
                                    .append(oauthAccess.getOpenId()).toString();
                            entity = new SysUserToken(authToken, site.getId(), user.getId(), channel, CommonUtils.getDate(),
                                    RequestUtils.getIpAddress(request));
                            sysUserTokenService.save(entity);
                            setUserToSession(session, user);
                            return REDIRECT + returnUrl;
                        }
                    }
                }
            } catch (IOException e) {
                log.error(e);
            }
        }
        return REDIRECT + site.getDynamicPath();
    }

    @Autowired
    public void init(List<Oauth> oauthList) {
        for (Oauth oauth : oauthList) {
            oauthChannelMap.put(oauth.getChannel(), oauth);
        }
    }
}
