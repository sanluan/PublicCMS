package com.publiccms.logic.component.workflow;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.WorkflowHandler;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysWorkflowProcess;
import com.publiccms.entities.sys.SysWorkflowProcessHistory;
import com.publiccms.logic.service.sys.SysWorkflowProcessHistoryService;

@Component
public class ProcessComponent {
    private Map<String, WorkflowHandler<?>> workflowHandlerMap;

    /**
     * @param <T>
     * @param entity
     * @param user
     * @param history
     */
    public <T> void finishProcess(SysWorkflowProcess entity, SysUser user, SysWorkflowProcessHistory history) {
        @SuppressWarnings("unchecked")
        WorkflowHandler<T> workflowHandler = (WorkflowHandler<T>) workflowHandlerMap.get(entity.getItemType());
        if (null != workflowHandler) {
            if (entity.isClosed() && SysWorkflowProcessHistoryService.OPERATE_AGREE.equalsIgnoreCase(history.getOperate())) {
                workflowHandler.finish(entity, user, history, workflowHandler.getItemId(entity.getItemId()));
            }
        }
    }

    public <T> void reject(SysWorkflowProcess entity, SysUser user, SysWorkflowProcessHistory history) {
        @SuppressWarnings("unchecked")
        WorkflowHandler<T> workflowHandler = (WorkflowHandler<T>) workflowHandlerMap.get(entity.getItemType());
        if (null != workflowHandler) {
            if (SysWorkflowProcessHistoryService.OPERATE_REJECT.equalsIgnoreCase(history.getOperate())) {
                workflowHandler.interrupt(entity, user, history, workflowHandler.getItemId(entity.getItemId()));
            }
        }
    }

    /**
     * @return the workflowHandlerMap
     */
    public Map<String, WorkflowHandler<?>> getWorkflowHandlerMap() {
        return workflowHandlerMap;
    }

    /**
     * @param <T>
     * @param workflowHandlerList
     */
    @Autowired
    public <T> void setWorkflowHandlerMap(List<WorkflowHandler<T>> workflowHandlerList) {
        this.workflowHandlerMap = new LinkedHashMap<>();
        for (WorkflowHandler<T> workflowHandler : workflowHandlerList) {
            this.workflowHandlerMap.put(workflowHandler.getItemType(), workflowHandler);
        }
    }
}
