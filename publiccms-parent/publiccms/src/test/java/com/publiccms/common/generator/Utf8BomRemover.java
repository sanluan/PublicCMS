package com.publiccms.common.generator;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;

import com.publiccms.common.constants.CommonConstants;

public class Utf8BomRemover extends DirectoryWalker<String> {
    public static void main(String[] args) throws IOException {
        new Utf8BomRemover().start(new File(new File(CommonConstants.BLANK).getAbsolutePath()).getParentFile());
    }

    public void start(File rootDir) throws IOException {
        walk(rootDir, null);
    }

    @Override
    protected void handleFile(File file, int depth, Collection<String> results) throws IOException {
        removeBom(file);
    }

    private static void removeBom(File file) throws IOException {
        byte[] bs = FileUtils.readFileToByteArray(file);
        if (bs.length >= 3 && bs[0] == -17 && bs[1] == -69 && bs[2] == -65) {
            byte[] nbs = new byte[bs.length - 3];
            System.arraycopy(bs, 3, nbs, 0, nbs.length);
            FileUtils.writeByteArrayToFile(file, nbs);
            System.out.println("Remove BOM: " + file);
        }
    }
}