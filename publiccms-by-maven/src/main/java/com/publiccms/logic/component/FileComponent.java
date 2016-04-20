package com.publiccms.logic.component;

import static java.util.Collections.synchronizedList;
import static java.util.Collections.synchronizedMap;
import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static com.publiccms.logic.component.MetadataComponent.METADATA_FILE;
import static com.publiccms.logic.component.TemplateComponent.INCLUDE_DIRECTORY;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.sanluan.common.base.Base;
import com.sanluan.common.base.Cacheable;

/**
 * 
 * FileComponent 文件操作组件
 *
 */
@Component
public class FileComponent extends Base implements Cacheable {
    private static List<String> cachedlist = synchronizedList(new ArrayList<String>());
    private static Map<String, List<FileInfo>> cachedMap = synchronizedMap(new HashMap<String, List<FileInfo>>());

    private void clearCache(int size) {
        if (size < cachedlist.size()) {
            for (int i = 0; i < size / 10; i++) {
                cachedMap.remove(cachedlist.remove(0));
            }
        }
    }

    /**
     * 获取目录下文件列表
     * 
     * @param dirPath
     * @return
     * @throws IOException
     */
    public List<FileInfo> getFileList(String dirPath) {
        List<FileInfo> fileList = cachedMap.get(dirPath);
        if (empty(fileList)) {
            DirectoryStream<Path> stream = null;
            try {
                fileList = new ArrayList<FileInfo>();
                List<FileInfo> tempList = new ArrayList<FileInfo>();
                stream = Files.newDirectoryStream(Paths.get(dirPath));
                for (Path entry : stream) {
                    Path fileNamePath = entry.getFileName();
                    if (notEmpty(fileNamePath)) {
                        String fileName = fileNamePath.toString();
                        if (!METADATA_FILE.equalsIgnoreCase(fileName) && !INCLUDE_DIRECTORY.equalsIgnoreCase(fileName)) {
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
                fileList = new ArrayList<FileInfo>();
            } finally {
                try {
                    if (notEmpty(stream)) {
                        stream.close();
                    }
                } catch (IOException e) {
                }
            }
            clearCache(100);
            cachedlist.add(dirPath);
            cachedMap.put(dirPath, fileList);
        }
        return fileList;
    }

    /**
     * 写入文件
     * 
     * @param filePath
     * @param content
     * @return
     * @throws IOException
     */
    public boolean createFile(File file, String content) throws IOException {
        if (empty(file)) {
            writeStringToFile(file, content, DEFAULT_CHARSET);
            clear();
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
            clear();
            return true;
        }
        return false;
    }

    /**
     * 修改文件内容
     * 
     * @param filePath
     * @param content
     * @return
     * @throws IOException
     */
    public boolean updateFile(File file, String content) throws IOException {
        if (notEmpty(file)) {
            writeStringToFile(file, content, DEFAULT_CHARSET);
            clear();
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
        return new SimpleDateFormat("yyyy/MM/dd/HH-mm-ssSSSS").format(getDate()) + r.nextInt() + suffix;
    }

    /**
     * 获取文件后缀
     * 
     * @param originalFilename
     * @return
     */
    public String getSuffix(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
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
        clear();
        return dest.getName();
    }

    /**
     * 
     * FileInfo 文件信息封装类
     *
     */
    public class FileInfo {
        private String fileName;
        private boolean directory;
        private Date lastModifiedTime;
        private Date lastAccessTime;
        private Date creationTime;
        private long size;

        public FileInfo(String fileName, boolean directory, BasicFileAttributes attrs) {
            this.fileName = fileName;
            this.directory = directory;
            this.lastModifiedTime = new Date(attrs.lastModifiedTime().toMillis());
            this.lastAccessTime = new Date(attrs.lastAccessTime().toMillis());
            this.creationTime = new Date(attrs.creationTime().toMillis());
            this.size = attrs.size();
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Date getLastModifiedTime() {
            return lastModifiedTime;
        }

        public void setLastModifiedTime(Date lastModifiedTime) {
            this.lastModifiedTime = lastModifiedTime;
        }

        public Date getLastAccessTime() {
            return lastAccessTime;
        }

        public void setLastAccessTime(Date lastAccessTime) {
            this.lastAccessTime = lastAccessTime;
        }

        public Date getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(Date creationTime) {
            this.creationTime = creationTime;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public boolean isDirectory() {
            return directory;
        }

        public void setDirectory(boolean directory) {
            this.directory = directory;
        }
    }

    @Override
    public void clear() {
        cachedlist.clear();
        cachedMap.clear();
    }
}
