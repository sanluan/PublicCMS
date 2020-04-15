package com.publiccms.views.method.tools;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.AnalyzerDictUtils;
import com.publiccms.logic.component.site.SiteComponent;

import freemarker.template.TemplateModelException;

@Component
public class GetDictMethod extends BaseMethod {
    @Autowired
    private SiteComponent siteComponent;

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        File dictFile = new File(siteComponent.getRootPath() + AnalyzerDictUtils.DIR_DICT + AnalyzerDictUtils.TXT_DICT);
        try {
            return FileUtils.readFileToString(dictFile, CommonConstants.DEFAULT_CHARSET_NAME);
        } catch (IOException e) {
        }
        return null;
    }

    @Override
    public int minParametersNumber() {
        return 0;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }
}
