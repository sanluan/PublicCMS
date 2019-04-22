package com.publiccms.common.tools.ajaxresponse;

import java.util.HashMap;
import java.util.Map;

public class RspCode {

    /**
     * code编号 00000
     * code 开始两位数划分code的类型
     * 10  权限和登录等
     * 12 在业务逻辑上获得角色问题
     **/
    public static final String R00000 = "00000";
    //10 权限和登录等
    public static final String R10001 = "100001";
    //
    public static final String R12000 = "120000";
    /*public static final String R10010 = "100100";
    public static final String R1002 = "1002";
    public static final String R1003 = "1003";
    public static final String R1004 = "1004";
    public static final String R1005 = "1005";
    public static final String R1006 = "1006";
    public static final String R1999 = "1999";*/
    //业务逻辑
    public static final String R20000 = "20000";
	/*public static final String R2001 = "2001";
	public static final String R2002 = "2002";
	public static final String R2003 = "2003";
	public static final String R2021 = "2021";
	public static final String R2022 = "2022";
	public static final String R2030 = "2030";	
	public static final String R8000 = "8000";
	public static final String R8001 = "8001";
	public static final String R9001 = "9001";
	public static final String R9999 = "9999";*/

    static Map<String, String> map = new HashMap<String, String>();

    static {
        map.put("00000", "返回成功");
        map.put("10001", "登录账号不存在");
        map.put("R12000", "获得登录角色失败");
		/*map.put("1000", "bizCode业务编码不存在");
		map.put("1001", "登录账号不存在");
		map.put("1002", "密码错误");
		map.put("1003", "无权限访问");
		//map.put("1004", "升级接口,找不到版本升级记录");
		map.put("1005", "短号呼叫群组接口错误");
		map.put("1999", "信息不存在");
		map.put("2000", "请求报文解析错误");
		map.put("2001", "请求报文格式有误,请检查请求报文是否合法的xml或json格式");
		map.put("2002", "请求报文参数有误");
		map.put("2003", "请求报文svccontent数据项格式校验失败");
		map.put("2030", "应答报文解析有误");
		map.put("8000", "服务器内部错误");
		map.put("8001", "服务器通信异常");
		map.put("9001", "手机端版本过低");
		map.put("9999", "未知错误");*/
    }

    public static String getDesc(String bizCode) {
        return map.get(bizCode);
    }

    public static String getMsg(String bizCode) {
        String msg = "错误编号:" + bizCode + ",错误信息：" + map.get(bizCode);
        return msg;
    }

}
