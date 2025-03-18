package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.base.HighLighterQuery;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.RequestUtils;
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
import com.publiccms.views.pojo.query.CmsContentSearchQuery;

import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

/**
 *
 * <h3 lang="zh">
 * search 内容列表查询指令
 * <h3 lang="en">
 * search content list query directive
 * <h3 lang="ja">
 * search コンテンツリストクエリーディレクティブ
 * <p lang="zh">
 * 参数列表
 * <p lang="en">
 * parameter list
 * <p lang="ja">
 * パラメータリスト
 * <ul>
 * <li lang="zh"><code>word</code>:搜索词,多个搜索词时取并集结果
 * <li lang="en"><code>word</code>:search word, take union result when multiple search words
 * <li lang="ja"><code>word</code>:検索語、複数の検索語の場合は合併結果を取得
 * <li lang="zh"><code>exclude</code>:排除词汇
 * <li lang="en"><code>exclude</code>:exclude words
 * <li lang="ja"><code>exclude</code>:除外語彙
 * <li lang="zh"><code>tagIds</code>:多个标签id,多个标签时取并集结果
 * <li lang="en"><code>tagIds</code>:multiple tag ids, take union result when multiple tags
 * <li lang="ja"><code>tagIds</code>:複数のタグID、複数のタグの場合は合併結果を取得
 * <li lang="zh"><code>userId</code>:用户id
 * <li lang="en"><code>userId</code>:user id
 * <li lang="ja"><code>userId</code>:ユーザーID
 * <li lang="zh"><code>parentId</code>:父内容id
 * <li lang="en"><code>parentId</code>:parent content id
 * <li lang="ja"><code>parentId</code>:親コンテンツID
 * <li lang="zh"><code>categoryId</code>:分类id
 * <li lang="en"><code>categoryId</code>:category id
 * <li lang="ja"><code>categoryId</code>:カテゴリID
 * <li lang="zh"><code>containChild</code>:包含子分类,当categoryId不为空时有效
 * <li lang="en"><code>containChild</code>:include subcategories, effective when categoryId is not empty
 * <li lang="ja"><code>containChild</code>:categoryIdが空でないときにサブカテゴリを含める
 * <li lang="zh"><code>categoryIds</code>:多个分类id,当categoryId为空时有效
 * <li lang="en"><code>categoryIds</code>:multiple category ids, effective when categoryId is empty
 * <li lang="ja"><code>categoryIds</code>:categoryIdが空のときに複数のカテゴリID
 * <li lang="zh"><code>modelIds</code>:多个模型id
 * <li lang="en"><code>modelIds</code>:multiple model ids
 * <li lang="ja"><code>modelIds</code>:複数のモデルID
 * <li lang="zh"><code>extendsValues</code>
 * 多个全文搜索字段値,格式：[字段编码]:字段値],例如:extendsValues='isbn:value1,unicode:value2'
 * <li lang="en"><code>extendsValues</code>
 * multiple full-text search field values, format: [field code]:field value], e.g.: extendsValues='isbn:value1,unicode:value2'
 * <li lang="ja"><code>extendsValues</code>
 * 複数の全文検索フィールド値、形式：[フィールドコード]:フィールド値],例:extendsValues='isbn:value1,unicode:value2'
 * <li lang="zh"><code>dictionaryValues</code>
 * 多个字典搜索字段値,只有数据字典父级値時包含所有子级结果,格式：[字段编码]_[字段値],例如:dictionaryValues='extend1_value1,extend1_value2'
 * <li lang="en"><code>dictionaryValues</code>
 * multiple dictionary search field values, include all child results when only data dictionary parent value, format: [field code]_[field value], e.g.: dictionaryValues='extend1_value1,extend1_value2'
 * <li lang="ja"><code>dictionaryValues</code>
 * 複数の辞書検索フィールド値、データ辞書親値のみの場合はすべての子結果を含む、形式：[フィールドコード]_[フィールド値],例:dictionaryValues='extend1_value1,extend1_value2'
 * <li lang="zh"><code>dictionaryUnion</code>
 * 取数据字典并集结果,dictionaryUnion不为空时有效,【true,false】,默认为交集结果
 * <li lang="en"><code>dictionaryUnion</code>
 * take union result of dictionary values, effective when dictionaryUnion is not empty, [true,false], default is intersection result
 * <li lang="ja"><code>dictionaryUnion</code>
 * 辞書値の合併結果を取得、dictionaryUnionが空でないときに有効、[true,false]、既定値は交差結果
 * <li lang="zh"><code>highlight</code>:高亮关键词,【true,false】,默认为false,启用高亮后,
 * 标题、作者、编辑、描述字段应该加?no_esc以使高亮html生效,cms会自动对原值有进行html安全转义
 * <li lang="en"><code>highlight</code>:highlight keywords, [true,false], default is false, after enabling highlight,
 * title, author, editor, description fields should add ?no_esc to make highlight html effective, cms will automatically perform html safety escaping on the original value
 * <li lang="ja"><code>highlight</code>:キーワードのハイライト、[true,false]、既定値はfalse、ハイライトを有効にした後、
 * タイトル、著者、編集者、説明フィールドにはハイライトhtmlを有効にするために?no_escを追加する必要があります。cmsは元の値に対して自動的にhtmlセーフティエスケープを実行します
 * <li lang="zh"><code>preTag</code>:高亮前缀,启用高亮时有效,默认为"&lt;B&gt;"
 * <li lang="en"><code>preTag</code>:highlight prefix, effective when highlight is enabled, default is "&lt;B&gt;"
 * <li lang="ja"><code>preTag</code>:ハイライトのプレフィックス、ハイライトが有効な場合に有効、既定値は"&lt;B&gt;"
 * <li lang="zh"><code>postTag</code>:高亮后缀,启用高亮时有效,默认为"&lt;/B&gt;"
 * <li lang="en"><code>postTag</code>:highlight suffix, effective when highlight is enabled, default is "&lt;/B&gt;"
 * <li lang="ja"><code>postTag</code>:ハイライトのサフィックス、ハイライトが有効な場合に有効、既定値は"&lt;/B&gt;"
 * <li lang="zh"><code>projection</code>:投影结果,【true,false】,默认为false
 * <li lang="en"><code>projection</code>:projection result, [true,false], default is false
 * <li lang="ja"><code>projection</code>:投影結果、[true,false]、既定値はfalse
 * <li lang="zh"><code>phrase</code>:精确搜索,【true,false】,默认为false
 * <li lang="en"><code>phrase</code>:exact search, [true,false], default is false
 * <li lang="ja"><code>phrase</code>:正確な検索、[true,false]、既定値はfalse
 * <li lang="zh"><code>fields</code>:搜索字段,【title:标题, author:作者, editor:编辑, description:描述,
 * text:正文,files:附件】
 * <li lang="en"><code>fields</code>:search fields, [title:title, author:author, editor:editor, description:description,
 * text:text,files:attachments]
 * <li lang="ja"><code>fields</code>:検索フィールド、[title:タイトル, author:著者, editor:編集者, description:説明,
 * text:本文,files:添付ファイル]
 * <li lang="zh"><code>containsAttribute</code>默认为<code>false</code>,http请求时为高级选项,为true时<code>content.attribute</code>为内容扩展数据<code>map</code>(字段编码,<code>value</code>)
 * <li lang="en"><code>containsAttribute</code>default is <code>false</code>, advanced option for http requests, when true, <code>content.attribute</code> is content extended data <code>map</code>(field code, <code>value</code>)
 * <li lang="ja"><code>containsAttribute</code>既定値は <code>false</code>, HTTPリクエスト時の高度なオプション、trueの場合 <code>content.attribute</code> はコンテンツ拡張データ <code>map</code>(フィールドコード, <code>value</code>)
 * <li lang="zh"><code>startPublishDate</code>:起始发布日期,【2000-01-01 23:59:59】,【2000-01-01】
 * <li lang="en"><code>startPublishDate</code>:start publish date, [2000-01-01 23:59:59],[2000-01-01]
 * <li lang="ja"><code>startPublishDate</code>:公開開始日、[2000-01-01 23:59:59],[2000-01-01]
 * <li lang="zh"><code>endPublishDate</code>:终止发布日期,【2000-01-01 23:59:59】,【2000-01-01】
 * <li lang="en"><code>endPublishDate</code>:end publish date, [2000-01-01 23:59:59],[2000-01-01]
 * <li lang="ja"><code>endPublishDate</code>:公開終了日、[2000-01-01 23:59:59],[2000-01-01]
 * <li lang="zh"><code>orderField</code>
 * 排序字段,【clicks:点击数倒序,score:分数倒序,publishDate:发布日期倒序,collections:收藏数倒叙,minPrice:最低价格,maxPrice:最高价格,extend.sort1-extend.sort10]:扩展字段排序标志】,默认相关度倒序
 * <li lang="en"><code>orderField</code>
 * sort field, [clicks:clicks in descending order,score:score in descending order,publishDate:publish date in descending order,collections:collections in descending order,minPrice:min price,maxPrice:max price,extend.sort1-extend.sort10]:extended field sort flag], default is descending order of relevance
 * <li lang="ja"><code>orderField</code>
 * ソートフィールド、[clicks:クリック数の降順、score:スコアの降順、publishDate:公開日の降順、collections:コレクション数の降順、minPrice:最低価格、maxPrice:最高価格、extend.sort1-extend.sort10]:拡張フィールドのソートフラグ]、既定値は関連性の降順
 * <li lang="zh"><code>pageIndex</code>:页码
 * <li lang="en"><code>pageIndex</code>:page number
 * <li lang="ja"><code>pageIndex</code>:ページ番号
 * <li lang="zh"><code>pageSize</code>:每页条数
 * <li lang="en"><code>pageSize</code>:number of items per page
 * <li lang="ja"><code>pageSize</code>:ページあたりのアイテム数
 * <li lang="zh"><code>maxResults</code>:最大结果数
 * <li lang="en"><code>maxResults</code>:maximum number of results
 * <li lang="ja"><code>maxResults</code>:最大結果数
 * </ul>
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
 * &lt;@cms.search word='cms' pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.search&gt;
 *
 * <pre>
*  &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/cms/search?word=cms&amp;pageSize=10', function(data){
    console.log(data.page.totalCount);
  });
  &lt;/script&gt;
 * </pre>
 *
 */
