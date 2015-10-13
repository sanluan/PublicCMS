package com.publiccms.admin.views.controller.cms;

import static com.sanluan.common.tools.RequestUtils.getValue;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.tools.UserUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsModel;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.logic.component.ExtendComponent;
import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.component.FileComponent.StaticResult;
import com.publiccms.logic.service.cms.CmsCategoryAttributeService;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsModelService;
import com.publiccms.logic.service.log.LogOperateService;
import com.sanluan.common.base.BaseController;
import com.sanluan.common.tools.RequestUtils;

@Controller
@RequestMapping("cmsCategory")
public class CmsCategoryController extends BaseController {
    @Autowired
    private CmsCategoryService service;
    @Autowired
    private CmsCategoryAttributeService attributeService;
    @Autowired
    private CmsModelService modelService;
    @Autowired
    private CmsCategoryModelService categoryModelService;
    @Autowired
    private ExtendComponent extendComponent;
    @Autowired
    private FileComponent fileComponent;
    @Autowired
    private LogOperateService logOperateService;

    private List<String> systemExtendList = Arrays.asList(new String[] { "extend1", "extend2", "extend3", "extend4" });

    @RequestMapping(SAVE)
    public String save(CmsCategory entity, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (notEmpty(entity.getId())) {
            CmsCategory oldEntity = service.getEntity(entity.getId());
            entity = service.update(entity.getId(), entity, new String[] { "childIds", "url", "disabled", "contents" });
            if (oldEntity.getParentId() != entity.getParentId()) {
                service.generateChildIds(oldEntity.getParentId());
                service.generateChildIds(entity.getParentId());
            }
            if (notEmpty(entity)) {
                logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "update.category",
                        RequestUtils.getIp(request), getDate(), entity.getId() + ":" + entity.getName()));
            }
        } else {
            entity = service.save(entity);
            service.addChildIds(entity.getParentId(), entity.getId());
            if (notEmpty(entity)) {
                logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "save.category",
                        RequestUtils.getIp(request), getDate(), entity.getId() + ":" + entity.getName()));
            }
        }

        Map<String, String[]> parameterMap = request.getParameterMap();

        @SuppressWarnings("unchecked")
        List<CmsModel> modelList = (List<CmsModel>) modelService.getPage(null, null, null, null, null, false, null, null)
                .getList();
        for (CmsModel cmsmodel : modelList) {
            categoryModelService.updateCategoryModel(notEmpty(getValue(parameterMap, "model_" + cmsmodel.getId())),
                    entity.getId(), cmsmodel, parameterMap);
        }

        extendComponent.updateExtend(ExtendComponent.ITEM_TYPE_CATEGORY, entity.getId(), ExtendComponent.EXTEND_TYPE_CATEGORY,
                systemExtendList, parameterMap);
        extendComponent.updateExtend(ExtendComponent.ITEM_TYPE_CATEGORY, entity.getId(), ExtendComponent.EXTEND_TYPE_CONTENT,
                systemExtendList, parameterMap);
        extendComponent.saveExtend(ExtendComponent.ITEM_TYPE_CATEGORY, entity.getId(), systemExtendList, parameterMap);

        attributeService.updateAttribute(entity.getId(), extendComponent.dealCategoryExtent(entity.getId(), parameterMap));
        publish(entity.getId(), request, session, model);
        return TEMPLATE_DONE;
    }

    @RequestMapping(value = { "static" })
    public String publish(Integer id, HttpServletRequest request, HttpSession session, ModelMap model) {
        CmsCategory entity = service.getEntity(id);
        if (notEmpty(entity)) {
            if (isNotBlank(entity.getPath())) {
                StaticResult result = fileComponent.createCategoryHtml(entity, entity.getTemplatePath(), entity.getPath());
                if (virifyCustom("static", !result.getResult(), model)) {
                    return TEMPLATE_ERROR;
                } else {
                    entity = service.updateUrl(entity.getId(), result.getFilePath());
                    logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "static", RequestUtils
                            .getIp(request), getDate(), entity.getUrl()));
                }
            }
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping(DELETE)
    public String delete(Integer id, HttpServletRequest request, HttpSession session) {
        CmsCategory entity = service.delete(id);
        if (notEmpty(entity)) {
            logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "delete.category", RequestUtils
                    .getIp(request), getDate(), id + ":" + entity.getName()));
        }
        return TEMPLATE_DONE;
    }
}