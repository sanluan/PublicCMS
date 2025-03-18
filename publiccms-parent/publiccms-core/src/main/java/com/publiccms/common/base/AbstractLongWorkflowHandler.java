package com.publiccms.common.base;

import com.publiccms.common.api.WorkflowHandler;

public abstract class AbstractLongWorkflowHandler implements WorkflowHandler<Long> {
    @Override
    public Long getItemId(String value) {
        return Long.valueOf(value);
    }
}
