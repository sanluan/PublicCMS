package com.publiccms.common.base;

import java.util.Arrays;

import com.publiccms.common.api.ParameterTypeHandler;
import com.publiccms.common.tools.CommonUtils;

public abstract class AbstractLongParameterHandler<E> implements ParameterTypeHandler<E, Long> {

    @Override
    public Long[] dealParameterValues(String[] values) {
        return Arrays.stream(values).filter(CommonUtils::notEmpty).map(Long::valueOf).toArray(Long[]::new);
    }

    @Override
    public Long dealParameterValue(String value) {
        return Long.valueOf(value);
    }
}
