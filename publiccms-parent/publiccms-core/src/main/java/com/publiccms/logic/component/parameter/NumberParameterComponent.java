package com.publiccms.logic.component.parameter;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.base.AbstractLongParameterHandler;
import com.publiccms.entities.sys.SysSite;

import jakarta.annotation.Priority;

/**
 * NumberParameterComponent 数字参数处理组件
 */
@Component
@Priority(2)
public class NumberParameterComponent extends AbstractLongParameterHandler<Long> {

    @Override
    public String getType() {
        return Config.INPUTTYPE_NUMBER;
    }

    @Override
    public List<Long> getParameterValueList(SysSite site, Long[] ids) {
        return Arrays.asList(ids);
    }

    @Override
    public Long getParameterValue(SysSite site, Long id) {
        return id;
    }
}
