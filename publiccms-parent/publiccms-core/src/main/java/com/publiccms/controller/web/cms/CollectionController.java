package com.publiccms.controller.web.cms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsUserCollection;
import com.publiccms.entities.cms.CmsUserCollectionId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsUserCollectionService;

import jakarta.annotation.Resource;

/**
 * 
 * CollectionController 收藏
 *
 */
@Controller
@RequestMapping("collection")
public class CollectionController {
    protected final Log log = LogFactory.getLog(getClass());
    @Resource
    protected ConfigDataComponent configDataComponent;

    /**
     * @param site
     * @param user
     * @param contentId
     * @return
     */
    @RequestMapping("collect")
    @Csrf
    @ResponseBody
    public boolean collect(@RequestAttribute SysSite site, @SessionAttribute SysUser user, long contentId) {
        return collection(site, user.getId(), contentId, true);
    }

    /**
     * @param site
     * @param user
     * @param contentId
     * @return
     */
    @RequestMapping("uncollect")
    @Csrf
    @ResponseBody
    public boolean uncollect(@RequestAttribute SysSite site, @SessionAttribute SysUser user, long contentId) {
        return collection(site, user.getId(), contentId, false);
    }

    private boolean collection(SysSite site, long userId, long contentId, boolean collection) {
        CmsUserCollectionId id = new CmsUserCollectionId(userId, contentId);
        CmsUserCollection entity = service.getEntity(id);
        if (collection && null == entity || !collection && null != entity) {
            CmsContent content = contentService.updateCollections(site.getId(), contentId, collection ? 1 : -1);
            if (null != content) {
                if (collection) {
                    entity = new CmsUserCollection();
                    entity.setId(id);
                    service.save(entity);
                } else {
                    service.delete(id);
                }
            } else if (!collection) {
                service.delete(id);
            }
            return true;
        }
        return false;
    }

    @Resource
    private CmsUserCollectionService service;
    @Resource
    private CmsContentService contentService;
}
