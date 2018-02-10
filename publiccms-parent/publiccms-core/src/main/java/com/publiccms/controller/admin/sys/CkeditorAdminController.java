package com.publiccms.controller.admin.sys;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.FileComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogUploadService;

/**
 *
 * CkeditorAdminController
 * 
 */
@Controller
@RequestMapping("ckeditor")
public class CkeditorAdminController extends AbstractController {
    @Autowired
    private FileComponent fileComponent;
    @Autowired
    protected LogUploadService logUploadService;

    private static final String RESULT_UPLOADED = "uploaded";
    private static final String RESULT_FILENAME = "fileName";
    private static final String RESULT_URL = "url";

    /**
     * @param upload
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("upload")
    public String upload(MultipartFile upload, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (null != upload && !upload.isEmpty()) {
            String originalName = upload.getOriginalFilename();
            String suffix = fileComponent.getSuffix(originalName);
            String fileName = fileComponent.getUploadFileName(suffix);
            try {
                fileComponent.upload(upload, siteComponent.getWebFilePath(site, fileName));
                logUploadService.save(
                        new LogUpload(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                false, upload.getSize(), RequestUtils.getIpAddress(request), CommonUtils.getDate(), fileName));
                Map<String, Object> map = getResultMap(true);
                map.put(RESULT_FILENAME, originalName);
                map.put(RESULT_URL, fileName);
                model.addAttribute("result", map);
            } catch (IllegalStateException | IOException e) {
                Map<String, Object> map = getResultMap(false);
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put(MESSAGE, e.getMessage());
                map.put(ERROR, messageMap);
                model.addAttribute("result", map);
            }
        } else {
            Map<String, Object> map = getResultMap(false);
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put(MESSAGE, "no file");
            map.put(ERROR, messageMap);
            model.addAttribute("result", map);
        }
        return "common/ckuploadResult";
    }

    private Map<String, Object> getResultMap(boolean success) {
        Map<String, Object> map = new HashMap<>();
        if (success) {
            map.put(RESULT_UPLOADED, 1);
        } else {
            map.put(RESULT_UPLOADED, 0);
        }
        return map;
    }
}