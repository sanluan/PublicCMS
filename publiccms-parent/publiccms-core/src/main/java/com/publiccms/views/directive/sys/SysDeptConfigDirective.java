package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDeptConfig;
import com.publiccms.entities.sys.SysDeptConfigId;
import com.publiccms.logic.service.sys.SysDeptConfigService;
import com.publiccms.logic.service.sys.SysDeptService;

/**
 *
 * sysDeptConfig 部门配置授权查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>deptId</code> 部门id
 * <li><code>config</code>
 * 配置编码,deptId、config都存在时，结果返回<code>true</code>或<code>false</code>，表示该部门是否拥有该配置的数据权限
 * <li><code>configs</code>
 * 多个配置编码，当deptId存在，且config为空时生效，结果返回<code>map</code>(配置编码,<code>true</code>或<code>false</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;@sys.deptConfig deptId=1
 * config='site'&gt;${object}&lt;/@sys.deptConfig&gt;
 * <p>
 * &lt;@sys.deptConfig deptId=1 configs='site,email'&gt;&lt;#list map as
 * k,v&gt;${k}:${v}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.deptConfig&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('//cms.publiccms.com/api/directive/sys/deptConfig?deptId=1&amp;config=site&amp;appToken=接口访问授权Token', function(data){    
   console.log(data);
 });
 &lt;/script&gt;
 * </pre>
 */
@Component
public class SysDeptConfigDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer deptId = handler.getInteger("deptId");
        String config = handler.getString("config");
        if (CommonUtils.notEmpty(deptId)) {
            if (CommonUtils.notEmpty(config)) {
                SysDept entity = sysDeptService.getEntity(deptId);
                handler.put("object",
                        null != entity
                                && (entity.isOwnsAllConfig() || null != service.getEntity(new SysDeptConfigId(deptId, config))))
                        .render();
            } else {
                String[] configs = handler.getStringArray("configs");
                if (CommonUtils.notEmpty(configs)) {
                    Map<String, Boolean> map = new LinkedHashMap<>();
                    SysDept entity = sysDeptService.getEntity(deptId);
                    if (null != entity) {
                        if (entity.isOwnsAllConfig()) {
                            for (String p : configs) {
                                map.put(p, true);
                            }
                        } else {
                            SysDeptConfigId[] ids = new SysDeptConfigId[configs.length];
                            for (int i = 0; i < configs.length; i++) {
                                map.put(configs[i], false);
                                ids[i] = new SysDeptConfigId(deptId, configs[i]);
                            }
                            for (SysDeptConfig e : service.getEntitys(ids)) {
                                map.put(e.getId().getConfig(), true);
                            }
                        }
                    }
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private SysDeptConfigService service;
    @Resource
    private SysDeptService sysDeptService;
}
