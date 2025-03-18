package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.Date;
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
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ContentConfigComponent;
import com.publiccms.logic.component.config.ContentConfigComponent.KeywordsConfig;
import com.publiccms.logic.component.site.FileUploadComponent;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.views.pojo.entities.ClickStatistics;
import com.publiccms.views.pojo.query.CmsContentQuery;

import freemarker.template.TemplateException;

/**
 *
 * <h3 lang="zh">
 * contentList 内容列表查询指令
 * <h3 lang="en">
 * contentList content list directive
 * <h3 lang="ja">
 * contentList コンテンツリストクエリーディレクティブ
 * <p lang="zh">参数列表
 * <p lang="en">parameter list
 * <p lang="ja">パラメータリスト
 * <ul>
 * <li lang="zh"><code>categoryId</code>:分类id,当parentId为空时有效
 * <li lang="en"><code>categoryId</code>:category id, effective when parentId is empty
 * <li lang="ja"><code>categoryId</code>:親IDが空のときに有効なカテゴリID
 * <li lang="zh"><code>containChild</code>:是否包含子分类,【true,false】
 * <li lang="en"><code>containChild</code>:whether to include subcategories, [true,false]
 * <li lang="ja"><code>containChild</code>:サブカテゴリを含めるかどうか, [true,false]
 * <li lang="zh"><code>categoryIds</code>:多个分类id,当categoryId为空时有效
 * <li lang="en"><code>categoryIds</code>:multiple category ids, effective when categoryId is empty
 * <li lang="ja"><code>categoryIds</code>:categoryIdが空のときに有効な複数のカテゴリID
 * <li lang="zh"><code>modelId</code>:多个模型id
 * <li lang="en"><code>modelId</code>:multiple model ids
 * <li lang="ja"><code>modelId</code>:複数のモデルID
 * <li lang="zh"><code>parentId</code>:父内容id
 * <li lang="en"><code>parentId</code>:parent content id
 * <li lang="ja"><code>parentId</code>:親コンテンツID
 * <li lang="zh"><code>onlyUrl</code>:外链,【true,false】
 * <li lang="en"><code>onlyUrl</code>:external link, [true,false]
 * <li lang="ja"><code>onlyUrl</code>:外部リンク, [true,false]
 * <li lang="zh"><code>hasImages</code>:拥有图片列表,【true,false】
 * <li lang="en"><code>hasImages</code>:has image list, [true,false]
 * <li lang="ja"><code>hasImages</code>:画像リストを持つ, [true,false]
 * <li lang="zh"><code>hasFiles</code>:拥有附件列表,【true,false】
 * <li lang="en"><code>hasFiles</code>:has file list, [true,false]
 * <li lang="ja"><code>hasFiles</code>:添付ファイルリストを持つ, [true,false]
 * <li lang="zh"><code>hasProducts</code>:拥有产品列表,【true,false】
 * <li lang="en"><code>hasProducts</code>:has product list, [true,false]
 * <li lang="ja"><code>hasProducts</code>:製品リストを持つ, [true,false]
 * <li lang="zh"><code>hasCover</code>:拥有封面图,【true,false】
 * <li lang="en"><code>hasCover</code>:has cover image, [true,false]
 * <li lang="ja"><code>hasCover</code>:カバー画像を持つ, [true,false]
 * <li lang="zh"><code>userId</code>:发布用户id
 * <li lang="en"><code>userId</code>:publish user id
 * <li lang="ja"><code>userId</code>:公開ユーザーID
 * <li lang="zh"><code>startPublishDate</code>:起始发布日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li lang="en"><code>startPublishDate</code>:start publish date, [2020-01-01 23:59:59],[2020-01-01]
 * <li lang="ja"><code>startPublishDate</code>:公開開始日, [2020-01-01 23:59:59],[2020-01-01]
 * <li lang="zh"><code>endPublishDate</code>:终止发布日期,高级选项禁用时不能超过现在,【2020-01-01 23:59:59】,【2020-01-01】
 * <li lang="en"><code>endPublishDate</code>:end publish date, cannot exceed now when advanced option is disabled, [2020-01-01 23:59:59],[2020-01-01]
 * <li lang="ja"><code>endPublishDate</code>:高度なオプションが無効のときに現在を超えることはできない公開終了日, [2020-01-01 23:59:59],[2020-01-01]
 * <li lang="zh"><code>advanced</code>:开启高级选项, 默认为<code>false</code>
 * <li lang="en"><code>advanced</code>:enable advanced options, default is <code>false</code>
 * <li lang="ja"><code>advanced</code>:高度なオプションを有効にする, 既定値は <code>false</code>
 * <li lang="zh"><code>status</code>:高级选项:内容状态,【0:操作,1:已发布,2:待审核,3:驳回】
 * <li lang="en"><code>status</code>:advanced option: content status, [0:operation,1:published,2:pending review,3:rejected]
 * <li lang="ja"><code>status</code>:高度なオプション: コンテンツステータス, [0:操作,1:公開,2:レビュー待ち,3:拒否]
 * <li lang="zh"><code>disabled</code>:高级选项:禁用状态,默认为<code>false</code>
 * <li lang="en"><code>disabled</code>:advanced option: disabled status, default is <code>false</code>
 * <li lang="ja"><code>disabled</code>:高度なオプション: 無効化ステータス, 既定値は <code>false</code>
 * <li lang="zh"><code>emptyParent</code>:高级选项:父内容id是否为空,【true,false】,当parentId为空时有效
 * <li lang="en"><code>emptyParent</code>:advanced option: whether parent content id is empty, [true,false], effective when parentId is empty
 * <li lang="ja"><code>emptyParent</code>:高度なオプション: 親コンテンツIDが空かどうか, [true,false], parentIdが空のときに有効
 * <li lang="zh"><code>title</code>:高级选项:标题
 * <li lang="en"><code>title</code>:advanced option: title
 * <li lang="ja"><code>title</code>:高度なオプション: タイトル
 * <li lang="zh"><code>absoluteURL</code>:url处理为绝对路径 默认为<code>true</code>
 * <li lang="en"><code>absoluteURL</code>:process url as absolute path, default is <code>true</code>
 * <li lang="ja"><code>absoluteURL</code>:URLを絶対パスに処理する, 既定値は <code>true</code>
 * <li lang="zh"><code>absoluteId</code>:id处理为引用内容的ID 默认为<code>true</code>
 * <li lang="en"><code>absoluteId</code>:process id as referenced content id, default is <code>true</code>
 * <li lang="ja"><code>absoluteId</code>:IDを参照コンテンツのIDに処理する, 既定値は <code>true</code>
 * <li lang="zh"><code>containsAttribute</code>默认为<code>false</code>,http请求时为高级选项,为true时<code>content.attribute</code>为内容扩展数据<code>map</code>(字段编码,<code>value</code>)
 * <li lang="en"><code>containsAttribute</code>default is <code>false</code>, advanced option for http requests, when true, <code>content.attribute</code> is content extended data <code>map</code>(field code, <code>value</code>)
 * <li lang="ja"><code>containsAttribute</code>既定値は <code>false</code>, HTTPリクエスト時の高度なオプション, trueの場合 <code>content.attribute</code> はコンテンツ拡張データ <code>map</code>(フィールドコード, <code>value</code>)
 * <li lang="zh"><code>orderField</code>
 * 排序字段,【score:评分,comments:评论数,clicks:点击数,collections收藏数,publishDate:发布日期,updateDate:更新日期,checkDate:审核日期】,默认置顶级别倒序、发布日期按orderType排序
 * <li lang="en"><code>orderField</code>
 * sort field, [score:rating,comments:number of comments,clicks:number of clicks,collections:number of collections,publishDate:publish date,updateDate:update date,checkDate:review date], default is descending order of top level, publish date sorted by orderType
 * <li lang="ja"><code>orderField</code>
 * ソートフィールド, [score:評価,comments:コメント数,clicks:クリック数,collections:コレクション数,publishDate:公開日,updateDate:更新日,checkDate:レビュー日], 既定値はトップレベルの降順、orderTypeでソートされた公開日
 * <li lang="zh"><code>orderType</code>:排序类型,【asc:正序,desc:倒序】,默认为倒序
 * <li lang="en"><code>orderType</code>:sort type, [asc:ascending,desc:descending], default is descending
 * <li lang="ja"><code>orderType</code>:ソートタイプ, [asc:昇順,desc:降順], 既定値は降順
 * <li lang="zh"><code>firstResult</code>:开始位置,从1开始
 * <li lang="en"><code>firstResult</code>:start position, starts from 1
 * <li lang="ja"><code>firstResult</code>:開始位置, 1から始める
 * <li lang="zh"><code>pageIndex</code>:页码,firstResult不存在时有效
 * <li lang="en"><code>pageIndex</code>:page number, effective when firstResult does not exist
 * <li lang="ja"><code>pageIndex</code>:ページ番号, firstResultが存在しないときに有効
 * <li lang="zh"><code>pageSize</code>:每页条数
 * <li lang="en"><code>pageSize</code>:number of items per page
 * <li lang="ja"><code>pageSize</code>:ページあたりのアイテム数
 * <li lang="zh"><code>maxResults</code>:最大结果数
 * <li lang="en"><code>maxResults</code>:maximum number of results
 * <li lang="ja"><code>maxResults</code>:最大結果数
 * </ul>
 * <p>
 * <p lang="zh">返回结果
 * <p lang="en">return result
 * <p lang="ja">戻り値
 * <ul>
 * <li><code>page</code>:{@link com.publiccms.common.handler.PageHandler}
 * <li lang="zh"><code>page.list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsContent}
 * <li lang="en"><code>page.list</code>:List type query result entity list
 * {@link com.publiccms.entities.cms.CmsContent}
 * <li lang="ja"><code>page.list</code>:リスト型 クエリ結果エンティティリスト
 * {@link com.publiccms.entities.cms.CmsContent}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.contentList pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.contentList&gt;
 * 
 * <pre>
 *  &lt;script&gt;
    $.getJSON('${site.dynamicPath}api/directive/cms/contentList?pageSize=10', function(data){    
      console.log(data.page.totalCount);
    });
    &lt;/script&gt;
 * </pre>
 * 
 */
