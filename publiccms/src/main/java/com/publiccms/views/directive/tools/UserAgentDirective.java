package com.publiccms.views.directive.tools;

import static com.sanluan.common.tools.RequestUtils.getUserAgent;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class UserAgentDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        HttpServletRequest request = handler.getRequest();
        if (null != request) {
            handler.put("object", getUserAgent(request));
        }
    }

    @Override
    public boolean needAppToken() {
        return false;
    }
}
