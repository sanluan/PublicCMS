package com.publiccms.common.base;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.common.base.app.App;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.SiteComponent;
import com.sanluan.common.directive.BaseHttpDirective;
import com.sanluan.common.handler.RenderHandler;

/**
 * 
 * BaseDirective 自定义接口指令基类
 *
 */
public abstract class AbstractAppDirective extends BaseHttpDirective implements App {

    public SysSite getSite(RenderHandler handler) throws IOException, Exception {
        HttpServletRequest request = handler.getRequest();
        return siteComponent.getSite(request.getServerName(), request.getServerPort());
    }

    public abstract boolean needUserToken();

    public abstract boolean needAppToken();

    @Autowired
    private SiteComponent siteComponent;
}
