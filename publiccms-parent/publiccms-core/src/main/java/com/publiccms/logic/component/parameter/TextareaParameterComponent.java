package com.publiccms.logic.component.parameter;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.base.AbstractStringParameterHandler;
import com.publiccms.entities.sys.SysSite;

/**
 * TextareaParameterComponent 多行文本参数处理组件
 */
@Component
public class TextareaParameterComponent extends AbstractStringParameterHandler<String> {

    @Override
    public String getType() {
        return Config.INPUTTYPE_TEXTAREA;
    }

    @Override
    public List<String> getParameterValueList(SysSite site, String[] ids) {
        return Arrays.asList(ids);
    }

    @Override
    public String getParameterValue(SysSite site, String id) {
        return id;
    }
}
