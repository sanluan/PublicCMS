package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsDictionaryExclude;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsDictionaryExcludeService;

import freemarker.template.TemplateException;

/**
*
* dictionaryExcludeList 数据字典数据列表
* <p>
* 参数列表
* <ul>
* <li><code>dictionaryId</code>:字典id
* <li><code>excludeDictionaryId</code>:排除数据字典id
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>list</code>:List类型 查询结果实体列表
* {@link com.publiccms.entities.cms.CmsDictionaryExclude}
* </ul>
* 使用示例
* <p>
* &lt;@cms.dictionaryExcludeList dictionaryId='data'&gt;&lt;#list list as
* a&gt;${a.id.excludeDictionaryId}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.dictionaryExcludeList&gt;
*
* <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/cms/dictionaryExcludeList?dictionaryId=1&amp;parentValue=text', function(data){
  console.log(data);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class CmsDictionaryExcludeListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        List<CmsDictionaryExclude> list = null;
        String dictionaryId = handler.getString("dictionaryId");
        String excludeDictionaryId = handler.getString("excludeDictionaryId");
        if (CommonUtils.notEmpty(dictionaryId) || CommonUtils.notEmpty(excludeDictionaryId)) {
            SysSite site = getSite(handler);
            short siteId = null == site.getParentId() ? site.getId() : site.getParentId();
            list = service.getList(siteId, dictionaryId, excludeDictionaryId);
        } else {
            list = Collections.emptyList();
        }
        handler.put("list", list).render();
    }

    @Resource
    private CmsDictionaryExcludeService service;

}