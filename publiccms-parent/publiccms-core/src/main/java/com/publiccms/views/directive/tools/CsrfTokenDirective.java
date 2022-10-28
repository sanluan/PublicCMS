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
* csrfToken 防跨站请求伪造token指令
* <p>
* 参数列表
* <ul>
* <li><code>admin</code> 是否后台,默认值<code>false</code>
* </ul>
* <p>
* 返回结果
* <ul>
* <li>打印token值
* </ul>
* 使用示例
* <p>
* &lt;input type="hidden" name="_csrf" value="&lt;@tools.csrfToken/&gt;"/&gt;
* </p>
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
