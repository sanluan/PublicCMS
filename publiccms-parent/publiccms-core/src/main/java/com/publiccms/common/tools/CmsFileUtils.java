package com.publiccms.common.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.views.pojo.entities.FileSize;

/**
 *
 * CmsFileUtils 文件操作组件
 *
 */
public class CmsFileUtils {
    public static final String UPLOAD_PATH = "upload/";
    public static final String USER_PRIVATE_PATH = "user/";
    public static final String METADATA_PATH = "metadata/";
    public static final Pattern UPLOAD_FILE_PATTERN = Pattern.compile(CommonUtils.joinString(".*(", UPLOAD_PATH, ".*)"));
    public static final String ORDERFIELD_FILENAME = "fileName";
    public static final String ORDERFIELD_FILESIZE = "fileSize";
    public static final String ORDERFIELD_CREATEDATE = "createDate";
    public static final String ORDERFIELD_MODIFIEDDATE = "modifiedDate";
    private static final FileInfoComparator FILENAME_COMPARATOR = new FileInfoComparator();
    private static final FileInfoComparator FILESIZE_COMPARATOR = new FileInfoComparator(ORDERFIELD_FILESIZE);
    private static final FileInfoComparator CREATEDATE_COMPARATOR = new FileInfoComparator(ORDERFIELD_CREATEDATE);
    private static final FileInfoComparator MODIFIEDDATE_COMPARATOR = new FileInfoComparator(ORDERFIELD_MODIFIEDDATE);
    public static final String HEADERS_SEND_CTRL = "Sendfile";
    public static final String HEADERS_SEND_NGINX = "X-Accel-Redirect";
    public static final String HEADERS_SEND_APACHE = "X-Sendfile";
    public static final String NGINX_PRIVATEFILE_PREFIX = "/privatefile/";
    /**
     * 
     */
    public static final List<String> IMAGE_FILE_SUFFIXS_LIST = Arrays.asList(".png", ".jpg", ".jpeg", ".gif", ".bmp", ".svg");

    /**
     * 
     */
    /**
     * 
     */
    public static final String[] AUDIO_FILE_SUFFIXS = new String[] { ".mp3", ".wav", ".mid" };

    /**
     * 
     */
    public static final String[] IMAGE_FILE_SUFFIXS = new String[] { ".png", ".jpg", ".jpeg", ".gif", ".svg", ".bmp" };
    /**
     * 
     */
    public static final String[] DOCUMENT_FILE_SUFFIXS = new String[] { ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".pdf",
            ".txt", ".md", ".xml", ".ofd" };
    /**
     * 
     */
    public static final String[] VIDEO_FILE_SUFFIXS = new String[] { ".flv", ".swf", ".mkv", ".avi", ".rm", ".rmvb", ".mpeg",
            ".mpg", ".ogg", ".ogv", ".mov", ".wmv", ".mp4", ".webm" };
    /**
     * 
     */
    public static final String[] OTHER_FILE_SUFFIXS = new String[] { ".rar", ".zip", ".tar", ".gz", ".7z", ".bz2", ".cab", ".iso",
            ".psd" };
    /**
     * 
     */
    public static final String[] ALLOW_FILES = ArrayUtils.addAll(
            ArrayUtils.addAll(ArrayUtils.addAll(ArrayUtils.addAll(AUDIO_FILE_SUFFIXS, VIDEO_FILE_SUFFIXS), IMAGE_FILE_SUFFIXS),
                    DOCUMENT_FILE_SUFFIXS),
            OTHER_FILE_SUFFIXS);
    /**
     * 
     */
    public static final String[] IMAGE_FILETYPES = new String[] { CmsFileUtils.FILE_TYPE_IMAGE };
    /**
     * 
     */
    public static final String[] OTHER_FILETYPES = new String[] { CmsFileUtils.FILE_TYPE_VIDEO, CmsFileUtils.FILE_TYPE_AUDIO,
            CmsFileUtils.FILE_TYPE_DOCUMENT, CmsFileUtils.FILE_TYPE_OTHER };

