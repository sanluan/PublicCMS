package com.publiccms.views.method.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
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
    private final Searcher searcher;

    public GetIpRegionMethod() throws IOException {
        try (InputStream inputStream = GetIpRegionMethod.class.getResourceAsStream("/ip2region.xdb")) {
            searcher = Searcher.newWithBuffer(IOUtils.toByteArray(inputStream));
        }
    }

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        String ip = getString(0, arguments);
        if (CommonUtils.notEmpty(ip)) {
            try {
                return getIpRegion(searcher.search(ip));
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
        if (strings.length > 2 && !"0".equals(strings[2])) {
            ipRegion.setProvince(strings[2]);
        }
        if (strings.length > 3 && !"0".equals(strings[3])) {
            ipRegion.setCity(strings[3]);
        }
        return ipRegion;
    }

}