@Component
public class CmsContentListDirective extends AbstractTemplateDirective {
    @Resource
    protected ContentConfigComponent contentConfigComponent;
    @Resource
    private CmsContentAttributeService attributeService;
    @Resource
    protected FileUploadComponent fileUploadComponent;
    @Resource
    private StatisticsComponent statisticsComponent;

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        CmsContentQuery queryEntity = new CmsContentQuery();
        SysSite site = getSite(handler);
        queryEntity.setSiteId(site.getId());
        queryEntity.setEndPublishDate(handler.getDate("endPublishDate"));
        boolean advanced = getAdvanced(handler);
        boolean containsAttribute = handler.getBoolean("containsAttribute", false) && (!handler.inHttp() || advanced);
        if (advanced) {
            queryEntity.setStatus(handler.getIntegerArray("status"));
            queryEntity.setDisabled(handler.getBoolean("disabled", false));
            queryEntity.setEmptyParent(handler.getBoolean("emptyParent"));
            queryEntity.setTitle(handler.getString("title"));
        } else {
            queryEntity.setStatus(CmsContentService.STATUS_NORMAL_ARRAY);
            queryEntity.setDisabled(false);
            queryEntity.setEmptyParent(true);
            Date now = CommonUtils.getMinuteDate();
            if (null == queryEntity.getEndPublishDate() || queryEntity.getEndPublishDate().after(now)) {
                queryEntity.setEndPublishDate(now);
            }
            queryEntity.setExpiryDate(now);
        }
        queryEntity.setCategoryId(handler.getInteger("categoryId"));
        queryEntity.setCategoryIds(handler.getIntegerArray("categoryIds"));
        queryEntity.setModelIds(handler.getStringArray("modelId"));
        queryEntity.setParentId(handler.getLong("parentId"));
        queryEntity.setOnlyUrl(handler.getBoolean("onlyUrl"));
        queryEntity.setHasImages(handler.getBoolean("hasImages"));
        queryEntity.setHasFiles(handler.getBoolean("hasFiles"));
        queryEntity.setHasProducts(handler.getBoolean("hasProducts"));
        queryEntity.setHasCover(handler.getBoolean("hasCover"));
        queryEntity.setUserId(handler.getLong("userId"));
        queryEntity.setDeptId(handler.getInteger("deptId"));
        queryEntity.setStartPublishDate(handler.getDate("startPublishDate"));
        PageHandler page = service.getPage(queryEntity, handler.getBoolean("containChild"), handler.getString("orderField"),
                handler.getString("orderType"), handler.getInteger("firstResult"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", handler.getInteger("count", 30)), handler.getInteger("maxResults"));
        @SuppressWarnings("unchecked")
        List<CmsContent> list = (List<CmsContent>) page.getList();
        if (null != list) {
            boolean absoluteURL = handler.getBoolean("absoluteURL", true);
            boolean absoluteId = handler.getBoolean("absoluteId", true);
            Long[] ids = list.stream().map(CmsContent::getId).toArray(Long[]::new);
            KeywordsConfig config = containsAttribute ? contentConfigComponent.getKeywordsConfig(site.getId()) : null;
            Map<Object, CmsContentAttribute> attributeMap = containsAttribute
                    ? CommonUtils.listToMap(attributeService.getEntitys(ids), k -> k.getContentId())
                    : null;
            Consumer<CmsContent> consumer = e -> {
                ClickStatistics statistics = statisticsComponent.getContentStatistics(e.getId());
                if (null != statistics) {
                    e.setClicks(e.getClicks() + statistics.getClicks());
                }
                if (absoluteId && null == e.getParentId() && null != e.getQuoteContentId()) {
                    e.setId(e.getQuoteContentId());
                }
                if (absoluteURL) {
                    CmsUrlUtils.initContentUrl(site, e);
                    fileUploadComponent.initContentCover(site, e);
                }
                if (containsAttribute) {
                    e.setAttribute(ExtendUtils.getAttributeMap(attributeMap.get(e.getId()), config));
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
    private CmsContentService service;
}