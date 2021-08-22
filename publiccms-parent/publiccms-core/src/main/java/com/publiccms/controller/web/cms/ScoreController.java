package com.publiccms.controller.web.cms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsComment;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsUserScore;
import com.publiccms.entities.cms.CmsUserScoreId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
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

    /**
     * @param site
     * @param user
     * @param userId
     * @param itemType
     * @param itemId
     * @return
     */
    @RequestMapping("score")
    @ResponseBody
    public boolean score(@RequestAttribute SysSite site, @SessionAttribute SysUser user, Long userId, String itemType,
            long itemId) {
        if (null != userId && userId.equals(user.getId())) {
            return score(site, user.getId(), itemType, itemId, true);
        } else {
            return false;
        }
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
    @ResponseBody
    public boolean unscore(@RequestAttribute SysSite site, @SessionAttribute SysUser user, Long userId, String itemType,
            long itemId) {
        if (null != userId && userId.equals(user.getId())) {
            return score(site, user.getId(), itemType, itemId, false);
        } else {
            return false;
        }
    }

    private boolean score(SysSite site, long userId, String itemType, long itemId, boolean score) {
        if (CommonUtils.notEmpty(itemType)) {
            CmsUserScoreId id = new CmsUserScoreId(userId, itemType, itemId);
            CmsUserScore entity = service.getEntity(id);
            if (score && null == entity || !score && null != entity) {
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
                        templateComponent.createContentFile(site, content, null, null);
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
                        templateComponent.createContentFile(site, contentService.getEntity(comment.getContentId()), null, null);
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
