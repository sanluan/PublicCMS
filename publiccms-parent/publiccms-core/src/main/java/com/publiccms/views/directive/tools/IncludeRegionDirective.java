package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.TemplateDirectiveHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.DiyComponent;
import com.publiccms.views.pojo.diy.CmsLayout;
import com.publiccms.views.pojo.diy.CmsLayoutData;
import com.publiccms.views.pojo.diy.CmsModuleData;
import com.publiccms.views.pojo.diy.CmsRegionData;

import freemarker.core.Environment;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import jakarta.annotation.Resource;

/**
 * includeRegion 包含diy区域指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>id</code> 布局id
 * </ul>
 * <p>
 * 打印包含结果
 * <p>
 * 使用示例
 * <p>
 * &lt;@tools.includeRegion id='00000000-0000-0000-0000-000000000000'
 * categoryId=1/&gt;
 * 
 */
@Component
public class IncludeRegionDirective extends AbstractTemplateDirective {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Environment environment, @SuppressWarnings("rawtypes") Map parameters, TemplateModel[] loopVars,
            TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        try {
            RenderHandler handler = new TemplateDirectiveHandler(parameters, loopVars, environment, templateDirectiveBody);
            String id = handler.getString("id");
            Integer categoryId = handler.getInteger("categoryId");
            if (CommonUtils.notEmpty(id)) {
                SysSite site = getSite(handler);
                CmsRegionData regionData = diyComponent.getRegionData(site, id);
                if (null != regionData) {
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
                                StringBuffer sb = new StringBuffer();
                                List<List<CmsModuleData>> moduleListList = layoutData.getModuleList();
                                int end = 0, i = 0;
                                while (matcher.find()) {
                                    sb.append(template.substring(end, matcher.start()));
                                    if (null != moduleListList && moduleListList.size() > i) {
                                        List<CmsModuleData> moduleList = moduleListList.get(i);
                                        if (null != moduleList) {
                                            for (CmsModuleData moduleData : moduleList) {
                                                if (CommonUtils.notEmpty(moduleData.getPlace())) {
                                                    sb.append("<@tools.includePlace path=\"").append(moduleData.getPlace())
                                                            .append("\"/>");
                                                } else if (CommonUtils.notEmpty(moduleData.getFragment())) {
                                                    sb.append("<@include path=\"").append(moduleData.getFragment())
                                                            .append("\"/>");
                                                }
                                            }
                                        }
                                    }
                                    i++;
                                    end = matcher.end();
                                }
                                if (end < template.length()) {
                                    sb.append(template.substring(end, template.length()));
                                }
                                environment
                                        .include(new Template(layout.getName(), sb.toString(), environment.getConfiguration()));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new TemplateException(e, environment);
        }
    }

    @Override
    public boolean httpEnabled() {
        return false;
    }

    @Resource
    private DiyComponent diyComponent;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        // nouse
    }
}
