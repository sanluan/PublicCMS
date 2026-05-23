package com.publiccms.views.directive.tools;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CmsLangUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.SiteAttributeComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;

import freemarker.template.TemplateException;

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
 * &lt;@tools.includePlace path='/00000000-0000-0000-0000-000000000000'/&gt;
 *
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/tools/includePlace?path=/00000000-0000-0000-0000-000000000000.html&amp;appToken=接口访问授权Token', function(data){
   console.log(data);
 });
 &lt;/script&gt;
 * </pre>
 *
 */
@Component
public class IncludePlaceDirective extends AbstractTemplateDirective {

    @Resource
    protected SiteAttributeComponent siteAttributeComponent;

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        String path = handler.getString("path");
        if (CommonUtils.notEmpty(path)) {
            SysSite site = getSite(handler);
            String defaultLang = siteAttributeComponent.getDefaultLanguage(site.getId());
            String lang = null;
            Object temp = handler.getAttribute("lang");
            if (null != temp && temp instanceof String) {
                lang = (String) temp;
            }

            String filepath = siteComponent.getTemplateFilePath(site.getId(),
                    CommonUtils.joinString(TemplateComponent.INCLUDE_DIRECTORY, path));
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath, lang, defaultLang);
            if (site.isUseSsi()) {
                StringBuilder sb = new StringBuilder("<!--#include virtual=\"/");
                if (null != site.getParentId() && CommonUtils.notEmpty(site.getDirectory())) {
                    sb.append(site.getDirectory()).append(Constants.SEPARATOR);
                }
                sb.append(TemplateComponent.INCLUDE_DIRECTORY);

                if (metadata.isEnableMultilingual() && CommonUtils.notEmpty(lang) && !lang.equalsIgnoreCase(defaultLang)) {
                    sb.append(Constants.SEPARATOR).append(lang);
                }
                sb.append(path).append("\"-->");
                handler.print(sb.toString());
            } else {
                String includePath = CommonUtils.joinString(TemplateComponent.INCLUDE_DIRECTORY,
                        CmsLangUtils.getPlaceFilepath(path, lang, defaultLang));
                String webfilepath = siteComponent.getWebFilePath(site.getId(), includePath);
                if (CmsFileUtils.exists(webfilepath)) {
                    handler.print(CmsFileUtils.getFileContent(webfilepath));
                } else {
                    templateComponent.printPlace(handler.getWriter(), site, path, lang, defaultLang, metadata);
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
