package com.publiccms.common.base;

import com.publiccms.common.api.WorkflowHandler;

public abstract class AbstractIntegerWorkflowHandler implements WorkflowHandler<Integer> {
    @Override
    public Integer getItemId(String value) {
        return Integer.valueOf(value);
    }
}
