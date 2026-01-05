package com.publiccms.views.method.tools;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.service.Config;
import org.lionsoul.ip2region.service.InvalidConfigException;
import org.lionsoul.ip2region.service.Ip2Region;
import org.lionsoul.ip2region.xdb.XdbException;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.AnalyzerDictUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.views.pojo.entities.IpRegion;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getIpRegion 获取IP区域信息
 * <p>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>string</code> ip
 * </ul>
 * 使用示例
 * <p>
 * ${getIpRegion('127.0.0.1')}
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getIpRegion?parameters=127.0.0.1', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetIpRegionMethod extends BaseMethod {
    private static Ip2Region ip2Region = null;

    private void init() {
        if (null == ip2Region) {
            synchronized (GetIpRegionMethod.class) {
                try {
                    if (null == ip2Region) {
                        Config v4Config = null;
                        File ipv4 = getFilePath("ip2region_v4.xdb");
                        File ipv6 = getFilePath("ip2region_v6.xdb");
                        if (ipv4.exists()) {
                            Config.custom().setCachePolicy(Config.VIndexCache).setSearchers(20).setXdbFile(ipv4).asV4();
                        } else {
                            Config.custom().setCachePolicy(Config.BufferCache)
                                    .setXdbInputStream(GetIpRegionMethod.class.getResourceAsStream("/ip2region_v4.xdb")).asV4();
                        }
                        Config v6Config = null;
                        if (ipv6.exists()) {
                            v6Config = Config.custom().setCachePolicy(Config.VIndexCache).setSearchers(20).setXdbFile(ipv6)
                                    .asV6();
                        }
                        ip2Region = Ip2Region.create(v4Config, v6Config);
                    }
                } catch (IOException | XdbException | InvalidConfigException e) {
                }
            }
        }
    }

    private File getFilePath(String path) {
        File file = new File(CommonUtils.joinString(CommonConstants.CMS_FILEPATH, AnalyzerDictUtils.DIR_DICT, path));
        return file;
    }

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        String ip = getString(0, arguments);
        if (CommonUtils.notEmpty(ip)) {
            try {
                init();
                if (null != ip2Region) {
                    return getIpRegion(ip2Region.search(ip));
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }

    private static IpRegion getIpRegion(String region) {
        IpRegion ipRegion = new IpRegion(region);
        String[] strings = StringUtils.split(region, '|');
        if (strings.length > 0 && !"0".equals(strings[0])) {
            ipRegion.setCountry(strings[0]);
        }
        if (strings.length > 1 && !"0".equals(strings[1])) {
            ipRegion.setProvince(strings[1]);
        }
        if (strings.length > 2 && !"0".equals(strings[2])) {
            ipRegion.setCity(strings[2]);
        }
        if (strings.length > 3 && !"0".equals(strings[3])) {
            ipRegion.setOperator(strings[3]);
        }
        return ipRegion;
    }

    @PreDestroy
    public void destroy() {
        if (null != ip2Region) {
            try {
                ip2Region.close();
            } catch (InterruptedException e) {
            }
        }
    }
}
