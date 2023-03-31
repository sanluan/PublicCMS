package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.component.template.TemplateComponent;

import freemarker.template.TemplateException;

/**
 * templateRegionList 模板文件页面可视化区域列表指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>path</code>:文件路径
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>list</code>:可视化区域id列表
 * </ul>
 * 使用示例
 * <p>
 * &lt;@tools.templateRegionList path='index.html'&gt;&lt;#list list as
 * a&gt;${a}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@tools.templateRegionList&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/tools/templateRegionList?path=index.html&amp;appToken=接口访问授权Token', function(data){    
   console.log(data);
 });
 &lt;/script&gt;
 * </pre>
 * 
 */
@Component
public class TemplateRegionListDirective extends AbstractTemplateDirective {
    public static final Pattern REGION_PATTERN = Pattern.compile("<@[_a-z\\.]*includeRegion[ ]+.*id=[\"|\']([^\"\']*)[\"|\'].*>");

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        String path = handler.getString("path");
        Set<String> regionList = new LinkedHashSet<>();
        if (CommonUtils.notEmpty(path)) {
            short siteId = getSite(handler).getId();
            String fileContent = CmsFileUtils.getFileContent(siteComponent.getTemplateFilePath(siteId, path));
            if (CommonUtils.notEmpty(fileContent)) {
                if (CommonUtils.notEmpty(fileContent)) {
                    Matcher matcher = REGION_PATTERN.matcher(fileContent);
                    while (matcher.find()) {
                        regionList.add(matcher.group(1));
                    }
                }
                Set<String> placeSet = new HashSet<>();
                Matcher matcher = TemplatePlaceListDirective.PLACE_PATTERN.matcher(fileContent);
                while (matcher.find()) {
                    placeSet.add(matcher.group(1));
                }
                for (String place : placeSet) {
                    String placeContent = CmsFileUtils.getFileContent(siteComponent.getTemplateFilePath(siteId,
                            CommonUtils.joinString(TemplateComponent.INCLUDE_DIRECTORY, place)));
                    if (CommonUtils.notEmpty(placeContent)) {
                        matcher = REGION_PATTERN.matcher(placeContent);
                        while (matcher.find()) {
                            regionList.add(matcher.group(1));
                        }
                    }
                }
            }
        }
        handler.put("list", regionList).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }
}