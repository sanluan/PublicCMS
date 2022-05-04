package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.TemplateDirectiveHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.component.template.DiyComponent;
import com.publiccms.views.pojo.diy.CmsDiyData;
import com.publiccms.views.pojo.diy.CmsLayout;
import com.publiccms.views.pojo.diy.CmsLayoutData;
import com.publiccms.views.pojo.diy.CmsModuleData;
import com.publiccms.views.pojo.diy.CmsRegionData;

import freemarker.core.Environment;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 *
 * MetadataDirective
 * 
 */
@Component
public class DiyRegionDirective extends AbstractTemplateDirective {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Environment environment, @SuppressWarnings("rawtypes") Map parameters, TemplateModel[] loopVars,
            TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        try {
            RenderHandler handler = new TemplateDirectiveHandler(parameters, loopVars, environment, templateDirectiveBody);
            String id = handler.getString("id");
            if (CommonUtils.notEmpty(id)) {
                Map<String, Object> metadata = (Map<String, Object>) handler.getAttribute("metadata");
                if (null != metadata) {
                    CmsDiyData diydata = (CmsDiyData) metadata.get("diydata");
                    if (null != diydata) {
                        Map<String, CmsRegionData> regionMap = diydata.getRegionMap();
                        CmsRegionData region = regionMap.get(id);
                        if (null != region) {
                            List<CmsLayoutData> layoutList = region.getLayoutList();
                            if (null != layoutList) {
                                for (CmsLayoutData layoutData : layoutList) {
                                    CmsLayout layout = diyComponent.getLayout(getSite(handler), layoutData.getId());
                                    String template = layout.getTemplate();
                                    Matcher matcher = CmsLayout.PLACE_PATTERN.matcher(template);
                                    StringBuffer sb = new StringBuffer();
                                    List<List<CmsModuleData>> moduleListList = layoutData.getModuleList();
                                    StringBuffer moduleSb = new StringBuffer();
                                    for (int i = 0; matcher.find(); i++) {
                                        if (null != moduleListList && moduleListList.size() > i) {
                                            List<CmsModuleData> moduleList = moduleListList.get(i);
                                            if (null != moduleList) {
                                                for (CmsModuleData moduleData : moduleList) {
                                                    moduleSb.append("<@_includePlace path=\"").append(moduleData.getPath())
                                                            .append("\"/>");
                                                }
                                                matcher.appendReplacement(sb, moduleSb.toString());
                                                moduleSb.setLength(0);
                                            } else {
                                                matcher.appendReplacement(sb, CommonConstants.BLANK);
                                            }
                                        } else {
                                            matcher.appendReplacement(sb, CommonConstants.BLANK);
                                        }
                                    }
                                    if (0 == sb.length()) {
                                        environment.include(
                                                new Template(layout.getName(), template, environment.getConfiguration()));
                                    } else {
                                        environment.include(
                                                new Template(layout.getName(), sb.toString(), environment.getConfiguration()));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (

        IOException e) {
            throw e;
        } catch (Exception e) {
            throw new TemplateException(e, environment);
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private DiyComponent diyComponent;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        // nouse
    }
}
