package com.publiccms.logic.component.site;

import java.util.Date;
import java.util.List;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysCluster;
import com.publiccms.logic.component.task.ScheduledTask;
import com.publiccms.logic.service.sys.SysClusterService;

/**
 *
 * ClusterComponent
 * 
 */
@Component
public class ClusterComponent {
    /**
     * 
     */
    public static final long THEARTBEAT_INTERVAL = 60 * 1000L;

    @Autowired
    private SysClusterService service;
    @Autowired
    private ScheduledTask scheduledTask;

    private void upgrade() {
        CmsVersion.setMaster(true);
        service.updateMaster(CmsVersion.getClusterId(), true);
    }

    /**
     * 每分钟心跳一次
     */
    @SuppressWarnings("unchecked")
    @Scheduled(fixedDelay = THEARTBEAT_INTERVAL)
    public void heartbeat() {
        if (CmsVersion.isInitialized()) {
            SysCluster entity = service.getEntity(CmsVersion.getClusterId());
            Date now = CommonUtils.getDate();
            Date lastHeartbeatDate = null;
            if (null == entity) {
                entity = new SysCluster(CmsVersion.getClusterId(), now, now, false, CmsVersion.getVersion(),
                        CmsVersion.getRevision());
                service.save(entity);
            } else {
                lastHeartbeatDate = entity.getHeartbeatDate();
                service.updateHeartbeatDate(CmsVersion.getClusterId(), now);
            }
            if (CmsVersion.isMaster() != entity.isMaster()) {
                CmsVersion.setMaster(entity.isMaster());
            }
            Date acceptTeartbeatDate = new Date(now.getTime() - (2 * THEARTBEAT_INTERVAL));
            if (entity.isMaster()) {
                PageHandler page = service.getPage(null, acceptTeartbeatDate, false, null, null, null, null);
                for (SysCluster cluster : (List<SysCluster>) page.getList()) {
                    service.delete(cluster.getUuid());
                }
            } else {
                PageHandler page = service.getPage(null, null, true, "heartbeatDate", "desc", null, null);
                if (page.getTotalCount() == 0) {
                    upgrade();
                } else if (page.getTotalCount() == 1) {
                    SysCluster master = (SysCluster) page.getList().get(0);
                    if (acceptTeartbeatDate.after(master.getHeartbeatDate())) {
                        upgrade();
                        service.delete(master.getUuid());
                    }
                } else {
                    boolean skip = false;
                    for (SysCluster cluster : (List<SysCluster>) page.getList()) {
                        if (skip) {
                            service.updateMaster(cluster.getUuid(), false);
                        } else {
                            skip = true;
                        }
                    }
                }
            }
            scheduledTask.init(lastHeartbeatDate);
        }
    }

    /**
     * 
     */
    @PreDestroy
    public void destroy() {
        if (CmsVersion.isInitialized()) {
            service.delete(CmsVersion.getClusterId());
        }
    }
}
