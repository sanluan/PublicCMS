package com.publiccms.test;

import java.io.File;
import java.io.IOException;

import com.publiccms.common.tools.ImageUtils;

public class ImageTest {

    public static void main(String[] args) {
        File webp = new File("D:/14-15-390448-2128219932.webp");
        try {
            ImageUtils.webp2Image(webp, false, new File("D:/14-15-390448-2128219932.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
