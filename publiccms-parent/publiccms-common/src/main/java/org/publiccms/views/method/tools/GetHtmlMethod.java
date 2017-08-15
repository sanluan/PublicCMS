package org.publiccms.views.method.tools;

import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.TemplateModelUtils.converString;
import static org.apache.http.util.EntityUtils.consume;

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
        TemplateHashModelEx paramters = getMap(1, arguments);
        String body = getString(1, arguments);
        String html = null;
        if (notEmpty(url)) {
            try (CloseableHttpClient httpclient = HttpClients.createDefault();) {
                HttpUriRequest request;
                if (null != paramters || notEmpty(body)) {
                    HttpPost httppost = new HttpPost(url);
                    if (null != paramters) {
                        List<NameValuePair> nvps = new ArrayList<>();
                        TemplateModelIterator it = paramters.keys().iterator();
                        while (it.hasNext()) {
                            String key = converString(it.next());
                            nvps.add(new BasicNameValuePair(key, converString(paramters.get(key))));
                        }
                        httppost.setEntity(new UrlEncodedFormEntity(nvps, DEFAULT_CHARSET));
                    } else {
                        httppost.setEntity(new StringEntity(body, DEFAULT_CHARSET));
                    }
                    request = httppost;
                } else {
                    request = new HttpGet(url);
                }
                try (CloseableHttpResponse response = httpclient.execute(request)) {
                    HttpEntity entity = response.getEntity();
                    if (null != entity) {
                        html = EntityUtils.toString(entity, DEFAULT_CHARSET);
                        consume(entity);
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
    public int minParamtersNumber() {
        return 1;
    }
}
