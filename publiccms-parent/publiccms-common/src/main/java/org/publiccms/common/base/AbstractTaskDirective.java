package org.publiccms.common.base;

import static com.publiccms.common.tools.CommonUtils.empty;
import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.StringUtils.split;
import static org.publiccms.common.base.AbstractFreemarkerView.CONTEXT_SITE;
import static org.publiccms.controller.api.ApiController.NEED_APP_TOKEN;
import static org.publiccms.controller.api.ApiController.UN_AUTHORIZED;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.publiccms.entities.sys.SysApp;
import org.publiccms.entities.sys.SysAppToken;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.component.site.SiteComponent;
import org.publiccms.logic.service.sys.SysAppService;
import org.publiccms.logic.service.sys.SysAppTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import com.publiccms.common.base.Base;
import com.publiccms.common.directive.BaseTemplateDirective;
import com.publiccms.common.handler.HttpParameterHandler;
import com.publiccms.common.handler.RenderHandler;
/**
 * 
 * AbstractTemplateDirective 自定义模板指令基类
 *
 */
public abstract class AbstractTaskDirective extends BaseTemplateDirective implements Base {
    /**
     * @param handler
     * @return
     * @throws Exception
     */
    public SysSite getSite(RenderHandler handler) throws Exception {
        return (SysSite) handler.getAttribute(CONTEXT_SITE);
    }

    @Override
    public void execute(HttpMessageConverter<Object> httpMessageConverter, MediaType mediaType, HttpServletRequest request,
            String callback, HttpServletResponse response) throws IOException, Exception {
        HttpParameterHandler handler = new HttpParameterHandler(httpMessageConverter, mediaType, request, callback, response);
        SysApp app = null;
        if (null == (app = getApp(handler))) {
            handler.put("error", NEED_APP_TOKEN).render();
        } else if (empty(app.getAuthorizedApis()) || !contains(split(app.getAuthorizedApis(), COMMA_DELIMITED), getName())) {
            handler.put("error", UN_AUTHORIZED).render();
        } else {
            execute(handler);
            handler.render();
        }
    }

    protected SysApp getApp(RenderHandler handler) throws Exception {
        SysAppToken appToken = appTokenService.getEntity(handler.getString("appToken"));
        if (null != appToken) {
            return appService.getEntity(appToken.getAppId());
        }
        return null;
    }

    @Autowired
    private SysAppTokenService appTokenService;
    @Autowired
    private SysAppService appService;
    @Autowired
    protected SiteComponent siteComponent;
}