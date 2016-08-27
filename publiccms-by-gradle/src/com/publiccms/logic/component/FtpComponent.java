package com.publiccms.logic.component;

import static com.publiccms.logic.component.SiteComponent.STATIC_FILE_PATH_RESOURCE;
import static com.publiccms.logic.component.SiteComponent.STATIC_FILE_PATH_WEB;
import static com.publiccms.logic.component.SiteComponent.TASK_FILE_PATH;
import static com.publiccms.logic.component.SiteComponent.TEMPLATE_PATH;
import static com.publiccms.logic.component.SiteComponent.getFullFileName;
import static com.sanluan.common.tools.StreamUtils.write;
import static com.sanluan.common.tools.VerificationUtils.encode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.publiccms.entities.sys.SysFtpUser;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.sys.SysFtpUserService;
import com.publiccms.logic.service.sys.SysSiteService;
import com.sanluan.common.base.Base;

@Component
public class FtpComponent extends Base {
    public static final String DATE_FORMAT = "yyyyMMddHHmmss";
    public static final String LIST_DATE_FORMAT = "MMM dd HH:mm";
    public static final String LIST_DATE_FORMAT1 = "MMM dd yyyy";
    public static final String ROOT = SEPARATOR;
    public static final String[] VIRTUAL_FILE_PATHS = { STATIC_FILE_PATH_WEB, STATIC_FILE_PATH_RESOURCE, TASK_FILE_PATH,
            TEMPLATE_PATH };
    private FtpServer ftpServer;

    @Autowired
    public void createFtpServer(SysFtpUserService service, SysSiteService siteService, SiteComponent siteComponent,
            Environment env) {
        if ("true".equalsIgnoreCase(env.getProperty("ftp.enable", "true"))) {
            try {
                ftpServer = new FtpServer(siteComponent.getRootPath(), 21, service, siteService);
            } catch (IOException e) {
                log.error("Ftp Start Error :" + e.getMessage());
            }
        }
    }

    @PreDestroy
    public void close() {
        if (notEmpty(ftpServer)) {
            ftpServer.stop();
        }
    }

    public class FtpServer implements Runnable {
        private SysFtpUserService service;
        private SysSiteService siteService;
        private ServerSocket serverSocket;
        private String rootPath;

        public FtpServer(String rootPath, int port, SysFtpUserService service, SysSiteService siteService) throws IOException {
            this.rootPath = rootPath;
            this.service = service;
            this.siteService = siteService;
            serverSocket = new ServerSocket(port);
            log.info("Ftp Listen on " + port);
            new Thread(this).start();
        }

