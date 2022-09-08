package com.publiccms.views.directive.tools;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryService;

import freemarker.template.TemplateException;

/**
 *
 * createCategoryFile 创建分类静态文件指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>id</code> 分类id
 * <li><code>templatePath</code> 模板路径
 * <li><code>filePath</code> 静态文件路径
 * <li><code>pageIndex</code> 当前页码，默认为1
 * <li><code>totalPage</code> 最大页码，为空时则只生成当前页
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>url</code>静态文件路径
 * </ul>
 * 使用示例
 * <p>
 * &lt;@tools.createCategoryFile id=1 templatePath='category.html' filePath='category/'+1+'.html'&gt;${url}&lt;/@tools.createCategoryFile&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('//cms.publiccms.com/api/directive/tools/createCategoryFile?id=1&amp;templatePath=category.html&amp;filePath=category/1.html&amp;appToken=接口访问授权Token', function(data){    
   console.log(data);
 });
 &lt;/script&gt;
 * </pre>
 */
@Component
public class CreateCategoryFileDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger("id");
        String templatePath = handler.getString("templatePath");
        String filepath = handler.getString("filePath");
        Integer pageIndex = handler.getInteger("pageIndex");
        if (CommonUtils.notEmpty(id) && CommonUtils.notEmpty(templatePath) && CommonUtils.notEmpty(filepath)) {
            SysSite site = getSite(handler);
            try {
                CmsCategory category = categoryService.getEntity(id);
                if (null != category && site.getId() == category.getSiteId()) {
                    handler.put("url",
                            templateComponent.createCategoryFile(site, category, templatePath, filepath, pageIndex, handler.getInteger("totalPage")))
                            .render();
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

    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private CmsCategoryService categoryService;

}
