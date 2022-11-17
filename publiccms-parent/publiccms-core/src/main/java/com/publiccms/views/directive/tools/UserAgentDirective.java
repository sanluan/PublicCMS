package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.RequestUtils;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * userAgent user agent分析指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>userAgent</code> user agent,为空时取当前请求的user agent
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>object.id</code>id
 * <li><code>object.ip</code>客户端ip
 * <li><code>object.browser</code>浏览器
 * <li><code>object.browserGroup</code>浏览器分组
 * <li><code>object.browserType</code>浏览器类型
 * <li><code>object.browserVersion</code>浏览器版本
 * <li><code>object.operatingSystem</code>操作系统
 * <li><code>object.operatingSystemGroup</code>操作系统分组
 * <li><code>object.deviceType</code>设备类型
 * <li><code>object.userAgent</code>user agent
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

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String userAgent = handler.getString("userAgent");
        HttpServletRequest request = null;
        if (null == userAgent) {
            request = handler.getRequest();
            if (null != request) {
                userAgent = RequestUtils.getUserAgent(request);
            }
        }
        UserAgent ua = UserAgent.parseUserAgentString(userAgent);
        Map<String, Object> map = new HashMap<>();
        map.put("id", ua.getId());
        map.put("ip", RequestUtils.getIpAddress(request));
        map.put("browser", ua.getBrowser());
        map.put("browserGroup", ua.getBrowser().getGroup());
        map.put("browserType", ua.getBrowser().getBrowserType());
        map.put("browserVersion", ua.getBrowserVersion());
        map.put("operatingSystem", ua.getOperatingSystem());
        map.put("operatingSystemGroup", ua.getOperatingSystem().getGroup());
        map.put("deviceType", ua.getOperatingSystem().getDeviceType());
        map.put("userAgent", userAgent);
        handler.put("object", map).render();
    }

}
