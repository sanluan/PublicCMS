package com.publiccms.common.base;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.common.api.Config;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.LoginConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogOperateService;

/**
 *
 * AbstractController
 * 
 */
public abstract class AbstractController {
    protected final Log log = LogFactory.getLog(getClass());

    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;
    @Autowired
    protected ConfigComponent configComponent;

    protected SysDomain getDomain(HttpServletRequest request) {
        return siteComponent.getDomain(request.getServerName());
    }

    protected SysSite getSite(HttpServletRequest request) {
        return siteComponent.getSite(request.getServerName());
    }

    protected boolean isUnSafeUrl(String url, SysSite site, HttpServletRequest request) {
        if (CommonUtils.empty(url)) {
            return true;
        } else if (unSafe(url, site, request)) {
            Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
            String safeReturnUrl = config.get(LoginConfigComponent.CONFIG_RETURN_URL);
            if (null != safeReturnUrl) {
                for (String safeUrlPrefix : StringUtils.split(safeReturnUrl, CommonConstants.COMMA_DELIMITED)) {
                    if (url.startsWith(safeUrlPrefix)) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    protected boolean isUnSafeUrl(String url, SysSite site, String safeReturnUrl, HttpServletRequest request) {
        if (CommonUtils.empty(url)) {
            return true;
        } else if (unSafe(url, site, request)) {
            if (null != safeReturnUrl) {
                for (String safeUrlPrefix : StringUtils.split(safeReturnUrl, CommonConstants.COMMA_DELIMITED)) {
                    if (url.startsWith(safeUrlPrefix)) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean unSafe(String url, SysSite site, HttpServletRequest request) {
        String scheme = request.getScheme();
        String basePath = AbstractFreemarkerView.getBasePath(scheme, request.getServerPort(), request.getServerName(),
                request.getContextPath());
        if (url.startsWith("http://") || url.startsWith("https://")) {
            String fixedUrl = url.substring(url.indexOf("://") + 1);
            if (url.startsWith(site.getDynamicPath()) || url.startsWith(site.getSitePath())
                    || fixedUrl.startsWith(site.getDynamicPath()) || fixedUrl.startsWith(site.getSitePath())
                    || url.startsWith(basePath)) {
                return false;
            } else {
                return true;
            }

        } else if (url.startsWith("//")) {
            String fixedUrl = new StringBuilder(scheme).append(":").append(url).toString();
            if (url.startsWith(site.getDynamicPath()) || url.startsWith(site.getSitePath())
                    || fixedUrl.startsWith(site.getDynamicPath()) || fixedUrl.startsWith(site.getSitePath())
                    || fixedUrl.startsWith(basePath)) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
