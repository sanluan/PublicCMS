package com.publiccms.views.directive.tools;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.sys.SysLock;
import com.publiccms.logic.component.site.LockComponent;

/**
 *
 * CreateCategoryFileDirective
 * 
 */
@Component
public class CreateLockDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String itemType = handler.getString("itemType");
        String itemId = handler.getString("itemId");
        Long userId = handler.getLong("userId");
        Boolean counter = handler.getBoolean("counter", false);
        SysLock entity = lockComponent.lock(getSite(handler).getId(), itemType, itemId, userId, counter);
        handler.put("object", entity).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private LockComponent lockComponent;

}
