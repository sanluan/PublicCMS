package com.publiccms.test.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.entities.sys.SysSite;

/**
 * BatchTest https://junit.org/junit5/docs/current/user-guide/
 * 
 */
@DisplayName("Controller utils test case")
class ControllerUtilsTest {
    @Test
    @DisplayName("test url unsafe")
    void isUnSafeUrl() {
        SysSite site = new SysSite();
        site.setDynamicPath("//www.publiccms.com/");
        site.setSitePath("//www.publiccms.com/");
        Assertions.assertTrue(ControllerUtils.isUnSafeUrl("http:www.baidu.com/", site, "", ""));
        Assertions.assertTrue(ControllerUtils.isUnSafeUrl("http://www.baidu.com/", site, "", ""));
        Assertions.assertFalse(ControllerUtils.isUnSafeUrl("//www.publiccms.com/", site, "", ""));
        Assertions.assertFalse(ControllerUtils.isUnSafeUrl("http://www.publiccms.com/", site, "", ""));
        Assertions.assertFalse(ControllerUtils.isUnSafeUrl("login.html", site, "", ""));
        Assertions.assertFalse(ControllerUtils.isUnSafeUrl("/login.html", site, "", ""));
        Assertions.assertFalse(ControllerUtils.isUnSafeUrl("/", site, "", ""));
        Assertions.assertFalse(ControllerUtils.isUnSafeUrl("http://www.baidu.com/", site, "http://www.baidu.com", ""));
    }

}
