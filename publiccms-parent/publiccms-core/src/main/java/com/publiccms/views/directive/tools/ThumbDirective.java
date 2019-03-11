package com.publiccms.views.directive.tools;

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
 * ThumbDirective
 *
 */
@Component
public class ThumbDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path");
        Integer width = handler.getInteger("width");
        Integer height = handler.getInteger("height");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(path) && CommonUtils.notEmpty(width) && CommonUtils.notEmpty(height)) {
            String thumbPath = path.substring(0, path.lastIndexOf(CommonConstants.DOT)) + CommonConstants.UNDERLINE + width
                    + CommonConstants.UNDERLINE + height + CmsFileUtils.getSuffix(path);
            String thumbFilePath = siteComponent.getWebFilePath(site, thumbPath);
            if (CmsFileUtils.exists(thumbFilePath)) {
                handler.print(thumbPath);
            } else {
                String sourceFilePath = siteComponent.getWebFilePath(site, path);
                if (CmsFileUtils.exists(sourceFilePath)) {
                    try {
                        CmsFileUtils.thumb(sourceFilePath, thumbFilePath, width, height);
                        handler.print(thumbPath);
                    } catch (IOException e) {
                        handler.print(path);
                        log.error(e.getMessage());
                    }
                } else {
                    handler.print(path);
                }
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }


}
