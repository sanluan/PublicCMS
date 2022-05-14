package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsDictionaryExclude;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsDictionaryExcludeService;

/**
*
* dictionaryExcludeList 数据字典数据列表
* <p>
* 参数列表
* <ul>
* <li><code>dictionaryId</code> 字典id
* <li><code>excludeDictionaryId</code> 排除数据字典id
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>list</code> List类型 查询结果实体列表
* {@link com.publiccms.entities.cms.CmsDictionaryExclude}
* </ul>
* 使用示例
* <p>
* &lt;@_dictionaryExcludeList dictionaryId='data'&gt;&lt;#list page.list as
* a&gt;${a.id.excludeDictionaryId}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@_dictionaryExcludeList&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('//cms.publiccms.com/api/directive/dictionaryExcludeList?dictionaryId=1&amp;parentValue=text', function(data){    
  console.log(data);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class CmsDictionaryExcludeListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        List<CmsDictionaryExclude> list = null;
        String dictionaryId = handler.getString("dictionaryId");
        String excludeDictionaryId = handler.getString("excludeDictionaryId");
        if (CommonUtils.notEmpty(dictionaryId) || CommonUtils.notEmpty(excludeDictionaryId)) {
            SysSite site = getSite(handler);
            list = service.getList(site.getId(), dictionaryId, excludeDictionaryId);
        } else {
            list = new ArrayList<>();
        }
        handler.put("list", list).render();
    }

    @Autowired
    private CmsDictionaryExcludeService service;

}