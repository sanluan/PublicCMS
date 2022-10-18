package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.sys.SysConfigData;
import com.publiccms.entities.sys.SysConfigDataId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.sys.SysConfigDataService;

/**
*
* sysConfigData 配置数据查询指令
* <p>
* 参数列表
* <ul>
* <li><code>code</code> 配置编码,结果返回<code>object</code>
* {@link com.publiccms.entities.sys.SysConfigData}
* <li><code>codes</code> 多个配置编码,结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@sys.configData code='site'&gt;${object.register_url}&lt;/@sys.configData&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('//cms.publiccms.com/api/directive/sys/configData?code=site&amp;appToken=接口访问授权Token', function(data){    
 console.log(data.register_url);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class SysConfigDataDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String code = handler.getString("code");
        String[] codes = handler.getStringArray("codes");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(code)) {
            SysConfigData entity = service.getEntity(new SysConfigDataId(site.getId(), code));
            if (null != entity) {
                handler.put("object", ExtendUtils.getExtendMap(entity.getData())).render();
            }
        } else if (CommonUtils.notEmpty(codes)) {
            SysConfigDataId[] ids = new SysConfigDataId[codes.length];
            int i = 0;
            for (String s : codes) {
                if (CommonUtils.notEmpty(s)) {
                    ids[i++] = new SysConfigDataId(site.getId(), s);
                }
            }
            Map<String, Map<String, String>> map = new LinkedHashMap<>();
            for (SysConfigData entity : service.getEntitys(ids)) {
                map.put(entity.getId().getCode(), ExtendUtils.getExtendMap(entity.getData()));
            }
            handler.put("map", map).render();
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private SysConfigDataService service;

}
