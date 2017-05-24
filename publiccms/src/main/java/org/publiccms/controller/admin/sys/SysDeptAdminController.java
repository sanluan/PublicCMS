package org.publiccms.controller.admin.sys;

import static com.publiccms.common.tools.JsonUtils.getString;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysDept;
import org.publiccms.entities.sys.SysDeptCategory;
import org.publiccms.entities.sys.SysDeptCategoryId;
import org.publiccms.entities.sys.SysDeptPage;
import org.publiccms.entities.sys.SysDeptPageId;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.sys.SysDeptCategoryService;
import org.publiccms.logic.service.sys.SysDeptPageService;
import org.publiccms.logic.service.sys.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * SysDeptAdminController
 * 
 */
@Controller
@RequestMapping("sysDept")
public class SysDeptAdminController extends AbstractController {
    @Autowired
    private SysDeptService service;
    @Autowired
    private SysDeptCategoryService sysDeptCategoryService;
    @Autowired
    private SysDeptPageService sysDeptPageService;

    private String[] ignoreProperties = new String[] { "id", "siteId" };

    /**
     * @param entity
     * @param categoryIds
     * @param pages
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("save")
    public String save(SysDept entity, Integer[] categoryIds, String[] pages, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        SysSite site = getSite(request);
        if (null != entity.getId()) {
            SysDept oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (null != entity) {
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.dept", getIpAddress(request), getDate(), getString(entity)));
            }
            sysDeptCategoryService.updateDeptCategorys(entity.getId(), categoryIds);
            sysDeptPageService.updateDeptPages(entity.getId(), pages);
        } else {
            entity.setSiteId(site.getId());
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.dept", getIpAddress(request), getDate(), getString(entity)));
            if (notEmpty(categoryIds)) {
                List<SysDeptCategory> list = new ArrayList<SysDeptCategory>();
                for (int categoryId : categoryIds) {
                    list.add(new SysDeptCategory(new SysDeptCategoryId(entity.getId(), categoryId)));
                }
                sysDeptCategoryService.save(list);
            }
            if (notEmpty(pages)) {
                List<SysDeptPage> list = new ArrayList<SysDeptPage>();
                for (String page : pages) {
                    list.add(new SysDeptPage(new SysDeptPageId(entity.getId(), page)));
                }
                sysDeptPageService.save(list);
            }
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param id
     * @param request
     * @param session
     * @return
     */
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