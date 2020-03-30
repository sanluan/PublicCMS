package com.publiccms.test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.DateFormatUtils;
import com.publiccms.entities.cms.CmsComment;
import com.publiccms.logic.service.cms.CmsCommentService;

import config.spring.ApplicationConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class CommentImportTest {
    @Autowired
    CmsCommentService commentsService;

    @BeforeClass
    public static void init() {
        // 不进入安装程序
        CmsVersion.setInitialized(true);
        // 数据目录地址，此目录中应该有 database.properties
        CommonConstants.CMS_FILEPATH = "D://data/publiccms/";
    }

    // 搜狐畅言评论导入
    @Test
    public void importTest() throws IOException, ParseException {
        File file = new File("D:\\Users\\kerneler\\Desktop\\b28141a5-8e9c-4f4f-aacc-9864c6bce235_16.json");
        String content = FileUtils.readFileToString(file);
        Map<String, Object> a = Constants.objectMapper.readValue(content,
                Constants.objectMapper.getTypeFactory().constructMapLikeType(HashMap.class, String.class, Object.class));
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> list = (List<Map<String, Object>>) a.get("comments");
        for (Map<String, Object> map : list) {
            try {
                String text = (String) map.get("content");
                if (map.get("attachs") instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> attachs = (List<Map<String, Object>>) map.get("attachs");
                    for (Map<String, Object> i : attachs) {
                        text += "![](" + i.get("url") + ")";
                    }
                }
                CmsComment entity = new CmsComment((short) 1, 1, null, null, 0,
                        Integer.parseInt((String) map.get("topicSourceId")), null, null, null,
                        DateFormatUtils.getDateFormat(DateFormatUtils.FULL_DATE_FORMAT_STRING).parse((String) map.get("ctime")),
                        CmsCommentService.STATUS_PEND, false, text);
                commentsService.save(entity);
            } catch (NumberFormatException e) {
            }
        }
    }

}
