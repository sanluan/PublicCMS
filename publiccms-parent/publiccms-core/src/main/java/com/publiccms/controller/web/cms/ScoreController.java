package com.publiccms.controller.web.cms;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.api.Config;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsComment;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsUserScore;
import com.publiccms.entities.cms.CmsUserScoreId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.logic.component.config.SiteConfigComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCommentService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsUserScoreService;

import freemarker.template.TemplateException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 * ScoreController 评分
 *
 */
@Controller
@RequestMapping("score")
public class ScoreController {
    protected final Log log = LogFactory.getLog(getClass());
    @Resource
    protected ConfigDataComponent configDataComponent;

    /**
     * @param site
     * @param user
     * @param itemType
     * @param itemId
     * @param scores
     * @param request
     * @return
     */
    @RequestMapping("score")
    @Csrf
    @ResponseBody
    public boolean score(@RequestAttribute SysSite site, @SessionAttribute SysUser user, String itemType, long itemId,
            Integer scores, HttpServletRequest request) {
        return score(site, user.getId(), itemType, itemId, true, null == scores ? 1 : scores, request);
    }

    /**
     * @param site
     * @param user
     * @param itemType
     * @param itemId
     * @param request
     * @return
     */
    @RequestMapping("unscore")
    @Csrf
    @ResponseBody
    public boolean unscore(@RequestAttribute SysSite site, @SessionAttribute SysUser user, String itemType, long itemId,
            HttpServletRequest request) {
        return score(site, user.getId(), itemType, itemId, false, 0, request);
    }

    private boolean score(SysSite site, long userId, String itemType, long itemId, boolean score, int scores,
            HttpServletRequest request) {
        if (CommonUtils.notEmpty(itemType)) {
            CmsUserScoreId id = new CmsUserScoreId(userId, itemType, itemId);
            CmsUserScore entity = service.getEntity(id);
            if (score && null == entity || !score && null != entity) {
                Map<String, String> config = configDataComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
                boolean needStatic = ConfigDataComponent.getBoolean(config.get(SiteConfigComponent.CONFIG_STATIC_AFTER_SCORE), false);
                String ip = RequestUtils.getIpAddress(request);
                if ("content".equals(itemType)) {
                    int maxScores = ConfigDataComponent.getInt(config.get(SiteConfigComponent.CONFIG_MAX_SCORES),
                            SiteConfigComponent.DEFAULT_MAX_SCORES);
                    if (scores > maxScores) {
                        scores = maxScores;
                    } else if (0 > scores) {
                        scores = 1;
                    }
                    CmsContent content = contentService.updateScores(site.getId(), itemId, score ? 1 : -1,
                            score ? scores : -entity.getScore());
                    if (null != content) {
                        if (score) {
                            entity = new CmsUserScore();
                            entity.setId(id);
                            entity.setIp(ip);
                            entity.setScore(scores);
                            service.save(entity);
                        } else {
                            service.delete(id);
                        }
                        if (needStatic) {
                            try {
                                templateComponent.createContentFile(site, content, null, null);
                            } catch (IOException | TemplateException e) {
                                log.error(e.getMessage(), e);
                            }

                        }
                    } else if (!score) {
                        service.delete(id);
                    }
                    return true;
                } else if ("comment".equals(itemType)) {
                    CmsComment comment = commentService.updateScores(site.getId(), itemId, score ? 1 : -1);
                    if (null != comment) {
                        if (score) {
                            entity = new CmsUserScore();
                            entity.setId(id);
                            entity.setIp(ip);
                            entity.setScore(1);
                            service.save(entity);
                        } else {
                            service.delete(id);
                        }
                        if (needStatic) {
                            try {
                                templateComponent.createContentFile(site, contentService.getEntity(comment.getContentId()), null,
                                        null);
                            } catch (IOException | TemplateException e) {
                                log.error(e.getMessage(), e);
                            }
                        }
                    } else if (!score) {
                        service.delete(id);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Resource
    private CmsUserScoreService service;
    @Resource
    private CmsCommentService commentService;
    @Resource
    private CmsContentService contentService;
    @Resource
    private TemplateComponent templateComponent;

}
