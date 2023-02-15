package com.publiccms.views.directive.tools;

import java.io.IOException;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.component.template.DiyComponent;
import com.publiccms.views.pojo.diy.CmsRegionData;

/**
 * regionData diy区域数据获取指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>id</code>:区域id
 * <li><code>categoryId</code>:分类id
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>object</code>:{@link com.publiccms.views.pojo.diy.CmsRegionData}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@tools.diyMetadata
 * id='00000000-0000-0000-0000-000000000000'&gt;${object.id}&lt;/@tools.diyMetadata&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/tools/regionData?itemId=00000000-0000-0000-0000-000000000000&amp;appToken=接口访问授权Token', function(data){    
   console.log(data.id);
 });
 &lt;/script&gt;
 * </pre>
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

    @Resource
    private DiyComponent diyComponent;
}
