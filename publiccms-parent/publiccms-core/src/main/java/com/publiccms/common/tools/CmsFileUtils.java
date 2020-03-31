package com.publiccms.common.tools;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.views.pojo.entities.FileSize;

/**
 *
 * FileComponent 文件操作组件
 *
 */
public class CmsFileUtils {
    private static final String FILE_NAME_FORMAT_STRING = "yyyy/MM-dd/HH-mm-ssSSSS";
    private static final String ORDERFIELD_FILENAME = "fileName";
    private static final String ORDERFIELD_FILESIZE = "fileSize";
    private static final String ORDERFIELD_CREATEDATE = "createDate";
    private static final String ORDERFIELD_MODIFIEDDATE = "modifiedDate";
    private static final FileInfoComparator FILENAME_COMPARATOR = new FileInfoComparator();
    private static final FileInfoComparator FILESIZE_COMPARATOR = new FileInfoComparator(ORDERFIELD_FILESIZE);
    private static final FileInfoComparator CREATEDATE_COMPARATOR = new FileInfoComparator(ORDERFIELD_CREATEDATE);
    private static final FileInfoComparator MODIFIEDDATE_COMPARATOR = new FileInfoComparator(ORDERFIELD_MODIFIEDDATE);
    /**
     * 
     */
    public static final List<String> IMAGE_FILE_SUFFIXS = Arrays.asList(new String[] { ".png", ".jpg", ".jpeg", ".gif", ".bmp" });

    /**
     * 
     */
    public static final List<String> VIDEO_FILE_SUFFIXS = Arrays.asList(new String[] { ".flv", ".swf", ".mkv", ".avi", ".rm",
            ".rmvb", ".mpeg", ".mpg", ".ogg", ".ogv", ".mov", ".wmv", ".mp4", ".webm" });
    /**
     * 
     */
    public static final List<String> AUDIO_FILE_SUFFIXS = Arrays.asList(new String[] { ".mp3", ".wav", ".mid" });
    /**
     * 
     */
    public static final String FILE_TYPE_IMAGE = "image";
    /**
     * 
     */
    public static final String DEFAULT_FORMAT_NAME = "jpg";
    /**
     * 
     */
    public static final String FILE_TYPE_VIDEO = "video";
    /**
     * 
     */
    public static final String FILE_TYPE_AUDIO = "audio";
    /**
     * 
     */
    public static final String FILE_TYPE_OTHER = "other";
    /**
     * 
     */
    public static final FileSize EMPTY = new FileSize();

