package com.publiccms.views.directive.tools;

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 * 
 * VersionDirective 技术框架版本指令
 *
 */
@Component
public class MemoryDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Runtime runtime = Runtime.getRuntime();
        handler.put("freeMemory", runtime.freeMemory());
        handler.put("totalMemory", runtime.totalMemory());
        handler.put("maxMemory", runtime.maxMemory());
        handler.render();
    }

}
