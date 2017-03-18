package com.publiccms.controller.web.home;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.home.HomeUser;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.home.HomeUserService;

/***
 * 
 * @author wangyadong
 *
 */
@Controller
@RequestMapping("homeUser")
public class HomeUserController extends AbstractController {

    private String[] ignoreProperties = new String[] { "id" };

    @RequestMapping("save")
    @ResponseBody
    public String save(HomeUser entity, HttpServletRequest request, HttpSession session) {

        if (null == getUserFromSession(session))
            return "";

        SysSite site = getSite(request);
        if (notEmpty(entity.getUserId())) {
            entity = service.update(entity.getUserId(), entity, ignoreProperties);
        } else {
            entity.setSiteId(site.getId());
            entity.setCreateDate(new Date());
            service.save(entity);
        }
        return SUCCESS;
    }

    @Autowired
    private HomeUserService service;
}