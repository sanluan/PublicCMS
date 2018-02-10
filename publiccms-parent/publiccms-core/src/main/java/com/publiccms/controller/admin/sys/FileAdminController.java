package com.publiccms.controller.admin.sys;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
 * FileAdminController
 *
 */
@Controller
@RequestMapping("file")
public class FileAdminController extends AbstractController {
    @Autowired
    private FileComponent fileComponent;
    @Autowired
    protected LogUploadService logUploadService;

    /**
     * @param file
     * @param field
     * @param originalField
     * @param onlyImage
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping(value = "doUpload", method = RequestMethod.POST)
    public String upload(MultipartFile file, String field, String originalField, Boolean onlyImage, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        SysSite site = getSite(request);
        if (null != file && !file.isEmpty()) {
			String originalName = file.getOriginalFilename();
			String suffix = fileComponent.getSuffix(originalName);
            String fileName = fileComponent.getUploadFileName(suffix);
            try {
                fileComponent.upload(file, siteComponent.getWebFilePath(site, fileName));
                model.put("field", field);
                model.put(field, fileName);
				if(CommonUtils.notEmpty(originalField)){
					model.put("originalField", originalField);
					model.put(originalField, originalName);
				}
                logUploadService.save(
                        new LogUpload(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                onlyImage, file.getSize(), RequestUtils.getIpAddress(request), CommonUtils.getDate(), fileName));
            } catch (IllegalStateException | IOException e) {
                log.error(e.getMessage(), e);
                return "common/uploadResult";
            }
        }
        return "common/uploadResult";
    }
}
