package com.publiccms.controller.admin.sys;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.annotation.Csrf;
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
 * FileAdminController
 *
 */
@Controller
@RequestMapping("file")
public class FileAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Autowired
    protected LogUploadService logUploadService;
    @Autowired
    protected SiteComponent siteComponent;

    /**
     * @param site
     * @param admin
     * @param file
     * @param field
     * @param originalField
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping(value = "doUpload", method = RequestMethod.POST)
    @Csrf
    public String upload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile file, String field,
            String originalField, HttpServletRequest request, ModelMap model) {
        if (null != file && !file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            String suffix = CmsFileUtils.getSuffix(originalName);
            if (ArrayUtils.contains(UeditorAdminController.ALLOW_FILES, suffix)) {
                String fileName = CmsFileUtils.getUploadFileName(suffix);
                try {
                    CmsFileUtils.upload(file, siteComponent.getWebFilePath(site, fileName));
                    model.put("field", field);
                    model.put(field, fileName);
                    String fileType = CmsFileUtils.getFileType(suffix);
                    model.put("fileType", fileType);
                    model.put("fileSize", file.getSize());
                    if (CommonUtils.notEmpty(originalField)) {
                        model.put("originalField", originalField);
                        model.put(originalField, originalName);
                    }
                    logUploadService.save(new LogUpload(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            originalName, fileType, file.getSize(), RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                            fileName));
                } catch (IllegalStateException | IOException e) {
                    log.error(e.getMessage(), e);
                    return "common/uploadResult";
                }
            } else {
                return "common/uploadResult";
            }
        }
        return "common/uploadResult";
    }
}
