package com.publiccms.views.directive.api;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.cache.CacheComponent;

import jakarta.annotation.Resource;
import freemarker.template.TemplateException;

/**
*
* clearCache 缓存清理接口
* <p>
* 使用示例
* <p>
* <pre>
  &lt;script&gt;
   $.getJSON('${site.dynamicPath}api/clearCache&amp;appToken=接口访问授权Token', function(data){    
     console.log("ok");
   });
   &lt;/script&gt;
* </pre>
*/
@Component
public class ClearCacheDirective extends AbstractAppDirective {

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, TemplateException {
        cacheComponent.clear();
        handler.render();
    }

    @Resource
    private CacheComponent cacheComponent;

    @Override
    public boolean needUserToken() {
        return false;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }
}