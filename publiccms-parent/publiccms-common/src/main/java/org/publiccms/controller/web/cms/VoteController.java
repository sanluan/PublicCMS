package org.publiccms.controller.web.cms;

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.sys.SysUser;
import org.publiccms.views.directive.api.VoteDirective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.handler.HttpParameterHandler;

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
