package com.publiccms.logic.component.parameter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.base.AbstractLongParameterHandler;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.FileUploadComponent;
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
    @Resource
    protected FileUploadComponent fileUploadComponent;

    @Override
    public String getType() {
        return Config.INPUTTYPE_USER;
    }

    @Override
    public List<SysUser> getParameterValueList(SysSite site, Long[] ids) {
        List<SysUser> entityList = service.getEntitys(ids);
        entityList =  entityList.stream().filter(entity -> site.getId() == entity.getSiteId() && !entity.isDisabled())
                .toList();
        entityList.forEach(e -> e.setCover(CmsUrlUtils.getUrl(fileUploadComponent.getPrefix(site), e.getCover())));
        return entityList;
    }

    @Override
    public SysUser getParameterValue(SysSite site, Long id) {
        SysUser entity = service.getEntity(id);
        if (null != entity && !entity.isDisabled() && entity.getSiteId() == site.getId()) {
            entity.setCover(CmsUrlUtils.getUrl(fileUploadComponent.getPrefix(site), entity.getCover()));
            return entity;
        }
        return null;
    }
}
