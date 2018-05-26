package com.publiccms.controller.web.cms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.views.directive.api.VoteDirective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.handler.HttpParameterHandler;
import com.publiccms.common.tools.ControllerUtils;

/**
 *
 * VoteController
 * 
 */
@Controller
@RequestMapping("vote")
@ResponseBody
public class VoteController extends AbstractController {

    @Autowired
    private VoteDirective voteDirective;

    /**
     * @param callback
     * @param request
     * @param session
     * @param response
     */
    @RequestMapping("vote")
    public void lottery(String callback, HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        try {
            HttpParameterHandler handler = new HttpParameterHandler(mappingJackson2HttpMessageConverter, CommonConstants.jsonMediaType, request,
                    callback, response);
            SysUser user = ControllerUtils.getUserFromSession(session);
            if (null != user) {
                voteDirective.execute(handler, null, user);
            }
            handler.render();
        } catch (Exception e) {
        }
    }
}
