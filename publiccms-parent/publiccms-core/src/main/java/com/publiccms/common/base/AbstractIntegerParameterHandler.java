package com.publiccms.common.base;

import java.util.Arrays;

import com.publiccms.common.api.ParameterTypeHandler;
import com.publiccms.common.tools.CommonUtils;

public abstract class AbstractIntegerParameterHandler<E> implements ParameterTypeHandler<E, Integer> {

    @Override
    public Integer[] dealParameterValues(String[] values) {
        return Arrays.stream(values).filter(CommonUtils::notEmpty).map(Integer::valueOf).toArray(Integer[]::new);
    }

    @Override
    public Integer dealParameterValue(String value) {
        return Integer.valueOf(value);
    }
}
