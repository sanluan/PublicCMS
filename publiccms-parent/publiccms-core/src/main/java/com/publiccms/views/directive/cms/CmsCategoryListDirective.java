package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsCategoryAttributeService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.views.pojo.query.CmsCategoryQuery;

import freemarker.template.TemplateException;

/**
 *
 * categoryList 分类列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>parentId</code>:父分类id
 * <li><code>typeId</code>:分类类型id
 * <li><code>absoluteURL</code>:url处理为绝对路径, 默认为<code> true</code>
 * <li><code>queryAll</code>:查询全部,【true,false】,parentId为空时有效
 * <li><code>advanced</code>:开启高级选项, 默认为<code>false</code>
 * <li><code>containsAttribute</code>默认为<code>true</code>,http请求时为高级选项,为true时<code>category.attribute</code>为分类扩展数据<code>map</code>(字段编码,<code>value</code>)
 * <li><code>disabled</code>:高级选项:禁用状态, 默认为<code>false</code>
 * <li><code>hidden</code>:高级选项:隐藏,【true,false】
 * <li><code>pageIndex</code>:页码
 * <li><code>pageSize</code>:每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code>:{@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code>:List类型 查询结果实体列表,顺序排序正序,id倒序
 * {@link com.publiccms.entities.cms.CmsCategory}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.categoryList pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.categoryList&gt;
 * 
 * <pre>
   &lt;script&gt;
    $.getJSON('${site.dynamicPath}api/directive/cms/categoryList?pageSize=10', function(data){    
      console.log(data.page.totalCount);
    });
    &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsCategoryListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        SysSite site = getSite(handler);
        CmsCategoryQuery queryEntity = new CmsCategoryQuery();
        queryEntity.setQueryAll(handler.getBoolean("queryAll"));
        boolean advanced = getAdvanced(handler);
        boolean containsAttribute = handler.getBoolean("containsAttribute", false) && (!handler.inHttp() || advanced);
        if (advanced) {
            queryEntity.setDisabled(handler.getBoolean("disabled", false));
            queryEntity.setHidden(handler.getBoolean("hidden"));
        } else {
            queryEntity.setDisabled(false);
            queryEntity.setHidden(false);
        }
        queryEntity.setSiteId(site.getId());
        queryEntity.setParentId(handler.getInteger("parentId"));
        queryEntity.setTypeId(handler.getString("typeId"));
        queryEntity.setAllowContribute(handler.getBoolean("allowContribute"));

        PageHandler page = service.getPage(queryEntity, handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", handler.getInteger("count", 30)));
        @SuppressWarnings("unchecked")
        List<CmsCategory> list = (List<CmsCategory>) page.getList();
        if (null != list) {
            boolean absoluteURL = handler.getBoolean("absoluteURL", true);
            Integer[] ids = list.stream().map(CmsCategory::getId).toArray(Integer[]::new);
            Map<Integer, CmsCategoryAttribute> attributeMap = containsAttribute
                    ? CommonUtils.listToMap(attributeService.getEntitys(ids), k -> k.getCategoryId())
                    : null;
            Consumer<CmsCategory> consumer = e -> {
                if (absoluteURL) {
                    CmsUrlUtils.initCategoryUrl(site, e);
                }
                if (containsAttribute) {
                    e.setAttribute(ExtendUtils.getAttributeMap(attributeMap.get(e.getId())));
                }
            };
            list.forEach(consumer);
        }
        handler.put("page", page).render();

    }

    @Override
    public boolean supportAdvanced() {
        return true;
    }

    @Resource
    private CmsCategoryService service;
    @Resource
    private CmsCategoryAttributeService attributeService;
}