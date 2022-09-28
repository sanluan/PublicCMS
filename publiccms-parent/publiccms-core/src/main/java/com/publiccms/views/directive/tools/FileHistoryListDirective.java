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

/**
 * fileHistoryList 文件修改历史列表获取
 * <p>
 * 参数列表
 * <ul>
 * <li><code>type</code> 文件类型【file,task,template】,默认template
 * <li><code>path</code> 文件路径
 * <li><code>orderField</code>
 * 排序类型【fileName,fileSize,modifiedDate,createDate】,默认fileName
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>list</code>文件列表
 * {@link com.publiccms.common.tools.CmsFileUtils.FileInfo}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@tools.fileHistoryList path='/'&gt;&lt;#list list as
 * a&gt;${a.fileName}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@tools.fileHistoryList&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('//cms.publiccms.com/api/directive/tools/fileHistoryList?path=/&amp;appToken=接口访问授权Token', function(data){    
   console.log(data);
 });
 &lt;/script&gt;
 * </pre>
 * 
 */
@Component
public class FileHistoryListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String type = handler.getString("type");
        String path = handler.getString("path", CommonConstants.SEPARATOR);
        SysSite site = getSite(handler);
        String realpath;
        if (CommonUtils.notEmpty(type)) {
            switch (type) {
            case "file":
                realpath = siteComponent.getWebHistoryFilePath(site, path, false);
                break;
            case "task":
                realpath = siteComponent.getTaskTemplateHistoryFilePath(site, path, false);
                break;
            case "template":
            default:
                realpath = siteComponent.getTemplateHistoryFilePath(site, path, false);
            }
        } else {
            realpath = siteComponent.getTemplateHistoryFilePath(site, path, false);
        }
        handler.put("list", CmsFileUtils.getFileList(realpath, handler.getString("orderField"))).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

}