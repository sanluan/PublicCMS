package com.publiccms.common.directive;


import com.publiccms.common.handler.RenderHandler;

/**
 * 
 * BaseDirective 指令接口
 *
 */
public interface Directive {
    /**
     * @return name
     */
    String getName();

    /**
     * @param handler
     * @throws Exception
     */
    void execute(RenderHandler handler) throws Exception;
}
