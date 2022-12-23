package com.publiccms.logic.component.site;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.database.CmsDataSource;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.VerificationUtils;

/**
 *
 * VisitComponent
 * 
 */
@Component
public class ScriptComponent {
    private static final Pattern PARAMETER_PATTERN = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_\\-\\.]{1,191}$");
    private static final String[] COMMANDS = { "sync.bat", "sync.sh", "backupdb.bat", "backupdb.sh" };

    public String execute(String command, String[] parameters, long timeoutHours)
            throws FileNotFoundException, IOException, InterruptedException {
        if (CommonUtils.notEmpty(command) && ArrayUtils.contains(COMMANDS, command.toLowerCase())) {
            String dir = CommonConstants.CMS_FILEPATH + "/script";
            String[] cmdarray;
            if ("backupdb.bat".equalsIgnoreCase(command) || "backupdb.sh".equalsIgnoreCase(command)) {
                String databaseConfiFile = CommonConstants.CMS_FILEPATH + CmsDataSource.DATABASE_CONFIG_FILENAME;
                Properties dbconfigProperties = CmsDataSource.loadDatabaseConfig(databaseConfiFile);
                String userName = dbconfigProperties.getProperty("jdbc.username");
                String database = dbconfigProperties.getProperty("database", "publiccms");
                String password = dbconfigProperties.getProperty("jdbc.password");
                String encryptPassword = dbconfigProperties.getProperty("jdbc.encryptPassword");
                if (null != encryptPassword) {
                    password = VerificationUtils.decrypt(VerificationUtils.base64Decode(encryptPassword),
                            CommonConstants.ENCRYPT_KEY);
                }
                cmdarray = new String[] { database, userName, password };
            } else {
                cmdarray = new String[parameters.length];
                if (null != parameters) {
                    int i = 0;
                    for (String c : parameters) {
                        if (!PARAMETER_PATTERN.matcher(c).matches()) {
                            cmdarray[i] = "";
                        } else {
                            cmdarray[i] = c;
                        }
                        i++;
                    }
                }
            }
            String filepath = new StringBuilder(dir).append("/").append(command).toString();
            File script = new File(filepath);
            if (!script.exists()) {
                try (InputStream inputStream = getClass()
                        .getResourceAsStream(new StringBuilder("/script/").append(command).toString())) {
                    FileUtils.copyInputStreamToFile(inputStream, script);
                }
            }
            if (command.toLowerCase().endsWith(".sh")) {
                cmdarray = ArrayUtils.insert(0, cmdarray, filepath);
                cmdarray = ArrayUtils.insert(0, cmdarray, "sh");
            } else {
                cmdarray = ArrayUtils.insert(0, cmdarray, filepath);
            }
            Process ps = Runtime.getRuntime().exec(cmdarray, null, new File(dir));
            ps.waitFor(timeoutHours, TimeUnit.HOURS);
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
        return command + " not exits";
    }

}