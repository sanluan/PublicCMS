package com.publiccms.common.generator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import com.publiccms.common.constants.Constants;

public class Comment {

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        File file = new File("D:\\Users\\YSXH\\Desktop\\b28141a5-8e9c-4f4f-aacc-9864c6bce235_16.json");
        String content = FileUtils.readFileToString(file);
        Constants.objectMapper.readValue(content,
                Constants.objectMapper.getTypeFactory().constructMapLikeType(HashMap.class, String.class, Object.class));
    }

}
