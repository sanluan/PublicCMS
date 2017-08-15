package org.publiccms.controller.admin.cms;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.cms.CmsLotteryUser;
import org.publiccms.logic.service.cms.CmsLotteryUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 *
 * CmsLotteryUserAdminController
 * 
 */
@Controller
@RequestMapping("cmsLotteryUser")
public class CmsLotteryUserAdminController extends AbstractController {

	private String[] ignoreProperties = new String[]{"id"};

    /**
     * @param entity
     * @return
     */
    @RequestMapping("save")
    public String save(CmsLotteryUser entity) {
        if (null != entity.getId()) {
            entity = service.update(entity.getId(), entity, ignoreProperties);
        } else {
            service.save(entity);
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param id
     * @return
     */
    @RequestMapping("delete")
    public String delete(Integer id) {
        service.delete(id);
        return TEMPLATE_DONE;
    }
    
    @Autowired
    private CmsLotteryUserService service;
}