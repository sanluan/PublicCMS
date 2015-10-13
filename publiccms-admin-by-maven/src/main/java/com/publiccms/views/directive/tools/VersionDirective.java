package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.SpringVersion;
import org.springframework.stereotype.Component;

import com.sanluan.common.base.BaseDirective;
import com.sanluan.common.handler.RenderHandler;

/**
 * 
 * VersionDirective 版本指令
 *
 */
@Component
public class VersionDirective extends BaseDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("spring", SpringVersion.getVersion());
        map.put("hibernate", org.hibernate.Version.getVersionString());
        map.put("hibernateSearch", org.hibernate.search.engine.Version.getVersionString());
        map.put("lucene", org.apache.lucene.util.Version.LATEST.toString());
        handler.put("object", map);
        handler.render();
    }
}
