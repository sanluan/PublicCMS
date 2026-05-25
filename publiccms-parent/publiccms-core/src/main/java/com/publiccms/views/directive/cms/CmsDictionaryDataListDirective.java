package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsDictionaryData;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsDictionaryDataService;

import freemarker.template.TemplateException;

/**
 *
 * dictionaryDataList 数据字典数据列表
 * <p>
 * 上下文变量
 * <ul>
 * <li><code>lang</code>:语言
 * </ul>
 * <p>
 * 参数列表
 * <ul>
 * <li><code>dictionaryId</code>:字典id,为空时返回空结果
 * <li><code>parentValue</code>:父节点值
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsDictionaryData}
 * </ul>
 * <p>
 * 使用示例
 * <p>
 * &lt;#assign lang="cn"/&gt;
 * &lt;@cms.dictionaryDataList dictionaryId='data'&gt;&lt;#list list as
 * a&gt;${a.text}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.dictionaryDataList&gt;
 *
 * <pre>
&lt;script&gt;
 fetch('${site.dynamicPath}api/directive/cms/dictionaryDataList?dictionaryId=1&amp;parentValue=text',{"headers":{"lang":"cn"}}).then(res => res.json()).then(data=>{
   console.log(data);
 });
 &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsDictionaryDataListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        List<CmsDictionaryData> list = null;
        String dictionaryId = handler.getString("dictionaryId");
        String parentValue = handler.getString("parentValue");
        String lang = handler.getStringAttribute("lang");
        if (CommonUtils.notEmpty(dictionaryId)) {
            SysSite site = getSite(handler);
            short siteId = null == site.getParentId() ? site.getId() : site.getParentId();
            list = service.getList(siteId, dictionaryId, parentValue);
            if (null != list) {
                list.forEach(entity -> {
                    entity.setAttribute(ExtendUtils.getExtendMap(entity.getLangdata()));
                    String text = entity.getAttribute().get(lang);
                    if (CommonUtils.notEmpty(lang) && CommonUtils.notEmpty(text)) {
                        entity.setText(text);
                    }
                });
            }
        } else {
            list = Collections.emptyList();
        }
        handler.put("list", list).render();
    }

    @Resource
    private CmsDictionaryDataService service;

}