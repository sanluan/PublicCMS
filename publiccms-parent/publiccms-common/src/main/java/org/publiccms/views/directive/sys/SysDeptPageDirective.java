package org.publiccms.views.directive.sys;

// Generated 2016-1-19 11:41:45 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.entities.sys.SysDept;
import org.publiccms.entities.sys.SysDeptPage;
import org.publiccms.entities.sys.SysDeptPageId;
import org.publiccms.logic.service.sys.SysDeptPageService;
import org.publiccms.logic.service.sys.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

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
        if (notEmpty(deptId)) {
            if (notEmpty(page)) {
                SysDeptPage entity = service.getEntity(deptId, page);
                handler.put("object", entity).render();
            } else {
                String[] pages = handler.getStringArray("pages");
                if (notEmpty(pages)) {
                    Map<String, Boolean> map = new LinkedHashMap<>();
                    SysDept entity = sysDeptService.getEntity(deptId);
                    if (null != entity && entity.isOwnsAllCategory()) {
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
