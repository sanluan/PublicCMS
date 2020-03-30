package com.publiccms.common.constants;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Random;
import java.util.function.BinaryOperator;

import org.apache.http.client.config.RequestConfig;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * Constants
 * 
 */
public class Constants {
    /**
     * Json Mapper
     */
    public static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Default Request Config
     */
    public static final RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000)
            .setConnectionRequestTimeout(5000).build();
    /**
     * 随机数
     * 
     * Random
     */
    public static final Random random = new SecureRandom();
    /**
     * 默认字符编码名称
     * 
     * Default CharSet Name
     */
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";
    /**
     * 默认字符编码
     * 
     * Default CharSet
     */
    public static final Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_NAME);
    /**
     * 间隔符
     * 
     * separator
     */
    public static final String SEPARATOR = "/";
    /**
     * 空白字符串
     * 
     * blank
     */
    public static final String BLANK = "";
    /**
     * 点
     * 
     * dot
     */
    public static final String DOT = ".";
    /**
     * 下划线
     * 
     * underline
     */
    public static final String UNDERLINE = "_";
    /**
     * 空格
     * 
     * blank space
     */
    public static final String BLANK_SPACE = " ";
    /**
     * 逗号分隔符
     * 
     * comma delimited
     */
    public static final String COMMA_DELIMITED = ",";
    /**
     * 逗号分隔符
     * 
     * comma delimited
     */
    public static final char COMMA = ',';

    /**
     * @return deafult meger function
     */
    public static <T> BinaryOperator<T> defaultMegerFunction() {
        return (first, second) -> first;
    }
}