package com.publiccms.views.method;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.sanluan.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

@Component
public class GetHtmlMethod extends BaseMethod {

    /*
     * (non-Javadoc)
     * 
     * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        String url = getString(0, arguments);
        if (null != url) {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            try {
                HttpGet httpget = new HttpGet(url);
                CloseableHttpResponse response = httpclient.execute(httpget);
                try {
                    HttpEntity entity = response.getEntity();
                    EntityUtils.consume(entity);
                    if (null != entity) {
                        return entity.getContent();
                    }
                } catch (Exception e) {
                    log.debug(e.getMessage());
                } finally {
                    try {
                        response.close();
                    } catch (IOException e) {
                        log.debug(e.getMessage());
                    }
                }
            } catch (Exception e) {
                log.debug(e.getMessage());
            } finally {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    log.debug(e.getMessage());
                }
            }
        }
        return null;
    }
}
