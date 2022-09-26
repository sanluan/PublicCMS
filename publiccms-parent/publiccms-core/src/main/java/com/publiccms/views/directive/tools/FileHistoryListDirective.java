package com.publiccms.views.directive.tools;

// Generated 2022-5-10 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;

/**
 *
 * fileHistoryList 文件历史列表查询指令
 */
@Component
public class FileHistoryListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String type = handler.getString("type");
        String path = handler.getString("path", CommonConstants.SEPARATOR);
        SysSite site = getSite(handler);
        String realpath;
        if (CommonUtils.notEmpty(type)) {
            switch (type) {
            case "file":
                realpath = siteComponent.getWebHistoryFilePath(site, path, false);
                break;
            case "task":
                realpath = siteComponent.getTaskTemplateHistoryFilePath(site, path, false);
                break;
            case "template":
            default:
                realpath = siteComponent.getTemplateHistoryFilePath(site, path, false);
            }
        } else {
            realpath = siteComponent.getTemplateHistoryFilePath(site, path, false);
        }
        handler.put("list", CmsFileUtils.getFileList(realpath, handler.getString("orderField"))).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

}