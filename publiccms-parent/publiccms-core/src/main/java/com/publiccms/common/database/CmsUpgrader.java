package com.publiccms.common.database;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.publiccms.common.base.AbstractCmsUpgrader;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;

/**
 *
 * CmsUpgrader
 *
 */
public class CmsUpgrader extends AbstractCmsUpgrader {

	/**
	 * 主键策略
	 */
	public static final String IDENTIFIER_GENERATOR = IDENTIFIER_GENERATOR_IDENTITY;
	/**
	 *
	 */
	private final static String VERSION_20170708 = "V2017.0708", VERSION_2019 = "V2019", VERSION_202011 = "V2021";
	/**
	 *
	 */
	private final static List<String> VERSION_LIST = Arrays.asList(VERSION_20170708, VERSION_2019, VERSION_202011);

	public CmsUpgrader(Properties config) {
		super(config);
	}

	/**
	 * @throws SQLException
	 * @throws IOException
	 */
	@Override
	public void update(StringWriter stringWriter, Connection connection, String fromVersion)
			throws SQLException, IOException {
		switch (fromVersion) {
		case VERSION_20170708:
			runScript(stringWriter, connection, VERSION_20170708, VERSION_2019);
			updateMetadata(stringWriter, connection);
		case VERSION_2019:
			runScript(stringWriter, connection, VERSION_2019, VERSION_202011);
		case VERSION_202011:
			runScript(stringWriter, connection, VERSION_202011, CmsVersion.getVersion());
		}
	}

	@Override
	public void setDataBaseUrl(Properties dbconfig, String host, String port, String database, String timeZone)
			throws IOException, URISyntaxException {
		StringBuilder sb = new StringBuilder();
		sb.append("jdbc:mysql://");
		sb.append(host);
		sb.append(":");
		if (CommonUtils.empty(port)) {
			sb.append(getDefaultPort());
		} else {
			sb.append(port);
		}
		sb.append("/");
		sb.append(database);
		sb.append("?characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true&useAffectedRows=true");
		if (CommonUtils.notEmpty(timeZone)) {
			try {
				sb.append("&serverTimezone=GMT");
				if (!"Z".equalsIgnoreCase(timeZone)) {
					sb.append(URLEncoder.encode(timeZone, Constants.DEFAULT_CHARSET_NAME));
				}
			} catch (UnsupportedEncodingException e) {
			}
		}
		dbconfig.setProperty("jdbc.url", sb.toString());
		dbconfig.setProperty("jdbc.driverClassName", "com.mysql.cj.jdbc.Driver");
	}

	@Override
	public List<String> getVersionList() {
		return VERSION_LIST;
	}

	@Override
	public int getDefaultPort() {
		return 3306;
	}
}