        public void stop() {
            try {
                serverSocket.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        @Override
        public void run() {
            while (!serverSocket.isClosed()) {
                try {
                    new Thread(new FtpHandler(serverSocket.accept(), rootPath, this)).start();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }

        public SysSite getSite(int siteId) {
            return siteService.getEntity(siteId);
        }

        public SysFtpUser getUser(String username) {
            return service.findByName(username);
        }
    }

    class FtpHandler extends Base implements Runnable {
        private FtpServer server;
        private Socket socket; // 用于控制的套接字
        private Socket transportSocket; // 用于传输的套接字
        private ServerSocket transportServerSocket; // 用于传输的套接字
        private boolean isPasv = false;
        private SysFtpUser user;
        private SysSite site;
        private String userPath;
        private String currentPath = ROOT;// 当前目录
        private String rootPath; // 根目录
        private int state = State.STATE_NEED_USERNAME; // 用户状态标识符,在checkPASS中设置
        private BufferedReader input;
        private PrintWriter output;
        private int type = 0; // 文件类型(ascII 或 bin)

        public FtpHandler(Socket socket, String rootPath, FtpServer ftpServer) {
            this.socket = socket;
            this.rootPath = rootPath;
            this.server = ftpServer;
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                log.error("The ftp server start error:" + e.getMessage());
            }
        }

        @Override
        public void run() {
            try {
                boolean flag = true;
                String inputString;
                pirnt("332 welcome to ThinServer.");
                output.flush();
                while (flag && null != (inputString = input.readLine())) {
                    String command;
                    String param = BLANK;
                    int index = inputString.indexOf(BLANK_SPACE);
                    if (-1 == index) {
                        command = inputString.toUpperCase();
                    } else {
                        command = inputString.substring(0, index).toUpperCase();
                        if (index < inputString.length()) {
                            param = inputString.substring(index + 1, inputString.length());
                        }
                    }
                    switch (state) {
                    case State.STATE_NEED_USERNAME:
                        flag = checkUsername(command, param);
                        break;
                    case State.STATE_NEED_PASSWORD:
                        flag = checkPassword(command, param);
                        break;
                    case State.STATE_READY:
                        switch (command) {
                        case "ABOR": // 中断数据连接程序
                            try {
                                if (notEmpty(transportSocket)) {
                                    transportSocket.close();
                                    if (isPasv) {
                                        transportServerSocket.close();
                                    }
                                }
                            } catch (Exception e) {
                                pirnt("451 failed to send.");
                            }
                            pirnt("421 service unavailable.");
                            break;
                        case "ACCT":// 系统特权帐号
                            pirnt("500 command not supported.");
                            break;
                        case "ALLO":// 为服务器上的文件存储器分配字节
                            pirnt("500 command not supported.");
                            break;
                        case "APPE":// 添加文件到服务器同名文件
                            pirnt("500 command not supported.");
                            break;
                        case "CDUP":// 到上一层目录
                            changeCurrentPath("..");
                            break;
                        case "CWD": // 到指定的目录
                            changeCurrentPath(param);
                            break;
                        case "DELE": // 删除指定文件
                            deleteFile(param);
                            break;
                        case "HELP": // 返回指定命令信息
                            pirnt("500 command not supported.");
                            break;
                        case "LIST": // 如果是文件名列出文件信息，如果是目录则列出文件列表
                            listFiles(param, false);
                            break;
                        case "NLST": // 列出指定目录内容
                            listFiles(param, true);
                            break;
                        case "MODE":
                            pirnt("500 command not supported.");
                            break;
                        case "MDTM":
                            lastModified(param);
                            break;
                        case "MKD": // 建立目录
                            makeDir(param);
                            break;
                        case "NOOP":
                            pirnt("200 ok.");
                            break;
                        case "PASV":
                            pasvMode();
                            break;
                        case "PORT": // IP 地址和两字节的端口 ID
                            portMode(param);
                            break;
                        case "PWD":
                        case "XPWD": // "当前目录" 信息
                            pirnt("257 \"" + currentPath + "\"");
                            break;
                        case "QUIT": // 退出
                            pirnt("221 close.");
                            if (notEmpty(transportSocket)) {
                                transportSocket.close();
                            }
                            flag = false;
                            break;
                        case "REIN":
                            pirnt("500 command not supported.");
                            break;
                        case "REST":
                            pirnt("500 command not supported.");
                            break;
                        case "RETR": // 从服务器中获得文件
                            getFile(param);
                            break;
                        case "RMD": // 删除指定目录
                            deleteDir(param);
                            break;
                        case "RNFR": // 对旧路径重命名
                            pirnt("500 command not supported.");
                            break;
                        case "RNTO": // 对旧路径重命名
                            pirnt("500 command not supported.");
                            break;
                        case "SITE": // 由服务器提供的站点特殊参数
                            pirnt("500 command not supported.");
                            break;
                        case "SIZE": // 文件大小
                            size(param);
                            break;
                        case "SMNT": // 挂载指定文件结构
                            pirnt("500 command not supported.");
                            break;
                        case "STAT": // 在当前程序或目录上返回信息
                            pirnt("500 command not supported.");
                            break;
                        case "STOR":// 储存（复制）文件到服务器上
                            reciveFile(param);
                            break;
                        case "STOU":// 储存文件到服务器名称上
                            pirnt("500 command not supported.");
                            break;
                        case "STRU":// 数据结构（F=文件，R=记录，P=页面）
                            pirnt("500 command not supported.");
                            break;
                        case "SYST":
                            pirnt("215 " + System.getProperty("os.name"));
                            break;
                        case "TYPE":// 数据类型（A=ASCII,E=EBCDIC,I=binary）
                            if ("A".equals(param)) {
                                type = State.TYPE_ASCII;
                                pirnt("200 changed to ASCII.");
                            } else if ("I".equals(param)) {
                                type = State.TYPE_IMAGE;
                                pirnt("200 changed to BINARY.");
                            } else {
                                pirnt("504 error paramter.");
                            }
                            break;
                        case "FEAT":
                            pirnt("extension supported:");
                            pirnt("MDTM");
                            pirnt("SIZE");
                            pirnt("PASV");
                            pirnt("211 ok.");
                            break;
                        default:
                            pirnt("500 error command.");
                            break;
                        }
                        break;
                    }
                }
                output.flush();
                input.close();
                output.close();
                socket.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        private void pirnt(String message) {
            output.println(message);
        }

        private boolean checkUsername(String command, String username) {
            if ("USER".equals(command)) {
                user = server.getUser(username);
                if (notEmpty(user)) {
                    site = server.getSite(user.getSiteId());
                    pirnt("331 need password.");
                    state = State.STATE_NEED_PASSWORD;
                    userPath = empty(user.getPath()) ? "" : user.getPath();
                    return true;
                }
                pirnt("501 user does't exist.");
            } else {
                pirnt("501 error command,need login.");
            }
            return false;
        }

        private void welcome() {
            state = State.STATE_READY;
            pirnt("230 login success,welcome " + user.getName() + " come from site " + user.getSiteId() + ".");
        }

        private boolean checkPassword(String command, String password) {
            if ("PASS".equals(command)) {
                if (notEmpty(password) && encode(password).equalsIgnoreCase(user.getPassword())) {
                    welcome();
                    return true;
                }
                pirnt("530 login failed.");
            } else {
                pirnt("501 error command,need password.");
            }
            return false;
        }

        private void pasvMode() {
            try {
                transportServerSocket = new ServerSocket(0, 1, socket.getLocalAddress());
                InetAddress inetAddress = transportServerSocket.getInetAddress();
                if (inetAddress.isAnyLocalAddress()) {
                    inetAddress = socket.getLocalAddress();
                }
                String str = BLANK;
                byte[] arrayOfByte = inetAddress.getAddress();
                for (int i = 0; i < arrayOfByte.length; ++i) {
                    str = str + (arrayOfByte[i] & 0xFF) + COMMA_DELIMITED;
                }
                str = str + (transportServerSocket.getLocalPort() >>> 8 & 0xFF) + COMMA_DELIMITED
                        + (transportServerSocket.getLocalPort() & 0xFF);
                pirnt("227  entering passive mode (" + str + ")");
                output.flush();
                transportSocket = transportServerSocket.accept();
                isPasv = true;
            } catch (Exception e) {
                pirnt("451 failed to send.");
            }
        }

        private void portMode(String param) {
            int p1 = 0;
            int p2 = 0;
            int[] a = new int[6];
            int j = 0;
            try {
                while ((p2 = param.indexOf(COMMA_DELIMITED, p1)) != -1) {
                    a[j] = Integer.parseInt(param.substring(p1, p2));
                    p2 = p2 + 1;
                    p1 = p2;
                    j++;
                }
                a[j] = Integer.parseInt(param.substring(p1, param.length()));// 最后一位
            } catch (NumberFormatException e) {
                pirnt("501 error command.");
            }
            try {
                transportSocket = new Socket(a[0] + "." + a[1] + "." + a[2] + "." + a[3], a[4] * 256 + a[5],
                        InetAddress.getLocalHost(), 20);
                pirnt("200 ok.");
            } catch (IOException e) {
                pirnt("451 failed to send.");
            }
        }

        private void getFile(String param) {
            try {
                File file = new File(getCurrentPath(param));
                if (file.exists()) {
                    try {
                        if (type == State.TYPE_IMAGE) {
                            pirnt("150 Opening ASCII mode data connection for " + param);
                            write(new FileInputStream(file), transportSocket.getOutputStream());
                        } else {
                            pirnt("150 Opening ASCII mode data connection for " + param);
                            BufferedReader fin = new BufferedReader(new FileReader(file));
                            PrintWriter dataOutput = new PrintWriter(transportSocket.getOutputStream(), true);
                            String s;
                            while ((s = fin.readLine()) != null) {
                                dataOutput.println(s);
                            }
                            fin.close();
                            dataOutput.close();
                        }
                        transportSocket.close();
                        if (isPasv) {
                            transportServerSocket.close();
                        }
                        pirnt("226 send completed.");
                    } catch (Exception e) {
                        pirnt("451 failed to send.");
                    }
                } else {
                    pirnt("550 file does't exist");
                }
            } catch (IOException e) {
                pirnt("550 file does't exist");
            }
        }

        private void lastModified(String param) {
            try {
                File file = new File(getCurrentPath(param));
                if (file.exists()) {
                    pirnt("213 " + new SimpleDateFormat(DATE_FORMAT).format(new Date(file.lastModified())));
                } else {
                    pirnt("550 file does't exist");
                }
            } catch (IOException e) {
                pirnt("550 file does't exist");
            }
        }

        private void size(String param) {
            try {
                File file = new File(getCurrentPath(param));
                if (file.exists()) {
                    pirnt("213 " + file.length());
                } else {
                    pirnt("550 file does't exist");
                }
            } catch (IOException e) {
                pirnt("550 file does't exist");
            }
        }

        private void reciveFile(String param) {
            if (notEmpty(param)) {
                try {
                    if (type == State.TYPE_IMAGE) {
                        pirnt("150 Opening Binary mode data connection for " + param);
                        write(transportSocket.getInputStream(), new FileOutputStream(getCurrentPath(param)));
                    } else {
                        pirnt("150 Opening ASCII mode data connection for " + param);
                        PrintWriter fout = new PrintWriter(new FileOutputStream(getCurrentPath(param)));
                        BufferedReader dataInput = new BufferedReader(new InputStreamReader(transportSocket.getInputStream()));
                        String line;
                        while ((line = dataInput.readLine()) != null) {
                            fout.println(line);
                        }
                        dataInput.close();
                        fout.close();
                    }
                    transportSocket.close();
                    if (isPasv) {
                        transportServerSocket.close();
                    }
                    pirnt("226 send completed.");
                } catch (Exception e) {
                    pirnt("451 failed to send.");
                }
            } else {
                pirnt("501 error paramter.");
            }
        }

        private String dealPath(String path) {
            int index;
            while (0 <= (index = path.indexOf(".."))) {
                int pindex = path.substring(0, index).lastIndexOf(SEPARATOR, index - 2);
                if (0 <= pindex) {
                    path = path.substring(0, pindex) + path.substring(index + 2);
                } else {
                    path = path.substring(index + 2);
                }
            }
            if (!path.endsWith(SEPARATOR)) {
                path += SEPARATOR;
            }
            return path.replace("//", SEPARATOR);
        }

        private void changeCurrentPath(String path) {
            try {
                File directory = new File(getCurrentPath(path));
                if (directory.exists() && directory.isDirectory()) {
                    path = (empty(path) ? ROOT : path.startsWith(SEPARATOR) ? path : currentPath + path + SEPARATOR);
                    currentPath = dealPath(path);
                    pirnt("250 current directory changed to " + currentPath);
                } else {
                    pirnt("550 path does't exist.");
                }
            } catch (IOException e) {
                pirnt("550 file does't exist");
            }
        }

        private void makeDir(String path) {
            try {
                File dir = new File(getCurrentPath(path));
                if (dir.exists()) {
                    pirnt("550 directory already exists.");
                } else {
                    dir.mkdirs();
                    pirnt("250 directory created.");
                }
            } catch (IOException e) {
                pirnt("550 file does't exist");
            }
        }

        private void deleteDir(String path) {
            try {
                File dir = new File(getCurrentPath(path));
                if (dir.exists() && dir.isDirectory()) {
                    dir.delete();
                    pirnt("250 directory deleted.");
                } else {
                    pirnt("550 directory does't exist.");
                }
            } catch (IOException e) {
                pirnt("550 file does't exist");
            }
        }

        private void deleteFile(String path) {
            try {
                File file = new File(getCurrentPath(path));
                if (file.exists() && !file.isDirectory()) {
                    file.delete();
                    pirnt("250 file deleted.");
                } else {
                    pirnt("550 file or directory does't exist.");
                }
            } catch (IOException e) {
                pirnt("550 file does't exist");
            }
        }

        private void listFiles(String param, boolean nlist) {
            String path;
            if (empty(param) || param.startsWith("-")) {
                path = getUserPath(null);
            } else {
                path = getUserPath(param);
            }
            try {
                PrintWriter dout = new PrintWriter(transportSocket.getOutputStream(), true);
                pirnt("150 Opening ASCII mode data connection.");
                if (ROOT.equals(path)) {
                    for (String virtualPath : VIRTUAL_FILE_PATHS) {
                        File file = new File(rootPath + getSitePath(virtualPath));
                        if (nlist) {
                            dout.println(file.getName());
                        } else {
                            dout.println(getListString(file));
                        }
                    }
                } else {
                    DirectoryStream<Path> stream = null;
                    try {
                        stream = Files.newDirectoryStream(Paths.get(rootPath + getSitePath(path)));
                        for (Path entry : stream) {
                            dout.println(getListString(entry.toFile()));
                            System.out.println(getListString(entry.toFile()));
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
                dout.flush();
                dout.close();
                transportSocket.close();
                if (isPasv) {
                    transportServerSocket.close();
                }
                pirnt("226 send completed.");
            } catch (Exception e) {
                pirnt("451 failed to send.");
            }
        }

        private String getListString(File file) {
            StringBuffer sb = new StringBuffer();
            sb.append(file.isDirectory() ? 'd' : '-');
            StringBuffer rightsb = new StringBuffer();
            rightsb.append(file.canRead() ? 'r' : '-').append(file.canWrite() ? 'w' : '-')
                    .append(file.isDirectory() ? '-' : file.canExecute() ? 'x' : '-');
            String rightString = rightsb.toString();
            sb.append(rightString).append(rightString).append(rightString);
            Date fileModifiedDate = new Date(file.lastModified());
            sb.append(BLANK_SPACE).append(file.isDirectory() ? 3 : 1).append(" user      group            ")
                    .append(String.valueOf(file.length())).append(BLANK_SPACE)
                    .append(System.currentTimeMillis() - file.lastModified() > 183L * 24L * 60L * 60L * 1000L
                            ? new SimpleDateFormat(LIST_DATE_FORMAT, Locale.ENGLISH).format(fileModifiedDate)
                            : new SimpleDateFormat(LIST_DATE_FORMAT1, Locale.ENGLISH).format(fileModifiedDate))
                    .append(BLANK_SPACE);
            sb.append(file.getName());
            return sb.toString();
        }

        private String getSitePath(String path) throws IOException {
            if (ROOT.equals(path)) {
                return path;
            }
            for (String prefix : VIRTUAL_FILE_PATHS) {
                if (path.startsWith(ROOT + prefix)) {
                    int length = ROOT.length() + prefix.length();
                    return path.substring(0, length) + getFullFileName(site, path.substring(length, path.length()));
                }
            }
            for (String prefix : VIRTUAL_FILE_PATHS) {
                if (path.startsWith(prefix)) {
                    return path;
                }
            }
            throw new IOException("file does't exist");
        }

        private String getUserPath(String path) {
            if (empty(path)) {
                path = currentPath;
            } else {
                if (!path.startsWith(ROOT)) {
                    path = currentPath + path;
                }
                if (!path.endsWith(SEPARATOR)) {
                    path += SEPARATOR;
                }
            }
            return userPath + path;
        }

        private String getCurrentPath(String path) throws IOException {
            return rootPath + getSitePath(getUserPath(path));
        }

        private class State {
            public final static int STATE_NEED_USERNAME = 0; // 需要用户名
            public final static int STATE_NEED_PASSWORD = 1; // 需要密码
            public final static int STATE_READY = 2; // 已经登陆状态

            public final static int TYPE_ASCII = 0;
            public final static int TYPE_IMAGE = 1;
        }
    }
}