package com.publiccms.common.base.oauth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.common.api.Config;
import com.publiccms.common.api.oauth.Oauth;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.view.pojo.oauth.OauthAccess;
import com.publiccms.view.pojo.oauth.OauthConfig;
import com.publiccms.views.pojo.entities.ExtendField;

/**
 *
 * AbstractOauth
 *
 */
public abstract class AbstractOauth implements Config, Oauth {
    /**
     * 
     */
    public static final String CONFIG_CODE = "oauth";
    /**
     * 
     */
    public static final String CONFIG_APP_KEY = "app_key";
    /**
     * 
     */
    public static final String CONFIG_APP_SECRET = "app_secret";
    /**
     * 
     */
    public static final String CONFIG_RETURN_URL = "return_url";
    /**
     * 
     */
    public static final String CONFIG_CODE_DESCRIPTION = CONFIGPREFIX + CONFIG_CODE;

    protected static final CloseableHttpClient httpclient = HttpClients.createDefault();
    protected final Log log = LogFactory.getLog(getClass());
    protected String channel;
    protected String prefix;

    @Autowired
    private ConfigComponent configComponent;

    /**
     * @param channel
     */
    public AbstractOauth(String channel) {
        this.channel = channel;
        this.prefix = channel + "_";
    }

    @Override
    public String getChannel() {
        return channel;
    }

    @Override
    public boolean enabled(short siteId) {
        return null != getConfig(siteId);
    }

    /**
     * @param siteId
     * @return
     */
    protected OauthConfig getConfig(short siteId) {
        Map<String, String> config = configComponent.getConfigData(siteId, CONFIG_CODE);
        OauthConfig oauthConfig = new OauthConfig(config.get(prefix + CONFIG_APP_KEY), config.get(prefix + CONFIG_APP_SECRET),
                config.get(prefix + CONFIG_RETURN_URL));
        if (CommonUtils.notEmpty(config) && CommonUtils.notEmpty(oauthConfig.getAppKey())
                && CommonUtils.notEmpty(oauthConfig.getAppSecret()) && CommonUtils.notEmpty(oauthConfig.getReturnUrl())) {
            return oauthConfig;
        }
        return null;
    }

    protected String get(String url) throws ClientProtocolException, IOException {
        String html = null;
        HttpUriRequest request = new HttpGet(url);
        try (CloseableHttpResponse response = httpclient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                html = EntityUtils.toString(entity, CommonConstants.DEFAULT_CHARSET);
                EntityUtils.consume(entity);
            }
        }
        return html;
    }

    protected String post(String url, Map<String, String> parameters) throws ClientProtocolException, IOException {
        String html = null;
        HttpPost request = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<>();
        for (Entry<String, String> entry : parameters.entrySet()) {
            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        request.setEntity(new UrlEncodedFormEntity(nvps, CommonConstants.DEFAULT_CHARSET));
        try (CloseableHttpResponse response = httpclient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                html = EntityUtils.toString(entity, CommonConstants.DEFAULT_CHARSET);
                EntityUtils.consume(entity);
            }
        }
        return html;
    }

    @Override
    public String getAuthorizeUrl(short siteId, String state) {
        return getAuthorizeUrl(siteId, state, false);
    }

    /**
     * @param siteId
     * @param oauthInfo
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public OauthAccess getOpenId(short siteId, OauthAccess oauthInfo) throws ClientProtocolException, IOException {
        return oauthInfo;
    }

    public OauthAccess getOpenId(short siteId, String code) throws ClientProtocolException, IOException {
        return getOpenId(siteId, getAccessToken(siteId, code));
    }

    /**
     * @param siteId
     * @param code
     * @return access token
     * @throws ClientProtocolException
     * @throws IOException
     */
    public abstract OauthAccess getAccessToken(short siteId, String code) throws ClientProtocolException, IOException;

    @Override
    public String getCode(SysSite site) {
        return CONFIG_CODE;
    }

    @Override
    public String getCodeDescription(SysSite site, Locale locale) {
        return LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, CONFIG_CODE_DESCRIPTION);
    }

    @Override
    public List<ExtendField> getExtendFieldList(SysSite site, Locale locale) {
        List<ExtendField> extendFieldList = new ArrayList<>();
        extendFieldList.add(new ExtendField(prefix + CONFIG_APP_KEY, INPUTTYPE_TEXT, true,
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + prefix + CONFIG_APP_KEY),
                null, null));
        extendFieldList.add(new ExtendField(prefix + CONFIG_APP_SECRET, INPUTTYPE_TEXT, true,
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + prefix + CONFIG_APP_SECRET),
                null, null));
        extendFieldList.add(new ExtendField(prefix + CONFIG_RETURN_URL, INPUTTYPE_TEXT, true,
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + prefix + CONFIG_RETURN_URL),
                null, null));
        return extendFieldList;
    }
}
