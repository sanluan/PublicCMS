package com.publiccms.views.method;

import static com.sanluan.common.tools.TemplateModelUtils.converString;
import static org.apache.http.util.EntityUtils.consume;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.sanluan.common.base.BaseMethod;

import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;

@Component
public class GetHtmlMethod extends BaseMethod {
    CloseableHttpClient httpclient = HttpClients.createDefault();

    /*
     * (non-Javadoc)
     * 
     * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        String url = getString(0, arguments);
        TemplateHashModelEx paramters = getMap(1, arguments);
        String body = getString(1, arguments);
        String html = null;
        if (notEmpty(url)) {
            CloseableHttpResponse response = null;
            try {
                if (notEmpty(paramters) || notEmpty(body)) {
                    HttpPost httppost = new HttpPost(url);
                    if (notEmpty(paramters)) {
                        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                        TemplateModelIterator it = paramters.keys().iterator();
                        while (it.hasNext()) {
                            String key = converString(it.next());
                            nvps.add(new BasicNameValuePair(key, converString(paramters.get(key))));
                        }
                        httppost.setEntity(new UrlEncodedFormEntity(nvps, DEFAULT_CHARSET));
                    } else {
                        httppost.setEntity(new StringEntity(body, DEFAULT_CHARSET));
                    }
                    response = httpclient.execute(httppost);
                } else {
                    response = httpclient.execute(new HttpGet(url));
                }
                HttpEntity entity = response.getEntity();
                if (notEmpty(entity)) {
                    html = EntityUtils.toString(entity, DEFAULT_CHARSET);
                    consume(entity);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                return null;
            } finally {
                try {
                    if (notEmpty(response)) {
                        response.close();
                    }
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
            return html;
        }
        return null;
    }
}
