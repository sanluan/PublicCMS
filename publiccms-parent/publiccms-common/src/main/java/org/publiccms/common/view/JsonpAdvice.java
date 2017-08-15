package org.publiccms.common.view;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 *
 * JsonpAdvice
 * 
 */
@ControllerAdvice
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
    
    /**
     * 
     */
    public JsonpAdvice() {
        super("callback", "jsonp");
    }
}
