package com.sanluan.common.tools;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class SpringContextUtils {
	private static WebApplicationContext applicationContext;

	/**
	 * @return the applicationContext
	 */
	public static WebApplicationContext getApplicationContext() {
		if (null == applicationContext)
			applicationContext = ContextLoader.getCurrentWebApplicationContext();
		return applicationContext;
	}

}
