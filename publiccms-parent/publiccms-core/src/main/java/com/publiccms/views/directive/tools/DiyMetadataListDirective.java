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
 * diyMetadataList diy元数据列表获取指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>itemType</code> 元数据类型,【region,layout,module】
 * <li><code>region</code> 区域id,当itemType为layout或module时有效
 * <li><code>showGlobal</code> 元数据类型,【true,false】,当itemType为layout或module时有效,默认为<code>true</code>
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>list</code>
 * diy元数据列表<code>region</code>{@link com.publiccms.views.pojo.diy.CmsRegion},<code>layout</code>{@link com.publiccms.views.pojo.diy.CmsLayout},<code>module</code>{@link com.publiccms.views.pojo.diy.CmsModule}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@tools.diyMetadataList itemType='region'&gt;&lt;#list page.list as
 * a&gt;${a.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@tools.diyMetadataList&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/tools/diyMetadataList?itemType=region&amp;appToken=接口访问授权Token', function(data){    
   console.log(data.name);
 });
 &lt;/script&gt;
 * </pre>
 * 
 */
@Component
public class DiyMetadataListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        SysSite site = getSite(handler);
        String itemType = handler.getString("itemType");
        String region = handler.getString("region");
        boolean showGlobal = handler.getBoolean("showGlobal", false);
        if (CommonUtils.notEmpty(itemType)) {
            if ("region".equalsIgnoreCase(itemType)) {
                handler.put("list", diyComponent.getRegionList(site)).render();
            } else if ("layout".equalsIgnoreCase(itemType)) {
                handler.put("list", diyComponent.getLayoutList(site, region, showGlobal)).render();
            } else if ("module".equalsIgnoreCase(itemType)) {
                handler.put("list", diyComponent.getModuleList(site, region, showGlobal)).render();
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
