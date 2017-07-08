package org.publiccms.controller.admin.sys;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;
import static org.publiccms.logic.service.log.LogLoginService.CHANNEL_WEB_MANAGER;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.log.LogUpload;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.component.site.FileComponent;
import org.publiccms.logic.service.log.LogUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

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
     * @param onlyImage
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "doUpload", method = RequestMethod.POST)
    public String upload(MultipartFile file, String field, Boolean onlyImage, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        SysSite site = getSite(request);
        if (null != file && !file.isEmpty()) {
            String fileName = fileComponent.getUploadFileName(fileComponent.getSuffix(file.getOriginalFilename()));
            try {
                fileComponent.upload(file, siteComponent.getWebFilePath(site, fileName));
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
