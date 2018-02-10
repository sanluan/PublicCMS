package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.RequestUtils;

import eu.bitwalker.useragentutils.UserAgent;

/**
 *
 * UserAgentDirective
 * 
 */
@Component
public class UserAgentDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        HttpServletRequest request = handler.getRequest();
        if (null != request) {
            String userAgent = RequestUtils.getUserAgent(request);
            UserAgent ua = UserAgent.parseUserAgentString(userAgent);
            Map<String, Object> map = new HashMap<>();
            map.put("id", ua.getId());
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

    @Override
    public boolean needAppToken() {
        return false;
    }

}
