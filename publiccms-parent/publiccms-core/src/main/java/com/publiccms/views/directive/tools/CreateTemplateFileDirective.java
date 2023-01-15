package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPageMetadata;

import freemarker.template.TemplateException;

/**
 *
 * createTemplateFile 创建分类静态文件指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>templatePath</code> 模板路径
 * <li><code>filePath</code> 静态文件路径
 * <li><code>pageIndex</code> 当前页码,默认为1
 * <li><code>parameters</code> 参数map
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>url</code>静态文件路径
 * </ul>
 * 使用示例
 * <p>
 * &lt;@tools.createTemplateFile templatePath='template.html'
 * filePath='page/'+1+'.html'
 * parameters={"parameter1","value1"}&gt;${url}&lt;/@tools.createTemplateFile&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/tools/createTemplateFile?id=1&amp;templatePath=template.html&amp;filePath=page/1.html&amp;parameters.parameter1=value1&amp;appToken=接口访问授权Token', function(data){    
  console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class CreateTemplateFileDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String templatePath = handler.getString("templatePath");
        String filepath = handler.getString("filePath");
        Integer pageIndex = handler.getInteger("pageIndex");
        if (CommonUtils.notEmpty(templatePath) && CommonUtils.notEmpty(filepath)) {
            SysSite site = getSite(handler);
            String templateFullPath = SiteComponent.getFullTemplatePath(site.getId(), templatePath);
            try {
                Map<String, Object> model = new HashMap<>();
                Map<String, Object> parameters = handler.getMap("parameters");
                if (null != parameters) {
                    model.putAll(parameters);
                }
                String realTemplatePath = siteComponent.getTemplateFilePath(site.getId(), templatePath);
                CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(realTemplatePath);
                CmsPageData data = metadataComponent.getTemplateData(realTemplatePath);
                Map<String, Object> metadataMap = metadata.getAsMap(data);
                handler.put("url",
                        templateComponent.createStaticFile(site, templateFullPath, filepath, pageIndex, metadataMap, model, null))
                        .render();
            } catch (IOException | TemplateException e) {
                handler.print(e.getMessage());
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
