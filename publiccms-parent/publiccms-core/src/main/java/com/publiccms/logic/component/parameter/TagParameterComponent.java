package com.publiccms.logic.component.parameter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.base.AbstractLongParameterHandler;
import com.publiccms.entities.cms.CmsTag;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsTagService;

import jakarta.annotation.Priority;
import jakarta.annotation.Resource;

/**
 * TagParameterComponent 标签参数处理组件
 */
@Component
@Priority(6)
public class TagParameterComponent extends AbstractLongParameterHandler<CmsTag> {
    @Resource
    private CmsTagService service;

    @Override
    public String getType() {
        return Config.INPUTTYPE_TAG;
    }

    @Override
    public List<CmsTag> getParameterValueList(SysSite site, Long[] ids) {
        List<CmsTag> entityList = service.getEntitys(ids);
        return entityList.stream().filter(entity -> site.getId() == entity.getSiteId()).collect(Collectors.toList());
    }

    @Override
    public CmsTag getParameterValue(SysSite site, Long id) {
        CmsTag entity = service.getEntity(id);
        if (null == entity || entity.getSiteId() != site.getId()) {
            return null;
        }
        return entity;
    }
}
