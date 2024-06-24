package com.publiccms.controller.web.cms;

import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsUserVote;
import com.publiccms.entities.cms.CmsUserVoteId;
import com.publiccms.entities.cms.CmsVote;
import com.publiccms.entities.cms.CmsVoteItem;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.LockComponent;
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
    @Resource
    private CmsVoteItemService voteItemService;
    @Resource
    private CmsUserVoteService userVoteService;
    @Resource
    private LockComponent lockComponent;

    /**
     * @param site
     * @param session
     * @param _csrf
     * @param itemId
     * @param request
     * @return
     */
    @RequestMapping("vote")
    @ResponseBody
    public boolean vote(@RequestAttribute SysSite site, HttpSession session, String _csrf, long itemId,
            HttpServletRequest request) {
        return vote(site.getId(), request, session, _csrf, itemId, true);
    }

    /**
     * @param site
     * @param session
     * @param _csrf
     * @param itemId
     * @param request
     * @return
     */
    @RequestMapping("unvote")
    @ResponseBody
    public boolean unvote(@RequestAttribute SysSite site, HttpSession session, String _csrf, long itemId,
            HttpServletRequest request) {
        return vote(site.getId(), request, session, _csrf, itemId, false);
    }

    private boolean vote(short siteId, HttpServletRequest request, HttpSession session, String _csrf, long itemId, boolean vote) {
        CmsVoteItem item = voteItemService.getEntity(itemId);
        if (null != item) {
            CmsVote cmsVote = service.getEntity(item.getVoteId());
            SysUser user = ControllerUtils.getUserFromSession(session);
            Date now = CommonUtils.getDate();
            if (null != cmsVote && !cmsVote.isDisabled() && cmsVote.getSiteId() == siteId && now.before(cmsVote.getEndDate())
                    && now.after(cmsVote.getStartDate())
                    && (null != user && null != _csrf && _csrf.equals(ControllerUtils.getWebToken(request))
                            || cmsVote.isAllowAnonymous())) {
                String ip = RequestUtils.getIpAddress(request);
                Long userId = null;
                if (null == user) {
                    if (lockComponent.isLocked(siteId, LockComponent.ITEM_TYPE_IP_VOTE, ip, null)) {
                        return false;
                    }
                    userId = userVoteService.getId();
                    lockComponent.lock(siteId, LockComponent.ITEM_TYPE_IP_VOTE, ip, null, true);
                } else {
                    userId = user.getId();
                }
                CmsUserVoteId id = new CmsUserVoteId(userId, cmsVote.getId());
                CmsUserVote entity = userVoteService.getEntity(id);
                if (vote) {
                    if (null == entity) {
                        entity = new CmsUserVote();
                        entity.setAnonymous(null == user);
                        entity.setId(id);
                        entity.setItemId(itemId);
                        entity.setIp(ip);
                        userVoteService.save(entity);
                        voteItemService.updateVotes(itemId, 1);
                        service.updateVotes(siteId, cmsVote.getId(), 1);
                        return true;
                    }
                } else if (null != entity) {
                    userVoteService.delete(id);
                    voteItemService.updateVotes(entity.getItemId(), -1);
                    service.updateVotes(siteId, cmsVote.getId(), -1);
                    return true;
                }
            }
        }
        return false;
    }

    @Resource
    private CmsVoteService service;
}
