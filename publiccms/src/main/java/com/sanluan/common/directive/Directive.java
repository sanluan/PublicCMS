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
     * @return
     */
    public String getName();

    /**
     * @param handler
     * @throws IOException
     * @throws Exception
     */
    public void execute(RenderHandler handler) throws IOException, Exception;
}
