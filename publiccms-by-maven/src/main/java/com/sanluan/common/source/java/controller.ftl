package ${base}.${controllerPack};

// Generated ${.now} by com.sanluan.common.source.SourceMaker

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
<#include "../include_imports/entity.ftl">

<#include "../include_imports/service.ftl">

@Controller
@RequestMapping("${entityName?uncap_first}")
public class ${entityName}Controller extends AbstractController {

    @RequestMapping("save")
    public String save(${entityName} entity, HttpServletRequest request) {
        if (notEmpty(entity.getId())) {
            entity = service.update(entity.getId(), entity, new String[]{"id"});
        } else {
            service.save(entity);
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping("delete")
    public String delete(Integer id) {
        service.delete(id);
        return TEMPLATE_DONE;
    }
    
    @Autowired
    private ${entityName}Service service;
}