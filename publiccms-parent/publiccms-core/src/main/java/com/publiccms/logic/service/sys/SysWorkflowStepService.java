package com.publiccms.logic.service.sys;

import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysWorkflowStep;
import com.publiccms.logic.dao.sys.SysWorkflowStepDao;

import jakarta.annotation.Resource;

/**
 *
 * SysWorkflowStepService
 * 
 */
@Service
@Transactional
public class SysWorkflowStepService extends BaseService<SysWorkflowStep> {

    /**
     * @param workflowId
     * @param sort
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public List<SysWorkflowStep> getList(Integer workflowId, Integer sort) {
        return dao.getList(workflowId, sort);
    }

    /**
     * @param workflowId
     * @param entityList
     * @return first step id
     */
    public Long save(int workflowId, List<SysWorkflowStep> entityList) {
        Long lastStepId = null;
        if (CommonUtils.notEmpty(entityList)) {
            ListIterator<SysWorkflowStep> iterator = entityList.listIterator(entityList.size());
            while (iterator.hasPrevious()) {
                SysWorkflowStep entity = iterator.previous();
                entity.setWorkflowId(workflowId);
                entity.setNextStepId(lastStepId);
                save(entity);
                lastStepId = entity.getId();
            }
        }
        return lastStepId;
    }

    /**
     * @param workflowId
     * @param entitys
     * @param ignoreProperties
     * @return first step id
     */
    public Long update(int workflowId, List<SysWorkflowStep> entitys, String[] ignoreProperties) {
        Set<Long> idList = new HashSet<>();
        Long lastStepId = null;
        if (CommonUtils.notEmpty(entitys)) {
            SysWorkflowStep last = null;
            ListIterator<SysWorkflowStep> iterator = entitys.listIterator(entitys.size());
            while (iterator.hasPrevious()) {
                SysWorkflowStep entity = iterator.previous();
                if (null != entity.getId()) {
                    SysWorkflowStep oldEntity = getEntity(entity.getId());
                    if (workflowId == oldEntity.getWorkflowId()) {
                        entity.setNextStepId(lastStepId);
                        update(entity.getId(), entity, ignoreProperties);
                        lastStepId = entity.getId();
                        idList.add(entity.getId());
                    }
                } else {
                    entity.setWorkflowId(workflowId);
                    entity.setNextStepId(null == last ? null : last.getId());
                    save(entity);
                    lastStepId = entity.getId();
                    idList.add(entity.getId());
                }
            }
        }
        for (SysWorkflowStep step : getList(workflowId, null)) {
            if (!idList.contains(step.getId())) {
                delete(step.getId());
            }
        }
        return lastStepId;
    }

    /**
     * @param wordflowId
     */
    public void deleteByWorkflowId(Long wordflowId) {
        dao.deleteByWorkflowId(wordflowId);
    }

    @Resource
    private SysWorkflowStepDao dao;

}