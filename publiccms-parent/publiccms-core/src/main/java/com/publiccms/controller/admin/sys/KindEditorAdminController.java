package com.publiccms.controller.admin.sys;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogUploadService;

/**
 *
 * CkeditorAdminController
 * 
 */
@Controller
@RequestMapping("kindeditor")
public class KindEditorAdminController {
    @Autowired
    protected LogUploadService logUploadService;
    @Autowired
    protected SiteComponent siteComponent;

    private static final String RESULT_URL = "url";

    /**
     * @param site
     * @param admin
     * @param imgFile
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("upload")
    public String upload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile imgFile,
            HttpServletRequest request, ModelMap model) {
        if (null != imgFile && !imgFile.isEmpty()) {
            String originalName = imgFile.getOriginalFilename();
            String suffix = CmsFileUtils.getSuffix(originalName);
            if (ArrayUtils.contains(UeditorAdminController.ALLOW_FILES, suffix)) {
                String fileName = CmsFileUtils.getUploadFileName(suffix);
                try {
                    CmsFileUtils.upload(imgFile, siteComponent.getWebFilePath(site, fileName));
                    logUploadService.save(new LogUpload(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            originalName, CmsFileUtils.getFileType(suffix), imgFile.getSize(), RequestUtils.getIpAddress(request),
                            CommonUtils.getDate(), fileName));
                    Map<String, Object> map = getResultMap(true);
                    map.put(RESULT_URL, fileName);
                    model.addAttribute("result", map);
                } catch (IllegalStateException | IOException e) {
                    Map<String, Object> map = getResultMap(false);
                    map.put(CommonConstants.MESSAGE, e.getMessage());
                    model.addAttribute("result", map);
                }
            } else {
                Map<String, Object> map = getResultMap(false);
                map.put(CommonConstants.MESSAGE, "unsafe file");
                model.addAttribute("result", map);
            }
        } else {
            Map<String, Object> map = getResultMap(false);
            map.put(CommonConstants.MESSAGE, "no file");
            model.addAttribute("result", map);
        }
        return "common/kinduploadResult";
    }

    private static Map<String, Object> getResultMap(boolean success) {
        Map<String, Object> map = new HashMap<>();
        if (success) {
            map.put(CommonConstants.ERROR, 0);
        } else {
            map.put(CommonConstants.ERROR, 1);
        }
        return map;
    }
}