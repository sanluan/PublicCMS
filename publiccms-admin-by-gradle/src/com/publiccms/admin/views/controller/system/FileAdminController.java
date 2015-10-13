package com.publiccms.admin.views.controller.system;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.tools.UserUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.service.log.LogOperateService;
import com.sanluan.common.base.BaseController;
import com.sanluan.common.tools.RequestUtils;

@Controller
@RequestMapping("file")
public class FileAdminController extends BaseController {
    @Autowired
    private FileComponent fileComponent;
    @Autowired
    private LogOperateService logOperateService;

    @RequestMapping(value = { "upload" }, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> upload(MultipartFile file, String field, HttpServletRequest request, HttpSession session) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (!file.isEmpty()) {
            String fileName = fileComponent.getUploadFileName(fileComponent.getSuffix(file.getOriginalFilename()));
            try {
                fileComponent.upload(file, fileName);
                map.put(field, fileName);
                logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(),
                        LogOperateService.OPERATE_UPLOADFILE, RequestUtils.getIp(request), getDate(), fileName));
            } catch (IllegalStateException | IOException e) {
                log.debug(e.getMessage());
            }
        }
        return map;
    }
}
