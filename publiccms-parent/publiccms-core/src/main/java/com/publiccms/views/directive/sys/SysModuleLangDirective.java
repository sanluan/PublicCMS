package com.publiccms.views.directive.sys;

// Generated 2018-6-5 21:58:15 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysModuleLang;
import com.publiccms.entities.sys.SysModuleLangId;
import com.publiccms.logic.service.sys.SysModuleLangService;

/**
*
* sysModuleLang 模块语言查询指令
* <p>
* 参数列表
* <ul>
* <li><code>moduleId</code> 模块id,结果返回<code>object</code>
* {@link com.publiccms.entities.sys.SysModule}
* <li><code>lang</code> 语言【zh:中文,en:英语,ja:日语】
* </ul>
* 使用示例
* <p>
* &lt;@sys.moduleLang moduleId='page' lang='zh'&gt;${object}&lt;/@sys.moduleLang&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('//sys.publicsys.com/api/directive/sys/moduleLang?moduleId=page&amp;lang=zh', function(data){    
 console.log(data);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class SysModuleLangDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String moduleId = handler.getString("moduleId");
        String lang = handler.getString("lang");
        if (CommonUtils.notEmpty(moduleId) && null != lang) {
            SysModuleLang entity = service.getEntity(new SysModuleLangId(moduleId, lang));
            if (null != entity) {
                handler.put("object", entity).render();
            }
        }
    }

    @Resource
    private SysModuleLangService service;

}
