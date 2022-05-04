package com.publiccms.test;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.publiccms.views.pojo.diy.CssBoxProperties;

@DisplayName("css test case")
public class CssPropertiesTest {
    private static Validator validator = Validation.byProvider(HibernateValidator.class).configure().failFast(false)
            .buildValidatorFactory().getValidator();

    @Test
    @DisplayName("px test case")
    public void widthPx() {
        testValue("100px");
    }

    @Test
    @DisplayName("percent test case")
    public void widthPercent() {
        testValue("100%");
    }

    @Test
    @DisplayName("rem test case")
    public void widthRem() {
        testValue("100rem");
    }

    @Test
    @DisplayName("hex color test case")
    public void colorHex1() {
        testValue("#FFF");
    }

    @Test
    @DisplayName("script test case")
    public void script() {
        testValue("<script>", false);
    }

    @Test
    @DisplayName("- test case")
    public void split1() {
        testValue("-", false);
    }

    @Test
    @DisplayName(", test case")
    public void split2() {
        testValue(",", false);
    }

    @Test
    @DisplayName("empty string test case")
    public void split3() {
        testValue(" ", false);
    }

    @Test
    @DisplayName("named color test case")
    public void colorName() {
        testValue("gray");
    }

    @Test
    @DisplayName("hex color test case")
    public void colorHex2() {
        testValue("#FF0000");
    }

    @Test
    @DisplayName("hex color test case")
    public void colorHex3() {
        testValue("#FF000000");
    }

    @Test
    @DisplayName("hex color test case")
    public void colorRgb1() {
        testValue("rgb (255 , 255 , 255)");
    }

    @Test
    @DisplayName("hex color test case")
    public void colorRgba() {
        testValue("rgba(250, 250, 250, 0.4)");
    }

    @Test
    @DisplayName("hex color test case")
    public void width() {
        testValue(new CssBoxProperties("10px", "auto"));
    }

    private void testValue(CssBoxProperties bean) {
        assertTrue(validator.validate(bean));
    }

    private void testValue(String value) {
        CssBoxProperties bean = new CssBoxProperties(value);
        assertTrue(validator.validate(bean));
    }

    private void testValue(String value, boolean expectError) {
        CssBoxProperties bean = new CssBoxProperties(value);
        if (expectError) {
            assertTrue(validator.validate(bean));
        } else {
            Set<ConstraintViolation<CssBoxProperties>> set = validator.validate(bean);
            Assertions.assertFalse(set.isEmpty(), set.toString());
        }
    }

    private void assertTrue(Set<ConstraintViolation<CssBoxProperties>> set) {
        Assertions.assertTrue(set.isEmpty(), set.toString());
    }
}
