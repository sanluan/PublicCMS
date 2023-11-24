package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserAttribute;
import com.publiccms.logic.component.site.FileUploadComponent;
import com.publiccms.logic.service.sys.SysUserAttributeService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.views.pojo.query.SysUserSearchQuery;

import freemarker.template.TemplateException;
import jakarta.annotation.Resource;

/**
 *
 * userSearch 用户搜索指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>word</code>:搜索词,多个搜索词时取并集结果
 * <li><code>exclude</code>:排除词汇
 * <li><code>orgIds</code>:多个机构id,多个机构时取并集结果
 * <li><code>deptId</code>:部门id
 * <li><code>extendsValues</code>
 * 多个全文搜索字段值,格式：[字段编码]:字段值],例如:extendsValues='isbn:value1,unicode:value2'
 * <li><code>dictionaryValues</code>
 * 多个字典搜索字段值,只有数据字典父级值时包含所有子级结果,格式：[字段编码]_[字段值],例如:dictionaryValues='extend1_value1,extend1_value2'
 * <li><code>dictionaryUnion</code>
 * 取数据字典并集结果,dictionaryUnion不为空时有效,【true,false】,默认为交集结果
 * <li><code>projection</code>:投影结果,【true,false】,默认为false
 * <li><code>phrase</code>:精确搜索,【true,false】,默认为false
 * <li><code>fields</code>:搜索字段,【name:标题, text:文本扩展字段】
 * <li><code>containsAttribute</code>默认为<code>false</code>,http请求时为高级选项,为true时<code>content.attribute</code>为内容扩展数据<code>map</code>(字段编码,<code>value</code>)
 * <li><code>orderField</code>
 * 排序字段,【lastLoginDate:上次登录日期倒序,loginCount:登录次数倒序,registeredDate:注册日期倒序,followers:粉丝数倒叙】,默认相关度倒序
 * <li><code>pageIndex</code>:页码
 * <li><code>pageSize</code>:每页条数
 * <li><code>maxResults</code>:最大结果数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code>:{@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.SysUser}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.search word='cms' pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.search&gt;
 *
 * <pre>
*  &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/sys/userSearch?word=cms&amp;pageSize=10', function(data){
    console.log(data.page.totalCount);
  });
  &lt;/script&gt;
 * </pre>
 *
 */
@Component
public class SysUserSearchDirective extends AbstractTemplateDirective {
    @Resource
    private SysUserAttributeService attributeService;
    @Resource
    protected FileUploadComponent fileUploadComponent;

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        PageHandler page = null;
        Integer pageIndex = handler.getInteger("pageIndex", 1);
        Integer pageSize = handler.getInteger("pageSize", 30);
        try {
            SysSite site = getSite(handler);

            SysUserSearchQuery query = new SysUserSearchQuery(site.getId(), handler.getBoolean("projection", false),
                    handler.getBoolean("phrase", false), handler.getString("word"), handler.getString("exclude"),
                    handler.getStringArray("fields"), handler.getInteger("deptId"), handler.getStringArray("extendsValues"),
                    handler.getStringArray("dictionaryValues"), handler.getBoolean("dictionaryUnion"));

            page = service.query(query, handler.getString("orderField"), handler.getInteger("pageIndex", 1),
                    handler.getInteger("pageSize", 30), handler.getInteger("maxResults"));

            @SuppressWarnings("unchecked")
            List<SysUser> list = (List<SysUser>) page.getList();
            if (null != list) {
                Consumer<SysUser> consumer = null;
                boolean containsAttribute = handler.getBoolean("containsAttribute", false);
                containsAttribute = handler.inHttp() ? getAdvanced(handler) && containsAttribute : containsAttribute;

                if (containsAttribute) {
                    Long[] ids = list.stream().map(SysUser::getId).toArray(Long[]::new);
                    List<SysUserAttribute> attributeList = attributeService.getEntitys(ids);
                    Map<Object, SysUserAttribute> attributeMap = CommonUtils.listToMap(attributeList, k -> k.getUserId());
                    consumer = e -> {
                        e.setCover(CmsUrlUtils.getUrl(fileUploadComponent.getPrefix(site), e.getCover()));
                        e.setAttribute(ExtendUtils.getUserAttributeMap(attributeMap.get(e.getId())));
                    };
                } else {
                    consumer = e -> {
                        e.setCover(CmsUrlUtils.getUrl(fileUploadComponent.getPrefix(site), e.getCover()));
                    };
                }
                list.forEach(consumer);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            page = new PageHandler(pageIndex, pageSize);
        }
        handler.put("page", page).render();
    }

    @Resource
    private SysUserService service;
}