package com.publiccms.views.directive.tools;

import static com.publiccms.common.constants.CmsVersion.getVersion;
import java.io.IOException;

import org.springframework.core.SpringVersion;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.RenderHandler;

/**
 * 
 * VersionDirective 技术框架版本指令
 *
 */
@Component
public class VersionDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        handler.put("spring", SpringVersion.getVersion());
        handler.put("hibernate", org.hibernate.Version.getVersionString());
        handler.put("hibernateSearch", org.hibernate.search.engine.Version.getVersionString());
        handler.put("lucene", org.apache.lucene.util.Version.LATEST.toString());
        handler.put("cms", getVersion());
        handler.render();
    }

}
