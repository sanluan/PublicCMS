package org.publiccms.logic.component.site;

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.publiccms.logic.component.template.TemplateComponent.INCLUDE_DIRECTORY;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.base.Base;

/**
 * 
 * FileComponent 文件操作组件
 *
 */
@Component
public class FileComponent implements Base {
    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM-dd/HH-mm-ssSSSS");

    /**
     * 获取目录下文件列表
     * 
     * @param dirPath
     * @return
     */
    public List<FileInfo> getFileList(String dirPath) {
        List<FileInfo> fileList = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dirPath))) {
            List<FileInfo> tempList = new ArrayList<>();
            for (Path entry : stream) {
                Path fileNamePath = entry.getFileName();
                if (null != fileNamePath) {
                    String fileName = fileNamePath.toString();
                    if (!fileName.endsWith(".data") && !INCLUDE_DIRECTORY.equalsIgnoreCase(fileName)) {
                        BasicFileAttributes attrs = Files.readAttributes(entry, BasicFileAttributes.class);
                        if (attrs.isDirectory()) {
                            fileList.add(new FileInfo(fileName, true, attrs));
                        } else {
                            tempList.add(new FileInfo(fileName, false, attrs));
                        }
                    }
                }
            }
            fileList.addAll(tempList);
        } catch (IOException e) {
        }
        return fileList;
    }

    /**
     * 写入文件
     * 
     * @param file
     * @param content
     * @return
     * @throws IOException
     */
    public boolean createFile(File file, String content) throws IOException {
        if (empty(file)) {
            writeStringToFile(file, content, DEFAULT_CHARSET);
            file.setReadable(true, false);
            file.setWritable(true, false);
            return true;
        }
        return false;
    }

    /**
     * 删除文件或目录
     * 
     * @param filePath
     * @return
     */
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (notEmpty(file)) {
            deleteQuietly(file);
            return true;
        }
        return false;
    }

    /**
     * 修改文件内容
     * 
     * @param file
     * @param content
     * @return
     * @throws IOException
     */
    public boolean updateFile(File file, String content) throws IOException {
        if (notEmpty(file)) {
            writeStringToFile(file, content, DEFAULT_CHARSET);
            return true;
        }
        return false;
    }

    /**
     * 获取模板文件内容
     * 
     * @param filePath
     * @return
     */
    public String getFileContent(String filePath) {
        try {
            return readFileToString(new File(filePath), DEFAULT_CHARSET);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 获取文件名
     * 
     * @param suffix
     * @return
     */
    public String getUploadFileName(String suffix) {
        StringBuilder sb = new StringBuilder("upload/");
        synchronized (dateFormat) {
            sb.append(dateFormat.format(getDate()));
        }
        return sb.append(r.nextInt()).append(suffix).toString();
    }

    /**
     * 获取文件后缀
     * 
     * @param originalFilename
     * @return
     */
    public String getSuffix(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf(DOT), originalFilename.length());
    }

    /**
     * 上传文件
     * 
     * @param file
     * @param fileName
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    public String upload(MultipartFile file, String fileName) throws IllegalStateException, IOException {
        File dest = new File(fileName);
        dest.getParentFile().mkdirs();
        file.transferTo(dest);
        dest.setReadable(true, false);
        dest.setWritable(true, false);
        return dest.getName();
    }

    /**
     * 
     * FileInfo 文件信息封装类
     *
     */
    /**
     *
     * FileInfo
     * 
     */
    public class FileInfo {
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
         * @return
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
         * @return
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
         * @return
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
         * @return
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
         * @return
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
         * @return
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
