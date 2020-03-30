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

import freemarker.template.TemplateModelException;

/**
 *
 * GetContentAttributesMethod
 * 
 */
@Component
public class GetContentPageMethod extends BaseMethod {

    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        String text = getString(0, arguments);
        Integer pageIndex = getInteger(1, arguments);
        if (CommonUtils.notEmpty(text)) {
            String pageBreakTag = null;
            if (text.contains(CommonConstants.getCkeditorPageBreakTag())) {
                pageBreakTag = CommonConstants.getCkeditorPageBreakTag();
            } else if (text.contains(CommonConstants.getKindEditorPageBreakTag())) {
                pageBreakTag = CommonConstants.getKindEditorPageBreakTag();
            } else {
                pageBreakTag = CommonConstants.getUeditorPageBreakTag();
            }
            String[] texts = StringUtils.splitByWholeSeparator(text, pageBreakTag);
            PageHandler page = new PageHandler(pageIndex, 1, texts.length, null);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("page", page);
            resultMap.put("text", texts[page.getPageIndex() - 1]);
            return resultMap;
        }
        return null;
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
