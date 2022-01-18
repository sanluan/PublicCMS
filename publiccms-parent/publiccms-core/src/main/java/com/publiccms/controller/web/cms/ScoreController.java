package com.publiccms.controller.web.cms;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.api.Config;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsComment;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsUserScore;
import com.publiccms.entities.cms.CmsUserScoreId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.SiteConfigComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCommentService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsUserScoreService;

/**
 * 
 * ScoreController 评分
 *
 */
@Controller
@RequestMapping("score")
public class ScoreController {
    @Autowired
    protected ConfigComponent configComponent;

    /**
     * @param site
     * @param user
     * @param userId
     * @param itemType
     * @param itemId
     * @return
     */
    @RequestMapping("score")
    @Csrf
    @ResponseBody
    public boolean score(@RequestAttribute SysSite site, @SessionAttribute SysUser user, String itemType, long itemId) {
        return score(site, user.getId(), itemType, itemId, true);
    }

    /**
     * @param site
     * @param user
     * @param userId
     * @param itemType
     * @param itemId
     * @return
     */
    @RequestMapping("unscore")
    @Csrf
    @ResponseBody
    public boolean unscore(@RequestAttribute SysSite site, @SessionAttribute SysUser user, String itemType, long itemId) {
        return score(site, user.getId(), itemType, itemId, false);
    }

    private boolean score(SysSite site, long userId, String itemType, long itemId, boolean score) {
        if (CommonUtils.notEmpty(itemType)) {
            CmsUserScoreId id = new CmsUserScoreId(userId, itemType, itemId);
            CmsUserScore entity = service.getEntity(id);
            if (score && null == entity || !score && null != entity) {
                Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
                boolean needStatic = ConfigComponent.getBoolean(config.get(SiteConfigComponent.CONFIG_STATIC_AFTER_SCORE), false);
                if ("content".equals(itemType)) {
                    CmsContent content = contentService.updateScores(site.getId(), itemId, score ? 1 : -1);
                    if (null != content) {
                        if (score) {
                            entity = new CmsUserScore();
                            entity.setId(id);
                            service.save(entity);
                        } else {
                            service.delete(id);
                        }
                        if (needStatic) {
                            templateComponent.createContentFile(site, content, null, null);
                        }
                    }
                    return true;
                } else if ("comment".equals(itemType)) {
                    CmsComment comment = commentService.updateScores(site.getId(), itemId, score ? 1 : -1);
                    if (null != comment) {
                        if (score) {
                            entity = new CmsUserScore();
                            entity.setId(id);
                            service.save(entity);
                        } else {
                            service.delete(id);
                        }
                        if (needStatic) {
                            templateComponent.createContentFile(site, contentService.getEntity(comment.getContentId()), null,
                                    null);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Autowired
    private CmsUserScoreService service;
    @Autowired
    private CmsCommentService commentService;
    @Autowired
    private CmsContentService contentService;
    @Autowired
    private TemplateComponent templateComponent;

}