    /**
     * 
     */
    public static final String[] VIDEO_FILETYPES = new String[] { CmsFileUtils.FILE_TYPE_VIDEO };
    /**
     * 
     */
    public static final String FILE_TYPE_IMAGE = "image";
    /**
     * 
     */
    public static final String FILE_TYPE_VIDEO = "video";
    /**
     * 
     */
    public static final String FILE_TYPE_DOCUMENT = "document";
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
        return getFileList(dirPath, true, orderField);
    }

    /**
     * 获取目录下文件列表
     *
     * @param dirPath
     * @param parentPath
     * @param word
     * @return file result list
     */
    public static List<FileSearchResult> searchFileList(String dirPath, String parentPath, String word) {
        List<FileSearchResult> fileList = new ArrayList<>();
        if (CommonUtils.notEmpty(word)) {
            searchFileList(Paths.get(dirPath), parentPath, word, fileList);
        }
        return fileList;
    }

    /**
     * 获取目录下文件列表
     *
     * @param dirPath
     * @param fileList
     * @param word
     * @param replace
     */
    public static void replaceFileList(String dirPath, List<FileReplaceResult> fileList, String word, String replace) {
        if (CommonUtils.notEmpty(fileList)) {
            for (FileReplaceResult result : fileList) {
                if (CommonUtils.notEmpty(result.getPath()) && null != result.getIndexs()) {
                    File file = Paths.get(dirPath, result.getPath()).toFile();
                    try {
                        List<String> list = FileUtils.readLines(file, CommonConstants.DEFAULT_CHARSET_NAME);
                        int i = 0, j = 0, n = 0;
                        for (String line : list) {
                            if (line.contains(word)) {
                                if (result.getIndexs().length > j && j == result.getIndexs()[n]) {
                                    list.set(i, line.replace(word, replace));
                                    n++;
                                }
                                j++;
                            }
                            i++;
                        }
                        FileUtils.writeLines(file, CommonConstants.DEFAULT_CHARSET_NAME, list);
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    /**
     * 获取目录下文件列表
     *
     * @param dirPath
     * @param parentPath
     * @param word
     * @param fileList
     */
    private static void searchFileList(Path dirPath, String parentPath, String word, List<FileSearchResult> fileList) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
            for (Path entry : stream) {
                Path fileNamePath = entry.getFileName();
                if (null != fileNamePath) {
                    String fileName = fileNamePath.toString();
                    if (!parentPath.endsWith(CommonConstants.SEPARATOR)) {
                        parentPath = CommonUtils.joinString(parentPath, CommonConstants.SEPARATOR);
                    }
                    File file = entry.toFile();
                    if (file.isDirectory()) {
                        if (!TemplateComponent.INCLUDE_DIRECTORY.equalsIgnoreCase(fileName)) {
                            searchFileList(entry, CommonUtils.joinString(parentPath, fileName), word, fileList);
                        }
                    } else if (!fileName.endsWith(".data")) {
                        List<String> matchList = new ArrayList<>();
                        List<String> list = FileUtils.readLines(entry.toFile(), CommonConstants.DEFAULT_CHARSET_NAME);
                        for (String line : list) {
                            if (line.contains(word)) {
                                matchList.add(line);
                            }
                        }
                        if (!matchList.isEmpty()) {
                            FileSearchResult result = new FileSearchResult();
                            result.setPath(CommonUtils.joinString(parentPath, fileName));
                            result.setMatchList(matchList);
                            fileList.add(result);
                        }
                    }
                }
            }
        } catch (IOException e) {
        }
    }

    /**
     * 获取目录下文件列表
     *
     * @param dirPath
     * @param useFilter
     * @param orderField
     * @return file info list
     */
    public static List<FileInfo> getFileList(String dirPath, boolean useFilter, String orderField) {
        List<FileInfo> fileList = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dirPath))) {
            for (Path entry : stream) {
                Path fileNamePath = entry.getFileName();
                if (null != fileNamePath) {
                    String fileName = fileNamePath.toString();
                    if (!useFilter
                            || !fileName.endsWith(".data") && !TemplateComponent.INCLUDE_DIRECTORY.equalsIgnoreCase(fileName)) {
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

    /**
     * @param filepath
     * @param data
     * @throws IOException
     */
    public static void writeByteArrayToFile(String filepath, byte[] data) throws IOException {
        FileUtils.writeByteArrayToFile(new File(filepath), data);
    }

    /**
     * @param filepath
     * @param suffix
     * @return fileSize
     */
    public static FileSize getFileSize(String filepath, String suffix) {
        return getFileSize(new File(filepath), suffix);
    }

    /**
     * @param file
     * @param suffix
     * @return fileSize
     */
    private static FileSize getFileSize(File file, String suffix) {
        if (null != suffix && !suffix.startsWith(CommonConstants.DOT)) {
            suffix = CommonUtils.joinString(CommonConstants.DOT, suffix);
        }
        if (ArrayUtils.contains(IMAGE_FILE_SUFFIXS, suffix)) {
            FileSize fileSize = new FileSize();
            fileSize.setFileSize(file.length());
            try (FileInputStream fis = new FileInputStream(file)) {
                BufferedImage bufferedImg = ImageIO.read(fis);
                if (null != bufferedImg) {
                    fileSize.setWidth(bufferedImg.getWidth());
                    fileSize.setHeight(bufferedImg.getHeight());
                }
            } catch (Exception e) {
            }
            return fileSize;
        }
        return EMPTY;
    }

    /**
     * @param source
     * @param destination
     * @param suffix
     * @throws IOException
     */
    public static void copyInputStreamToFile(InputStream source, String destination) throws IOException {
        File dest = new File(destination);
        FileUtils.copyInputStreamToFile(source, dest);
    }

    /**
     * @param source
     * @param destination
     * @param suffix
     * @throws IOException
     */
    public static void copyFileToFile(String source, String destination) throws IOException {
        FileUtils.copyFile(new File(source), new File(destination));
    }

    /**
     * @param filepath
     * @param outputStream
     * @throws IOException
     */
    public static void copyFileToOutputStream(String filepath, OutputStream outputStream) throws IOException {
        IOUtils.copy(new FileInputStream(filepath), outputStream);
    }

    /**
     * @param filepath
     */
    public static void mkdirs(String filepath) {
        File file = new File(filepath);
        file.mkdirs();
    }

    /**
     * @param filepath
     */
    public static void mkdirsParent(String filepath) {
        File file = new File(filepath);
        file.getParentFile().mkdirs();
    }

    /**
     * @param filepath
     * @return
     */
    public static boolean isDirectory(String filepath) {
        File file = new File(filepath);
        return CommonUtils.notEmpty(file) && file.isDirectory();
    }

    /**
     * @param filepath
     * @return
     */
    public static boolean isFile(String filepath) {
        File file = new File(filepath);
        return CommonUtils.notEmpty(file) && file.isFile();
    }

    /**
     * @param filepath
     * @return
     */
    public static boolean exists(String filepath) {
        return CommonUtils.notEmpty(new File(filepath));
    }

    /**
     * 写入文件
     *
     * @param filepath
     * @param content
     * @return whether to create successfully
     * @throws IOException
     */
    public static boolean createFile(String filepath, String content) throws IOException {
        File file = new File(filepath);
        if (CommonUtils.empty(file)) {
            FileUtils.writeStringToFile(file, content, CommonConstants.DEFAULT_CHARSET_NAME);
            return true;
        }
        return false;
    }

    /**
     * 移动文件或目录
     *
     * @param filepath
     * @param backupFilePath
     * @return whether to move successfully
     */
    public static boolean moveFile(String filepath, String backupFilePath) {
        File file = new File(filepath);
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
     * 移动文件或目录
     *
     * @param filepath
     * @return whether to move successfully
     */
    public static boolean delete(String filepath) {
        File file = new File(filepath);
        if (CommonUtils.notEmpty(file)) {
            FileUtils.deleteQuietly(file);
            return true;
        }
        return false;
    }

    /**
     * 修改文件内容
     * 
     * @param filepath
     * @param historyFilePath
     * @param content
     * @return whether to modify successfully
     * @throws IOException
     */
    public static boolean updateFile(String filepath, String historyFilePath, String content) throws IOException {
        File file = new File(filepath);
        if (CommonUtils.notEmpty(file) && null != content) {
            File history = new File(historyFilePath);
            if (null != history.getParentFile()) {
                history.getParentFile().mkdirs();
            }
            FileUtils.copyFile(file, history);
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                outputStream.write(content.getBytes(CommonConstants.DEFAULT_CHARSET));
            }
            return true;
        }
        return false;
    }

    /**
     * 获取文件内容
     *
     * @param filepath
     * @param suffix
     * @return is safe
     */
    public static boolean isSafe(String filepath, String suffix) {
        if (CommonUtils.notEmpty(suffix) && suffix.endsWith(ImageUtils.FORMAT_NAME_SVG)) {
            File file = new File(filepath);
            try {
                if (file.isFile()) {
                    return ImageUtils.svgSafe(file);
                }
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取文件内容
     *
     * @param filepath
     * @return file content
     */
    public static String getFileContent(String filepath) {
        File file = new File(filepath);
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
        StringBuilder sb = new StringBuilder(UPLOAD_PATH);
        sb.append(DateFormatUtils.getDateFormat(DateFormatUtils.UPLOAD_FILE_NAME_FORMAT_STRING).format(CommonUtils.getDate()));
        sb.append(CommonConstants.random.nextInt());
        if (!suffix.contains(CommonConstants.DOT)) {
            sb.append(CommonConstants.DOT);
        }
        sb.append(suffix);
        return sb.toString();
    }

    /**
     * 获取私有文件前面字符串
     * 
     * @param expiry
     * @param filepath
     * @return avatar file name
     */
    public static String getPrivateFileSignString(long expiry, String filepath) {
        return CommonUtils.joinString("expiry=", expiry, "&filePath=", filepath);
    }

    /**
     * 获取用户私有文件名
     * 
     * @param userId
     * @param filepath
     * @return user private file name
     */
    public static String getUserPrivateFileName(long userId, String filepath) {
        return CommonUtils.joinString(USER_PRIVATE_PATH, userId, CommonConstants.SEPARATOR, filepath);
    }

    /**
     * 获取文件名
     * 
     * @param filePath
     * @return suffix
     */
    public static String getFileName(String filePath) {
        if (null != filePath) {
            int index = filePath.lastIndexOf(CommonConstants.SEPARATOR);
            if (-1 < index) {
                return filePath.substring(filePath.lastIndexOf(CommonConstants.SEPARATOR) + 1, filePath.length());
            }
        }
        return null;
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
        if (null != suffix && !suffix.startsWith(CommonConstants.DOT)) {
            suffix = CommonUtils.joinString(CommonConstants.DOT, suffix);
        }
        if (ArrayUtils.contains(IMAGE_FILE_SUFFIXS, suffix)) {
            return FILE_TYPE_IMAGE;
        } else if (ArrayUtils.contains(VIDEO_FILE_SUFFIXS, suffix)) {
            return FILE_TYPE_VIDEO;
        } else if (ArrayUtils.contains(AUDIO_FILE_SUFFIXS, suffix)) {
            return FILE_TYPE_AUDIO;
        } else if (ArrayUtils.contains(DOCUMENT_FILE_SUFFIXS, suffix)) {
            return FILE_TYPE_DOCUMENT;
        } else {
            return FILE_TYPE_OTHER;
        }
    }

    /**
     * 上传文件
     *
     * @param data
     * @param fileName
     * @return file name
     * @throws IllegalStateException
     * @throws IOException
     */
    public static String upload(byte[] data, String fileName) throws IllegalStateException, IOException {
        return upload(data, fileName, null, null);
    }

    /**
     * 上传文件
     *
     * @param data
     * @param fileName
     * @param originalName
     * @param metadataPath
     * @return file name
     * @throws IllegalStateException
     * @throws IOException
     */
    public static String upload(byte[] data, String fileName, String originalName, String metadataPath)
            throws IllegalStateException, IOException {
        File dest = new File(fileName);
        dest.getParentFile().mkdirs();
        FileUtils.writeByteArrayToFile(dest, data);
        if (CommonUtils.notEmpty(originalName) && CommonUtils.notEmpty(metadataPath)) {
            try {
                FileUtils.writeStringToFile(new File(metadataPath), originalName, CommonConstants.DEFAULT_CHARSET_NAME);
            } catch (IOException e) {
            }
        }
        return dest.getName();
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
     * FileSearchResult 文件查找结果
     * 
     */
    public static class FileReplaceResult {
        private String path;
        private int[] indexs;

        /**
         * @return the path
         */
        public String getPath() {
            return path;
        }

        /**
         * @param path
         *            the path to set
         */
        public void setPath(String path) {
            this.path = path;
        }

        /**
         * @return the indexs
         */
        public int[] getIndexs() {
            return indexs;
        }

        /**
         * @param indexs
         *            the indexs to set
         */
        public void setIndexs(int[] indexs) {
            this.indexs = indexs;
        }

        @Override
        public String toString() {
            return CommonUtils.joinString("FileReplaceResult [path=", path, ", indexs=", Arrays.toString(indexs), "]");
        }
    }

    /**
     * FileSearchResult 文件查找结果
     * 
     */
    public static class FileSearchResult {
        private String path;
        private List<String> matchList;

        /**
         * @return the path
         */
        public String getPath() {
            return path;
        }

        /**
         * @param path
         *            the path to set
         */
        public void setPath(String path) {
            this.path = path;
        }

        /**
         * @return the matchList
         */
        public List<String> getMatchList() {
            return matchList;
        }

        /**
         * @param matchList
         *            the matchList to set
         */
        public void setMatchList(List<String> matchList) {
            this.matchList = matchList;
        }

        @Override
        public String toString() {
            return CommonUtils.joinString("FileSearchResult [path=", path, ", matchList=", matchList, "]");
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
    }
}
