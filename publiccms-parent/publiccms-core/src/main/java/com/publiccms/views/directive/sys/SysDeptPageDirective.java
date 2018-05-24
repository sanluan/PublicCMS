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
import com.publiccms.entities.sys.SysDeptPage;
import com.publiccms.entities.sys.SysDeptPageId;
import com.publiccms.logic.service.sys.SysDeptPageService;
import com.publiccms.logic.service.sys.SysDeptService;

/**
 *
 * SysDeptPageDirective
 * 
 */
@Component
public class SysDeptPageDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer deptId = handler.getInteger("deptId");
        String page = handler.getString("page");
        if (CommonUtils.notEmpty(deptId)) {
            if (CommonUtils.notEmpty(page)) {
                SysDeptPage entity = service.getEntity(deptId, page);
                if (null != entity) {
                    handler.put("object", entity).render();
                }
            } else {
                String[] pages = handler.getStringArray("pages");
                if (CommonUtils.notEmpty(pages)) {
                    Map<String, Boolean> map = new LinkedHashMap<>();
                    SysDept entity = sysDeptService.getEntity(deptId);
                    if (null != entity && entity.isOwnsAllPage()) {
                        for (String p : pages) {
                            map.put(p, true);
                        }
                    } else {
                        SysDeptPageId[] ids = new SysDeptPageId[pages.length];
                        for (int i = 0; i < pages.length; i++) {
                            map.put(pages[i], false);
                            ids[i] = new SysDeptPageId(deptId, pages[i]);
                        }
                        for (SysDeptPage e : service.getEntitys(ids)) {
                            map.put(e.getId().getPage(), true);
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
    private SysDeptPageService service;
    @Autowired
    private SysDeptService sysDeptService;
}
