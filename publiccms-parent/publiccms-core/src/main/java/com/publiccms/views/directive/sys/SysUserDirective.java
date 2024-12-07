package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserAttribute;
import com.publiccms.logic.component.config.ContentConfigComponent;
import com.publiccms.logic.component.config.ContentConfigComponent.KeywordsConfig;
import com.publiccms.logic.component.site.FileUploadComponent;
import com.publiccms.logic.service.sys.SysUserAttributeService;
import com.publiccms.logic.service.sys.SysUserService;

import freemarker.template.TemplateException;

/**
 *
 * sysUser 用户查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>replaceSensitive</code>:替换敏感词, 默认为<code>true</code>
 * <li><code>id</code>:用户id,结果返回<code>object</code>
 * {@link com.publiccms.entities.sys.SysUser}
 * <li><code>ids</code>:
 * 多个用户id,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
 * <li><code>absoluteURL</code>:cover处理为绝对路径 默认为<code> true</code>
 * </ul>
 * 使用示例
 * <p>
 * &lt;@sys.user id=1&gt;${object.name}&lt;/@sys.user&gt;
 * <p>
 * &lt;@sys.user ids='1,2,3'&gt;&lt;#list map as
 * k,v&gt;${k}:${v.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.user&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('//sys.publicsys.com/api/directive/sys/user?id=1&amp;appToken=接口访问授权Token', function(data){    
  console.log(data.name);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class SysUserDirective extends AbstractTemplateDirective {
    @Resource
    protected ContentConfigComponent contentConfigComponent;

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Long id = handler.getLong("id");
        boolean absoluteURL = handler.getBoolean("absoluteURL", true);
        boolean replaceSensitive = handler.getBoolean("replaceSensitive", true);
        boolean containsAttribute = handler.getBoolean("containsAttribute", true);
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            SysUser entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                if (absoluteURL) {
                    entity.setCover(CmsUrlUtils.getUrl(fileUploadComponent.getPrefix(site), entity.getCover()));
                }
                if (replaceSensitive) {
                    KeywordsConfig config = contentConfigComponent.getKeywordsConfig(site.getId());
                    entity.setNickname(ExtendUtils.replaceSensitive(entity.getNickname(), config));
                }
                entity.setPassword(null);
                if (containsAttribute) {
                    entity.setAttribute(ExtendUtils.getAttributeMap(attributeService.getEntity(id)));
                }
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<SysUser> entityList = service.getEntitys(ids);
                KeywordsConfig config = contentConfigComponent.getKeywordsConfig(site.getId());
                Map<Long, SysUserAttribute> attributeMap = containsAttribute
                        ? CommonUtils.listToMap(attributeService.getEntitys(ids), k -> k.getUserId())
                        : null;
                UnaryOperator<SysUser> valueMapper = e -> {
                    if (absoluteURL) {
                        e.setCover(CmsUrlUtils.getUrl(fileUploadComponent.getPrefix(site), e.getCover()));
                    }
                    if (replaceSensitive) {
                        e.setNickname(ExtendUtils.replaceSensitive(e.getNickname(), config));
                    }
                    if (containsAttribute) {
                        e.setAttribute(ExtendUtils.getAttributeMap(attributeMap.get(e.getId())));
                    }
                    return e;
                };

                Map<String, SysUser> map = CommonUtils.listToMapSorted(entityList, k -> k.getId().toString(), valueMapper, ids,
                        entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private SysUserService service;
    @Resource
    private SysUserAttributeService attributeService;
    @Resource
    protected FileUploadComponent fileUploadComponent;

}
