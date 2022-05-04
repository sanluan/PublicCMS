package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;

/**
 *
 * TemplateListDirective
 * 
 */
@Component
public class TemplateRegionListDirective extends AbstractTemplateDirective {
    public static final Pattern PLACE_PATTERN = Pattern.compile("<@_diyRegion[ ]+.*id=[\"|\']([^\"\']*)[\"|\'].*>");

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path");
        Set<String> regionList = new LinkedHashSet<>();
        if (CommonUtils.notEmpty(path)) {
            String fileContent = CmsFileUtils.getFileContent(siteComponent.getWebTemplateFilePath(getSite(handler), path));
            if (CommonUtils.notEmpty(fileContent)) {
                Matcher matcher = PLACE_PATTERN.matcher(fileContent);
                while (matcher.find()) {
                    regionList.add(matcher.group(1));
                }
            }
        }
        handler.put("list", regionList).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }
}