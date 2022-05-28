package com.publiccms.common.tools;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class IpUtils {
    private static Pattern IPV6_PATTERN = Pattern.compile(
            "^\\[?((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:)|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}(:[0-9A-Fa-f]{1,4}){1,2})|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){1,3})|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){1,4})|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){1,5})|([0-9A-Fa-f]{1,4}:(:[0-9A-Fa-f]{1,4}){1,6})|(:(:[0-9A-Fa-f]{1,4}){1,7})|(([0-9A-Fa-f]{1,4}:){6}(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){5}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){0,1}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){0,2}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){0,3}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|([0-9A-Fa-f]{1,4}:(:[0-9A-Fa-f]{1,4}){0,4}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})|(:(:[0-9A-Fa-f]{1,4}){0,5}:(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}))\\]?$");
    private static Pattern IPV4_PATTERN = Pattern
            .compile("^(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$");

    /**
     * @param value
     * @return number
     */
    public static long getIpv4Number(String value) {
        String[] array = StringUtils.split(value, '.');
        long result = 0;
        if (null != array && 4 == array.length) {
            try {
                result = Integer.parseInt(array[0]) << 24;
                result += Integer.parseInt(array[1]) << 16;
                result += Integer.parseInt(array[2]) << 8;
                result += Integer.parseInt(array[3]);
            } catch (NumberFormatException e) {
                result = 0;
            }
        }
        return result;
    }

    /**
     * @param value
     * @return string
     */
    public static String getIpv4String(long value) {
        StringBuilder sb = new StringBuilder();
        sb.append(value >> 24);
        sb.append(".").append((0x00ffffff & value) >> 16);
        sb.append(".").append((0x0000ffff & value) >> 8);
        sb.append(".").append(0x000000ff & value);
        return sb.toString();
    }

    /**
     * @param value
     * @return number
     */
    public static BigInteger getIpv6Number(String value) {
        try {
            return new BigInteger(1, InetAddress.getByName(value).getAddress());
        } catch (UnknownHostException e) {
            return null;
        }
    }

    /**
     * @param value
     * @return string
     */
    public static String getIpv6String(BigInteger value) {
        if (null != value) {
            StringBuilder sb = new StringBuilder();
            BigInteger ff = BigInteger.valueOf(0xffff);
            for (int i = 0; i < 8; i++) {
                sb.insert(0, ":").insert(0, value.and(ff).toString(16));
                value = value.shiftRight(16);
            }
            sb.setLength(sb.length() - 1);
            return sb.toString().replaceFirst("(^|:)(0+(:|$)){1,8}", "::");
        } else {
            return null;
        }
    }

    /**
     * @param value
     * @return boolean
     */
    public static boolean isIpv4(String value) {
        return IPV4_PATTERN.matcher(value).matches();
    }

    /**
     * @param value
     * @return boolean
     */
    public static boolean isIpv6(String value) {
        return IPV6_PATTERN.matcher(value).matches();
    }

    /**
     * @param value
     * @return boolean
     */
    public static boolean isIp(String value) {
        return isIpv4(value) || isIpv6(value);
    }

}