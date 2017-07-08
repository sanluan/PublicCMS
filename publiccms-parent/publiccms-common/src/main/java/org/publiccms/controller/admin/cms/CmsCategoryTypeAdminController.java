package org.publiccms.controller.admin.cms;

import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEquals;
import static com.publiccms.common.tools.ControllerUtils.verifyNotGreaterThen;
import static com.publiccms.common.tools.JsonUtils.getString;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.cms.CmsCategoryType;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysExtend;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.service.cms.CmsCategoryService;
import org.publiccms.logic.service.cms.CmsCategoryTypeService;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.sys.SysExtendFieldService;
import org.publiccms.logic.service.sys.SysExtendService;
import org.publiccms.views.pojo.CmsCategoryTypeParamters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * CmsCategoryTypeController
 *
 */
@Controller
@RequestMapping("cmsCategoryType")
public class CmsCategoryTypeAdminController extends AbstractController {
    @Autowired
    private CmsCategoryTypeService service;
    @Autowired
    private CmsCategoryService categoryService;
    @Autowired
    private SysExtendService extendService;
    @Autowired
    private SysExtendFieldService extendFieldService;

    private String[] ignoreProperties = new String[] { "id", "siteId", "extendId" };

    /**
     * @param entity
     * @param categoryTypeParamters
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("save")
    public String save(CmsCategoryType entity, @ModelAttribute CmsCategoryTypeParamters categoryTypeParamters,
            HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (null != entity.getId()) {
            CmsCategoryType oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (null != entity) {
                logOperateService.save(
                        new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                "update.categoryType", getIpAddress(request), getDate(), getString(entity)));
            }
        } else {
            entity.setSiteId(site.getId());
            service.save(entity);
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "save.categoryType", getIpAddress(request), getDate(), getString(entity)));
        }
        if (null == extendService.getEntity(entity.getExtendId())) {
            entity = service.updateExtendId(entity.getId(),
                    (Integer) extendService.save(new SysExtend("categoryType", entity.getId())));
        }
        extendFieldService.update(entity.getExtendId(), categoryTypeParamters.getCategoryExtends());// 修改或增加分类类型扩展字段
        return TEMPLATE_DONE;
    }

    /**
     * @param id
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("delete")
    public String delete(Integer id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        CmsCategoryType entity = service.getEntity(id);
        if (null != entity) {
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model) || verifyNotGreaterThen("category",
                    categoryService.getPage(site.getId(), null, true, id, null, null, false, null, 1).getTotalCount(), 1,
                    model)) {
                return TEMPLATE_ERROR;
            }
            service.delete(id);
            extendService.delete(entity.getExtendId());
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.categoryType", getIpAddress(request), getDate(), id.toString()));
        }
        return TEMPLATE_DONE;
    }
}