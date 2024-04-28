package com.publiccms.controller.admin.sys;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.base.AbstractTinymceController;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.log.LogLoginService;

/**
 * TinymceAdminController tinymce编辑器
 * 
 */
@Controller
@RequestMapping("tinymce")
public class TinymceAdminController extends AbstractTinymceController {
    
    /**
     * @param site
     * @param admin
     * @param file
     * @param request
     * @return view name
     */
    @RequestMapping("upload")
    @ResponseBody
    public Map<String, Object> upload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile file,
            HttpServletRequest request) {
        return upload(site, admin, file, LogLoginService.CHANNEL_WEB_MANAGER, request);
    }

    /**
     * @param site
     * @param admin
     * @return view name
     */
    @RequestMapping("imageList")
    @ResponseBody
    public List<Map<String, String>> imageList(SysSite site, @SessionAttribute SysUser admin) {
        return super.imageList(site, admin);
    }
}