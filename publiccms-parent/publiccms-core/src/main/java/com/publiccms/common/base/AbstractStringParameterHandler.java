package com.publiccms.common.base;

import java.util.Arrays;

import com.publiccms.common.api.ParameterTypeHandler;
import com.publiccms.common.tools.CommonUtils;

public abstract class AbstractStringParameterHandler<E> implements ParameterTypeHandler<E, String> {
    @Override
    public String[] dealParameterValues(String[] values) {
        return Arrays.stream(values).filter(CommonUtils::notEmpty).toArray(String[]::new);
    }

    @Override
    public String dealParameterValue(String value) {
        return value;
    }
}
