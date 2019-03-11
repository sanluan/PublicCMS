package com.publiccms.logic.component.oauth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.oauth.AbstractOauth;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.view.pojo.oauth.OauthAccess;
import com.publiccms.view.pojo.oauth.OauthConfig;
import com.publiccms.view.pojo.oauth.OauthUser;

/**
 *
 * WeiboOauth
 * 
 */
@Component
public class WeiboOauthComponent extends AbstractOauth {

    public WeiboOauthComponent() {
        super("weibo");
    }

    /*
     * http://open.weibo.com/wiki/Oauth2/authorize
     */
    @Override
    public String getAuthorizeUrl(short siteId, String state, boolean mobile) {
        OauthConfig config = getConfig(siteId);
        if (null != config) {
            StringBuilder sb = new StringBuilder("https://api.weibo.com/oauth2/authorize?client_id=");
            sb.append(config.getAppKey()).append("&redirect_uri=").append(config.getReturnUrl()).append("&scope=email&state=")
                    .append(state);
            if (mobile) {
                sb.append("&display=mobile");
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * http://open.weibo.com/wiki/Oauth2/access_token
     */
    @Override
    public OauthAccess getAccessToken(short siteId, String code) throws ClientProtocolException, IOException {
        OauthConfig config = getConfig(siteId);
        if (CommonUtils.notEmpty(code) && null != config) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("client_id", config.getAppKey());
            parameters.put("client_secret", config.getAppSecret());
            parameters.put("grant_type", "authorization_code");
            parameters.put("redirect_uri", config.getReturnUrl());
            parameters.put("code", code);
            String html = post("https://api.weibo.com/oauth2/access_token", parameters);
            if (CommonUtils.notEmpty(html)) {
                Map<String, Object> map = CommonConstants.objectMapper.readValue(html, CommonConstants.objectMapper
                        .getTypeFactory().constructMapLikeType(HashMap.class, String.class, Object.class));
                return new OauthAccess(code, (String) map.get("access_token"), String.valueOf(map.get("uid")));
            }
        }
        return null;
    }

    /*
     * http://open.weibo.com/wiki/2/users/show
     */
    @Override
    public OauthUser getUserInfo(short siteId, OauthAccess oauthInfo) throws ClientProtocolException, IOException {
        if (null != oauthInfo) {
            StringBuilder sb = new StringBuilder("https://api.weibo.com/2/users/show.json?access_token=");
            sb.append(oauthInfo.getAccessToken()).append("&uid=").append(oauthInfo.getOpenId());
            String html = get(sb.toString());
            Map<String, Object> map = CommonConstants.objectMapper.readValue(html, CommonConstants.objectMapper.getTypeFactory()
                    .constructMapLikeType(HashMap.class, String.class, Object.class));
            if (null != map.get("id")) {
                return new OauthUser(oauthInfo.getOpenId(), (String) map.get("screen_name"));
            }
        }
        return null;
    }
}
