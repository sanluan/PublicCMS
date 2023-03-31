package com.publiccms.common.directive;

import java.io.IOException;

import com.publiccms.common.handler.RenderHandler;

import freemarker.template.TemplateException;

/**
 * 
 * BaseDirective 指令接口
 *
 */
public interface Directive {
    /**
     * @return namespace
     */
    String getNamespace();

    /**
     * @return name
     */
    String getName();

    /**
     * @return short name
     */
    String getShortName();

    /**
     * @param handler
     * @throws IOException
     * @throws TemplateException
     */
    void execute(RenderHandler handler) throws IOException, TemplateException;
}
