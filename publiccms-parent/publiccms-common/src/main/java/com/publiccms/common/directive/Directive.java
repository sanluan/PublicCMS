package com.publiccms.common.directive;

import java.io.IOException;

import com.publiccms.common.handler.RenderHandler;

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
     * @throws Exception
     */
    void execute(RenderHandler handler) throws IOException, Exception;
}