@Component
public class CmsSearchDirective extends AbstractTemplateDirective {
    @Resource
    private CmsContentAttributeService attributeService;
    @Resource
    private StatisticsComponent statisticsComponent;
    @Resource
    protected ContentConfigComponent contentConfigComponent;
    @Resource
    protected FileUploadComponent fileUploadComponent;

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        handler.put("page", query(handler, handler.getBoolean("factSearch", false))).render();
    }

    public PageHandler query(RenderHandler handler, boolean factSearch) throws TemplateModelException {
        String word = handler.getString("word");
        Long[] tagIds = handler.getLongArray("tagIds");
        if (null == tagIds) {
            tagIds = handler.getLongArray("tagId");
        }
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(word)) {
            word = CommonUtils.keep(word, 100, null);
            String ip = RequestUtils.getIpAddress(handler.getRequest());
            statisticsComponent.search(site.getId(), word, ip);
        }
        if (CommonUtils.notEmpty(tagIds)) {
            for (Long tagId : tagIds) {
                statisticsComponent.searchTag(tagId);
            }
        }
        Integer pageIndex = handler.getInteger("pageIndex", 1);
        Integer pageSize = handler.getInteger("pageSize", handler.getInteger("count", 30));
        Date currentDate = CommonUtils.getMinuteDate();
        HighLighterQuery highLighterQuery = new HighLighterQuery(handler.getBoolean("highlight", false));
        if (highLighterQuery.isHighlight()) {
            highLighterQuery.setPreTag(handler.getString("preTag"));
            highLighterQuery.setPostTag(handler.getString("postTag"));
        }
        boolean containsAttribute = handler.getBoolean("containsAttribute", false);
        containsAttribute = handler.inHttp() ? getAdvanced(handler) && containsAttribute : containsAttribute;
        try {
            CmsContentSearchQuery query = new CmsContentSearchQuery(site.getId(), handler.getBoolean("projection", false),
                    handler.getBoolean("phrase", false), highLighterQuery, word, handler.getString("exclude"),
                    handler.getStringArray("fields"), tagIds, handler.getLong("userId"), handler.getLong("parentId"),
                    handler.getInteger("categoryId"), handler.getIntegerArray("categoryIds"), handler.getStringArray("modelIds"),
                    handler.getStringArray("extendsValues"), handler.getStringArray("dictionaryValues"),
                    handler.getBoolean("dictionaryUnion"), handler.getDate("startPublishDate"),
                    handler.getDate("endPublishDate", currentDate), currentDate);
            PageHandler page = null;
            if (factSearch) {
                page = service.facetQuery(query, handler.getBoolean("containChild"), handler.getString("orderField"),
                        handler.getString("orderType"), pageIndex, pageSize, handler.getInteger("maxResults"));
            } else {
                page = service.query(query, handler.getBoolean("containChild"), handler.getString("orderField"),
                        handler.getString("orderType"), pageIndex, pageSize, handler.getInteger("maxResults"));
            }

            @SuppressWarnings("unchecked")
            List<CmsContent> list = (List<CmsContent>) page.getList();
            if (null != list) {
                Consumer<CmsContent> consumer = null;
                if (containsAttribute) {
                    Long[] ids = list.stream().map(CmsContent::getId).toArray(Long[]::new);
                    List<CmsContentAttribute> attributeList = attributeService.getEntitys(ids);
                    KeywordsConfig config = contentConfigComponent.getKeywordsConfig(site.getId());
                    Map<Object, CmsContentAttribute> attributeMap = CommonUtils.listToMap(attributeList, k -> k.getContentId());
                    consumer = e -> {
                        ClickStatistics statistics = statisticsComponent.getContentStatistics(e.getId());
                        if (null != statistics) {
                            e.setClicks(e.getClicks() + statistics.getClicks());
                        }
                        CmsUrlUtils.initContentUrl(site, e);
                        fileUploadComponent.initContentCover(site, e);
                        e.setAttribute(ExtendUtils.getAttributeMap(attributeMap.get(e.getId()), config));
                    };
                } else {
                    consumer = e -> {
                        ClickStatistics statistics = statisticsComponent.getContentStatistics(e.getId());
                        if (null != statistics) {
                            e.setClicks(e.getClicks() + statistics.getClicks());
                        }
                        CmsUrlUtils.initContentUrl(site, e);
                        fileUploadComponent.initContentCover(site, e);
                    };
                }
                list.forEach(consumer);
            }
            return page;
        } catch (Exception e) {
            log.error(e.getMessage());
            PageHandler page = new PageHandler(pageIndex, pageSize);
            page.setList(Collections.emptyList());
            return page;
        }
    }

    @Override
    public boolean supportAdvanced() {
        return true;
    }

    @Resource
    private CmsContentService service;
}