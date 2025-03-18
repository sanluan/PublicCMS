package com.publiccms.logic.service.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.publiccms.common.base.BaseService;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.sys.SysWorkflow;
import com.publiccms.entities.sys.SysWorkflowStep;
import com.publiccms.logic.dao.sys.SysWorkflowDao;

import jakarta.annotation.Resource;

/**
 *
 * SysWorkflowService
 * 
 */
@Service
@Transactional
public class SysWorkflowService extends BaseService<SysWorkflow> {

    private String[] ignoreProperties = new String[] { "id", "siteId", "createDate" };
    private String[] stepIgnoreProperties = new String[] { "id", "workflowId" };
    @Resource
    private SysWorkflowStepService stepService;

    /**
     * @param siteId
     * @param name
     * @param disabled
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, String name, Boolean disabled, String orderType, Integer pageIndex,
            Integer pageSize) {
        return dao.getPage(siteId, name, disabled, orderType, pageIndex, pageSize);
    }

    public SysWorkflow update(SysWorkflow entity, String stepdata) {
        entity.setStartStepId(stepService.update(entity.getId(), getFlowStepList(stepdata), stepIgnoreProperties));
        return update(entity.getId(), entity, ignoreProperties);
    }

    public SysWorkflow save(SysWorkflow entity, String stepdata) {
        save(entity);
        Long startStepId = stepService.save(entity.getId(), getFlowStepList(stepdata));
        if (null != startStepId) {
            entity.setStartStepId(startStepId);
        }
        return entity;
    }

    /**
     * @param siteId
     * @param ids
     * @return
     */
    public List<SysWorkflow> delete(short siteId, Integer[] ids) {
        List<SysWorkflow> entityList = new ArrayList<>();
        for (SysWorkflow entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && !entity.isDisabled()) {
                entity.setDisabled(true);
                entityList.add(entity);
            }
        }
        return entityList;
    }

    /**
     * @param stepdata
     * @return step list
     */
    public List<SysWorkflowStep> getFlowStepList(String stepdata) {
        if (null != stepdata) {
            try {
                List<Map<String, Object>> datalist = Constants.objectMapper.readValue(stepdata,
                        Constants.objectMapper.getTypeFactory().constructCollectionLikeType(List.class, Map.class));
                if (null != datalist && 1 == datalist.size()) {
                    Map<String, Object> map = datalist.get(0);
                    if (null != map) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> items = (List<Map<String, Object>>) map.get("items");
                        if (null != items) {
                            List<SysWorkflowStep> stepList = new ArrayList<>();
                            int i = 0;
                            for (Map<String, Object> data : items) {
                                SysWorkflowStep entity = new SysWorkflowStep();
                                if (null != data.get("id")) {
                                    entity.setId(Long.parseLong(data.get("id").toString()));
                                }
                                if (null != data.get("name")) {
                                    entity.setName(data.get("name").toString());
                                }
                                if (null != data.get("roleId")) {
                                    entity.setRoleId(Integer.parseInt((String) data.get("roleId")));
                                }
                                if (null != data.get("deptId")) {
                                    entity.setDeptId(Integer.parseInt((String) data.get("deptId")));
                                }
                                if (null != data.get("userId")) {
                                    entity.setUserId(Long.parseLong((String) data.get("userId")));
                                }
                                entity.setSort(i);
                                i++;
                                stepList.add(entity);
                            }
                            return stepList;
                        }
                    }
                }
                return null;
            } catch (JsonProcessingException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Resource
    private SysWorkflowDao dao;

}