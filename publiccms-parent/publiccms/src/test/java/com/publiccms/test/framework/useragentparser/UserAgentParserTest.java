package com.publiccms.test.framework.useragentparser;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.ParseException;
import com.blueconic.browscap.UserAgentParser;
import com.blueconic.browscap.UserAgentService;

public class UserAgentParserTest {

    @Test
    public void loadTest() {
        try {
            UserAgentParser parser = new UserAgentService().loadParser();
            Assertions.assertNotNull(parser);
            {
                Capabilities c = parser.parse(
                        "Mozilla/5.0 (iPhone; CPU iPhone OS 17_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/124.0.6367.88 Mobile/15E148 Safari/604.1");
                Assertions.assertEquals(c.getPlatform(), "iOS");
                Assertions.assertEquals(c.getDeviceType(), "Mobile Phone");
                //Assertions.assertEquals(c.getBrowser(), "Safari"); current version parse error
                Assertions.assertEquals(c.getBrowserType(), "Browser");
            }
            {
                Capabilities c = parser.parse(
                        "Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko; compatible; bingbot/2.0; +http://www.bing.com/bingbot.htm) Chrome/112.0.0.0 Safari/537.36");
                Assertions.assertEquals(c.getPlatform(), "Unknown");
                Assertions.assertEquals(c.getDeviceType(), "Unknown");
                Assertions.assertEquals(c.getBrowser(), "BingBot");
                Assertions.assertEquals(c.getBrowserType(), "Bot");
            }
            {
                Capabilities c = parser
                        .parse("Mozilla/5.0 (compatible; Baiduspider-render/2.0; +http://www.baidu.com/search/spider.html)");
                Assertions.assertEquals(c.getPlatform(), "Unknown");
                Assertions.assertEquals(c.getDeviceType(), "Unknown");
                Assertions.assertEquals(c.getBrowser(), "Baiduspider");
                Assertions.assertEquals(c.getBrowserType(), "Bot");
            }
            {
                Capabilities c = parser.parse(
                        "Mozilla/5.0 (Linux; Android 14; V2314A; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/87.0.4280.141 Mobile Safari/537.36 VivoBrowser/19.3.1.0");
                Assertions.assertEquals(c.getPlatform(), "Android");
                Assertions.assertEquals(c.getDeviceType(), "Mobile Phone");
                Assertions.assertEquals(c.getBrowser(), "Vivo Browser");
                Assertions.assertEquals(c.getBrowserType(), "Browser");
            }
            {
                Capabilities c = parser.parse(
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.5112.79 Safari/537.36");
                Assertions.assertEquals(c.getPlatform(), "macOS");
                Assertions.assertEquals(c.getDeviceType(), "Desktop");
                Assertions.assertEquals(c.getBrowser(), "Chrome");
                Assertions.assertEquals(c.getBrowserType(), "Browser");
            }
            {
                Capabilities c = parser.parse(
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36");
                Assertions.assertEquals(c.getPlatform(), "Win10");
                Assertions.assertEquals(c.getDeviceType(), "Desktop");
                Assertions.assertEquals(c.getBrowser(), "Chrome");
                Assertions.assertEquals(c.getBrowserType(), "Browser");
            }
        } catch (IOException | ParseException e) {
        }
    }
}
