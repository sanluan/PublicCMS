package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.DiyComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.views.pojo.diy.CmsLayout;
import com.publiccms.views.pojo.diy.CmsLayoutData;
import com.publiccms.views.pojo.diy.CmsModuleData;
import com.publiccms.views.pojo.diy.CmsRegionData;

/**
 *
 * TemplatePlaceListDirective
 *
 */
@Component
public class TemplatePlaceListDirective extends AbstractTemplateDirective {
    public static final Pattern PLACE_PATTERN = Pattern
            .compile("<@[_a-z\\.]*includePlace[ ]+path=[\"|\']([^\"\']*)[\"|\'][ ]*/>");

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path");
        Set<String> placeSet = new LinkedHashSet<>();
        Set<String> regionList = new LinkedHashSet<>();
        Map<String, List<Integer>> regionMap = new HashMap<>();
        if (CommonUtils.notEmpty(path)) {
            SysSite site = getSite(handler);
            String fileContent = CmsFileUtils.getFileContent(siteComponent.getWebTemplateFilePath(site, path));
            if (CommonUtils.notEmpty(fileContent)) {
                Matcher matcher = PLACE_PATTERN.matcher(fileContent);
                while (matcher.find()) {
                    placeSet.add(matcher.group(1));
                }
                Matcher regionMatcher = TemplateRegionListDirective.REGION_PATTERN.matcher(fileContent);
                while (regionMatcher.find()) {
                    regionList.add(regionMatcher.group(2));
                }
                Set<String> placeSet2 = new HashSet<>();
                for (String place : placeSet) {
                    String placeContent = CmsFileUtils.getFileContent(
                            siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + place));
                    if (CommonUtils.notEmpty(placeContent)) {
                        Matcher placeMatcher = PLACE_PATTERN.matcher(placeContent);
                        while (placeMatcher.find()) {
                            placeSet2.add(placeMatcher.group(1));
                        }
                        regionMatcher = TemplateRegionListDirective.REGION_PATTERN.matcher(placeContent);
                        while (regionMatcher.find()) {
                            String id = regionMatcher.group(2);
                            regionList.add(id);
                            findCategoryId(id, regionMatcher, regionMap);
                        }
                    }
                }
                placeSet.addAll(placeSet2);
                for (String id : regionList) {
                    addPlaceInRegion(site, id, regionMap, placeSet);
                }
            }
        }
        handler.put("list", placeSet).render();
    }

    private void addPlaceInRegion(SysSite site, String id, Map<String, List<Integer>> regionMap, Set<String> placeSet) {
        CmsRegionData regionData = diyComponent.getRegionData(site, id);
        if (null != regionData) {
            List<Integer> categoryIdList = regionMap.get(id);
            if (null == categoryIdList) {
                addPlaceInRegion(site, null, regionData, placeSet);
            } else {
                for (Integer categoryId : categoryIdList) {
                    addPlaceInRegion(site, categoryId, regionData, placeSet);
                }
            }
        }
    }

    private void addPlaceInRegion(SysSite site, Integer categoryId, CmsRegionData regionData, Set<String> placeSet) {
        List<CmsLayoutData> layoutList = null;
        if (null == categoryId) {
            layoutList = regionData.getLayoutList();
        } else {
            if (null != regionData.getCategoryLayoutMap()) {
                layoutList = regionData.getCategoryLayoutMap().get(categoryId);
            }
        }
        if (null != layoutList) {
            for (CmsLayoutData layoutData : layoutList) {
                CmsLayout layout = diyComponent.getLayout(site, layoutData.getId());
                if (null != layout) {
                    String template = layout.getTemplate();
                    Matcher matcher = CmsLayout.PLACE_PATTERN.matcher(template);
                    List<List<CmsModuleData>> moduleListList = layoutData.getModuleList();
                    if (null != moduleListList) {
                        for (int i = 0; matcher.find() && moduleListList.size() > i; i++) {
                            List<CmsModuleData> moduleList = moduleListList.get(i);
                            if (null != moduleList) {
                                for (CmsModuleData moduleData : moduleList) {
                                    placeSet.add(moduleData.getPlace());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void findCategoryId(String id, Matcher regionMatcher, Map<String, List<Integer>> regionMap) {
        String parameter1 = regionMatcher.group(1);
        if (CommonUtils.notEmpty(parameter1)) {
            Matcher categoryIdMatcher = TemplateRegionListDirective.CATEGORY_ID_PATTERN.matcher(parameter1);
            if (categoryIdMatcher.find()) {
                List<Integer> categoryIdList = regionMap.computeIfAbsent(id, k -> new ArrayList<>());
                categoryIdList.add(Integer.parseInt(categoryIdMatcher.group(1)));
            } else {
                String parameter2 = regionMatcher.group(3);
                if (CommonUtils.notEmpty(parameter2)) {
                    categoryIdMatcher = TemplateRegionListDirective.CATEGORY_ID_PATTERN.matcher(parameter2);
                    if (categoryIdMatcher.find()) {
                        List<Integer> categoryIdList = regionMap.computeIfAbsent(id, k -> new ArrayList<>());
                        categoryIdList.add(Integer.parseInt(categoryIdMatcher.group(1)));
                    }
                }
            }
        }

    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private DiyComponent diyComponent;
}