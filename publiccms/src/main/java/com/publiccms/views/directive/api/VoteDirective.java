package com.publiccms.views.directive.api;

import static com.sanluan.common.tools.RequestUtils.getIpAddress;
import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

//Generated 2015-5-10 17:54:56 by com.sanluan.common.source.SourceGenerator

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.entities.cms.CmsVote;
import com.publiccms.entities.cms.CmsVoteUser;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.cms.CmsVoteService;
import com.publiccms.logic.service.cms.CmsVoteUserService;
import com.sanluan.common.handler.RenderHandler;

@Component
public class VoteDirective extends AbstractAppDirective {

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        Integer voteId = handler.getInteger("voteId");
        String[] itemIds = handler.getStringArray("itemIds");
        CmsVote vote = voteService.getEntity(voteId);
        if (notEmpty(vote) && notEmpty(itemIds) && !vote.isDisabled()) {
            CmsVoteUser entity = new CmsVoteUser(voteId, user.getId(), arrayToCommaDelimitedString(itemIds),
                    getIpAddress(handler.getRequest()), getDate());
            voteUserService.save(entity);
        }
    }

    @Autowired
    private CmsVoteService voteService;
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