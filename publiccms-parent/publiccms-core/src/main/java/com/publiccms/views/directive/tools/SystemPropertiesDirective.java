package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import com.publiccms.common.base.AbstractTemplateDirective;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 * systemProperties 服务器参数指令
 * <p>
 * 返回结果
 * <ul>
 * <li><code>java.version</code>:Java version number
 * <li><code>java.vendor</code>:Java vendor specific string
 * <li><code>java.vendor.url</code>:Java vendor URL
 * <li><code>java.home</code>:Java installation directory
 * <li><code>java.class.version</code>:Java class version number
 * <li><code>java.class.path</code>:Java classpath
 * <li><code>os.name</code>:Operating System Name
 * <li><code>os.arch</code>:Operating System Architecture
 * <li><code>os.version</code>:Operating System Version
 * <li><code>file.separator</code>:File separator (SEPARATOR on Unix)
 * <li><code>path.separator</code>:Path separator (":" on Unix)
 * <li><code>line.separator</code>:Line separator ("\n" on Unix)
 * <li><code>user.name</code>:User account name
 * <li><code>user.home</code>:User home directory
 * <li><code>user.dir</code>:User's current working directory
 * </ul>
 * <p>
 * 使用示例
 * <p>
 * &lt;@tools.systemProperties&gt;${.vars['java.version']}&lt;/@tools.placeMetadata&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/tools/systemProperties?appToken=接口访问授权Token', function(data){    
   console.log(data['java.version']);
 });
 &lt;/script&gt;
 * </pre>
 */
@Component
public class SystemPropertiesDirective extends AbstractTemplateDirective {
    Properties props = System.getProperties();

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Iterator<Object> keysIterator = props.keySet().iterator();
        while (keysIterator.hasNext()) {
            Object key = keysIterator.next();
            handler.put(key.toString(), props.get(key));
        }
        handler.render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }
}
