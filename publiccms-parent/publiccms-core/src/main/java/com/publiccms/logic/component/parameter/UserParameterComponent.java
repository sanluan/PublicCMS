package com.publiccms.logic.component.parameter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.base.AbstractLongParameterHandler;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.sys.SysUserService;

import jakarta.annotation.Priority;
import jakarta.annotation.Resource;

/**
 * UserParameterComponent 用户参数处理组件
 */
@Component
@Priority(7)
public class UserParameterComponent extends AbstractLongParameterHandler<SysUser> {
    @Resource
    private SysUserService service;

    @Override
    public String getType() {
        return Config.INPUTTYPE_TAG;
    }

    @Override
    public List<SysUser> getParameterValueList(SysSite site, Long[] ids) {
        List<SysUser> entityList = service.getEntitys(ids);
        return entityList.stream().filter(entity -> site.getId() == entity.getSiteId() && !entity.isDisabled())
                .collect(Collectors.toList());
    }

    @Override
    public SysUser getParameterValue(SysSite site, Long id) {
        SysUser entity = service.getEntity(id);
        if (null == entity || entity.isDisabled() || entity.getSiteId() != site.getId()) {
            return null;
        }
        return entity;
    }
}
