package com.sanluan.common.servlet;

public class PageNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PageNotFoundException() {
        super();
    }

    public PageNotFoundException(String s, Throwable throwable, boolean flag, boolean flag1) {
        super(s, throwable, flag, flag1);
    }

    public PageNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PageNotFoundException(String s) {
        super(s);
    }

    public PageNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
