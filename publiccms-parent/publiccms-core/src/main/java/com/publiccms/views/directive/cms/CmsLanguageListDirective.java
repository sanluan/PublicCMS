package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.entities.cms.CmsLanguage;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.FileUploadComponent;
import com.publiccms.logic.service.cms.CmsLanguageService;

import freemarker.template.TemplateException;

/**
 *
 * languageList 数据字典数据列表
 * <p>
 * 参数列表
 * <ul>
 * <li><code>code</code>:字典id,为空时返回空结果
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsLanguage}
 * </ul>
 * <p>
 * 使用示例
 * <p>
 * &lt;@cms.LanguageList&gt;&lt;#list list as
 * a&gt;${a.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.LanguageList&gt;
 *
 * <pre>
&lt;script&gt;
 fetch('${site.dynamicPath}api/directive/cms/languageList').then(res => res.json()).then(data=>{
   console.log(data);
 });
 &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsLanguageListDirective extends AbstractTemplateDirective {

    @Resource
    protected FileUploadComponent fileUploadComponent;

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        SysSite site = getSite(handler);
        List<CmsLanguage> list = service.getList(site.getId());
        boolean absoluteURL = handler.getBoolean("absoluteURL", true);
        if (absoluteURL) {
            Consumer<CmsLanguage> consumer = entity -> {
                entity.setCover(CmsUrlUtils.getUrl(fileUploadComponent.getPrefix(site), entity.getCover()));
            };
            list.forEach(consumer);
        }
        handler.put("list", list).render();
    }

    @Resource
    private CmsLanguageService service;

}