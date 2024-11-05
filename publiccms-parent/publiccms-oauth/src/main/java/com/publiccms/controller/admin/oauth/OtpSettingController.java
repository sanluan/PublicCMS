package com.publiccms.controller.admin.oauth;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.bastiaanjansen.otp.SecretGenerator;
import com.bastiaanjansen.otp.TOTPGenerator;
import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserAttribute;
import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.logic.service.sys.SysUserAttributeService;
import com.publiccms.logic.service.sys.SysUserService;

import jakarta.annotation.Resource;

@Controller
@RequestMapping("otpSetting")
public class OtpSettingController {
    protected final Log log = LogFactory.getLog(getClass());
    @Resource
    private ConfigDataComponent configDataComponent;
    @Resource
    private SysUserService service;
    @Resource
    private SysUserAttributeService attributeService;

    /**
     * @param site
     * @param admin
     * @param otpadmin
     * @param returnUrl
     * @param model
     * @return view name
     */
    @PostMapping(value = "unbind")
    @Csrf
    public String unbind(@SessionAttribute SysUser admin) {
        SysUserAttribute attribute = attributeService.getEntity(admin.getId());
        Map<String, String> map = ExtendUtils.getSettingsMap(attribute);
        map.remove(SysUserAttributeService.OPTSECRET_SETTINGS_CODE);
        attributeService.updateSettings(admin.getId(), ExtendUtils.getExtendString(map));
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param otpadmin
     * @param returnUrl
     * @param model
     * @return view name
     */
    @PostMapping(value = "check")
    @ResponseBody
    public boolean check(@SessionAttribute SysUser admin) {
        SysUserAttribute attribute = attributeService.getEntity(admin.getId());
        Map<String, String> map = ExtendUtils.getSettingsMap(attribute);
        return CommonUtils.empty(map.get(SysUserAttributeService.OPTSECRET_SETTINGS_CODE));
    }

    /**
     * @param site
     * @param admin
     * @param otpadmin
     * @param returnUrl
     * @param secret
     * @param code
     * @param model
     * @return view name
     */
    @RequestMapping(value = "getRegisterURI")
    @ResponseBody
    public Map<String, String> getRegisterURI(@SessionAttribute SysUser admin) {
        byte[] secret = SecretGenerator.generate();
        TOTPGenerator totp = new TOTPGenerator.Builder(secret).build();
        Map<String, String> result = new HashMap<>();
        result.put("secret", new String(secret, StandardCharsets.UTF_8));
        try {
            result.put("bindURI", totp.getURI("cms", admin.getName()).toString());
        } catch (URISyntaxException e) {
            result.put(CommonConstants.ERROR, e.getMessage());
        }
        return result;
    }

    /**
     * @param admin
     * @param secret
     * @param code
     * @return view name
     */
    @PostMapping(value = "bind")
    @Csrf
    public String bind(@SessionAttribute SysUser admin, String secret, String code) {
        TOTPGenerator totp = new TOTPGenerator.Builder(secret.getBytes()).build();
        if (totp.verify(code)) {
            SysUserAttribute attribute = attributeService.getEntity(admin.getId());
            Map<String, String> map = ExtendUtils.getSettingsMap(attribute);
            map.put(SysUserAttributeService.OPTSECRET_SETTINGS_CODE, secret);
            attributeService.updateSettings(admin.getId(), ExtendUtils.getExtendString(map));
            return CommonConstants.TEMPLATE_DONE;
        }
        return CommonConstants.TEMPLATE_ERROR;
    }
}
