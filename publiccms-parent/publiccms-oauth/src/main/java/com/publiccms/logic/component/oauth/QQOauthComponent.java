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
 * QQOauth
 * 
 */
@Component
public class QQOauthComponent extends AbstractOauth {

    public QQOauthComponent() {
        super("qq");
    }

    /*
     * http://wiki.connect.qq.com/%E4%BD%BF%E7%94%A8authorization_code%E8%8E%B7%
     * E5%8F%96access_token
     */
    @Override
    public String getAuthorizeUrl(short siteId, String state, boolean mobile) {
        OauthConfig config = getConfig(siteId);
        if (null != config) {
            StringBuilder sb = new StringBuilder("https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=");
            sb.append(config.getAppKey()).append("&redirect_uri=").append(config.getReturnUrl()).append("&scope=get_user_info")
                    .append("&state=").append(state);
            if (mobile) {
                sb.append("&display=mobile");
            }
            return sb.toString();
        }
        return null;
    }

    @Override
    public OauthAccess getAccessToken(short siteId, String code) throws ClientProtocolException, IOException {
        OauthConfig config = getConfig(siteId);
        if (CommonUtils.notEmpty(code) && null != config) {
            StringBuilder sb = new StringBuilder("https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&code=");
            sb.append(code).append("&client_id=").append(config.getAppKey()).append("&client_secret=")
                    .append(config.getAppSecret()).append("&redirect_uri=").append(config.getReturnUrl());
            String html = get(sb.toString());
            if (CommonUtils.notEmpty(html)) {
                String[] values = html.split("&");
                for (String value : values) {
                    if (value.startsWith("access_token=")) {
                        return new OauthAccess(code, value.split("=")[1]);
                    }
                }
            }
        }
        return null;
    }

    /*
     * http://wiki.connect.qq.com/%E5%BC%80%E5%8F%91%E6%94%BB%E7%95%A5_server-
     * side
     */
    @Override
    public OauthAccess getOpenId(short siteId, OauthAccess oauthInfo) throws ClientProtocolException, IOException {
        if (null != oauthInfo) {
            StringBuilder sb = new StringBuilder("https://graph.qq.com/oauth2.0/me?");
            sb.append("access_token=" + oauthInfo.getAccessToken());
            String html = get(sb.toString());
            if (CommonUtils.notEmpty(html)) {
                html = html.substring(html.indexOf("{"), html.indexOf("}") + 1);
                Map<String, String> map = CommonConstants.objectMapper.readValue(html, CommonConstants.objectMapper
                        .getTypeFactory().constructMapLikeType(HashMap.class, String.class, String.class));
                oauthInfo.setOpenId(map.get("openid"));
                return oauthInfo;
            }
        }
        return null;
    }

    /*
     * http://wiki.connect.qq.com/get_user_info
     */
    @Override
    public OauthUser getUserInfo(short siteId, OauthAccess oauthAccess) throws ClientProtocolException, IOException {
        OauthConfig config = getConfig(siteId);
        if (null != oauthAccess && null != config) {
            StringBuilder sb = new StringBuilder("https://graph.qq.com/user/get_user_info?access_token=");
            sb.append(oauthAccess.getAccessToken()).append("&oauth_consumer_key=").append(config.getAppKey()).append("&openid=")
                    .append(oauthAccess.getOpenId()).append("&format=format");
            String html = get(sb.toString());
            if (CommonUtils.notEmpty(html)) {
                Map<String, Object> map = CommonConstants.objectMapper.readValue(html, CommonConstants.objectMapper
                        .getTypeFactory().constructMapLikeType(HashMap.class, String.class, Object.class));
                if (0 == (Integer) map.get("ret")) {
                    return new OauthUser(oauthAccess.getOpenId(), (String) map.get("nickname"));
                }
            }
        }
        return null;
    }
}
