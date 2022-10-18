package com.publiccms.views.directive.tools;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CommonConstants;

import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;

/**
 * templateSearchList 模板文件搜索列表获取
 * <p>
 * 参数列表
 * <ul>
 * <li><code>path</code> 文件路径
 * <li><code>word</code> 搜索词
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>list</code>文件列表
 * {@link com.publiccms.common.tools.CmsFileUtils.FileSearchResult}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@tools.templateSearchList path='/' word='script'&gt;&lt;#list list as
 * a&gt;${a.path}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@tools.templateSearchList&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('//cms.publiccms.com/api/directive/tools/templateSearchList?path=/&amp;word=script&amp;appToken=接口访问授权Token', function(data){    
   console.log(data);
 });
 &lt;/script&gt;
 * </pre>
 * 
 */
@Component
public class TemplateSearchListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path", CommonConstants.SEPARATOR);
        handler.put("list", CmsFileUtils.searchFileList(siteComponent.getTemplateFilePath(getSite(handler), path), path,
                handler.getString("word"))).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

}