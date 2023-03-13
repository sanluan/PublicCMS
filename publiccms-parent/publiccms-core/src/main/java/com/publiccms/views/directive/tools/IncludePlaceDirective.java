package com.publiccms.views.directive.tools;

import java.io.IOException;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;

/**
 * includePlace 包含页面片段指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>path</code>:路径
 * </ul>
 * <p>
 * 打印包含结果
 * <p>
 * 使用示例
 * <p>
 * &lt;@tools.includePlace path='00000000-0000-0000-0000-000000000000'/&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/tools/includePlace?path=00000000-0000-0000-0000-000000000000.html&amp;appToken=接口访问授权Token', function(data){    
   console.log(data);
 });
 &lt;/script&gt;
 * </pre>
 * 
 */
@Component
public class IncludePlaceDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path");
        if (CommonUtils.notEmpty(path)) {
            SysSite site = getSite(handler);
            String filepath = siteComponent.getTemplateFilePath(site.getId(),
                    CommonUtils.joinString(TemplateComponent.INCLUDE_DIRECTORY, path));
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath);
            if (site.isUseSsi()) {
                StringBuilder sb = new StringBuilder("<!--#include virtual=\"/");
                if (null != site.getParentId() && CommonUtils.notEmpty(site.getDirectory())) {
                    sb.append(site.getDirectory()).append(CommonConstants.SEPARATOR);
                }
                sb.append(TemplateComponent.INCLUDE_DIRECTORY).append(path).append("\"-->");
                handler.print(sb.toString());
            } else {
                String webfilepath = siteComponent.getWebFilePath(site.getId(),
                        CommonUtils.joinString(TemplateComponent.INCLUDE_DIRECTORY, path));
                if (CmsFileUtils.exists(webfilepath)) {
                    handler.print(CmsFileUtils.getFileContent(webfilepath));
                } else {
                    CmsPageData data = metadataComponent.getTemplateData(filepath);
                    templateComponent.printPlace(handler.getWriter(), site, path, metadata, data);
                }
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private TemplateComponent templateComponent;
    @Resource
    private MetadataComponent metadataComponent;

}
