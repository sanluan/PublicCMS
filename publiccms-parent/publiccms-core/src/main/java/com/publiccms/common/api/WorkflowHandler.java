package com.publiccms.common.api;

import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysWorkflowProcess;
import com.publiccms.entities.sys.SysWorkflowProcessHistory;

public interface WorkflowHandler<T> {
    public String getItemType();

    public T getItemId(String value);

    public void finish(SysWorkflowProcess entity, SysUser user, SysWorkflowProcessHistory history, T itemId);

    public void interrupt(SysWorkflowProcess entity, SysUser user, SysWorkflowProcessHistory history, T itemId);
}
