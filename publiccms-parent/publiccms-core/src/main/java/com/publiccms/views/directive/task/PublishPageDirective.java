package com.publiccms.views.directive.task;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTaskDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CmsFileUtils.FileInfo;
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
 * publishPage 页面静态化指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>path</code> 页面路径,默认值"/"
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>map</code>map类型,键值页面路径,值为生成结果
 * </ul>
 * 使用示例
 * <p>
 * &lt;@task.publishPage&gt;&lt;#list map as
 * k,v&gt;${k}:${v}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@task.publishPage&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/task/publishPage?path=&amp;appToken=接口访问授权Token', function(data){    
   console.log(data);
 });
 &lt;/script&gt;
 * </pre>
 */
@Component
public class PublishPageDirective extends AbstractTaskDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path", CommonConstants.SEPARATOR);
        SysSite site = getSite(handler);
        String filepath = siteComponent.getTemplateFilePath(site, path);
        if (CmsFileUtils.isFile(filepath)) {
            Map<String, Boolean> map = new LinkedHashMap<>();
            CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(filepath);
            if (CommonUtils.notEmpty(metadata.getPublishPath())) {
                try {
                    CmsPageData data = metadataComponent.getTemplateData(filepath);
                    templateComponent.createStaticFile(site, SiteComponent.getFullTemplatePath(site, path),
                            metadata.getPublishPath(), null, metadata.getAsMap(data), null, null);
                    map.put(path, true);
                } catch (IOException | TemplateException e) {
                    handler.getWriter().append(e.getMessage());
                    map.put(path, false);
                }
                handler.put("map", map).render();
            }
        } else if (CmsFileUtils.isDirectory(filepath)) {
            handler.put("map", deal(site, handler, path)).render();
        }
    }

    private Map<String, Boolean> deal(SysSite site, RenderHandler handler, String path) throws IOException {
        path = path.replace("\\", CommonConstants.SEPARATOR).replace("//", CommonConstants.SEPARATOR);
        Map<String, Boolean> map = new LinkedHashMap<>();
        List<FileInfo> list = CmsFileUtils.getFileList(siteComponent.getTemplateFilePath(site, path), null);
        for (FileInfo fileInfo : list) {
            String filepath = path + fileInfo.getFileName();
            if (fileInfo.isDirectory()) {
                map.putAll(deal(site, handler, filepath + CommonConstants.SEPARATOR));
            } else {
                String realTemplatePath = siteComponent.getTemplateFilePath(site, filepath);
                CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(realTemplatePath);
                if (null != metadata && CommonUtils.notEmpty(metadata.getPublishPath())) {
                    try {
                        String templatePath = SiteComponent.getFullTemplatePath(site, filepath);
                        CmsPageData data = metadataComponent.getTemplateData(realTemplatePath);
                        templateComponent.createStaticFile(site, templatePath, metadata.getPublishPath(), null,
                                metadata.getAsMap(data), null, null);
                        map.put(filepath, true);
                    } catch (IOException | TemplateException e) {
                        handler.getWriter().append(e.getMessage());
                        handler.getWriter().append("\n");
                        map.put(filepath, false);
                    }
                }
            }
        }
        return map;
    }

    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private MetadataComponent metadataComponent;

}
