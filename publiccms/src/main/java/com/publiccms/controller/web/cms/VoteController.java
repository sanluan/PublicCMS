package com.publiccms.controller.web.cms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.views.directive.api.VoteDirective;
import com.sanluan.common.handler.HttpParameterHandler;

@Controller
@RequestMapping("vote")
@ResponseBody
public class VoteController extends AbstractController {
    @Autowired
    private VoteDirective voteDirective;

    @RequestMapping("vote")
    public void lottery(String callback, HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        try {
            HttpParameterHandler handler = new HttpParameterHandler(mappingJackson2HttpMessageConverter, jsonMediaType, request,
                    callback, response);
            SysUser user = getUserFromSession(session);
            if (notEmpty(user)) {
                voteDirective.execute(handler, null, user);
            }
            handler.render();
        } catch (Exception e) {
        }
    }
}
