package com.publiccms.common.tools;

import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.sys.SysSite;

/**
 *
 * CmsUrlUtils Url操作组件
 *
 */
public class CmsUrlUtils {

    /**
     * @param site
     * @param entity
     */
    public static void initPlaceUrl(SysSite site, CmsPlace entity) {
        entity.setUrl(getUrl(site, site.isUseStatic(), entity.getUrl()));
    }

    /**
     * @param site
     * @param entity
     */
    public static void initContentUrl(SysSite site, CmsContent entity) {
        entity.setUrl(getUrl(site, entity.isHasStatic(), entity.getUrl()));
    }

    /**
     * @param site
     * @param hasStatic
     * @param url
     * @return
     */
    public static String getUrl(SysSite site, boolean hasStatic, String url) {
        return getUrl(hasStatic ? site.getSitePath() : site.getDynamicPath(), url);
    }

    /**
     * @param sitePath
     * @param url
     * @return
     */
    public static String getUrl(String sitePath, String url) {
        if (CommonUtils.empty(url) || url.contains("://") || url.startsWith("//") || url.startsWith("#")) {
            return url;
        } else {
            return CommonUtils.joinString(sitePath, url);
        }
    }

    /**
     * @param site
     * @param entity
     */
    public static void initCategoryUrl(SysSite site, CmsCategory entity) {
        if (!entity.isOnlyUrl()) {
            entity.setUrl(getUrl(site, entity.isHasStatic(), entity.getUrl()));
        }
    }
}
