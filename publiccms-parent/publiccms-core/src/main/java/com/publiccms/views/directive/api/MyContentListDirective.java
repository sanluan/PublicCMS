package com.publiccms.views.directive.api;

//Generated 2015-5-10 17:54:56 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.views.pojo.entities.ClickStatistics;
import com.publiccms.views.pojo.query.CmsContentQuery;

/**
 *
 * myContentList 我的内容接口
 * <p>
 * 参数列表
 * <ul>
 * <li><code>categoryId</code>:分类id,当parentId为空时有效
 * <li><code>containChild</code>:是否包含子分类,【true,false】
 * <li><code>categoryIds</code>:多个分类id,当categoryId为空时有效
 * <li><code>modelId</code>:多个模型id
 * <li><code>parentId</code>:父内容id
 * <li><code>onlyUrl</code>:外链,【true,false】
 * <li><code>hasImages</code>:拥有图片列表,【true,false】
 * <li><code>hasFiles</code>:拥有附件列表,【true,false】
 * <li><code>hasProducts</code>:拥有产品列表,【true,false】
 * <li><code>startPublishDate</code>:发布日期开始时间,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>endPublishDate</code>:发布日期结束时间,高级选项禁用时不能超过现在,【2020-01-01
 * 23:59:59】,【2020-01-01】
 * <li><code>status</code>:内容状态,【0:操作,1:已发布,2:待审核,3:驳回】
 * <li><code>emptyParent</code>:高级选项:父内容id是否为空,【true,false】,当parentId为空时有效
 * <li><code>orderField</code>
 * 排序字段,【score:评分,comments:评论数,clicks:点击数,publishDate:发布日期,updateDate:更新日期,checkDate:审核日期】,默认置顶级别倒序、发布日期按orderType排序
 * <li><code>orderType</code>:排序类型,【asc:正序,desc:倒序】,默认为倒序
 * <li><code>pageIndex</code>:页码
 * <li><code>pageSize</code>:每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code>:{@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsContent}
 * </ul>
 * 使用示例
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath!}api/myContentList?pageSize=10&amp;authToken=用户登录授权&amp;authUserId=1', function(data){
    console.log(data.totalCount);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class MyContentListDirective extends AbstractAppDirective {

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        SysSite site = getSite(handler);
        PageHandler page = service.getPage(
                new CmsContentQuery(site.getId(), handler.getIntegerArray("status"), handler.getInteger("categoryId"),
                        handler.getIntegerArray("categoryIds"), false, handler.getStringArray("modelIds"),
                        handler.getLong("parentId"), handler.getBoolean("emptyParent"), handler.getBoolean("onlyUrl"),
                        handler.getBoolean("hasImages"), handler.getBoolean("hasCover"), handler.getBoolean("hasFiles"), null,
                        user.getId(), handler.getDate("startPublishDate"), handler.getDate("endPublishDate"), null),
                handler.getBoolean("containChild"), null, null, handler.getInteger("offset"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", handler.getInteger("count", 30)), null);
        @SuppressWarnings("unchecked")
        List<CmsContent> list = (List<CmsContent>) page.getList();
        list.forEach(e -> {
            ClickStatistics statistics = statisticsComponent.getContentStatistics(e.getId());
            if (null != statistics) {
                e.setClicks(e.getClicks() + statistics.getClicks());
            }
            TemplateComponent.initContentUrl(site, e);
            TemplateComponent.initContentCover(site, e);
        });
        handler.put("page", page).render();
    }

    @Resource
    private CmsContentService service;
    @Resource
    private StatisticsComponent statisticsComponent;

    @Override
    public boolean needUserToken() {
        return true;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }

}