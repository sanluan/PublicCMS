package com.publiccms.views.method.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.TemplateModelUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.SafeConfigComponent;

import freemarker.core.Environment;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getHtml 获取远程页面文本
 * <p>
 * 参数列表
 * <ol>
 * <li><code>url</code>:
 * <li>参数,<code>map</code>类型或<code>string</code>类型,为空时以get方式请求,不为空时以post方式请求
 * <li>请求头,<code>map</code>类型
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>html</code>:页面文本
 * </ul>
 * 使用示例
 * <p>
 * ${getHtml('https://www.publiccms.com/')}
 * <p>
 * ${getHtml('https://www.publiccms.com/',"body")}
 * <p>
 * ${getHtml('https://www.publiccms.com/',{"parameters1":"value1","parameters2":"value2"},{"headers1":"value1","headers2":"value2"})}
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getHtml?appToken=接口访问授权Token&amp;parameters=https://www.publiccms.com/', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetHtmlMethod extends BaseMethod {
    @Resource
    private SafeConfigComponent safeConfigComponent;

    @Override
    public Object execute(HttpServletRequest request, List<TemplateModel> arguments) throws TemplateModelException {
        SysSite site = (SysSite) request.getAttribute("site");
        return execute(site, arguments);
    }

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        TemplateModel model = Environment.getCurrentEnvironment().getGlobalVariable(CommonConstants.getAttributeSite());
        SysSite site = null;
        if (null != model) {
            site = (SysSite) TemplateModelUtils.converBean(model);
        }
        return execute(site, arguments);
    }

    public Object execute(SysSite site, List<TemplateModel> arguments) throws TemplateModelException {
        String url = getString(0, arguments);
        Map<?, ?> parameters = getMap(1, arguments);
        String body = getString(1, arguments);
        Map<?, ?> headers = getMap(2, arguments);
        String html = null;
        if (CommonUtils.notEmpty(url)) {
            if (StringUtils.startsWithAny(url, safeConfigComponent.getAllowUrls(site))) {
                try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(Constants.defaultRequestConfig)
                        .build()) {
                    HttpUriRequest request;
                    if (!parameters.isEmpty() || CommonUtils.notEmpty(body)) {
                        HttpPost httppost = new HttpPost(url);
                        if (!parameters.isEmpty()) {
                            List<NameValuePair> nvps = new ArrayList<>();
                            Iterator<?> it = parameters.keySet().iterator();
                            while (it.hasNext()) {
                                String key = (String) it.next();
                                Object value = parameters.get(key);
                                nvps.add(new BasicNameValuePair(key, null == value ? null : value.toString()));
                            }
                            httppost.setEntity(new UrlEncodedFormEntity(nvps, Constants.DEFAULT_CHARSET));
                        } else {
                            httppost.setEntity(new StringEntity(body, Constants.DEFAULT_CHARSET));
                        }
                        request = httppost;
                    } else {
                        request = new HttpGet(url);
                    }
                    if (!headers.isEmpty()) {
                        for (Entry<?, ?> entry : headers.entrySet()) {
                            request.addHeader((String) entry.getKey(), (String) entry.getValue());
                        }
                    }
                    try (CloseableHttpResponse response = httpclient.execute(request)) {
                        HttpEntity entity = response.getEntity();
                        if (null != entity) {
                            html = EntityUtils.toString(entity, Constants.DEFAULT_CHARSET);
                            EntityUtils.consume(entity);
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                    html = e.getMessage();
                }
            } else {
                html = "verify.custom.url.unsafe";
            }
        }
        return html;
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
