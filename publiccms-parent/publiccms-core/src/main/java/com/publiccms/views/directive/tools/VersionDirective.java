package com.publiccms.views.directive.tools;

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CmsVersion;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 * version 版本指令
 * <p>
 * 返回结果
 * <ul>
 * <li><code>cms</code>cms版本
 * <li><code>authorizationEdition</code>使用授权
 * <li><code>authorizationStartDate</code>授权起始日期
 * <li><code>authorizationEndDate</code>授权结束日期
 * <li><code>authorizationOrganization</code>授权单位
 * <li><code>cluster</code>节点id
 * <li><code>master</code>是否管理节点【true:管理节点,false:普通节点】
 * </ul>
 * 使用示例
 * <p>
 * &lt;@tools.version path='/'&gt;${a.fileName}&lt;/@tools.version&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('//cms.publiccms.com/api/directive/tools/version', function(data){    
   console.log(data.cms);
 });
 &lt;/script&gt;
 * </pre>
 */
@Component
public class VersionDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        handler.put("cms", CmsVersion.getVersion());
        boolean authorizationEdition = CmsVersion.isAuthorizationEdition();
        handler.put("authorizationEdition", authorizationEdition);
        if (authorizationEdition) {
            handler.put("authorizationStartDate", CmsVersion.getLicense().getStartDate());
            handler.put("authorizationEndDate", CmsVersion.getLicense().getEndDate());
            handler.put("authorizationOrganization", CmsVersion.getLicense().getOrganization());
        }
        handler.put("cluster", CmsVersion.getClusterId());
        handler.put("master", CmsVersion.isMaster());
        handler.render();
    }

}
