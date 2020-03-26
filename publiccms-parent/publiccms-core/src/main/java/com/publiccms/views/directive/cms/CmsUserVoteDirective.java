package com.publiccms.views.directive.cms;

// Generated 2020-3-26 11:46:48 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.cms.CmsUserVote;
import com.publiccms.entities.cms.CmsUserVoteId;
import com.publiccms.logic.service.cms.CmsUserVoteService;

/**
 *
 * CmsVoteUserDirective
 * 
 */
@Component
public class CmsUserVoteDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long userId = handler.getLong("userId");
        Long voteId = handler.getLong("voteId");
        if (null != userId && null != voteId) {
            CmsUserVote entity = service.getEntity(new CmsUserVoteId(userId, voteId));
            if (null != entity) {
                handler.put("object", entity).render();
            }
        }
    }

    @Autowired
    private CmsUserVoteService service;

}
