package com.publiccms.views.directive.tools;

// Generated 2022-5-10 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;

@Component
public class FileHistoryContentDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String type = handler.getString("type");
        String path = handler.getString("path");
        if (CommonUtils.notEmpty(path)) {
            SysSite site = getSite(handler);
            String realpath;

            if (CommonUtils.notEmpty(type)) {
                switch (type) {
                case "file":
                    realpath = siteComponent.getWebHistoryFilePath(site, path);
                    break;
                case "task":
                    realpath = siteComponent.getTaskTemplateHistoryFilePath(site, path);
                    break;
                case "template":
                default:
                    realpath = siteComponent.getTemplateHistoryFilePath(site, path);
                }
            } else {
                realpath = siteComponent.getTemplateFilePath(site, path);
            }
            handler.put("object", CmsFileUtils.getFileContent(realpath)).render();
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

}
