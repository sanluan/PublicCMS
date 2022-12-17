package com.publiccms.views.directive.tools;

// Generated 2022-5-10 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;

/**
 * fileBackupContent 备份文件内容获取指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>type</code> 文件类型【file,task,template】,默认template
 * <li><code>path</code> 文件路径
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>object</code>文件内容文本
 * </ul>
 * 使用示例
 * <p>
 * &lt;@tools.fileBackupContent type='file'
 * path='index.html'&gt;${object}&lt;/@tools.fileBackupContent&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/tools/fileBackupContent?type=file&amp;path=index.html&amp;appToken=接口访问授权Token', function(data){    
   console.log(data);
 });
 &lt;/script&gt;
 * </pre>
 * 
 */
@Component
public class FileBackupContentDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String type = handler.getString("type");
        String path = handler.getString("path");
        if (CommonUtils.notEmpty(path)) {
            SysSite site = getSite(handler);
            String realpath;
            if (CommonUtils.notEmpty(type)) {
                switch (type) {
                case "file":
                    realpath = siteComponent.getWebBackupFilePath(site.getId(), path);
                    break;
                case "task":
                    realpath = siteComponent.getTaskTemplateBackupFilePath(site.getId(), path);
                    break;
                case "template":
                default:
                    realpath = siteComponent.getTemplateBackupFilePath(site.getId(), path);
                }
            } else {
                realpath = siteComponent.getTemplateBackupFilePath(site.getId(), path);
            }
            handler.put("object", CmsFileUtils.getFileContent(realpath)).render();
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }
}
