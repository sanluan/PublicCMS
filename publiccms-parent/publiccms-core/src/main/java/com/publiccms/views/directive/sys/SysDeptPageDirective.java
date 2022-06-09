package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDeptPage;
import com.publiccms.entities.sys.SysDeptPageId;
import com.publiccms.logic.service.sys.SysDeptPageService;
import com.publiccms.logic.service.sys.SysDeptService;

/**
 *
 * sysDeptPage 部门页面授权查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>deptId</code> 部门id
 * <li><code>page</code>
 * 页面路径,deptId、page都存在时，结果返回<code>true</code>或<code>false</code>，表示该部门是否拥有该页面的数据权限
 * <li><code>pages</code>
 * 多个页面路径，当deptId存在，且page为空时生效，结果返回<code>map</code>(页面路径,<code>true</code>或<code>false</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;@sys.deptPage deptId=1
 * page='/index.html'&gt;${object}&lt;/@sys.deptPage&gt;
 * <p>
 * &lt;@sys.deptPage deptId=1 pages='/index.html,/search'&gt;&lt;#list map as
 * k,v&gt;${k}:${v}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.deptPage&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('//cms.publiccms.com/api/directive/sys/deptPage?deptId=1&amp;page=/index.html&amp;appToken=接口访问授权Token', function(data){    
   console.log(data);
 });
 &lt;/script&gt;
 * </pre>
 */
@Component
public class SysDeptPageDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer deptId = handler.getInteger("deptId");
        String page = handler.getString("page");
        if (CommonUtils.notEmpty(deptId)) {
            if (CommonUtils.notEmpty(page)) {
                SysDept entity = sysDeptService.getEntity(deptId);
                handler.put("object",
                        null != entity && (entity.isOwnsAllPage() || null != service.getEntity(new SysDeptPageId(deptId, page))))
                        .render();
            } else {
                String[] pages = handler.getStringArray("pages");
                if (CommonUtils.notEmpty(pages)) {
                    Map<String, Boolean> map = new LinkedHashMap<>();
                    SysDept entity = sysDeptService.getEntity(deptId);
                    if (null != entity) {
                        if (entity.isOwnsAllPage()) {
                            for (String p : pages) {
                                map.put(p, true);
                            }
                        } else {
                            SysDeptPageId[] ids = new SysDeptPageId[pages.length];
                            for (int i = 0; i < pages.length; i++) {
                                map.put(pages[i], false);
                                ids[i] = new SysDeptPageId(deptId, pages[i]);
                            }
                            for (SysDeptPage e : service.getEntitys(ids)) {
                                map.put(e.getId().getPage(), true);
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

    @Autowired
    private SysDeptPageService service;
    @Autowired
    private SysDeptService sysDeptService;
}
