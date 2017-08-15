package org.publiccms.controller.admin.home;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.JsonUtils.getString;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;
import static org.apache.commons.lang3.StringUtils.join;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.home.HomeComment;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.service.home.HomeCommentService;
import org.publiccms.logic.service.log.LogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * HomeCommentAdminController
 * 
 */
@Controller
@RequestMapping("homeComment")
public class HomeCommentAdminController extends AbstractController {

    private String[] ignoreProperties = new String[] { "id" };

    /**
     * @param entity
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("save")
    public String save(HomeComment entity, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        if (null != entity.getId()) {
            entity = service.update(entity.getId(), entity, ignoreProperties);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "update.homeComment", getIpAddress(request), getDate(),
                    getString(entity)));
        } else {
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.homeComment", getIpAddress(request), getDate(),
                    getString(entity)));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("delete")
    public String delete(Integer[] ids, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        if (notEmpty(ids)) {
            service.delete(ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.homeComment", getIpAddress(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    @Autowired
    private HomeCommentService service;
}