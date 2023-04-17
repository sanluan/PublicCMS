package com.publiccms.controller.web.sys;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.base.AbstractUeditorController;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.LockComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.views.pojo.entities.FileUploadResult;
import com.publiccms.views.pojo.entities.UeditorConfig;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * UeditorController
 * 
 */
@Controller
@RequestMapping("ueditor")
public class UeditorController extends AbstractUeditorController {

    @Resource
    private LockComponent lockComponent;

    /**
     * @param site
     * @return view name
     */
    @RequestMapping(params = "action=" + ACTION_CONFIG)
    @ResponseBody
    public UeditorConfig config(@RequestAttribute SysSite site) {
        String urlPrefix = fileUploadComponent.getPrefix(site);
        UeditorConfig config = new UeditorConfig();
        config.setImageActionName(ACTION_UPLOAD);
        config.setSnapscreenActionName(ACTION_UPLOAD);
        config.setScrawlActionName(ACTION_UPLOAD_SCRAW);
        config.setVideoActionName(ACTION_UPLOAD);
        config.setFileActionName(ACTION_UPLOAD);
        config.setImageManagerActionName(ACTION_LISTIMAGE);
        config.setFileManagerActionName(ACTION_LISTFILE);
        config.setVideoManagerActionName(ACTION_LISTVIDEO);
        config.setImageFieldName(FIELD_NAME);
        config.setScrawlFieldName(FIELD_NAME);
        config.setCatchRemoteImageEnable(false);
        config.setVideoFieldName(FIELD_NAME);
        config.setFileFieldName(FIELD_NAME);
        config.setImageUrlPrefix(urlPrefix);
        config.setScrawlUrlPrefix(urlPrefix);
        config.setSnapscreenUrlPrefix(urlPrefix);
        config.setVideoUrlPrefix(urlPrefix);
        config.setFileUrlPrefix(urlPrefix);
        config.setImageManagerUrlPrefix(urlPrefix);
        config.setVideoManagerUrlPrefix(urlPrefix);
        config.setFileManagerUrlPrefix(urlPrefix);
        config.setImageAllowFiles(CmsFileUtils.IMAGE_FILE_SUFFIXS);
        config.setVideoAllowFiles(CmsFileUtils.VIDEO_FILE_SUFFIXS);
        config.setFileAllowFiles(safeConfigComponent.getSafeSuffix(site));
        config.setImageManagerAllowFiles(CmsFileUtils.IMAGE_FILE_SUFFIXS);
        config.setFileManagerAllowFiles(safeConfigComponent.getSafeSuffix(site));
        return config;
    }

    /**
     * @param site
     * @param user
     * @param file
     * @param request
     * @return view name
     */
    @RequestMapping(params = "action=" + ACTION_UPLOAD)
    @ResponseBody
    public Map<String, Object> upload(@RequestAttribute SysSite site, @SessionAttribute SysUser user, MultipartFile file,
            HttpServletRequest request) {
        boolean locked = lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD, String.valueOf(user.getId()),
                null);
        lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD, String.valueOf(user.getId()), null, true);

        ModelMap messageMap = new ModelMap();
        if (ControllerUtils.errorCustom("locked.user", locked, messageMap) || ControllerUtils.errorCustom("locked.size",
                lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD_SIZE, String.valueOf(user.getId()), null),
                messageMap)) {
            return getResultMap(false, LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(),
                    (String) messageMap.get(CommonConstants.ERROR)));
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
     * @param file
     * @param request
     * @return view name
     */
    @RequestMapping(params = "action=" + ACTION_UPLOAD_SCRAW)
    @ResponseBody
    public Map<String, Object> uploadScraw(@RequestAttribute SysSite site, @SessionAttribute SysUser user, String file,
            HttpServletRequest request) {

        boolean locked = lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD, String.valueOf(user.getId()),
                null);
        lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD, String.valueOf(user.getId()), null, true);

        ModelMap messageMap = new ModelMap();
        if (ControllerUtils.errorCustom("locked.user", locked, messageMap) || ControllerUtils.errorCustom("locked.size",
                lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD_SIZE, String.valueOf(user.getId()), null),
                messageMap)) {
            return getResultMap(false, LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(),
                    (String) messageMap.get(CommonConstants.ERROR)));
        }
        if (CommonUtils.notEmpty(file)) {
            byte[] data = VerificationUtils.base64Decode(file);
            String fileName = CmsFileUtils.getUploadFileName(SCRAW_TYPE);
            String filepath = siteComponent.getWebFilePath(site.getId(), fileName);
            try {
                CmsFileUtils.writeByteArrayToFile(filepath, data);
                FileUploadResult uploadResult = CmsFileUtils.getFileSize(filepath, fileName, SCRAW_TYPE);
                lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD_SIZE, String.valueOf(user.getId()), null,
                        (int) uploadResult.getFileSize() / 1024);
                logUploadService.save(new LogUpload(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, Constants.BLANK,
                        false, CmsFileUtils.FILE_TYPE_IMAGE, data.length, uploadResult.getWidth(), uploadResult.getHeight(),
                        RequestUtils.getIpAddress(request), CommonUtils.getDate(), fileName));
                Map<String, Object> map = getResultMap();
                map.put("size", data.length);
                map.put("title", fileName);
                map.put("url", fileName);
                map.put("type", SCRAW_TYPE);
                map.put("original", "scraw" + SCRAW_TYPE);
                return map;
            } catch (IllegalStateException | IOException e) {
                log.error(e.getMessage(), e);
                return getResultMap(false, e.getMessage());
            }
        } else {
            return getResultMap(false,
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(), "verify.notEmpty.file"));
        }

    }

    /**
     * @param site
     * @param user
     * @param start
     * @return view name
     */
    @RequestMapping(params = { "action=" + ACTION_LISTIMAGE })
    @ResponseBody
    public Map<String, Object> listimage(@RequestAttribute SysSite site, @SessionAttribute SysUser user, Integer start) {
        return listfile(site, user, CmsFileUtils.IMAGE_FILETYPES, start);
    }

    /**
     * @param site
     * @param user
     * @param start
     * @return view name
     */
    @RequestMapping(params = { "action=" + ACTION_LISTFILE })
    @ResponseBody
    public Map<String, Object> listfile(@RequestAttribute SysSite site, @SessionAttribute SysUser user, Integer start) {
        return listfile(site, user, null, start);
    }

    /**
     * @param site
     * @param user
     * @param start
     * @return view name
     */
    @RequestMapping(params = { "action=" + ACTION_LISTVIDEO })
    @ResponseBody
    public Map<String, Object> listvideo(@RequestAttribute SysSite site, @SessionAttribute SysUser user, Integer start) {
        return listfile(site, user, CmsFileUtils.VIDEO_FILETYPES, start);
    }

}