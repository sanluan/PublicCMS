package org.publiccms.views.directive.sys;

// Generated 2016-7-16 11:54:15 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.publiccms.common.tools.ExtendUtils.getExtendMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.entities.sys.SysConfigData;
import org.publiccms.entities.sys.SysConfigDataId;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.service.sys.SysConfigDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 *
 * SysConfigDataDirective
 * 
 */
@Component
public class SysConfigDataDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String code = handler.getString("code");
        String[] codes = handler.getStringArray("codes");
        SysSite site = getSite(handler);
        if (notEmpty(code)) {
            SysConfigData entity = service.getEntity(new SysConfigDataId(site.getId(), code));
            if (null != entity) {
                handler.put("object", getExtendMap(entity.getData())).render();
            }
        } else if (notEmpty(codes)) {
            SysConfigDataId[] ids = new SysConfigDataId[codes.length];
            int i = 0;
            for (String s : codes) {
                if (notEmpty(s)) {
                    ids[i++] = new SysConfigDataId(site.getId(), s);
                }
            }
            Map<String, Map<String, String>> map = new HashMap<>();
            for (SysConfigData entity : service.getEntitys(ids)) {
                map.put(entity.getId().getCode(), getExtendMap(entity.getData()));
            }
            handler.put("map", map).render();
        }
    }

    @Autowired
    private SysConfigDataService service;

}
