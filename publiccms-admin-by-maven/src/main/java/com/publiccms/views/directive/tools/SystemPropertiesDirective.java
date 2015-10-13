package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.sanluan.common.base.BaseDirective;
import com.sanluan.common.handler.RenderHandler;

/**
 * 
 * SystemPropertiesDirective 系统参数指令
 *
 */
@Component
public class SystemPropertiesDirective extends BaseDirective {

    /*
     * @java.version Java version number
     * 
     * @java.vendor Java vendor specific string
     * 
     * @java.vendor.url Java vendor URL
     * 
     * @java.home Java installation directory
     * 
     * @java.class.version Java class version number
     * 
     * @java.class.path Java classpath
     * 
     * @os.name Operating System Name
     * 
     * @os.arch Operating System Architecture
     * 
     * @os.version Operating System Version
     * 
     * @file.separator File separator ("/" on Unix)
     * 
     * @path.separator Path separator (":" on Unix)
     * 
     * @line.separator Line separator ("\n" on Unix)
     * 
     * @user.name User account name
     * 
     * @user.home User home directory
     * 
     * @user.dir User's current working directory
     */
    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Properties props = System.getProperties();
        Iterator<Object> keysIterator = props.keySet().iterator();
        while (keysIterator.hasNext()) {
            Object key = keysIterator.next();
            handler.put(key.toString(), props.get(key));
        }
        handler.render();
    }
}
