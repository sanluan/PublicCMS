package org.publiccms.views.directive.home;

// Generated 2016-11-13 11:38:14 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.entities.home.HomeUser;
import org.publiccms.logic.service.home.HomeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 *
 * HomeUserDirective
 * 
 */
@Component
public class HomeUserDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger("id");
        if (notEmpty(id)) {
            handler.put("object", service.getEntity(id)).render();
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (notEmpty(ids)) {
                List<HomeUser> entityList = service.getEntitys(ids);
                Map<String, HomeUser> map = new LinkedHashMap<>();
                for (HomeUser entity : entityList) {
                    map.put(String.valueOf(entity.getUserId()), entity);
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private HomeUserService service;

}
