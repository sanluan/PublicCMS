package com.publiccms.controller.web.cms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsUserScore;
import com.publiccms.entities.cms.CmsUserScoreId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.service.cms.CmsUserScoreService;
import com.publiccms.views.pojo.entities.CmsContentStatistics;

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
            if (score) {
                if (null == entity) {
                    if ("content".equals(itemType)) {
                        CmsContentStatistics contentStatistics = statisticsComponent.contentScores(site, itemId);
                        if (null != contentStatistics && site.getId().equals(contentStatistics.getSiteId())) {
                            entity = new CmsUserScore();
                            entity.setId(id);
                            service.save(entity);
                            return true;
                        }
                    }
                }
            } else if (null != entity) {
                if ("content".equals(itemType)) {
                    CmsContentStatistics contentStatistics = statisticsComponent.contentScores(site, itemId, false);
                    if (null != contentStatistics && site.getId().equals(contentStatistics.getSiteId())) {
                        service.delete(id);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Autowired
    private CmsUserScoreService service;
    @Autowired
    private StatisticsComponent statisticsComponent;

}
