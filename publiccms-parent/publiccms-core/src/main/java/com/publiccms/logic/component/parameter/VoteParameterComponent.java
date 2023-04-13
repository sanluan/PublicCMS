package com.publiccms.logic.component.parameter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.base.AbstractLongParameterHandler;
import com.publiccms.entities.cms.CmsVote;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsVoteService;

import jakarta.annotation.Priority;
import jakarta.annotation.Resource;

/**
 * VoteParameterComponent 投票参数处理组件
 */
@Component
@Priority(9)
public class VoteParameterComponent extends AbstractLongParameterHandler<CmsVote> {
    @Resource
    private CmsVoteService service;

    @Override
    public String getType() {
        return Config.INPUTTYPE_VOTE;
    }

    @Override
    public List<CmsVote> getParameterValueList(SysSite site, Long[] ids) {
        List<CmsVote> entityList = service.getEntitys(ids);
        return entityList.stream().filter(entity -> site.getId() == entity.getSiteId() && !entity.isDisabled())
                .toList();
    }

    @Override
    public CmsVote getParameterValue(SysSite site, Long id) {
        CmsVote entity = service.getEntity(id);
        if (null != entity && !entity.isDisabled() && entity.getSiteId() == site.getId()) {
            return entity;
        }
        return null;
    }
}
