package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.component.template.TemplateComponent;

/**
 *
 * TemplateRegionListDirective
 *
 */
@Component
public class TemplateRegionListDirective extends AbstractTemplateDirective {
    public static final Pattern REGION_PATTERN = Pattern.compile("<@[_a-z\\.]*includeRegion[ ]+.*id=[\"|\']([^\"\']*)[\"|\'].*>");

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path");
        Set<String> regionList = new LinkedHashSet<>();
        if (CommonUtils.notEmpty(path)) {
            String fileContent = CmsFileUtils.getFileContent(siteComponent.getTemplateFilePath(getSite(handler), path));
            if (CommonUtils.notEmpty(fileContent)) {
                if (CommonUtils.notEmpty(fileContent)) {
                    Matcher matcher = REGION_PATTERN.matcher(fileContent);
                    while (matcher.find()) {
                        regionList.add(matcher.group(1));
                    }
                }
                Set<String> placeSet = new HashSet<>();
                Matcher matcher = TemplatePlaceListDirective.PLACE_PATTERN.matcher(fileContent);
                while (matcher.find()) {
                    placeSet.add(matcher.group(1));
                }
                for (String place : placeSet) {
                    String placeContent = CmsFileUtils.getFileContent(
                            siteComponent.getTemplateFilePath(getSite(handler), TemplateComponent.INCLUDE_DIRECTORY + place));
                    if (CommonUtils.notEmpty(placeContent)) {
                        matcher = REGION_PATTERN.matcher(placeContent);
                        while (matcher.find()) {
                            regionList.add(matcher.group(1));
                        }
                    }
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