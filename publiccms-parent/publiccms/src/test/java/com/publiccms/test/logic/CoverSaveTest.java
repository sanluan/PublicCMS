package com.publiccms.test.logic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.drew.imaging.FileType;
import com.drew.imaging.FileTypeDetector;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.logic.service.tools.SqlService;

import config.spring.ApplicationConfig;
import jakarta.annotation.Resource;

/**
 *
 * SysSiteServiceTest https://junit.org/junit5/docs/current/user-guide/
 * 
 */
@DisplayName("SysSite test case")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationConfig.class)
class Cover {
    protected final Log log = LogFactory.getLog(getClass());

    @BeforeAll
    public static void init() {
        // 不进入安装程序 数据目录有 database.properties才能进行测试
        CmsVersion.setInitialized(true);
        CmsVersion.setScheduled(false);
    }

    @Resource
    private SqlService sqlService;

    /**
     * 
     */
    @Test
    @DisplayName("mybatis query test case")
    void mybatisTest() {
        List<Map<String, Object>> list = sqlService.select(
                "select b.id,a.newimg from oldaobo.abwz_news a,oldaobo.news b where a.id =b.oldid and a.newimg is not null");
        for (Map<String, Object> map : list) {
            Integer id = (Integer) map.get("id");
            byte[] img = (byte[]) map.get("newimg");
            String fileName = CmsFileUtils.getUploadFileName(".jpg");
            String filePath = "D:/aaa/" + fileName;
            try {
                CmsFileUtils.writeByteArrayToFile(filePath, img);
                try (FileInputStream fileInputStream = new FileInputStream(filePath);
                        BufferedInputStream inputStream = new BufferedInputStream(fileInputStream)) {
                    FileType fileType = FileTypeDetector.detectFileType(inputStream);
                    if (!"JPEG".equalsIgnoreCase(fileType.getName())) {
                        File file = new File(filePath);
                        if ("PNG".equalsIgnoreCase(fileType.getName())) {
                            file.renameTo(new File(file.getAbsolutePath().replace(".jpg", ".png")));
                            fileName=fileName.replace(".jpg", ".png");
                        } else if ("WebP".equalsIgnoreCase(fileType.getName())) {
                            file.renameTo(new File(file.getAbsolutePath().replace(".jpg", ".webp")));
                            fileName=fileName.replace(".jpg", ".webp");
                        } else if ("gif".equalsIgnoreCase(fileType.getName())) {
                            file.renameTo(new File(file.getAbsolutePath().replace(".jpg", ".gif")));
                            fileName=fileName.replace(".jpg", ".gif");
                        } else {
                            System.out.println(fileType.getName());
                        }
                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("update cms_content set cover = '" + fileName + "' where id = " + id + ";");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
