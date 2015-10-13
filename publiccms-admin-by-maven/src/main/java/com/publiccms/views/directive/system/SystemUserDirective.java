package com.publiccms.views.directive.system;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.system.SystemUser;
import com.publiccms.logic.service.system.SystemUserService;
import com.sanluan.common.base.BaseDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class SystemUserDirective extends BaseDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger(ID);
        if (null != id) {
            SystemUser entity= service.getEntity(id);
            entity.setPassword(null);
            entity.setAuthToken(null);
            handler.put("object", entity).renderIfNotNull(entity);
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (isNotEmpty(ids)) {
                List<SystemUser> entityList = service.getEntitys(ids);
                Map<String, SystemUser> map = new HashMap<String, SystemUser>();
                for (SystemUser entity: entityList) {
                    entity.setPassword(null);
                    entity.setAuthToken(null);
                    map.put(String.valueOf(entity.getId()), entity);
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private SystemUserService service;

}
