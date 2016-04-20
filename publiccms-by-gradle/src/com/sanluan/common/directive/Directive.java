package com.sanluan.common.directive;

import java.io.IOException;

import com.sanluan.common.handler.RenderHandler;

/**
 * 
 * BaseDirective 指令接口
 *
 */
public interface Directive {

    /**
     * @param handler
     * @throws IOException
     * @throws Exception
     */
    public abstract void execute(RenderHandler handler) throws IOException, Exception;
}
