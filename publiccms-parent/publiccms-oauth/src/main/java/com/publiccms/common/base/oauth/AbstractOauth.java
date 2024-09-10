package com.publiccms.common.base.oauth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.publiccms.common.api.Config;
import com.publiccms.common.api.oauth.OauthGateway;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.views.pojo.oauth.OauthAccess;
import com.publiccms.views.pojo.oauth.OauthConfig;

import jakarta.annotation.Resource;

/**
 *
 * AbstractOauth
 *
 */
public abstract class AbstractOauth implements Config, OauthGateway {
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
    public static final String CONFIG_CODE_DESCRIPTION = CommonUtils.joinString(CONFIGPREFIX, CONFIG_CODE);

    @Resource
    protected ConfigDataComponent configDataConponent;

    protected static final CloseableHttpClient httpclient = HttpClients.custom()
            .setDefaultRequestConfig(Constants.defaultRequestConfig).build();
    protected final Log log = LogFactory.getLog(getClass());
    protected String channel;
    protected String prefix;

    /**
     * @param channel
     */
    public AbstractOauth(String channel) {
        this.channel = channel;
        this.prefix = CommonUtils.joinString(channel, Constants.UNDERLINE);
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
        Map<String, String> config = configDataConponent.getConfigData(siteId, CONFIG_CODE);
        OauthConfig oauthConfig = new OauthConfig(config.get(CommonUtils.joinString(prefix, CONFIG_APP_KEY)),
                config.get(CommonUtils.joinString(prefix, CONFIG_APP_SECRET)),
                config.get(CommonUtils.joinString(prefix, CONFIG_RETURN_URL)));
        if (CommonUtils.notEmpty(config) && CommonUtils.notEmpty(oauthConfig.getAppKey())
                && CommonUtils.notEmpty(oauthConfig.getAppSecret()) && CommonUtils.notEmpty(oauthConfig.getReturnUrl())) {
            return oauthConfig;
        }
        return null;
    }

    protected String get(String url) throws IOException {
        String html = null;
        HttpUriRequest request = new HttpGet(url);
        try (CloseableHttpResponse response = httpclient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                html = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                EntityUtils.consume(entity);
            }
        }
        return html;
    }

    protected String post(String url, Map<String, String> parameters) throws IOException {
        String html = null;
        HttpPost request = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<>();
        for (Entry<String, String> entry : parameters.entrySet()) {
            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        request.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8));
        try (CloseableHttpResponse response = httpclient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                html = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                EntityUtils.consume(entity);
            }
        }
        return html;
    }

    @Override
    public String getAuthorizeUrl(short siteId, String state) {
        return getAuthorizeUrl(siteId, state, false);
    }

    @Override
    public OauthAccess getOpenId(short siteId, String code) throws IOException {
        return getOpenId(siteId, getAccessToken(siteId, code));
    }

    /**
     * @param siteId
     * @param code
     * @return access token
     * @throws IOException
     */
    public abstract OauthAccess getAccessToken(short siteId, String code) throws IOException;

    @Override
    public String getCode(short siteId, boolean showAll) {
        return CONFIG_CODE;
    }

    @Override
    public String getCodeDescription(Locale locale) {
        return LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, CONFIG_CODE_DESCRIPTION);
    }

    @Override
    public List<SysExtendField> getExtendFieldList(SysSite site, Locale locale) {
        List<SysExtendField> extendFieldList = new ArrayList<>();
        extendFieldList.add(new SysExtendField(CommonUtils.joinString(prefix, CONFIG_APP_KEY), INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, prefix, CONFIG_APP_KEY)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, prefix, CONFIG_APP_KEY,
                        CONFIG_CODE_DESCRIPTION_SUFFIX))));
        extendFieldList.add(new SysExtendField(CommonUtils.joinString(prefix, CONFIG_APP_SECRET), INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, prefix, CONFIG_APP_SECRET)),
                null));
        extendFieldList.add(new SysExtendField(CommonUtils.joinString(prefix, CONFIG_RETURN_URL), INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, prefix, CONFIG_RETURN_URL)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, prefix, CONFIG_RETURN_URL,
                        CONFIG_CODE_DESCRIPTION_SUFFIX), site.getDynamicPath())));
        return extendFieldList;
    }
}
