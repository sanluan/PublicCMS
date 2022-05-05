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
 * DiyMetadataDirective diy元数据获取
 * 参数列表：
 * itemType 元数据类型 可选值 region,layout,module
 * itemId 元数据id
 * 结果项 object 为diy元数据 
 * 
 */
@Component
public class DiyMetadataDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        SysSite site = getSite(handler);
        String itemType = handler.getString("itemType");
        String itemId = handler.getString("itemId");
        if (CommonUtils.notEmpty(itemType) && CommonUtils.notEmpty(itemId)) {
            if ("region".equalsIgnoreCase(itemType)) {
                handler.put("object", diyComponent.getRegion(site, itemId)).render();
            } else if ("layout".equalsIgnoreCase(itemType)) {
                handler.put("object", diyComponent.getLayout(site, itemId)).render();
            } else if ("module".equalsIgnoreCase(itemType)) {
                handler.put("object", diyComponent.getModule(site, itemId)).render();
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
