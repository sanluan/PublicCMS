package ${base}.${controllerPack};

// Generated ${.now} by com.publiccms.common.generator.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;
import static com.publiccms.common.tools.JsonUtils.getString;
import static org.apache.commons.lang3.StringUtils.join;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.publliccms.common.base.AbstractController;

<#include "../include_imports/entity.ftl">

<#include "../include_imports/service.ftl">
import org.publliccms.entities.sys.SysSite;
import org.publliccms.entities.log.LogOperate;
import org.publliccms.logic.service.log.LogLoginService;

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
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("save")
    public String save(${entityName} entity, HttpServletRequest request, HttpSession session) {
    	SysSite site = getSite(request);
        if (null != entity.getId()) {
            entity = service.update(entity.getId(), entity, ignoreProperties);
            logOperateService.save(
                        new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                "update.${entityName?uncap_first}", getIpAddress(request), getDate(), getString(entity)));
        } else {
            service.save(entity);
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "save.${entityName?uncap_first}", getIpAddress(request), getDate(), getString(entity)));
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
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.${entityName?uncap_first}", getIpAddress(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }
    
    @Autowired
    private ${entityName}Service service;
}