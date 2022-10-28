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
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;

import freemarker.template.TemplateException;

/**
 *
 * publishPlace 页面片段静态化指令
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
 * &lt;@task.publishPlace&gt;&lt;#list map as
 * k,v&gt;${k}:${v}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@task.publishPlace&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/task/publishPlace?path=&amp;appToken=接口访问授权Token', function(data){    
   console.log(data);
 });
 &lt;/script&gt;
 * </pre>
 */
@Component
public class PublishPlaceDirective extends AbstractTaskDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path", CommonConstants.SEPARATOR);
        SysSite site = getSite(handler);
        String filepath = siteComponent.getTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path);
        if (CmsFileUtils.isFile(filepath)) {
            Map<String, Boolean> map = new LinkedHashMap<>();
            try {
                CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath);
                CmsPageData data = metadataComponent.getTemplateData(filepath);
                templateComponent.staticPlace(site, path, metadata, data);
                map.put(path, true);
            } catch (IOException | TemplateException e) {
                handler.getWriter().append(e.getMessage());
                map.put(path, false);
            }
            handler.put("map", map).render();
        } else if (CmsFileUtils.isDirectory(filepath)) {
            handler.put("map", dealDir(site, handler, path)).render();
        }
    }

    private Map<String, Boolean> dealDir(SysSite site, RenderHandler handler, String path) throws IOException {
        path = path.replace("\\", CommonConstants.SEPARATOR).replace("//", CommonConstants.SEPARATOR);
        Map<String, Boolean> map = new LinkedHashMap<>();
        String realPath = siteComponent.getTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path);
        List<FileInfo> list = CmsFileUtils.getFileList(realPath, null);
        for (FileInfo fileInfo : list) {
            String filepath = path + fileInfo.getFileName();
            if (fileInfo.isDirectory()) {
                map.putAll(dealDir(site, handler, filepath + CommonConstants.SEPARATOR));
            } else {
                try {
                    String realfilepath = siteComponent.getTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + filepath);
                    CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(realfilepath);
                    CmsPageData data = metadataComponent.getTemplateData(realfilepath);
                    templateComponent.staticPlace(site, filepath, metadata, data);
                    map.put(filepath, true);
                } catch (IOException | TemplateException e) {
                    handler.getWriter().append(e.getMessage());
                    handler.getWriter().append("\n");
                    map.put(filepath, false);
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
