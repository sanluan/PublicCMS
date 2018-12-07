package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.HashSet;
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
 * TemplateListDirective
 * 
 */
@Component
public class TemplatePlaceListDirective extends AbstractTemplateDirective {
    public static final Pattern PLACE_PATTERN = Pattern.compile("<@_includePlace[ ]+path=[\"|\'](.*)[\"|\'][ ]*/>");

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path");
        Set<String> placeSet = new HashSet<>();
        if (CommonUtils.notEmpty(path)) {
            String fileContent = CmsFileUtils.getFileContent(siteComponent.getWebTemplateFilePath(getSite(handler), path));
            if (CommonUtils.notEmpty(fileContent)) {
                Matcher matcher = PLACE_PATTERN.matcher(fileContent);
                while (matcher.find()) {
                    placeSet.add(matcher.group(1));
                }
                Set<String> placeSet2 = new HashSet<>();
                for (String place : placeSet) {
                    String placeContent = CmsFileUtils.getFileContent(
                            siteComponent.getWebTemplateFilePath(getSite(handler), TemplateComponent.INCLUDE_DIRECTORY + place));
                    if (CommonUtils.notEmpty(placeContent)) {
                        Matcher placeMatcher = PLACE_PATTERN.matcher(placeContent);
                        while (placeMatcher.find()) {
                            placeSet2.add(placeMatcher.group(1));
                        }
                    }
                }
                placeSet.addAll(placeSet2);
            }
        }
        handler.put("list", placeSet).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

}