package com.publiccms.views.method.tools;

import static org.springframework.util.StringUtils.arrayToDelimitedString;

import java.util.List;

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

    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        String url = getString(0, arguments);
        Integer pageIndex = getInteger(1, arguments);
        String pageParameter = getString(2, arguments);
        if (CommonUtils.notEmpty(url) && CommonUtils.notEmpty(pageIndex)) {
            return getPageUrl(url, pageIndex, pageParameter);
        }
        return url;
    }

    public String getPageUrl(String url, int pageIndex, String pageParameter) {
        if (CommonUtils.notEmpty(pageParameter)) {
            int splitIndex = url.lastIndexOf('?');
            if (-1 < splitIndex) {
                boolean flag = true;
                String[] parameters = StringUtils.split(url.substring(splitIndex + 1), '&');
                for (int i = 0; i < parameters.length; i++) {
                    String[] temp = StringUtils.split(parameters[i], "=", 2);
                    if (pageParameter.equalsIgnoreCase(temp[0])) {
                        parameters[i] = pageParameter + "=" + pageIndex;
                        flag = false;
                        break;
                    }
                }
                StringBuilder sb = new StringBuilder(url.substring(0, splitIndex)).append("?")
                        .append(arrayToDelimitedString(parameters, "&"));
                if (flag) {
                    sb.append("&").append(pageParameter).append("=").append(pageIndex);
                }
                return sb.toString();
            } else {
                return new StringBuilder(url).append("?").append(pageParameter).append("=").append(pageIndex).toString();
            }
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
    public int minParametersNumber() {
        return 2;
    }
}
