package com.publiccms.views.method.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * GetContentAttributesMethod
 *
 */
@Component
public class GetContentPageMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        String text = getString(0, arguments);
        Integer pageIndex = getInteger(1, arguments);
        Map<String, Object> resultMap = new HashMap<>();
        if (CommonUtils.notEmpty(text)) {
            String pageBreakTag = null;
            if (text.contains(CommonConstants.getCkeditorPageBreakTag())) {
                pageBreakTag = CommonConstants.getCkeditorPageBreakTag();
            } else if (text.contains(CommonConstants.getTinyMCEPageBreakTag())) {
                pageBreakTag = CommonConstants.getTinyMCEPageBreakTag();
            } else if (text.contains(CommonConstants.getKindEditorPageBreakTag())) {
                pageBreakTag = CommonConstants.getKindEditorPageBreakTag();
            } else {
                pageBreakTag = CommonConstants.getUeditorPageBreakTag();
            }
            String[] texts = StringUtils.splitByWholeSeparator(text, pageBreakTag);
            PageHandler page = new PageHandler(pageIndex, 1);
            page.setTotalCount(texts.length);
            resultMap.put("page", page);
            resultMap.put("text", texts[page.getPageIndex() - 1]);
        }
        return resultMap;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }
}
