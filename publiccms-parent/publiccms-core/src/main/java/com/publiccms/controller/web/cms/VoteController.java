package com.publiccms.controller.web.cms;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsUserVote;
import com.publiccms.entities.cms.CmsUserVoteId;
import com.publiccms.entities.cms.CmsVote;
import com.publiccms.entities.cms.CmsVoteItem;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.cms.CmsUserVoteService;
import com.publiccms.logic.service.cms.CmsVoteItemService;
import com.publiccms.logic.service.cms.CmsVoteService;

/**
 * 
 * VoteController 投票
 *
 */
@Controller
@RequestMapping("vote")
public class VoteController {

    /**
     * @param site
     * @param user
     * @param itemId
     * @param request
     * @return
     */
    @RequestMapping("vote")
    @Csrf
    @ResponseBody
    public boolean vote(@RequestAttribute SysSite site, @SessionAttribute SysUser user, long itemId, HttpServletRequest request) {
        return vote(site.getId(), user.getId(), itemId, RequestUtils.getIpAddress(request), true);
    }

    /**
     * @param site
     * @param user
     * @param itemId
     * @param request
     * @return
     */
    @RequestMapping("unvote")
    @Csrf
    @ResponseBody
    public boolean unvote(@RequestAttribute SysSite site, @SessionAttribute SysUser user, long itemId,
            HttpServletRequest request) {
        return vote(site.getId(), user.getId(), itemId, RequestUtils.getIpAddress(request), false);
    }

    private boolean vote(short siteId, long userId, long itemId, String ip, boolean vote) {
        CmsVoteItem item = voteItemService.getEntity(itemId);
        if (null != item) {
            CmsVote cmsVote = service.getEntity(item.getVoteId());
            Date now = CommonUtils.getDate();
            if (null != cmsVote && !cmsVote.isDisabled() && cmsVote.getSiteId() == siteId && now.before(cmsVote.getEndDate())
                    && now.after(cmsVote.getStartDate())) {
                CmsUserVoteId id = new CmsUserVoteId(userId, cmsVote.getId());
                CmsUserVote entity = userVoteService.getEntity(id);
                if (vote) {
                    if (null == entity) {
                        entity = new CmsUserVote();
                        entity.setId(id);
                        entity.setItemId(itemId);
                        entity.setIp(ip);
                        userVoteService.save(entity);
                        voteItemService.updateScores(itemId, 1);
                        service.updateScores(siteId, cmsVote.getId(), 1);
                        return true;
                    }
                } else if (null != entity) {
                    userVoteService.delete(id);
                    voteItemService.updateScores(entity.getItemId(), -1);
                    service.updateScores(siteId, cmsVote.getId(), -1);
                    return true;
                }
            }
        }
        return false;
    }

    @Autowired
    private CmsVoteService service;
    @Autowired
    private CmsVoteItemService voteItemService;
    @Autowired
    private CmsUserVoteService userVoteService;

}
