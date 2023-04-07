package com.publiccms.controller.web.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.base.AbstractTinymceController;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.LockComponent;
import com.publiccms.logic.service.log.LogLoginService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * TinymceController
 * 
 */
@Controller
@RequestMapping("tinymce")
public class TinymceController extends AbstractTinymceController {
    @Resource
    private LockComponent lockComponent;

    /**
     * @param site
     * @param user
     * @param file
     * @param request
     * @return view name
     */
    @RequestMapping("upload")
    @ResponseBody
    public Map<String, Object> upload(@RequestAttribute SysSite site, @SessionAttribute SysUser user, MultipartFile file,
            HttpServletRequest request) {
        boolean locked = lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD, String.valueOf(user.getId()),
                null);
        lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD, String.valueOf(user.getId()), null, true);

        Map<String, Object> messageMap = new HashMap<>();
        if (ControllerUtils.errorCustom("locked.user", locked, messageMap)) {
            Map<String, Object> result = new HashMap<>();
            result.put(CommonConstants.MESSAGE, LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(),
                    (String) messageMap.get(CommonConstants.ERROR)));
            result.put(CommonConstants.ERROR, 1);
            return result;
        } else if (ControllerUtils.errorCustom("locked.size",
                lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD_SIZE, String.valueOf(user.getId()), null),
                messageMap)) {
            Map<String, Object> result = new HashMap<>();
            result.put(CommonConstants.MESSAGE, LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(),
                    (String) messageMap.get(CommonConstants.ERROR)));
            result.put(CommonConstants.ERROR, 1);
            return result;
        }
        if (null != file) {
            lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD_SIZE, String.valueOf(user.getId()), null,
                    (int) file.getSize() / 1024);
        }
        return upload(site, user, file, LogLoginService.CHANNEL_WEB, request);
    }

    /**
     * @param site
     * @param user
     * @return view name
     */
    @RequestMapping("imageList")
    @ResponseBody
    public List<Map<String, String>> imageList(SysSite site, @SessionAttribute SysUser user) {
        return super.imageList(site, user);
    }
}