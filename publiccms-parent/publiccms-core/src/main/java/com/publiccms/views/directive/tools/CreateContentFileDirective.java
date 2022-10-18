package com.publiccms.views.directive.tools;

import java.io.IOException;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentService;

import freemarker.template.TemplateException;

/**
 *
 * createContentFile 创建内容静态文件指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>id</code> 内容id
 * <li><code>templatePath</code> 模板路径
 * <li><code>filePath</code> 静态文件路径
 * <li><code>pageIndex</code> 当前页码,默认为1
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>url</code>静态文件路径
 * </ul>
 * 使用示例
 * <p>
 * &lt;@tools.createContentFile id=1 templatePath='content.html'
 * filePath='content/'+1+'.html'&gt;${url}&lt;/@tools.createContentFile&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('//cms.publiccms.com/api/directive/tools/createContentFile?id=1&amp;templatePath=content.html&amp;filePath=content/1.html&amp;appToken=接口访问授权Token', function(data){    
  console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class CreateContentFileDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        String templatePath = handler.getString("templatePath");
        String filepath = handler.getString("filePath");
        Integer pageIndex = handler.getInteger("pageIndex");
        if (CommonUtils.notEmpty(id) && CommonUtils.notEmpty(templatePath) && CommonUtils.notEmpty(filepath)) {
            SysSite site = getSite(handler);
            try {
                CmsContent content = contentService.getEntity(id);
                if (null != content && site.getId() == content.getSiteId()) {
                    CmsCategory category = categoryService.getEntity(content.getCategoryId());
                    handler.put("url", templateComponent.createContentFile(site, content, category, false, templatePath, filepath,
                            pageIndex)).render();
                }
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
    private CmsCategoryService categoryService;
    @Resource
    private CmsContentService contentService;

}
