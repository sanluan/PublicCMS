package com.publiccms.logic.component.paymentgateway;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.github.wxpay.sdk.WXPayConfig;

public class WechatConfig extends WXPayConfig {
    public WechatConfig(String appId, String mchId, String key, String certFilePath) {
        this.appId = appId;
        this.mchId = mchId;
        this.key = key;
        this.certFilePath = certFilePath;
    }

    private String appId;
    private String mchId;
    private String key;
    private String certFilePath;

    public String getAppID() {
        return appId;
    }

    public String getMchID() {
        return mchId;
    }

    public String getKey() {
        return key;
    }

    public InputStream getCertStream() {
        try {
            return new FileInputStream(new File(certFilePath));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

}
