package com.publiccms.views.directive.sys;

// Generated 2016-1-19 11:41:45 by com.sanluan.common.source.SourceMaker

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDeptCategory;
import com.publiccms.logic.service.sys.SysDeptCategoryService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class SysDeptCategoryDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer deptId = handler.getInteger("deptId");
        Integer categoryId = handler.getInteger("categoryId");
        if (notEmpty(deptId)) {
            if (notEmpty(categoryId)) {
                handler.put("object", service.getEntity(deptId, categoryId)).render();
            } else {
                Integer[] categoryIds = handler.getIntegerArray("categoryIds");
                if (notEmpty(categoryIds)) {
                    Map<String, Boolean> map = new LinkedHashMap<String, Boolean>();
                    SysDept entity = sysDeptService.getEntity(deptId);
                    if (notEmpty(entity)) {
                        if (entity.isOwnsAllCategory()) {
                            for (Integer id : categoryIds) {
                                map.put(String.valueOf(id), true);
                            }
                        } else {
                            for (Integer id : categoryIds) {
                                map.put(String.valueOf(id), false);
                            }
                            for (SysDeptCategory e : service.getEntitys(deptId, categoryIds)) {
                                map.put(String.valueOf(e.getCategoryId()), true);
                            }
                        }
                    }
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Autowired
    private SysDeptCategoryService service;
    @Autowired
    private SysDeptService sysDeptService;
}
