package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysUserSetting;
import com.publiccms.entities.sys.SysUserSettingId;
import com.publiccms.logic.service.sys.SysUserSettingService;

import freemarker.template.TemplateException;
import jakarta.annotation.Resource;

/**
 *
 * sysUserSetting 配置数据查询指令
 * <p lang="zh">
 * 参数列表
 * <p lang="en">
 * parameter list
 * <p lang="ja">
 * パラメータリスト
 * <ul>
 * <li><code>userId</code>:用户ID
 * <li><code>code</code>:配置编码,userId不为空时结果返回<code>string</code>
 * <li><code>data</code>:配置数据，配置编码、userId不为空时结果返回<code>string</code>
 * <li><code>codes</code>:多个配置编码,userId不为空时结果返回<code>map</code>(code,<code>string</code>)
 * <li><code>userIds</code>:多个用户ID,code不为空时,结果返回<code>map</code>(userId,<code>string</code>)
 * </ul>
 * <p lang="zh">
 * 使用示例
 * <p lang="en">
 * usage example
 * <p lang="ja">
 * 使用例
 * <p>
 * &lt;@sys.UserSetting userId=1
 * code='home_title'&gt;${object}&lt;/@sys.UserSetting&gt;
 *
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/sys/UserSetting?uscode=site&amp;appToken=接口访问授权Token', function(data){
 console.log(data.register_url);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class SysUserSettingDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Long userId = handler.getLong("userId");
        String code = handler.getString("code");
        String[] codes = handler.getStringArray("codes");
        if (null != userId) {
            if (CommonUtils.notEmpty(code)) {
                String data = handler.getString("data");
                SysUserSetting entity = service.getOrCreateOrUpdate(userId, code, data);
                if (null != entity) {
                    handler.put("object", entity.getData()).render();
                }
            } else if (CommonUtils.notEmpty(codes)) {
                SysUserSettingId[] ids = new SysUserSettingId[codes.length];
                int i = 0;
                for (String s : codes) {
                    if (CommonUtils.notEmpty(s)) {
                        ids[i++] = new SysUserSettingId(userId, s);
                    }
                }
                Map<String, String> map = new LinkedHashMap<>();
                for (SysUserSetting entity : service.getEntitys(ids)) {
                    map.put(entity.getId().getCode(), entity.getData());
                }
                handler.put("map", map).render();
            }
        } else {
            Long[] userIds = handler.getLongArray("userIds");
            if (null != userIds && CommonUtils.notEmpty(code)) {
                SysUserSettingId[] ids = new SysUserSettingId[userIds.length];
                int i = 0;
                for (long uid : userIds) {
                    if (CommonUtils.notEmpty(code)) {
                        ids[i++] = new SysUserSettingId(uid, code);
                    }
                }
                Map<String, String> map = new LinkedHashMap<>();
                for (SysUserSetting entity : service.getEntitys(ids)) {
                    map.put(String.valueOf(entity.getId().getUserId()), entity.getData());
                }
                handler.put("map", map).render();
            }
        }

    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private SysUserSettingService service;

}
