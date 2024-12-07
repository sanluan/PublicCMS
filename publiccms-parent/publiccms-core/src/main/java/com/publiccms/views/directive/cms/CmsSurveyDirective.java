package com.publiccms.views.directive.cms;

// Generated 2020-7-1 21:06:19 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsSurvey;
import com.publiccms.logic.service.cms.CmsSurveyService;

import freemarker.template.TemplateException;

/**
*
* survey 调查问卷查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code>:调查问卷id,结果返回<code>object</code>
* {@link com.publiccms.entities.cms.CmsSurvey}
* <li><code>ids</code>:
* 多个调查问卷id,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@cms.survey id=1&gt;${object.title}&lt;/@cms.survey&gt;
* <p>
* &lt;@cms.survey ids='1,2,3'&gt;&lt;#list map as
* k,v&gt;${k}:${v.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.survey&gt;
* 
* <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/cms/survey?id=1', function(data){    
   console.log(data.title);
 });
 &lt;/script&gt;
* </pre>
*/
@Component
public class CmsSurveyDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Long id = handler.getLong("id");
        if (CommonUtils.notEmpty(id)) {
            CmsSurvey entity = service.getEntity(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsSurvey> entityList = service.getEntitys(ids);
                Map<String, CmsSurvey> map = CommonUtils.listToMapSorted(entityList, k -> k.getId().toString(), ids);
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private CmsSurveyService service;

}
