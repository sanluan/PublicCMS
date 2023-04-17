package com.publiccms.controller.admin.oss;

import java.security.InvalidKeyException;
import java.text.DateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.DateFormatUtils;
import com.publiccms.common.tools.OSSUtils;
import com.publiccms.common.tools.PolicyConditions;
import com.publiccms.common.tools.PolicyConditions.MatchMode;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.OSSComponent;
import com.publiccms.logic.component.OSSFileUploaderComponent;
import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogUploadService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import software.amazon.awssdk.utils.BinaryUtils;

@Controller
@RequestMapping("common")
public class OSSFileAdminController {
    @Resource
    private ConfigDataComponent configDataComponent;
    @Resource
    private OSSComponent ossComponent;
    @Resource
    protected LogUploadService logUploadService;

    @RequestMapping(value = { "uploadresult.html" })
    public String uploadresult(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, boolean privatefile,
            String originalName, String filepath, HttpServletRequest request) {
        String suffix = CmsFileUtils.getSuffix(filepath);
        String fileType = CmsFileUtils.getFileType(suffix);
        logUploadService.save(new LogUpload(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, originalName,
                privatefile, fileType, 0, 0, 0, RequestUtils.getIpAddress(request), CommonUtils.getDate(), filepath));
        return "common/uploadresult";
    }

    @RequestMapping(value = { "beforeupload" })
    @ResponseBody
    @Csrf
    public Map<String, String> beforeupload(@RequestAttribute SysSite site, boolean privatefile, String field, String filename,
            HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        Map<String, String> config = configDataComponent.getConfigData(site.getId(), OSSComponent.CONFIG_CODE);
        String suffix = CmsFileUtils.getSuffix(filename);
        String newfilename = CmsFileUtils.getUploadFileName(suffix);

        PolicyConditions policyConds = new PolicyConditions();
        policyConds.addConditionItem(PolicyConditions.COND_BUCKET,
                config.get(privatefile ? OSSComponent.CONFIG_PRIVATE_BUCKET : OSSComponent.CONFIG_BUCKET));
        policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, CmsFileUtils.UPLOAD_PATH);
        String acl = privatefile ? "authenticated-read" : "public-read";
        policyConds.addConditionItem(PolicyConditions.COND_ACL, acl);
        String returnUrl;
        if (site.getDynamicPath().startsWith("//")) {
            returnUrl = CommonUtils.joinString(request.getScheme(), ":", site.getDynamicPath(),
                    ossComponent.getAdminContextPath().substring(1), "/common/uploadresult.html?field=",
                    CommonUtils.encodeURI(field), "&privatefile=", privatefile, "&originalName=", CommonUtils.encodeURI(filename),
                    "&filepath=", CommonUtils.encodeURI(newfilename));
        } else {
            returnUrl = CommonUtils.joinString(site.getDynamicPath(), ossComponent.getAdminContextPath().substring(1),
                    "/common/uploadresult.html?field=", CommonUtils.encodeURI(field), "&privatefile=", privatefile,
                    "&originalName=", CommonUtils.encodeURI(filename), "&filepath=", CommonUtils.encodeURI(newfilename));
        }

        policyConds.addConditionItem(PolicyConditions.COND_SUCCESS_ACTION_REDIRECT, returnUrl);
        DateFormat dateformat = DateFormatUtils.getDateFormat("yyyyMMdd");
        dateformat.setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC")));
        Date now = new Date();
        String date = dateformat.format(now);
        String dateStamp = CommonUtils.joinString(date, "T000000Z");
        String credential = CommonUtils.joinString(config.get(OSSComponent.CONFIG_ACCESSKEYID), "/", date, "/",
                config.get(privatefile ? OSSComponent.CONFIG_PRIVATE_REGION : OSSComponent.CONFIG_REGION), "/s3/aws4_request");
        policyConds.addConditionItem(PolicyConditions.COND_X_OSS_CREDENTIAL, credential);
        String algorithm = "AWS4-HMAC-SHA256";
        policyConds.addConditionItem(PolicyConditions.COND_X_OSS_ALGORITHM, algorithm);
        policyConds.addConditionItem(PolicyConditions.COND_X_OSS_DATE, dateStamp);
        String policy = OSSUtils.generatePostPolicy(Instant.ofEpochMilli(now.getTime() + 30 * 60 * 1000), policyConds);
        String encodedPolicy = VerificationUtils.base64Encode(policy.getBytes(Constants.DEFAULT_CHARSET));
        try {
            byte[] signkey = OSSUtils.newSigningKey(config.get(OSSComponent.CONFIG_ACCESSKEYSECRET), date,
                    config.get(privatefile ? OSSComponent.CONFIG_PRIVATE_REGION : OSSComponent.CONFIG_REGION));
            String signature = BinaryUtils.toHex(OSSUtils.computeSignature(encodedPolicy, signkey));
            result.put(PolicyConditions.COND_ACL, acl);
            result.put("returnUrl", returnUrl);
            result.put("date", dateStamp);
            result.put("policy", encodedPolicy);
            result.put("signature", signature);
            result.put("credential", credential);
            result.put("algorithm", algorithm);
            result.put("filename", newfilename);
        } catch (InvalidKeyException e) {
            result.put(CommonConstants.ERROR, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = { "upload.html" })
    public String upload(@RequestAttribute SysSite site, boolean privatefile, ModelMap model) {
        Map<String, String> config = configDataComponent.getConfigData(site.getId(), OSSComponent.CONFIG_CODE);
        if (OSSFileUploaderComponent.enableUpload(config, privatefile)) {
            model.addAttribute("bucketUrl",
                    config.get(privatefile ? OSSComponent.CONFIG_PRIVATE_BUCKET_URL : OSSComponent.CONFIG_BUCKET_URL));
            return "common/ossupload";
        } else {
            return "common/upload";
        }
    }
}
