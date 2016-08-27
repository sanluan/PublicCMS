package com.publiccms.views.controller.admin.sys;

import static com.sanluan.common.tools.RequestUtils.getIpAddress;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysDeptCategoryService;
import com.publiccms.logic.service.sys.SysDeptPageService;
import com.publiccms.logic.service.sys.SysDeptService;

@Controller
@RequestMapping("sysDept")
public class SysDeptAdminController extends AbstractController {
    @Autowired
    private SysDeptService service;
    @Autowired
    private SysDeptCategoryService sysDeptCategoryService;
    @Autowired
    private SysDeptPageService sysDeptPageService;

    @RequestMapping("save")
    public String save(SysDept entity, Integer[] categoryIds, String[] pages, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (notEmpty(entity.getId())) {
            SysDept oldEntity = service.getEntity(entity.getId());
            if (empty(oldEntity) || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            entity = service.update(entity.getId(), entity, new String[] { "id", "siteId" });
            if (notEmpty(entity)) {
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.dept", getIpAddress(request), getDate(), entity.getId()
                                + ":" + entity.getName()));
            }
            sysDeptCategoryService.updateDeptCategorys(entity.getId(), categoryIds);
            sysDeptPageService.updateDeptPages(entity.getId(), pages);
        } else {
            entity.setSiteId(site.getId());
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.dept", getIpAddress(request), getDate(), entity.getId() + ":"
                            + entity.getName()));
            sysDeptCategoryService.saveDeptCategorys(entity.getId(), categoryIds);
            sysDeptPageService.saveDeptPages(entity.getId(), pages);
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping("delete")
    public String delete(Integer id, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        List<Integer> list = service.delete(site.getId(), id);
        if (0 < list.size()) {
            for (Integer childId : list) {
                sysDeptCategoryService.delete(childId, null);
                sysDeptPageService.delete(childId, null);
            }
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.dept", getIpAddress(request), getDate(), id.toString()));
        }
        return TEMPLATE_DONE;
    }
}