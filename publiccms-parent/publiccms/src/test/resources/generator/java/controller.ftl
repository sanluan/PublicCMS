package ${base}.${controllerPack};

// Generated ${.now} by com.publiccms.common.generator.SourceGenerator

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.base.AbstractController;
import com.publiccms.common.constants.CommonConstants;

<#include "../include_imports/entity.ftl">
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.logic.service.log.LogLoginService;
<#include "../include_imports/service.ftl">

/**
 *
 * ${entityName}${controllerSuffix}
 * 
 */
@Controller
@RequestMapping("${entityName?uncap_first}")
public class ${entityName}${controllerSuffix} extends AbstractController {

    private String[] ignoreProperties = new String[]{"id"};
    
    /**
     * @param entity
     * @param _csrf
     * @param request
     * @param session
     * @return operate result
     */
    @RequestMapping("save")
    public String save(${entityName} entity, String _csrf, HttpServletRequest request, HttpSession session,
            ModelMap model) {
    	if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
                return CommonConstants.TEMPLATE_ERROR;
        }
        SysSite site = getSite(request);
        if (null != entity.getId()) {
            entity = service.update(entity.getId(), entity, ignoreProperties);
            logOperateService.save(
                        new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                "update.${entityName?uncap_first}", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        } else {
            service.save(entity);
            logOperateService
                    .save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "save.${entityName?uncap_first}", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param _csrf
     * @param request
     * @param session
     * @return operate result
     */
    @RequestMapping("delete")
    public String delete(Integer[] ids, String _csrf, HttpServletRequest request, HttpSession session,
            ModelMap model) {
    	if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
                return CommonConstants.TEMPLATE_ERROR;
        }
        SysSite site = getSite(request);
        if (CommonUtils.notEmpty(ids)) {
            service.delete(ids);
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.${entityName?uncap_first}", RequestUtils.getIpAddress(request), CommonUtils.getDate(), StringUtils.join(ids, ',')));
        }
        return CommonConstants.TEMPLATE_DONE;
    }
    
    @Autowired
    private ${entityName}Service service;
}