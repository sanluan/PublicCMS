package com.publiccms.views.directive.tools;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPageMetadata;

/**
 * metadata 模板元数据获取指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>path</code> 模板路径
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>object</code>
 * 元数据{@link com.publiccms.views.pojo.entities.CmsPageMetadata}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@tools.metadata
 * path='index.html'&gt;${object.alias}&lt;/@tools.metadata&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/tools/metadata?path=index.html&amp;appToken=接口访问授权Token', function(data){    
   console.log(data.alias);
 });
 &lt;/script&gt;
 * </pre>
 * 
 */
@Component
public class MetadataDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path");
        if (CommonUtils.notEmpty(path) && !path.endsWith(CommonConstants.SEPARATOR)) {
            SysSite site = getSite(handler);
            String filepath = siteComponent.getTemplateFilePath(site.getId(), path);
            CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(filepath);
            CmsPageData data = metadataComponent.getTemplateData(filepath);
            handler.put("object", metadata.getAsMap(data)).render();
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private MetadataComponent metadataComponent;
}
