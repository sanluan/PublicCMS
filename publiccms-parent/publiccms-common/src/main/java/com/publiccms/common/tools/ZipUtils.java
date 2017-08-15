package com.publiccms.common.tools;

import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.StreamUtils.write;

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

import com.publiccms.common.base.Base;

/**
 * 压缩/解压缩zip包处理类
 * 
 * ZipUtils
 *
 */
public class ZipUtils implements Base {
    static {
        System.setProperty("sun.zip.encoding", System.getProperty("sun.jnu.encoding"));
    }

    /**
     * @param sourceFilePath
     * @param zipFilePath
     * @return
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
            File zipFile = new File(zipFilePath);
            if (zipFile.exists() && !overwrite) {
                return false;
            } else {
                zipFile.getParentFile().mkdirs();
                try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));) {
                    compress(Paths.get(sourceFilePath), zipOutputStream, BLANK);
                    zipFile.setReadable(true, false);
                    zipFile.setWritable(true, false);
                    return true;
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
        if (Files.isDirectory(sourceFilePath)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceFilePath);) {
                for (Path entry : stream) {
                    BasicFileAttributes attrs = Files.readAttributes(entry, BasicFileAttributes.class);
                    String fullName = BLANK.equals(basedir) ? entry.toFile().getName()
                            : basedir + SEPARATOR + entry.toFile().getName();
                    if (attrs.isDirectory()) {
                        ZipEntry zipEntry = new ZipEntry(fullName + SEPARATOR);
                        out.putNextEntry(zipEntry);
                        compress(entry, out, fullName);
                    } else {
                        compressFile(entry.toFile(), out, fullName);
                    }
                }
            } catch (IOException e) {
            }
        } else {
            compressFile(sourceFilePath.toFile(), out, sourceFilePath.toFile().getName());
        }
    }

    /**
     * @param file
     * @param out
     * @param basedir
     * @throws IOException
     */
    private static void compressFile(File file, ZipOutputStream out, String fullName) throws IOException {
        if (notEmpty(file)) {
            ZipEntry entry = new ZipEntry(fullName);
            entry.setTime(file.lastModified());
            out.putNextEntry(entry);
            write(new FileInputStream(file), out, false);
        }
    }

    /**
     * @param zipFilePath
     * @throws IOException
     */
    public static void unzipHere(String zipFilePath) throws IOException {
        int index = zipFilePath.lastIndexOf("/");
        if (0 > index) {
            index = zipFilePath.lastIndexOf("\\");
        }
        unzip(zipFilePath, zipFilePath.substring(0, index), true);
    }

    /**
     * @param zipFilePath
     * @throws IOException
     */
    public static void unzip(String zipFilePath) throws IOException {
        unzip(zipFilePath, zipFilePath.substring(0, zipFilePath.lastIndexOf(DOT)), true);
    }

    /**
     * @param zipFilePath
     * @param targetPath
     * @param overwrite
     * @throws IOException
     */
    public static void unzip(String zipFilePath, String targetPath, boolean overwrite) throws IOException {
        ZipFile zipFile = new ZipFile(zipFilePath);
        Enumeration<? extends ZipEntry> entryEnum = zipFile.getEntries();
        if (null != entryEnum) {
            while (entryEnum.hasMoreElements()) {
                ZipEntry zipEntry = entryEnum.nextElement();
                if (zipEntry.isDirectory()) {
                    File dir = new File(targetPath + File.separator + zipEntry.getName());
                    dir.mkdirs();
                } else {
                    File targetFile = new File(targetPath + File.separator + zipEntry.getName());
                    if (!targetFile.exists() || overwrite) {
                        targetFile.getParentFile().mkdirs();
                        write(zipFile.getInputStream(zipEntry), new FileOutputStream(targetFile));
                    }
                }
            }
        }
        zipFile.close();
    }
}
