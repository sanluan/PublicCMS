package com.publiccms.views.directive.tools;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;

/**
 * fileExists 文件是否存在指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>type</code> 文件类型【file,task,template】,默认template
 * <li><code>path</code> 文件路径
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>object</code> boolean类型文件是否存在
 * </ul>
 * 使用示例
 * <p>
 * &lt;@tools.fileExists type='file' path='/'&gt;&lt;#list list as
 * a&gt;${a}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@tools.fileExists&gt;
 *
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/tools/fileExists?type=file&amp;path=/&amp;appToken=接口访问授权Token', function(data){
   console.log(data);
 });
 &lt;/script&gt;
 * </pre>
 *
 */
@Component
public class FileExistDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String type = handler.getString("type");
        String path = handler.getString("path");
        SysSite site = getSite(handler);
        String realpath;
        if (CommonUtils.notEmpty(type)) {
            switch (type) {
            case "file":
                realpath = siteComponent.getWebFilePath(site, path);
                break;
            case "task":
                realpath = siteComponent.getTaskTemplateFilePath(site, path);
                break;
            case "template":
            default:
                realpath = siteComponent.getTemplateFilePath(site, path);
            }
        } else {
            realpath = siteComponent.getTemplateFilePath(site, path);
        }
        handler.put("object", CommonUtils.notEmpty(path) && CmsFileUtils.exists(realpath)).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

}