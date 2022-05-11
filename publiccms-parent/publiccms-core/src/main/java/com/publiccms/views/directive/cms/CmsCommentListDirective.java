package com.publiccms.views.directive.cms;

// Generated 2018-11-7 16:25:07 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.component.site.DatasourceComponent;
import com.publiccms.logic.service.cms.CmsCommentService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.database.CmsDataSource;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * categoryList 分类列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>userId</code> 发布评论用户id
 * <li><code>replyId</code> 被回复评论id
 * <li><code>contentId</code> 内容id
 * <li><code>emptyReply</code> 回复id是否为空， replyId为空时有效，默认为<code>false</code>
 * <li><code>replyUserId</code> 被回复用户id
 * <li><code>advanced</code> 开启高级选项， 默认为<code> false</code>
 * <li><code>status</code> 高级选项:评论状态，【1:已发布,2:待审核】
 * <li><code>checkUserId</code> 高级选项:审核用户id
 * <li><code>disabled</code> 高级选项:评论已删除,【true,false】
 * <li><code>orderField</code>
 * 排序字段,【replies,scores,checkDate,updateDate,createDate】,默认置顶id按orderType排序
 * <li><code>orderType</code> 排序类型,【asc,desc】，默认为倒叙
 * <li><code>pageIndex</code> 页码
 * <li><code>pageSize</code> 每页条数
 * </ul>
 * <p>
 * 返回结果page子属性:
 * <ul>
 * <li><code>totalCount</code> int类型 数据总数
 * <li><code>pageIndex</code> int类型 当前页码
 * <li><code>list</code> List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsComment}
 * <li><code>totalPage</code> int类型 总页数
 * <li><code>firstResult</code> int类型 第一条序号
 * <li><code>firstPage</code> boolean类型 是否第一页
 * <li><code>lastPage</code> boolean类型 是否最后一页
 * <li><code>nextPage</code> int类型 下一页页码
 * <li><code>prePage</code> int类型 上一页页码
 * </ul>
 * 使用示例
 * <p>
 * &lt;@_commentList contentId=1 pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.text}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@_commentList&gt;
 * 
 * <pre>
  &lt;script&gt;
   $.getJSON('//cms.publiccms.com/api/directive/commentList?contentId=1&pageSize=10&appToken=接口访问授权Token', function(data){    
     console.log(data.totalCount);
   });
   &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsCommentListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer status;
        Long checkUserId = null;
        Boolean disabled;
        if (handler.getBoolean("advanced", false)) {
            status = handler.getInteger("status");
            checkUserId = handler.getLong("checkUserId");
            disabled = handler.getBoolean("disabled", false);
        } else {
            status = CmsCommentService.STATUS_NORMAL;
            disabled = false;
        }
        SysSite site = getSite(handler);
        try {
            CmsDataSource.setDataSourceName(datasourceComponent.getRandomDatasource(site.getId()));
            PageHandler page = service.getPage(site.getId(), handler.getLong("userId"), handler.getLong("replyId"),
                    handler.getBoolean("emptyReply", false), handler.getLong("replyUserId"), handler.getLong("contentId"),
                    checkUserId, status, disabled, handler.getString("orderField"), handler.getString("orderType"),
                    handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", handler.getInteger("count", 30)));
            handler.put("page", page).render();
        } finally {
            CmsDataSource.resetDataSourceName();
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private CmsCommentService service;
    @Autowired
    private DatasourceComponent datasourceComponent;

}