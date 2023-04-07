package com.publiccms.controller.admin.sys;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.base.AbstractCkEditorController;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.log.LogLoginService;

import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * CkeditorAdminController ck编辑器
 * 
 */
@Controller
@RequestMapping("ckeditor")
public class CkEditorAdminController extends AbstractCkEditorController {

    /**
     * @param site
     * @param admin
     * @param upload
     * @param ckCsrfToken
     * @param csrfToken
     * @param request
     * @return view name
     */
    @RequestMapping("upload")
    @ResponseBody
    public Map<String, Object> upload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile upload,
            String ckCsrfToken, @CookieValue("ckCsrfToken") String csrfToken, HttpServletRequest request) {
        return upload(site, admin, upload, ckCsrfToken, csrfToken, LogLoginService.CHANNEL_WEB_MANAGER, request);
    }
}