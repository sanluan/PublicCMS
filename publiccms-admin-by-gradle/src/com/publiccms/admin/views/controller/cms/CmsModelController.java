package com.publiccms.admin.views.controller.cms;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.tools.UserUtils;
import com.publiccms.entities.cms.CmsModel;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.logic.component.ExtendComponent;
import com.publiccms.logic.service.cms.CmsModelService;
import com.publiccms.logic.service.log.LogOperateService;
import com.sanluan.common.base.BaseController;
import com.sanluan.common.tools.RequestUtils;

@Controller
@RequestMapping("cmsModel")
public class CmsModelController extends BaseController {
    @Autowired
    private CmsModelService service;
    @Autowired
    private ExtendComponent extendComponent;
    @Autowired
    private LogOperateService logOperateService;

    private List<String> systemExtendList = Arrays.asList(new String[] { "modelExtend1", "modelExtend2", "modelExtend3",
            "modelExtend4" });

    @RequestMapping(SAVE)
    public String save(CmsModel entity, HttpServletRequest request, HttpSession session) {
        if (notEmpty(entity.getId())) {
            entity = service.update(entity.getId(), entity, new String[] { ID, "disabled" });
            if (notEmpty(entity)) {
                logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "update.model",
                        RequestUtils.getIp(request), getDate(), entity.getId() + ":" + entity.getName()));
            }
        } else {
            entity = service.save(entity);
            if (notEmpty(entity)) {
                logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "save.model", RequestUtils
                        .getIp(request), getDate(), entity.getId() + ":" + entity.getName()));
            }
        }
        extendComponent.updateExtend(ExtendComponent.ITEM_TYPE_MODEL, entity.getId(), ExtendComponent.EXTEND_TYPE_CONTENT,
                systemExtendList, request.getParameterMap());
        extendComponent.saveExtend(ExtendComponent.ITEM_TYPE_MODEL, entity.getId(), systemExtendList, request.getParameterMap());
        return TEMPLATE_DONE;
    }

    @RequestMapping(DELETE)
    public String delete(Integer id, HttpServletRequest request, HttpSession session) {
        CmsModel entity = service.delete(id);
        if (notEmpty(entity)) {
            logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "delete.model", RequestUtils
                    .getIp(request), getDate(), id + ":" + entity.getName()));
        }
        return TEMPLATE_DONE;
    }
}