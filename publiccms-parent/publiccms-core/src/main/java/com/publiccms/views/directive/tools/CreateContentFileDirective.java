package com.publiccms.views.directive.tools;

import java.io.IOException;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsLangUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryLangId;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentLang;
import com.publiccms.entities.cms.CmsContentLangId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryLangService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentLangService;
import com.publiccms.logic.service.cms.CmsContentService;

import freemarker.template.TemplateException;

/**
 *
 * createContentFile 创建内容静态文件指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>id</code>:内容id
 * <li><code>lang</code>:语言
 * <li><code>templatePath</code>:模板路径
 * <li><code>filePath</code>:静态文件路径
 * <li><code>pageIndex</code>:当前页码,默认为1
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>url</code>:静态文件路径
 * </ul>
 * <p>
 * 使用示例
 * <p>
 * &lt;@tools.createContentFile id=1 templatePath='content.html'
 * filePath='content/'+1+'.html'&gt;${url}&lt;/@tools.createContentFile&gt;
 *
 * <pre>
&lt;script&gt;
fetch('${site.dynamicPath}api/directive/tools/createContentFile?id=1&amp;templatePath=content.html&amp;filePath=content/1.html',{"headers":{"appToken":"接口访问授权Token"}}).then(res => res.json()).then(data=>{
  console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class CreateContentFileDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Long id = handler.getLong("id");
        String lang = handler.getStringAttribute("lang");
        String templatePath = handler.getString("templatePath");
        String filepath = handler.getString("filePath");
        Integer pageIndex = handler.getInteger("pageIndex");
        if (CommonUtils.notEmpty(id) && CommonUtils.notEmpty(templatePath) && CommonUtils.notEmpty(filepath)) {
            SysSite site = getSite(handler);
            try {
                CmsContent content = contentService.getEntity(id);
                if (null != content && site.getId() == content.getSiteId()) {
                    CmsContentLang langEntity = null;
                    if (CommonUtils.notEmpty(lang) && !lang.equalsIgnoreCase(content.getLang())) {
                        langEntity = contentLangService.getEntity(new CmsContentLangId(id, lang));
                        CmsLangUtils.initLang(content, langEntity);
                    }
                    CmsCategory category = categoryService.getEntity(content.getCategoryId());
                    if (null != category && CommonUtils.notEmpty(lang) && !lang.equalsIgnoreCase(category.getLang())) {
                        CmsLangUtils.initLang(category,
                                categoryLangService.getEntity(new CmsCategoryLangId(content.getCategoryId(), lang)));
                    }
                    handler.put("url", templateComponent.createContentFile(site, content, langEntity, category, false,
                            templatePath, filepath, pageIndex)).render();
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
    @Resource
    private CmsCategoryLangService categoryLangService;
    @Resource
    private CmsContentLangService contentLangService;

}
