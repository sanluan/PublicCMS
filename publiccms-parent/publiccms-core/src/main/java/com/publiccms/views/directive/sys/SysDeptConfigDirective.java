package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDeptConfig;
import com.publiccms.entities.sys.SysDeptConfigId;
import com.publiccms.logic.service.sys.SysDeptConfigService;
import com.publiccms.logic.service.sys.SysDeptService;

/**
 *
 * SysDeptConfigDirective
 * 
 */
@Component
public class SysDeptConfigDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer deptId = handler.getInteger("deptId");
        String config = handler.getString("config");
        if (CommonUtils.notEmpty(deptId)) {
            if (CommonUtils.notEmpty(config)) {
                SysDeptConfig entity = service.getEntity(deptId, config);
                if (null != entity) {
                    handler.put("object", entity).render();
                }
            } else {
                String[] configs = handler.getStringArray("configs");
                if (CommonUtils.notEmpty(configs)) {
                    Map<String, Boolean> map = new LinkedHashMap<>();
                    SysDept entity = sysDeptService.getEntity(deptId);
                    if (null != entity && entity.isOwnsAllConfig()) {
                        for (String p : configs) {
                            map.put(p, true);
                        }
                    } else {
                        SysDeptConfigId[] ids = new SysDeptConfigId[configs.length];
                        for (int i = 0; i < configs.length; i++) {
                            map.put(configs[i], false);
                            ids[i] = new SysDeptConfigId(deptId, configs[i]);
                        }
                        for (SysDeptConfig e : service.getEntitys(ids)) {
                            map.put(e.getId().getConfig(), true);
                        }
                    }
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private SysDeptConfigService service;
    @Autowired
    private SysDeptService sysDeptService;
}
