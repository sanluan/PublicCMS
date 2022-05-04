package com.publiccms.views.pojo.diy;

import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * CssBoxProperties diy盒子模型属性
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CssBoxProperties implements java.io.Serializable {
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