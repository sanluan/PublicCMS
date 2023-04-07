package com.publiccms.common.generator;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.commons.io.FileUtils;

import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;

public class Utf8BomRemover {
    public static void main(String[] args) throws IOException {
        Files.walkFileTree(new File(new File(Constants.BLANK).getAbsolutePath()).getParentFile().toPath(),
                new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        removeBom(file.toFile());
                        return FileVisitResult.CONTINUE;
                    }
                });

    }

    private static void removeBom(File file) throws IOException {
        byte[] bs = FileUtils.readFileToByteArray(file);
        if (bs.length >= 3 && bs[0] == -17 && bs[1] == -69 && bs[2] == -65) {
            byte[] nbs = new byte[bs.length - 3];
            System.arraycopy(bs, 3, nbs, 0, nbs.length);
            FileUtils.writeByteArrayToFile(file, nbs);
            System.out.println(CommonUtils.joinString("Remove BOM: ", file.toString()));
        }
    }
}