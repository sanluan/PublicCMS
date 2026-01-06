package com.publiccms.views.directive.tools;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.SiteAttributeComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;

import freemarker.template.TemplateException;

/**
 * placeMetadata 页面片段元数据获取指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>path</code>:模板路径
 * <li><code>lang</code>:语言
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>object</code>:
 * 元数据{@link com.publiccms.views.pojo.entities.CmsPlaceMetadata}
 * </ul>
 * <p>
 * 使用示例
 * <p>
 * &lt;@tools.placeMetadata
 * path='00000000-0000-0000-0000-000000000000'&gt;${object.alias}&lt;/@tools.placeMetadata&gt;
 *
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/tools/placeMetadata?path=00000000-0000-0000-0000-000000000000.html&amp;appToken=接口访问授权Token', function(data){
   console.log(data.alias);
 });
 &lt;/script&gt;
 * </pre>
 *
 */
@Component
public class PlaceMetadataDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        String path = handler.getString("path");
        String lang = handler.getString("lang");
        if (CommonUtils.notEmpty(path) && !path.endsWith(Constants.SEPARATOR)) {
            SysSite site = getSite(handler);
            String filepath = siteComponent.getTemplateFilePath(site.getId(),
                    CommonUtils.joinString(TemplateComponent.INCLUDE_DIRECTORY, path));
            String defaultLang = siteAttributeComponent.getDefaultLanguage(site.getId());
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath, lang, defaultLang);
            handler.put("object", metadata).render();
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private MetadataComponent metadataComponent;
    @Resource
    protected SiteAttributeComponent siteAttributeComponent;
}
