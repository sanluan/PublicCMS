package com.publiccms.views.directive.api;

//Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsVote;
import com.publiccms.entities.cms.CmsVoteUser;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.cms.CmsVoteItemService;
import com.publiccms.logic.service.cms.CmsVoteService;
import com.publiccms.logic.service.cms.CmsVoteUserService;

/**
 *
 * VoteDirective
 * 
 */
@Component
public class VoteDirective extends AbstractAppDirective {

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        Integer voteId = handler.getInteger("voteId");
        Long[] itemIds = handler.getLongArray("itemIds");
        CmsVote vote = voteService.getEntity(voteId);
        if (null != vote && CommonUtils.notEmpty(itemIds) && !vote.isDisabled()
                && 0 == voteUserService.getPage(voteId, user.getId(), null, null, null, null, null, null).getTotalCount()) {
            CmsVoteUser entity = new CmsVoteUser(voteId, user.getId(), StringUtils.arrayToCommaDelimitedString(itemIds),
                    RequestUtils.getIpAddress(handler.getRequest()), CommonUtils.getDate());
            voteUserService.save(entity);
            voteService.updateUserCounts(voteId);
            voteItemService.updateScores(itemIds);
        }
    }

    @Autowired
    private CmsVoteService voteService;
    @Autowired
    private CmsVoteItemService voteItemService;
    @Autowired
    private CmsVoteUserService voteUserService;

    @Override
    public boolean needUserToken() {
        return true;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }
}