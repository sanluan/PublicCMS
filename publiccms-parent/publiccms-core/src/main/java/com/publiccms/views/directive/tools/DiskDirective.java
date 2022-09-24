package com.publiccms.views.directive.tools;

import java.io.File;
import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 * 
 * disk PublicCMS磁盘空间与路径指令
* <p>
* 返回结果
* <ul>
* <li><code>freeSpace</code>空白空间
* <li><code>totalSpace</code>总空间
* <li><code>usableSpace</code>可用空间
* <li><code>rootPath</code>绝对路径
* </ul>
* 使用示例
* <p>
* &lt;@tools.disk&gt;${freeSpace}&lt;/@tools.disk&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('//cms.publiccms.com/api/directive/tools/disk?appToken=接口访问授权Token', function(data){    
 console.log(data);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class DiskDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        File root = new File(siteComponent.getRootPath());
        handler.put("freeSpace", root.getFreeSpace());
        handler.put("totalSpace", root.getTotalSpace());
        handler.put("usableSpace", root.getUsableSpace());
        handler.put("rootPath", root.getAbsolutePath());
        handler.render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }
}
