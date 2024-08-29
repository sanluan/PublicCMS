package com.publiccms.controller.api;

import static com.publiccms.common.base.AbstractTemplateDirective.APP_TOKEN;
import static com.publiccms.common.base.AbstractTemplateDirective.AUTH_TOKEN;
import static com.publiccms.common.base.AbstractTemplateDirective.AUTH_USER_ID;

import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.LocaleResolver;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.HttpParameterHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JavaDocUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysAppToken;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.component.config.SafeConfigComponent;
import com.publiccms.logic.component.site.DirectiveComponent;
import com.publiccms.logic.component.site.FileUploadComponent;
import com.publiccms.logic.component.site.LockComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogUploadService;
import com.publiccms.logic.service.sys.SysAppService;
import com.publiccms.logic.service.sys.SysAppTokenService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;
import com.publiccms.views.pojo.entities.FileUploadResult;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * AppController 接口指令统一分发
 *
 */
@Controller
public class ApiController {
    protected final Log log = LogFactory.getLog(getClass());
    private Map<String, AbstractAppDirective> appDirectiveMap = new HashMap<>();
    private List<Map<String, String>> appList = new ArrayList<>();
    @Resource
    protected MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
    @Resource
    private LockComponent lockComponent;
    @Resource
    protected FileUploadComponent fileUploadComponent;
    @Resource
    private LocaleResolver localeResolver;
    @Resource
    private LogUploadService logUploadService;
    @Resource
    private SafeConfigComponent safeConfigComponent;
    @Resource
    private SysUserTokenService sysUserTokenService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysAppTokenService appTokenService;
    @Resource
    private SysAppService appService;
    /**
     *
     */
    public static final String INTERFACE_NOT_FOUND = "interfaceNotFound";
    /**
     *
     */
    public static final String NEED_APP_TOKEN = "needAppToken";
    /**
     *
     */
    public static final String UN_AUTHORIZED = "unAuthorized";
    /**
     *
     */
    public static final String NEED_LOGIN = "needLogin";
    /**
     *
     */
    public static final String EXCEPTION = "exception";
    /**
     *
     */
    protected static final Map<String, String> NOT_FOUND_MAP = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put(CommonConstants.ERROR, INTERFACE_NOT_FOUND);
        }
    };

    /**
     * 接口请求统一分发
     *
     * @return result
     */
    @RequestMapping({ Constants.SEPARATOR, "/**" })
    @ResponseBody
    public Map<String, String> api() {
        return NOT_FOUND_MAP;
    }

    /**
     * 接口指令统一分发
     *
     * @param api
     * @param appToken
     * @param authToken
     * @param authUserId
     * @param request
     * @param response
     */
    @RequestMapping("{api}")
    public void api(@PathVariable String api, @RequestHeader(required = false) String appToken,
            @RequestHeader(required = false) String authToken, @RequestHeader(required = false) Long authUserId,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            AbstractAppDirective directive = appDirectiveMap.get(api);
            if (null != directive) {
                if (directive.needAppToken() && null != appToken) {
                    request.setAttribute(APP_TOKEN, appToken);
                }
                if (directive.needUserToken()) {
                    if (null != authToken) {
                        request.setAttribute(AUTH_TOKEN, authToken);
                    }
                    if (null != authUserId) {
                        request.setAttribute(AUTH_USER_ID, authUserId);
                    }
                }
                directive.execute(mappingJackson2HttpMessageConverter, CommonConstants.jsonMediaType, request, response);
            } else {
                HttpParameterHandler handler = new HttpParameterHandler(mappingJackson2HttpMessageConverter,
                        CommonConstants.jsonMediaType, request, response);
                handler.put(CommonConstants.ERROR, INTERFACE_NOT_FOUND).render();
            }
        } catch (Exception e) {
            HttpParameterHandler handler = new HttpParameterHandler(mappingJackson2HttpMessageConverter,
                    CommonConstants.jsonMediaType, request, response);
            try {
                log.error(e.getMessage(), e);
                handler.put(CommonConstants.ERROR, e.getMessage()).render();
            } catch (Exception renderException) {
                log.error(renderException.getMessage());
            }
        }
    }

    /**
     * @param site
     * @param appToken
     * @param authToken
     * @param authUserId
     * @param user
     * @param privatefile
     * @param captcha
     * @param file
     * @param base64File
     * @param originalFilename
     * @param request
     * @return view name
     */
    @PostMapping("upload")
    @ResponseBody
    public Map<String, Object> upload(@RequestAttribute SysSite site, String appToken, String authToken, Long authUserId,
            boolean privatefile, MultipartFile file, String base64File, String originalFilename, HttpServletRequest request) {
        ModelMap result = new ModelMap();
        result.put("result", false);
        SysUserToken sysUserToken = sysUserTokenService.getEntity(authToken);
        SysAppToken token = appTokenService.getEntity(appToken);
        if (null != token && (null == token.getExpiryDate() || CommonUtils.getDate().before(token.getExpiryDate()))) {
            SysApp app = appService.getEntity(token.getAppId());
            if (app.getSiteId() == site.getId()) {
                if (null != sysUserToken
                        && (null == sysUserToken.getExpiryDate() || CommonUtils.getDate().before(sysUserToken.getExpiryDate()))
                        && authUserId.equals(sysUserToken.getUserId())) {
                    SysUser user = sysUserService.getEntity(sysUserToken.getUserId());
                    if (user.getSiteId() == site.getId() && !user.isDisabled()) {
                        boolean locked = lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD,
                                String.valueOf(authUserId), null);
                        boolean sizeLocked = lockComponent.isLocked(site.getId(),
                                privatefile ? LockComponent.ITEM_TYPE_FILEUPLOAD_PRIVATE_SIZE
                                        : LockComponent.ITEM_TYPE_FILEUPLOAD_SIZE,
                                String.valueOf(authUserId), null);
                        if (ControllerUtils.errorCustom("locked.user", locked, result)) {
                            lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD, String.valueOf(authUserId), null,
                                    true);
                            return result;
                        } else if (ControllerUtils.errorCustom("locked.user", sizeLocked, result)) {
                            return result;
                        }
                        lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_FILEUPLOAD, String.valueOf(authUserId), null,
                                true);
                        if (null != file && !file.isEmpty() || CommonUtils.notEmpty(base64File)) {
                            String originalName;
                            if (null != file && !file.isEmpty()) {
                                originalName = file.getOriginalFilename();
                            } else {
                                originalName = originalFilename;
                            }
                            String suffix = CmsFileUtils.getSuffix(originalName);
                            if (ArrayUtils.contains(
                                    privatefile ? CmsFileUtils.IMAGE_FILE_SUFFIXS : safeConfigComponent.getSafeSuffix(site),
                                    suffix)) {
                                try {
                                    FileUploadResult uploadResult = null;
                                    if (CommonUtils.notEmpty(base64File)) {
                                        uploadResult = fileUploadComponent.upload(site.getId(),
                                                VerificationUtils.base64Decode(base64File), privatefile, suffix,
                                                localeResolver.resolveLocale(request));
                                    } else {
                                        uploadResult = fileUploadComponent.upload(site.getId(), file, privatefile, suffix,
                                                localeResolver.resolveLocale(request));
                                    }
                                    lockComponent.lock(site.getId(),
                                            privatefile ? LockComponent.ITEM_TYPE_FILEUPLOAD_PRIVATE_SIZE
                                                    : LockComponent.ITEM_TYPE_FILEUPLOAD_SIZE,
                                            String.valueOf(authUserId), null, (int) uploadResult.getFileSize() / 1024);
                                    result.put("result", true);
                                    result.put("fileName", uploadResult.getFilename());
                                    String fileType = CmsFileUtils.getFileType(suffix);
                                    result.put("fileType", fileType);
                                    result.put("fileSize", uploadResult.getFileSize());
                                    logUploadService.save(new LogUpload(site.getId(), authUserId, LogLoginService.CHANNEL_WEB,
                                            originalName, privatefile, fileType, uploadResult.getFileSize(),
                                            uploadResult.getWidth(), uploadResult.getHeight(), RequestUtils.getIpAddress(request),
                                            CommonUtils.getDate(), uploadResult.getFilename()));
                                } catch (IOException e) {
                                    log.error(e.getMessage(), e);
                                    result.put(CommonConstants.ERROR, e.getMessage());
                                }
                            } else {
                                result.put(CommonConstants.ERROR, "verify.custom.fileType");
                            }
                        } else {
                            result.put(CommonConstants.ERROR, "verify.notEmpty.file");
                        }
                    } else {
                        result.put(CommonConstants.ERROR, ApiController.NEED_LOGIN);
                    }
                } else {
                    result.put(CommonConstants.ERROR, ApiController.NEED_LOGIN);
                }
            } else {
                result.put(CommonConstants.ERROR, ApiController.NEED_APP_TOKEN);
            }
        } else {
            result.put(CommonConstants.ERROR, ApiController.NEED_APP_TOKEN);
        }
        return result;
    }

    /**
     * 接口列表
     *
     * @return result
     */
    @RequestMapping("apis")
    @ResponseBody
    public List<Map<String, String>> apis() {
        return appList;
    }

    /**
     * 接口初始化
     *
     * @param directiveComponent
     * @param directiveList
     *
     */
    @Autowired(required = false)
    public void init(DirectiveComponent directiveComponent, List<AbstractAppDirective> directiveList) {
        for (AbstractAppDirective appDirective : directiveList) {
            if (null == appDirective.getName()) {
                appDirective.setName(directiveComponent.getDirectiveName(appDirective.getClass().getSimpleName()));
            }
            appDirectiveMap.put(appDirective.getName(), appDirective);

            Map<String, String> map = new HashMap<>();
            map.put("name", appDirective.getName());
            map.put("needAppToken", String.valueOf(appDirective.needAppToken()));
            map.put("needUserToken", String.valueOf(appDirective.needUserToken()));
            map.put("supportAdvanced", String.valueOf(false));
            map.put("doc", JavaDocUtils.getClassComment(appDirective.getClass().getName()));
            appList.add(map);
        }
        Collections.sort(appList, (o1, o2) -> Collator.getInstance().compare(o1.get("name"), o2.get("name")));
    }
}
