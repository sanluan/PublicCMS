package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.views.pojo.query.CmsCategoryQuery;

/**
 *
 * categoryList 分类列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>parentId</code> 父分类id
 * <li><code>typeId</code> 分类类型id
 * <li><code>absoluteURL</code> url处理为绝对路径 默认为<code> true</code>
 * <li><code>queryAll</code> 查询全部,【true,false】，parentId为空时有效
 * <li><code>advanced</code> 开启高级选项， 默认为<code> false</code>
 * <li><code>disabled</code> 高级选项:禁用状态， 默认为<code> false</code>
 * <li><code>hidden</code> 高级选项:隐藏，【true,false】
 * <li><code>pageIndex</code> 页码
 * <li><code>pageSize</code> 每页条数
 * </ul>
 * <p>
 * 返回结果page子属性:
 * <ul>
 * <li><code>totalCount</code> int类型 数据总数
 * <li><code>pageIndex</code> int类型 当前页码
 * <li><code>list</code> List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsCategory}
 * <li><code>totalPage</code> int类型 总页数
 * <li><code>firstResult</code> int类型 第一条序号
 * <li><code>firstPage</code> boolean类型 是否第一页
 * <li><code>lastPage</code> boolean类型 是否最后一页
 * <li><code>nextPage</code> int类型 下一页页码
 * <li><code>prePage</code> int类型 上一页页码
 * </ul>
 * 使用示例
 * <p>
 * &lt;@_categoryList pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@_categoryList&gt;
 * 
 * <pre>
   &lt;script&gt;
    $.getJSON('//cms.publiccms.com/api/directive/categoryList?pageSize=10', function(data){    
      console.log(data.totalCount);
    });
    &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsCategoryListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        SysSite site = getSite(handler);
        CmsCategoryQuery queryEntity = new CmsCategoryQuery();
        queryEntity.setQueryAll(handler.getBoolean("queryAll"));
        if (handler.getBoolean("advanced", false)) {
            queryEntity.setDisabled(handler.getBoolean("disabled", false));
            queryEntity.setHidden(handler.getBoolean("hidden"));
        } else {
            queryEntity.setDisabled(false);
            queryEntity.setHidden(false);
        }
        queryEntity.setSiteId(site.getId());
        queryEntity.setParentId(handler.getInteger("parentId"));
        queryEntity.setTypeId(handler.getInteger("typeId"));
        queryEntity.setAllowContribute(handler.getBoolean("allowContribute"));

        PageHandler page = service.getPage(queryEntity, handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", handler.getInteger("count", 30)));
        @SuppressWarnings("unchecked")
        List<CmsCategory> list = (List<CmsCategory>) page.getList();
        if (null != list && handler.getBoolean("absoluteURL", true)) {
            list.forEach(e -> {
                TemplateComponent.initCategoryUrl(site, e);
            });
        }
        handler.put("page", page).render();
    }

    @Autowired
    private CmsCategoryService service;

}