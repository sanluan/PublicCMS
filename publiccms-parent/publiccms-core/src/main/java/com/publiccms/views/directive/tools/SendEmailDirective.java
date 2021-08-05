package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.FreeMarkerUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.EmailComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPageMetadata;

/**
 *
 * SendEmailDirective
 * 
 */
@Component
public class SendEmailDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String[] email = handler.getStringArray("email");
        String[] cc = handler.getStringArray("cc");
        String[] bcc = handler.getStringArray("bcc");
        String title = handler.getString("title");
        String templatePath = handler.getString("templatePath");
        if (CommonUtils.notEmpty(email) && CommonUtils.notEmpty(title)) {
            SysSite site = getSite(handler);
            if (CommonUtils.notEmpty(templatePath)) {
                Map<String, Object> model = new HashMap<>();
                expose(handler, model);
                CmsPageMetadata metadata = metadataComponent
                        .getTemplateMetadata(siteComponent.getWebTemplateFilePath(site, templatePath));
                CmsPageData data = metadataComponent
                        .getTemplateData(siteComponent.getWebTemplateFilePath(site, templatePath));
                model.put("metadata", metadata.getAsMap(data));
                String content = FreeMarkerUtils.generateStringByFile(SiteComponent.getFullTemplatePath(site, templatePath),
                        templateComponent.getWebConfiguration(), model);
                handler.put("result", emailComponent.sendHtml(site.getId(), email, cc, bcc, title, content)).render();
            } else {
                String content = handler.getString("content");
                if (CommonUtils.notEmpty(content)) {
                    handler.put("result", emailComponent.send(site.getId(), email, cc, bcc, title, content)).render();
                }
            }

        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private EmailComponent emailComponent;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private MetadataComponent metadataComponent;
}
