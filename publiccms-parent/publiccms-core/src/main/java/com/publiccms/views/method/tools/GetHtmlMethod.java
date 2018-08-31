package com.publiccms.views.method.tools;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.TemplateModelUtils;

import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;

/**
 *
 * GetHtmlMethod
 * 
 */
@Component
public class GetHtmlMethod extends BaseMethod {

    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        String url = getString(0, arguments);
        TemplateHashModelEx parameters = getMap(1, arguments);
        String body = getString(1, arguments);
        String html = null;
        if (CommonUtils.notEmpty(url)) {
            try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(CommonConstants.defaultRequestConfig).build();) {
                HttpUriRequest request;
                if (null != parameters || CommonUtils.notEmpty(body)) {
                    HttpPost httppost = new HttpPost(url);
                    if (null != parameters) {
                        List<NameValuePair> nvps = new ArrayList<>();
                        TemplateModelIterator it = parameters.keys().iterator();
                        while (it.hasNext()) {
                            String key = TemplateModelUtils.converString(it.next());
                            nvps.add(new BasicNameValuePair(key, TemplateModelUtils.converString(parameters.get(key))));
                        }
                        httppost.setEntity(new UrlEncodedFormEntity(nvps, CommonConstants.DEFAULT_CHARSET));
                    } else {
                        httppost.setEntity(new StringEntity(body, CommonConstants.DEFAULT_CHARSET));
                    }
                    request = httppost;
                } else {
                    request = new HttpGet(url);
                }
                try (CloseableHttpResponse response = httpclient.execute(request)) {
                    HttpEntity entity = response.getEntity();
                    if (null != entity) {
                        html = EntityUtils.toString(entity, CommonConstants.DEFAULT_CHARSET);
                        EntityUtils.consume(entity);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                return null;
            }
            return html;
        }
        return null;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }
}
