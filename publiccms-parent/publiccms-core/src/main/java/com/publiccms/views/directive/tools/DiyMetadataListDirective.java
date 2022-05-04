package com.publiccms.views.directive.tools;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.DiyComponent;

/**
 *
 * DiyMetadataListDirective
 * 
 */
@Component
public class DiyMetadataListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        SysSite site = getSite(handler);
        String itemType = handler.getString("itemType");
        if (CommonUtils.notEmpty(itemType)) {
            if ("layout".equalsIgnoreCase(itemType)) {
                handler.put("list", diyComponent.getLayoutList(site)).render();
            } else if ("module".equalsIgnoreCase(itemType)) {
                handler.put("list", diyComponent.getModuleList(site)).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private DiyComponent diyComponent;
}
