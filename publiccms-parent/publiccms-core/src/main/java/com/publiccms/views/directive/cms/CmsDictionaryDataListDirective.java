package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsDictionaryData;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsDictionaryDataService;

/**
 *
 * dictionaryDataList 数据字典数据列表
 * <p>
 * 参数列表
 * <ul>
 * <li><code>dictionaryId</code> 字典id,为空时返回空结果
 * <li><code>parentValue</code> 父节点值
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>list</code> List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsDictionaryData}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.dictionaryDataList dictionaryId='data'&gt;&lt;#list list as
 * a&gt;${a.text}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.dictionaryDataList&gt;
 *
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/cms/dictionaryDataList?dictionaryId=1&amp;parentValue=text', function(data){
   console.log(data);
 });
 &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsDictionaryDataListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        List<CmsDictionaryData> list = null;
        String dictionaryId = handler.getString("dictionaryId");
        String parentValue = handler.getString("parentValue");
        if (CommonUtils.notEmpty(dictionaryId)) {
            SysSite site = getSite(handler);
            list = service.getList(site.getId(), dictionaryId, parentValue);
        } else {
            list = new ArrayList<>();
        }
        handler.put("list", list).render();
    }

    @Autowired
    private CmsDictionaryDataService service;

}