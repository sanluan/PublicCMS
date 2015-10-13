package com.publiccms.admin.views.controller.cms;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.entities.cms.CmsTagType;
import com.publiccms.logic.service.cms.CmsTagTypeService;
import com.sanluan.common.base.BaseController;
@Controller
@RequestMapping("cmsTagType")
public class CmsTagTypeController extends BaseController {
    @Autowired
    private CmsTagTypeService service;

    @RequestMapping(value = { SAVE })
    public String save(CmsTagType entity, HttpServletRequest request) {
        if (notEmpty(entity.getId())) {
            service.update(entity.getId(), entity, new String[]{ID});
        } else {
            service.save(entity);
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping(value = { DELETE })
    public String delete(Integer id) {
        service.delete(id);
        return TEMPLATE_DONE;
    }
}