package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.List;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.FileUploadComponent;
import com.publiccms.logic.service.sys.SysUserService;

import freemarker.template.TemplateException;

/**
 *
 * sysUserList 用户列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>advanced</code>:开启高级选项, 默认为<code>false</code>
 * <li><code>disabled</code>:高级选项:已禁用,【true,false】, 默认为<code>false</code>
 * <li><code>deptId</code>:部门id
 * <li><code>startRegisteredDate</code>:起始注册日期,【2020-01-01
 * 23:59:59】,【2020-01-01】
 * <li><code>endRegisteredDate</code>:终止注册日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>startLastLoginDate</code>:起始上次登录日期,【2020-01-01
 * 23:59:59】,【2020-01-01】
 * <li><code>endLastLoginDate</code>:终止上次登录日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>superuser</code>:管理员,【true,false】
 * <li><code>emailChecked</code>:邮箱已验证,【true,false】
 * <li><code>name</code>:昵称、用户名、邮箱
 * <li><code>orderField</code>
 * 排序字段,【expiryDate:,createDate:,】,默认创建日期按orderType排序
 * <li><code>orderType</code>:排序类型,【asc:正序,desc:倒序】,默认为倒序
 * <li><code>pageIndex</code>:页码
 * <li><code>pageSize</code>:每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code>:{@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.sys.SysUser}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@sys.userList deptId=1 pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.nickname}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.userList&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/sys/userList?deptId=1&amp;pageSize=10&amp;appToken=接口访问授权Token', function(data){    
   console.log(data.page.totalCount);
 });
 &lt;/script&gt;
 * </pre>
 */
@Component
public class SysUserListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Boolean disabled = false;
        if (getAdvanced(handler)) {
            disabled = handler.getBoolean("disabled", false);
        }
        SysSite site = getSite(handler);
        PageHandler page = service.getPage(site.getId(), handler.getInteger("deptId"), handler.getDate("startRegisteredDate"),
                handler.getDate("endRegisteredDate"), handler.getDate("startLastLoginDate"), handler.getDate("endLastLoginDate"),
                handler.getBoolean("superuser"), handler.getBoolean("emailChecked"), disabled, handler.getString("name"),
                handler.getString("orderField"), handler.getString("orderType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", handler.getInteger("count", 30)));
        @SuppressWarnings("unchecked")
        List<SysUser> list = (List<SysUser>) page.getList();
        if (null != list) {
            boolean absoluteURL = handler.getBoolean("absoluteURL", true);
            if (absoluteURL) {
                list.forEach(e -> e.setCover(CmsUrlUtils.getUrl(fileUploadComponent.getPrefix(site), e.getCover())));
            }

        }
        handler.put("page", page).render();

    }

    @Override
    public boolean supportAdvanced() {
        return true;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private SysUserService service;
    @Resource
    protected FileUploadComponent fileUploadComponent;

}
