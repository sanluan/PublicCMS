package com.publiccms.views.directive.tools;

// Generated 2022-5-10 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;

import freemarker.template.TemplateException;

/**
 * fileBackupList 文件回收站列表获取指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>type</code>:文件类型【file,task,template】,默认template
 * <li><code>path</code>:文件路径
 * <li><code>orderField</code>:
 * 排序类型【fileName,fileSize,modifiedDate,createDate】,默认fileName
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>list</code>:文件列表
 * {@link com.publiccms.common.tools.CmsFileUtils.FileInfo}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@tools.fileBackupList path='/'&gt;&lt;#list list as
 * a&gt;${a.fileName}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@tools.fileBackupList&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/tools/fileBackupList?path=/&amp;appToken=接口访问授权Token', function(data){    
   console.log(data);
 });
 &lt;/script&gt;
 * </pre>
 * 
 */
@Component
public class FileBackupListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        String type = handler.getString("type");
        String path = handler.getString("path", CommonConstants.SEPARATOR);
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
        handler.put("list", CmsFileUtils.getFileList(realpath, handler.getString("orderField"))).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

}