package com.publiccms.views.directive.tools;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;

/**
 * 
 * DiskDirective PublicCMS磁盘空间与路径指令
 *
 */
@Component
public class CsrfTokenDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String cookiesName = null;
        if (handler.getBoolean("admin", false)) {
            cookiesName = CommonConstants.getCookiesAdmin();
        } else {
            cookiesName = CommonConstants.getCookiesUser();
        }
        HttpServletRequest request = handler.getRequest();
        if (null != request) {
            Cookie userCookie = RequestUtils.getCookie(request.getCookies(), cookiesName);
            if (null != userCookie && CommonUtils.notEmpty(userCookie.getValue())) {
                String value = userCookie.getValue();
                if (null != value) {
                    String[] userData = value.split(CommonConstants.getCookiesUserSplit());
                    if (userData.length > 1) {
                        handler.print(userData[1]);
                    }
                }
            }
        }
    }

    /**
     * @return whether to enable http
     */
    @Override
    public boolean httpEnabled() {
        return false;
    }

}
