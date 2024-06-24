package com.publiccms.test.framework.validator;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.publiccms.entities.cms.CmsPlace;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Pattern;

@DisplayName("css test case")
class BeanValidatorTest {
    protected final Log log = LogFactory.getLog(getClass());
    private static Validator validator = Validation.byProvider(HibernateValidator.class).configure().failFast(false)
            .buildValidatorFactory().getValidator();

    @Test
    @DisplayName("null test case")
    void nullTest() {
        CmsPlace bean = new CmsPlace();
        bean.setTitle(null);
        Set<ConstraintViolation<CmsPlace>> set = validator.validate(bean);
        log.info(set);
        Assertions.assertFalse(set.isEmpty(), set.toString());
    }

    @Test
    @DisplayName("px test case")
    void widthPx() {
        testValue("100px");
    }

    @Test
    @DisplayName("percent test case")
    void widthPercent() {
        testValue("100%");
    }

    @Test
    @DisplayName("rem test case")
    void widthRem() {
        testValue("100rem");
    }

    @Test
    @DisplayName("hex color test case")
    void colorHex1() {
        testValue("#FFF");
    }

    @Test
    @DisplayName("script test case")
    void script() {
        testValue("<script>", false);
    }

    @Test
    @DisplayName("- test case")
    void split1() {
        testValue("-", false);
    }

    @Test
    @DisplayName(", test case")
    void split2() {
        testValue(",", false);
    }

    @Test
    @DisplayName("empty string test case")
    void split3() {
        testValue(" ", false);
    }

    @Test
    @DisplayName("named color test case")
    void colorName() {
        testValue("gray");
    }

    @Test
    @DisplayName("hex color test case")
    void colorHex2() {
        testValue("#FF0000");
    }

    @Test
    @DisplayName("hex color test case")
    void colorHex3() {
        testValue("#FF000000");
    }

    @Test
    @DisplayName("hex color test case")
    void colorRgb1() {
        testValue("rgb (255 , 255 , 255)");
    }

    @Test
    @DisplayName("hex color test case")
    void colorRgba() {
        testValue("rgba(250, 250, 250, 0.4)");
    }

    @Test
    @DisplayName("hex color test case")
    void width() {
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

class CssBoxProperties implements java.io.Serializable {
    public static final String REG_INPUT_COLOR_HEX = "#\\w+";
    public static final String REG_INPUT_COLOR_RGB = "rgba?\\s*\\(\\d+\\s*,\\s*\\d+\\s*,\\s*\\d+\\s*(,\\s*\\d?(\\.\\d+)?)?\\s*\\)";
    public static final String REG_INPUT_COLOR_HSL = "hsla?\\s*\\(\\d+\\s*,\\s*\\d+%\\s*,\\s*\\d+\\s*%(,\\s*\\d?(\\.\\d+)?)?\\s*\\)";
    public static final String REG_INPUT_COLOR = REG_INPUT_COLOR_HEX + "|" + REG_INPUT_COLOR_RGB + "|" + REG_INPUT_COLOR_HSL + "|"
            + "\\w+";
    public static final String REG_INPUT_BORDER_STYLE = "none|hidden|dotted|dashed|solid|double|groove|ridge|inset|outset|inherit";
    public static final String REG_INPUT_BORDER_WIDTH = "thin|medium|thick|auto";
    public static final String REG_INPUT_LENGTH = "^\\d+(\\.\\d+)?(cm|px|em|ex|mm|in|pt|pc|vh|vw|vmin|vmax|ch|rem|%)?";

    public static final String REG_INPUT = REG_INPUT_COLOR + "|" + REG_INPUT_BORDER_STYLE + "|" + REG_INPUT_BORDER_WIDTH + "|"
            + REG_INPUT_LENGTH;
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Pattern(regexp = REG_INPUT)
    private String top;// top-left for radius
    @Pattern(regexp = REG_INPUT)
    private String right;// top-right for radius
    @Pattern(regexp = REG_INPUT)
    private String bottom;// bottom-right for radius
    @Pattern(regexp = REG_INPUT)
    private String left;// bottom-left for radius

    public CssBoxProperties() {
    }

    public CssBoxProperties(String topBottomLeftRight) {
        this(topBottomLeftRight, topBottomLeftRight, topBottomLeftRight, topBottomLeftRight);
    }

    public CssBoxProperties(String topBottom, String leftRight) {
        this(topBottom, leftRight, topBottom, leftRight);
    }

    public CssBoxProperties(String top, String leftRight, String bottom) {
        this(top, leftRight, bottom, leftRight);
    }

    public CssBoxProperties(String top, String right, String bottom, String left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public boolean isEmpty() {
        return StringUtils.isAllEmpty(left, right, top, bottom);
    }

    public boolean isAllSame() {
        return StringUtils.equals(left, right) && StringUtils.equals(top, bottom) && StringUtils.equals(top, left);
    }

    public boolean isSame2() {
        return StringUtils.equals(left, right) && StringUtils.equals(top, bottom);
    }

    public boolean isSame3() {
        return StringUtils.equals(left, right);
    }

    /**
     * @return the top
     */
    public String getTop() {
        return top;
    }

    /**
     * @param top
     *            the top to set
     */
    public void setTop(String top) {
        this.top = top;
    }

    /**
     * @return the right
     */
    public String getRight() {
        return right;
    }

    /**
     * @param right
     *            the right to set
     */
    public void setRight(String right) {
        this.right = right;
    }

    /**
     * @return the bottom
     */
    public String getBottom() {
        return bottom;
    }

    /**
     * @param bottom
     *            the bottom to set
     */
    public void setBottom(String bottom) {
        this.bottom = bottom;
    }

    /**
     * @return the left
     */
    public String getLeft() {
        return left;
    }

    /**
     * @param left
     *            the left to set
     */
    public void setLeft(String left) {
        this.left = left;
    }
}