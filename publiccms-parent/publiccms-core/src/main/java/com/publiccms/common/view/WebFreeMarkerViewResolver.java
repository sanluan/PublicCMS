package com.publiccms.common.view;

import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import freemarker.template.Configuration;

/**
 *
 * WebFreeMarkerViewResolver
 * 
 */
public class WebFreeMarkerViewResolver extends FreeMarkerViewResolver {
    
    private Configuration configuration;

    @Override
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        FreeMarkerView view = (FreeMarkerView) super.buildView(viewName);
        if (null != configuration) {
            view.setConfiguration(configuration);
        }
        return view;
    }

    /**
     * @param configuration
     */
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}