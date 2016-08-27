package com.sanluan.common.tools;

import static com.sanluan.common.tools.StreamUtils.write;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import com.sanluan.common.base.Base;

/**
 * 压缩/解压缩zip包处理类
 * 
 * @author kerneler
 *
 */
public class ZipUtils extends Base {

    /**
     * @param srcPathName
     * @param zipFilePath
     * @throws IOException
     */
    public static boolean zip(String sourceFilePath, String zipFilePath) throws IOException {
        return zip(sourceFilePath, zipFilePath, true);
    }

    /**
     * @param sourceFilePath
     * @param zipFilePath
     * @param overwrite
     * @return
     * @throws IOException
     */
    public static boolean zip(String sourceFilePath, String zipFilePath, boolean overwrite) throws IOException {
        if (notEmpty(sourceFilePath)) {
            ZipOutputStream zipOutputStream = null;
            File zipFile = new File(zipFilePath);
            if (zipFile.exists() && !overwrite) {
                return false;
            } else {
                try {
                    if (!zipFile.getParentFile().exists()) {
                        zipFile.getParentFile().mkdirs();
                    }
                    zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
                    zipOutputStream.setEncoding(DEFAULT_CHARSET);
                    compress(Paths.get(sourceFilePath), zipOutputStream, BLANK);
                    return true;
                } finally {
                    if (notEmpty(zipOutputStream)) {
                        zipOutputStream.close();
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param file
     * @param out
     * @param basedir
     * @throws IOException
     */
    private static void compress(Path sourceFilePath, ZipOutputStream out, String basedir) throws IOException {
        DirectoryStream<Path> stream = null;
        try {
            stream = Files.newDirectoryStream(sourceFilePath);
            for (Path entry : stream) {
                BasicFileAttributes attrs = Files.readAttributes(entry, BasicFileAttributes.class);
                if (attrs.isDirectory()) {
                    ZipEntry zipEntry = new ZipEntry(entry.toString());
                    out.putNextEntry(zipEntry);
                    compress(entry.getFileName(), out, basedir);
                } else {
                    compressFile(entry.getFileName().toFile(), out, basedir);
                }
            }
        } catch (IOException e) {
        } finally {
            try {
                if (notEmpty(stream)) {
                    stream.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * @param file
     * @param out
     * @param basedir
     * @throws IOException
     */
    private static void compressFile(File file, ZipOutputStream out, String basedir) throws IOException {
        if (!file.exists()) {
            return;
        }
        ZipEntry entry = new ZipEntry(basedir + file.getName());
        out.putNextEntry(entry);
        write(new FileInputStream(file), out, false);
    }

    /**
     * @param zipFilePath
     * @param targetPath
     * @throws IOException
     */
    public static void unzip(String zipFilePath, String targetPath) throws IOException {
        ZipFile zipFile = new ZipFile(zipFilePath);
        String directoryPath = "";
        if (null == targetPath || "".equals(targetPath)) {
            directoryPath = zipFilePath.substring(0, zipFilePath.lastIndexOf("."));
        } else {
            directoryPath = targetPath;
        }
        Enumeration<ZipEntry> entryEnum = zipFile.getEntries();
        if (notEmpty(entryEnum)) {
            while (entryEnum.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) entryEnum.nextElement();
                if (zipEntry.isDirectory()) {
                    directoryPath = directoryPath + File.separator + zipEntry.getName();
                    File dir = new File(directoryPath + File.separator + zipEntry.getName());
                    dir.mkdirs();
                } else {
                    File targetFile = new File(directoryPath + File.separator + zipEntry.getName());
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }
                    write(zipFile.getInputStream(zipEntry), new FileOutputStream(targetFile));
                }
            }
        }

    }

    public static void main(String arg[]) {
        try {
            ZipUtils.zip("F:\\Users\\kerneler\\文档\\OneDrive\\FreeMarker", "F:\\Users\\kerneler\\文档\\OneDrive\\aa.zip");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
