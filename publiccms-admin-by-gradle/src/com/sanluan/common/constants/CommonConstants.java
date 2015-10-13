package com.sanluan.common.constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CommonConstants {
    public static final String SESSION_USER = "S_USER";
    public static final String SESSION_USER_TIME = "S_USER_TIME";
    public static final String SESSION_ADMIN = "S_ADMIN";
    public static final String COOKIES_USER = "C_USER";

    public static final String UTF8 = "utf-8";

    public static final String MOBILE_PATTERN = "^(13|14|15|17|18|)\\d{9}$";
    public static final String NUMBER_PATTERN = "^[0-9]*$";

    public static final String USERNAME_PATTERN = "^[A-Za-z_]{1}[0-9A-Za-z_]{3,40}$";
    public static final String NICKNAME_PATTERN = "^[0-9A-Za-z_\u4E00-\uFA29\uE7C7-\uE7F3]{2,45}$";

    private static final String SPECIAL_CHARS = "\\(\\)<>@,;:\\\\\\\"\\.\\[\\]+";
    private static final String VALID_CHARS = "[^\\s" + SPECIAL_CHARS + "]+";

    public static final String EMAIL_PATTERN = "(" + VALID_CHARS + "(\\." + VALID_CHARS + ")*@" + VALID_CHARS + "(\\."
            + VALID_CHARS + ")*)";

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    public static final DateFormat FULL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final int FULL_DATE_LENGTH = 19;

    public static final DateFormat SHORT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final int SHORT_DATE_LENGTH = 10;
}
