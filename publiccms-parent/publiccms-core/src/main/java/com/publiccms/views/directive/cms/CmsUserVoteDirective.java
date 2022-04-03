package com.publiccms.views.directive.cms;

// Generated 2020-3-26 11:46:48 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
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
        } else {
            Long[] voteIds = handler.getLongArray("voteIds");
            if (CommonUtils.notEmpty(voteIds)) {
                CmsUserVoteId[] entityIds = new CmsUserVoteId[voteIds.length];
                for (int i = 0; i < voteIds.length; i++) {
                    entityIds[i] = new CmsUserVoteId(userId, voteIds[i]);
                }
                List<CmsUserVote> entityList = service.getEntitys(entityIds);
                Map<String, CmsUserVote> map = CommonUtils.listToMap(entityList, k -> String.valueOf(k.getId().getVoteId()), null,
                        null);
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private CmsUserVoteService service;

}
