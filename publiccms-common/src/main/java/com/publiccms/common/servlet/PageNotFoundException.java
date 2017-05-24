package com.publiccms.common.servlet;

/**
 *
 * PageNotFoundException
 * 
 */
public class PageNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public PageNotFoundException() {
        super();
    }

    /**
     * @param s
     * @param throwable
     * @param flag
     * @param flag1
     */
    public PageNotFoundException(String s, Throwable throwable, boolean flag, boolean flag1) {
        super(s, throwable, flag, flag1);
    }

    /**
     * @param s
     * @param throwable
     */
    public PageNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    /**
     * @param s
     */
    public PageNotFoundException(String s) {
        super(s);
    }

    /**
     * @param throwable
     */
    public PageNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
