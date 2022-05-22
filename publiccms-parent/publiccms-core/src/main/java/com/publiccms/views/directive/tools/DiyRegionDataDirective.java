package com.publiccms.views.directive.tools;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.component.template.DiyComponent;
import com.publiccms.views.pojo.diy.CmsRegionData;

/**
 *
 * DiyRegionDataDirective 区域diy数据获取 参数 id 区域id 结果项 object diy数据
 * 
 */
@Component
public class DiyRegionDataDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String id = handler.getString("id");
        Integer categoryId = handler.getInteger("categoryId");
        if (CommonUtils.notEmpty(id)) {
            CmsRegionData regionData = diyComponent.getRegionData(getSite(handler), id);
            if (null != regionData) {
                if (null != categoryId) {
                    CmsRegionData newRegionData = new CmsRegionData();
                    newRegionData.setId(regionData.getId());
                    if (null != regionData.getCategoryLayoutMap()) {
                        newRegionData.setLayoutList(regionData.getCategoryLayoutMap().get(categoryId));
                    }
                    regionData = newRegionData;
                }
                handler.put("object", regionData).render();
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
