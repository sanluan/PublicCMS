package com.publiccms.views.method.tools;

import static org.springframework.util.StringUtils.arrayToDelimitedString;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;

import freemarker.template.TemplateModelException;

/**
 *
 * GetPageMethod
 * 
 */
@Component
public class GetPageMethod extends BaseMethod {
    private static final String[] PAGE_PARAMTERS = { "p", "page", "pageIndex", "pageNo" };

    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        String url = getString(0, arguments);
        Integer pageIndex = getInteger(1, arguments);
        if (CommonUtils.notEmpty(url) && CommonUtils.notEmpty(pageIndex)) {
            return getPageUrl(url, pageIndex);
        }
        return url;
    }

    public String getPageUrl(String url, int pageIndex) {
        int splitIndex = url.lastIndexOf('?');
        if (-1 < splitIndex) {
            String[] paramters = StringUtils.split(url.substring(splitIndex + 1), '&');
            for (int i = 0; i < paramters.length; i++) {
                String[] temp = StringUtils.split(paramters[i], "=", 2);
                if (ArrayUtils.contains(PAGE_PARAMTERS, temp[0]) && 2 == temp.length) {
                    temp[1] = String.valueOf(pageIndex);
                    paramters[i] = arrayToDelimitedString(temp, "=");
                    break;
                }
            }
            return url.substring(0, splitIndex) + "?" + arrayToDelimitedString(paramters, "&");
        } else {
            int index = url.lastIndexOf('.');
            if (-1 < index) {
                String prefixFilePath = url.substring(0, index);
                String suffixFilePath = url.substring(index, url.length());
                if (url.lastIndexOf("/") < url.lastIndexOf("_")) {
                    prefixFilePath = prefixFilePath.substring(0, url.lastIndexOf("_"));
                }
                if (1 < pageIndex) {
                    return prefixFilePath + '_' + pageIndex + suffixFilePath;
                } else {
                    return prefixFilePath + suffixFilePath;
                }

            } else {
                String prefixFilePath = url;
                if (url.lastIndexOf("/") < url.lastIndexOf("_")) {
                    prefixFilePath = prefixFilePath.substring(0, url.lastIndexOf("_"));
                }
                if (1 < pageIndex) {
                    return prefixFilePath + '_' + pageIndex;
                } else {
                    return prefixFilePath;
                }
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return false;
    }

    @Override
    public int minParamtersNumber() {
        return 2;
    }
}
