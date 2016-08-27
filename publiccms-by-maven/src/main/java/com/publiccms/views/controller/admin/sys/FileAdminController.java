package com.publiccms.views.controller.admin.sys;

import static com.publiccms.logic.service.log.LogLoginService.CHANNEL_WEB_MANAGER;
import static com.sanluan.common.tools.RequestUtils.getIpAddress;

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
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.service.log.LogUploadService;

@Controller
@RequestMapping("file")
public class FileAdminController extends AbstractController {
    @Autowired
    private FileComponent fileComponent;
    @Autowired
    protected LogUploadService logUploadService;

    @RequestMapping(value = "doUpload", method = RequestMethod.POST)
    public String upload(MultipartFile file, String field, Boolean onlyImage, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        SysSite site = getSite(request);
        if (notEmpty(file) && !file.isEmpty()) {
            String fileName = fileComponent.getUploadFileName(fileComponent.getSuffix(file.getOriginalFilename()));
            try {
                fileComponent.upload(file, siteComponent.getResourceFilePath(site, fileName));
                model.put("field", field);
                model.put(field, fileName);
                logUploadService.save(new LogUpload(site.getId(), getAdminFromSession(session).getId(), CHANNEL_WEB_MANAGER,
                        onlyImage, file.getSize(), getIpAddress(request), getDate(), fileName));
            } catch (IllegalStateException | IOException e) {
                log.error(e.getMessage());
                return "common/uploadResult";
            }
        }
        return "common/uploadResult";
    }
}
