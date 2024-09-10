package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.ParseException;
import com.blueconic.browscap.UserAgentParser;
import com.blueconic.browscap.UserAgentService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.RequestUtils;

import freemarker.template.TemplateException;

/**
 * userAgent user agent分析指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>userAgent</code>:user agent,为空时取当前请求的user agent
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>object.ip</code>:客户端ip
 * <li><code>object.browser</code>:浏览器
 * <li><code>object.browserType</code>:浏览器类型
 * <li><code>object.browserVersion</code>:浏览器版本
 * <li><code>object.operatingSystem</code>:操作系统
 * <li><code>object.operatingSystemVersion</code>:操作系统版本
 * <li><code>object.deviceType</code>:设备类型
 * <li><code>object.userAgent</code>:user agent
 * </ul>
 * 使用示例
 * <p>
 * &lt;@tools.userAgent&gt;${a.deviceType}&lt;/@tools.userAgent&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/tools/userAgent', function(data){    
   console.log(data.deviceType);
 });
 &lt;/script&gt;
 * </pre>
 */
@Component
public class UserAgentDirective extends AbstractTemplateDirective {
    public static UserAgentParser parser = null;

    public UserAgentDirective() {
        try {
            parser = new UserAgentService().loadParser();
        } catch (IOException | ParseException e) {
        }
    }

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        String userAgent = handler.getString("userAgent");
        HttpServletRequest request = null;
        if (null == userAgent) {
            request = handler.getRequest();
            if (null != request) {
                userAgent = RequestUtils.getUserAgent(request);
            }
        }
        Map<String, Object> map = new HashMap<>();
        if (null != parser) {
            Capabilities capabilities = parser.parse(userAgent);
            map.put("ip", RequestUtils.getIpAddress(request));
            map.put("browser", capabilities.getBrowser());
            map.put("browserType", capabilities.getBrowserType());
            map.put("browserVersion", capabilities.getBrowserMajorVersion());
            map.put("operatingSystem", capabilities.getPlatform());
            map.put("operatingSystemVersion", capabilities.getPlatformVersion());
            map.put("deviceType", capabilities.getDeviceType());
        }
        map.put("userAgent", userAgent);
        handler.put("object", map).render();
    }

}