    /**
     * 获取目录下文件列表
     *
     * @param dirPath
     * @param orderField
     * @return file info list
     */
    public static List<FileInfo> getFileList(String dirPath, String orderField) {
        List<FileInfo> fileList = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dirPath))) {
            for (Path entry : stream) {
                Path fileNamePath = entry.getFileName();
                if (null != fileNamePath) {
                    String fileName = fileNamePath.toString();
                    if (!fileName.endsWith(".data") && !TemplateComponent.INCLUDE_DIRECTORY.equalsIgnoreCase(fileName)) {
                        BasicFileAttributes attrs = Files.readAttributes(entry, BasicFileAttributes.class);
                        fileList.add(new FileInfo(fileName, attrs.isDirectory(), attrs));
                    }
                }
            }
            if (null == orderField) {
                orderField = ORDERFIELD_FILENAME;
            }
            Comparator<FileInfo> comparator;
            switch (orderField) {
            case ORDERFIELD_MODIFIEDDATE:
                comparator = MODIFIEDDATE_COMPARATOR;
                break;
            case ORDERFIELD_CREATEDATE:
                comparator = CREATEDATE_COMPARATOR;
                break;
            case ORDERFIELD_FILESIZE:
                comparator = FILESIZE_COMPARATOR;
                break;
            default:
                comparator = FILENAME_COMPARATOR;
            }
            Collections.sort(fileList, comparator);
        } catch (IOException e) {
        }
        return fileList;
    }

    public static void thumb(String sourceFilePath, String thumbFilePath, int width, int height, String suffix)
            throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(thumbFilePath);) {
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            BufferedImage sourceImage = ImageIO.read(new File(sourceFilePath));
            Image scaledImage = sourceImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            img.createGraphics().drawImage(scaledImage, 0, 0, null);
            if (null != suffix && suffix.length() > 1) {
                ImageIO.write(img, suffix.substring(1), outputStream);
            } else {
                ImageIO.write(img, DEFAULT_FORMAT_NAME, outputStream);
            }
        }
    }

    /**
     * @param filePath
     * @param data
     * @throws IOException
     */
    public static void writeByteArrayToFile(String filePath, byte[] data) throws IOException {
        FileUtils.writeByteArrayToFile(new File(filePath), data);
    }

    /**
     * @param filePath
     * @param suffix
     * @return fileSize
     */
    public static FileSize getFileSize(String filePath, String suffix) {
        if (IMAGE_FILE_SUFFIXS.contains(suffix)) {
            try {
                FileInputStream fis = new FileInputStream(filePath);
                BufferedImage bufferedImg = ImageIO.read(fis);
                FileSize fileSize = new FileSize();
                fileSize.setWidth(bufferedImg.getWidth());
                fileSize.setHeight(bufferedImg.getHeight());
                return fileSize;
            } catch (IOException e) {
            }
        }
        return EMPTY;
    }

    /**
     * @param source
     * @param destination
     * @throws IOException
     */
    public static void copyInputStreamToFile(InputStream source, String destination) throws IOException {
        File dest = new File(destination);
        FileUtils.copyInputStreamToFile(source, dest);
    }

    /**
     * @param filePath
     */
    public static void mkdirs(String filePath) {
        File file = new File(filePath);
        file.mkdirs();
    }

    /**
     * @param filePath
     * @return
     */
    public static boolean isDirectory(String filePath) {
        File file = new File(filePath);
        return CommonUtils.notEmpty(file) && file.isDirectory();
    }

    /**
     * @param filePath
     * @return
     */
    public static boolean isFile(String filePath) {
        File file = new File(filePath);
        return CommonUtils.notEmpty(file) && file.isFile();
    }

    /**
     * @param filePath
     * @return
     */
    public static boolean exists(String filePath) {
        return CommonUtils.notEmpty(new File(filePath));
    }

    /**
     * 写入文件
     *
     * @param filePath
     * @param content
     * @return whether to create successfully
     * @throws IOException
     */
    public static boolean createFile(String filePath, String content) throws IOException {
        File file = new File(filePath);
        if (CommonUtils.empty(file)) {
            FileUtils.writeStringToFile(file, content, CommonConstants.DEFAULT_CHARSET_NAME);
            return true;
        }
        return false;
    }

    /**
     * 移动文件或目录
     *
     * @param filePath
     * @param backupFilePath
     * @return whether to move successfully
     */
    public static boolean moveFile(String filePath, String backupFilePath) {
        File file = new File(filePath);
        if (CommonUtils.notEmpty(file)) {
            File backupFile = new File(backupFilePath);
            try {
                if (backupFile.exists()) {
                    FileUtils.deleteQuietly(backupFile);
                }
                if (file.isDirectory()) {
                    FileUtils.moveDirectory(file, backupFile);
                } else {
                    FileUtils.moveFile(file, backupFile);
                }
                return true;
            } catch (IOException e) {
            }
        }
        return false;
    }

    /**
     * 修改文件内容
     * 
     * @param filePath
     * @param historyFilePath
     * @param content
     * @return whether to modify successfully
     * @throws IOException
     */
    public static boolean updateFile(String filePath, String historyFilePath, String content) throws IOException {
        File file = new File(filePath);
        if (CommonUtils.notEmpty(file) && null != content) {
            File history = new File(historyFilePath);
            if (null != history.getParentFile()) {
                history.getParentFile().mkdirs();
            }
            FileUtils.copyFile(file, history);
            try (FileOutputStream outputStream = new FileOutputStream(file);) {
                outputStream.write(content.getBytes(CommonConstants.DEFAULT_CHARSET));
            }
            return true;
        }
        return false;
    }

    /**
     * 获取文件内容
     *
     * @param filePath
     * @return file content
     */
    public static String getFileContent(String filePath) {
        File file = new File(filePath);
        try {
            if (file.isFile()) {
                return FileUtils.readFileToString(file, CommonConstants.DEFAULT_CHARSET_NAME);
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    /**
     * 获取文件名
     *
     * @param suffix
     * @return upload file name
     */
    public static String getUploadFileName(String suffix) {
        StringBuilder sb = new StringBuilder("upload/");
        sb.append(DateFormatUtils.getDateFormat(FILE_NAME_FORMAT_STRING).format(CommonUtils.getDate()));
        sb.append(CommonConstants.random.nextInt());
        if (!suffix.contains(CommonConstants.DOT)) {
            sb.append(CommonConstants.DOT);
        }
        sb.append(suffix);
        return sb.toString();
    }

    /**
     * 获取文件后缀
     *
     * @param originalFilename
     * @return suffix
     */
    public static String getSuffix(String originalFilename) {
        if (null != originalFilename) {
            int index = originalFilename.lastIndexOf(CommonConstants.DOT);
            if (-1 < index) {
                return originalFilename.substring(originalFilename.lastIndexOf(CommonConstants.DOT), originalFilename.length())
                        .toLowerCase();
            }
        }
        return null;
    }

    public static String getFileType(String suffix) {
        if (IMAGE_FILE_SUFFIXS.contains(suffix)) {
            return FILE_TYPE_IMAGE;
        } else if (VIDEO_FILE_SUFFIXS.contains(suffix)) {
            return FILE_TYPE_VIDEO;
        } else if (AUDIO_FILE_SUFFIXS.contains(suffix)) {
            return FILE_TYPE_AUDIO;
        } else {
            return FILE_TYPE_OTHER;
        }
    }

    /**
     * 上传文件
     *
     * @param file
     * @param fileName
     * @return file name
     * @throws IllegalStateException
     * @throws IOException
     */
    public static String upload(MultipartFile file, String fileName) throws IllegalStateException, IOException {
        File dest = new File(fileName);
        dest.getParentFile().mkdirs();
        file.transferTo(dest);
        return dest.getName();
    }

    public static class FileInfoComparator implements Comparator<FileInfo> {
        private String mode = ORDERFIELD_FILENAME;

        public FileInfoComparator() {
        }

        public FileInfoComparator(String mode) {
            if (null != mode) {
                this.mode = mode;
            }
        }

        @Override
        public int compare(FileInfo o1, FileInfo o2) {
            if (o1.isDirectory() && !o2.isDirectory()) {
                return -1;
            } else if (!o1.isDirectory() && o2.isDirectory()) {
                return 1;
            } else {
                int result = 0;
                switch (mode) {
                case ORDERFIELD_MODIFIEDDATE:
                    result = o2.getLastModifiedTime().compareTo(o1.getLastModifiedTime());
                    break;
                case ORDERFIELD_CREATEDATE:
                    result = o2.getCreationTime().compareTo(o1.getCreationTime());
                    break;
                case ORDERFIELD_FILESIZE:
                    result = Long.compare(o2.getSize(), o1.getSize());
                    break;
                default:
                    result = o1.getFileName().toLowerCase().compareTo(o2.getFileName().toLowerCase());
                }
                return result;
            }
        }

    }

    /**
     *
     * FileInfo 文件信息封装类
     *
     */
    public static class FileInfo {
        private String fileName;
        private boolean directory;
        private Date lastModifiedTime;
        private Date lastAccessTime;
        private Date creationTime;
        private long size;

        /**
         * @param fileName
         * @param directory
         * @param attrs
         */
        public FileInfo(String fileName, boolean directory, BasicFileAttributes attrs) {
            this.fileName = fileName;
            this.directory = directory;
            this.lastModifiedTime = new Date(attrs.lastModifiedTime().toMillis());
            this.lastAccessTime = new Date(attrs.lastAccessTime().toMillis());
            this.creationTime = new Date(attrs.creationTime().toMillis());
            this.size = attrs.size();
        }

        /**
         * @return file name
         */
        public String getFileName() {
            return fileName;
        }

        /**
         * @param fileName
         */
        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        /**
         * @return last modified time
         */
        public Date getLastModifiedTime() {
            return lastModifiedTime;
        }

        /**
         * @param lastModifiedTime
         */
        public void setLastModifiedTime(Date lastModifiedTime) {
            this.lastModifiedTime = lastModifiedTime;
        }

        /**
         * @return last access time
         */
        public Date getLastAccessTime() {
            return lastAccessTime;
        }

        /**
         * @param lastAccessTime
         */
        public void setLastAccessTime(Date lastAccessTime) {
            this.lastAccessTime = lastAccessTime;
        }

        /**
         * @return creation time
         */
        public Date getCreationTime() {
            return creationTime;
        }

        /**
         * @param creationTime
         */
        public void setCreationTime(Date creationTime) {
            this.creationTime = creationTime;
        }

        /**
         * @return size
         */
        public long getSize() {
            return size;
        }

        /**
         * @param size
         */
        public void setSize(long size) {
            this.size = size;
        }

        /**
         * @return directory
         */
        public boolean isDirectory() {
            return directory;
        }

        /**
         * @param directory
         */
        public void setDirectory(boolean directory) {
            this.directory = directory;
        }
    }
}
