package com.publiccms.admin.views.controller.system;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.io.FileUtils.copyInputStreamToFile;
import static org.apache.commons.io.FileUtils.writeByteArrayToFile;
import static org.apache.commons.lang3.ArrayUtils.addAll;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.tools.UserUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.service.log.LogOperateService;
import com.sanluan.common.base.BaseController;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.tools.RequestUtils;

@Controller
@RequestMapping("ueditor")
public class UeditorController extends BaseController {
    @Autowired
    private FileComponent fileComponent;
    @Autowired
    private LogOperateService logOperateService;

    private static final String ACTION_CONFIT = "config";
    private static final String ACTION_UPLOAD = "upload";
    private static final String ACTION_UPLOAD_SCRAW = "uploadScraw";
    private static final String ACTION_CATCHIMAGE = "catchimage";
    private static final String ACTION_LISTFILE = "listfile";

    private static final String FIELD_NAME = "file";
    private static final String SCRAW_TYPE = ".jpg";

    private static final String[] IMAGE_ALLOW_FILES = new String[] { ".png", ".jpg", ".jpeg", ".gif", ".bmp" };

    private static final String[] VIDEO_ALLOW_FILES = new String[] { ".flv", ".swf", ".mkv", ".avi", ".rm", ".rmvb", ".mpeg",
            ".mpg", ".ogg", ".ogv", ".mov", ".wmv", ".mp4", ".webm", ".mp3", ".wav", ".mid" };
    private static final String[] ALLOW_FILES = addAll(addAll(VIDEO_ALLOW_FILES, IMAGE_ALLOW_FILES), new String[] { ".rar",
            ".zip", ".tar", ".gz", ".7z", ".bz2", ".cab", ".iso", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".pdf",
            ".txt", ".md", ".xml" });
    private static final Map<String, String> CONTENT_TYPE_MAP = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put("Content-Type: image/gif", ".gif");
            put("Content-Type: image/jpeg", ".jpg");
            put("Content-Type: image/jpg", ".jpg");
            put("Content-Type: image/png", ".png");
            put("Content-Type: image/bmp", ".bmp");
        }
    };

    @RequestMapping(params = "action=" + ACTION_CONFIT)
    @ResponseBody
    public Map<String, Object> config(HttpServletRequest request) throws IllegalStateException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("imageActionName", ACTION_UPLOAD);
        map.put("snapscreenActionName", ACTION_UPLOAD);
        map.put("scrawlActionName", ACTION_UPLOAD_SCRAW);
        map.put("videoActionName", ACTION_UPLOAD);
        map.put("fileActionName", ACTION_UPLOAD);
        map.put("catcherActionName", ACTION_CATCHIMAGE);
        map.put("imageManagerActionName", ACTION_LISTFILE);
        map.put("fileManagerActionName", ACTION_LISTFILE);
        map.put("imageFieldName", FIELD_NAME);
        map.put("scrawlFieldName", FIELD_NAME);
        map.put("catcherFieldName", FIELD_NAME);
        map.put("videoFieldName", FIELD_NAME);
        map.put("fileFieldName", FIELD_NAME);
        String urlPrefix = fileComponent.getUploadPath();
        map.put("imageUrlPrefix", urlPrefix);
        map.put("scrawlUrlPrefix", urlPrefix);
        map.put("snapscreenUrlPrefix", urlPrefix);
        map.put("catcherUrlPrefix", urlPrefix);
        map.put("videoUrlPrefix", urlPrefix);
        map.put("fileUrlPrefix", urlPrefix);
        map.put("imageManagerUrlPrefix", urlPrefix);
        map.put("fileManagerUrlPrefix", urlPrefix);
        map.put("imageAllowFiles", IMAGE_ALLOW_FILES);
        map.put("catcherAllowFiles", IMAGE_ALLOW_FILES);
        map.put("videoAllowFiles", VIDEO_ALLOW_FILES);
        map.put("fileAllowFiles", ALLOW_FILES);
        map.put("imageManagerAllowFiles", IMAGE_ALLOW_FILES);
        map.put("fileManagerAllowFiles", ALLOW_FILES);
        return map;
    }

    @RequestMapping(params = "action=" + ACTION_UPLOAD)
    @ResponseBody
    public Map<String, Object> upload(MultipartFile file, HttpServletRequest request, HttpSession session)
            throws IllegalStateException, IOException {
        if (!file.isEmpty()) {
            String suffix = fileComponent.getSuffix(file.getOriginalFilename());
            String fileName = fileComponent.getUploadFileName(suffix);
            try {
                String name = fileComponent.upload(file, fileName);
                logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(),
                        LogOperateService.OPERATE_UPLOADFILE, RequestUtils.getIp(request), getDate(), fileName));
                Map<String, Object> map = getResultMap(true);
                map.put("size", file.getSize());
                map.put("title", name);
                map.put("url", fileName);
                map.put("type", suffix);
                map.put("original", file.getOriginalFilename());
                return map;
            } catch (IllegalStateException | IOException e) {
                log.debug(e.getMessage());
            }
        }
        return getResultMap(false);
    }

    @RequestMapping(params = "action=" + ACTION_UPLOAD_SCRAW)
    @ResponseBody
    public Map<String, Object> uploadScraw(String file, HttpServletRequest request, HttpSession session)
            throws IllegalStateException, IOException {
        if (null != file) {
            byte[] data = decodeBase64(file);
            String fileName = fileComponent.getUploadFileName(SCRAW_TYPE);
            try {
                File dest = new File(fileComponent.getUploadFilePath(fileName));
                writeByteArrayToFile(dest, data);

                logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(),
                        LogOperateService.OPERATE_UPLOADFILE, RequestUtils.getIp(request), getDate(), fileName));
                Map<String, Object> map = getResultMap(true);
                map.put("size", data.length);
                map.put("title", dest.getName());
                map.put("url", fileName);
                map.put("type", SCRAW_TYPE);
                map.put("original", "scraw" + SCRAW_TYPE);
                return map;
            } catch (IllegalStateException | IOException e) {
                log.debug(e.getMessage());
            }
        }
        return getResultMap(false);
    }

    @RequestMapping(params = "action=" + ACTION_CATCHIMAGE)
    @ResponseBody
    public Map<String, Object> catchimage(HttpServletRequest request, HttpSession session) throws IllegalStateException,
            IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            String[] file = request.getParameterValues(FIELD_NAME + "[]");
            if (null != file) {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                for (String image : file) {
                    HttpGet httpget = new HttpGet(image);
                    CloseableHttpResponse response = httpclient.execute(httpget);
                    try {
                        HttpEntity entity = response.getEntity();
                        if (null != entity) {
                            String suffix = CONTENT_TYPE_MAP.get(entity.getContentType());
                            if (null == suffix)
                                suffix = ".jpg";
                            String fileName = fileComponent.getUploadFileName(suffix);
                            File dest = new File(fileComponent.getUploadFilePath(fileName));
                            copyInputStreamToFile(entity.getContent(), dest);
                            EntityUtils.consume(entity);
                            logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(),
                                    LogOperateService.OPERATE_UPLOADFILE, RequestUtils.getIp(request), getDate(), fileName));
                            Map<String, Object> map = getResultMap(true);
                            map.put("size", entity.getContentLength());
                            map.put("title", dest.getName());
                            map.put("url", fileName);
                            map.put("source", image);
                            list.add(map);
                        }
                    } catch (Exception e) {
                        log.debug(e.getMessage());
                    } finally {
                        try {
                            response.close();
                        } catch (IOException e) {
                            log.debug(e.getMessage());
                        }
                    }
                }
                Map<String, Object> map = getResultMap(true);
                map.put("list", list);
                return map;
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                log.debug(e.getMessage());
            }
        }
        return getResultMap(false);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(params = "action=" + ACTION_LISTFILE)
    @ResponseBody
    public Map<String, Object> listfile(String file, Integer start, HttpSession session) throws IllegalStateException,
            IOException {
        if (null == start) {
            start = 0;
        }

        PageHandler page = logOperateService.getPage(null, LogOperateService.OPERATE_UPLOADFILE, null, UserUtils
                .getAdminFromSession(session).getId(), null, null, null, null, start / 20 + 1, 20);

        Map<String, Object> map = getResultMap(true);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (LogOperate logOperate : ((List<LogOperate>) page.getList())) {
            Map<String, Object> tempMap = getResultMap(true);
            tempMap.put("url", logOperate.getContent());
            list.add(tempMap);
        }
        map.put("list", list);
        map.put("start", start);
        map.put("total", page.getTotalCount());
        return map;
    }

    private Map<String, Object> getResultMap(boolean success) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (success) {
            map.put("state", "SUCCESS");
        } else {
            map.put("state", "error");
        }
        return map;
    }
}
