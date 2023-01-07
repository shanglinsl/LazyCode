package lazycode;

import org.apache.commons.lang3.SystemUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;

public class LzFileDirTool {
    public static String userHomeDir() {
        // 用户家目录
        return SystemUtils.getUserHome().getPath();
    }

    public static String tempDir() {
        return SystemUtils.getJavaIoTmpDir().getPath();
    }

    public static String classLoaderPath() {
        return Objects.requireNonNull(LzFileDirTool.class.getClassLoader().getResource("")).getFile();
    }

    public static String pathJoin(String base, String path) {
        return base + "/" + path;
    }


    public static String absPath(String path) throws UnsupportedEncodingException {
        return absPath(classLoaderPath(), path);
    }

    public static String absPath(String baseDir, String path) throws UnsupportedEncodingException {
        if (new File(path).isAbsolute()) {
            return path;
        }
        File file = new File(LzFileDirTool.pathJoin(baseDir, path));
        return java.net.URLDecoder.decode(file.getAbsolutePath(), StandardCharsets.UTF_8.toString());
    }

    public static boolean exists(String absPath) {
        return new File(absPath).exists();
    }

    public static boolean isFile(String absPath) {
        return new File(absPath).isFile();
    }

    public static boolean isDir(String path) {
        return new File(path).isDirectory();
    }

    public static boolean createFile(String absPath) throws IOException {
        if (LzFileDirTool.exists(absPath)) {
            return true;
        }
        return new File(absPath).createNewFile();
    }

    public static boolean makeDir(String absPath) {
        return new File(absPath).mkdirs();
    }

    public static boolean dirIsEmpty(String absPath) {
        return Objects.requireNonNull(new File(absPath).list()).length == 0;
    }

    public static String[] dirList(String absPath) {
        return new File(absPath).list();
    }

    public static String[] dirListDeep(String absPath) {
        List<String> list = new ArrayList<>();

        for (String file : LzFileDirTool.dirList(absPath)) {
            file = LzFileDirTool.pathJoin(absPath, file);
            list.add(file);
            if (LzFileDirTool.isDir(file)) {
                list.addAll(Arrays.asList(LzFileDirTool.dirListDeep(file)));
            }
        }
        return list.toArray(new String[0]);
    }

    public static String readFileText(String absPath) {
        return readFileText(absPath, StandardCharsets.UTF_8.name());
    }

    public static String readFileText(String absPath, String charset) {
        File file = new File(absPath);
        BufferedReader reader = null;
        // 用来保存一次读取到的数据
        StringBuilder sbf = new StringBuilder();
        char[] buffer = new char[1024];
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
            int len;
            while ((len = reader.read(buffer)) != -1) {
                sbf.append(new String(buffer, 0, len));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    public static boolean writeFileText(String absPath, String text) {
        return writeFileText(absPath, text, StandardCharsets.UTF_8.name());
    }

    public static boolean writeFileText(String absPath, String text, String charset) {
        File file = new File(absPath);
        BufferedWriter writer = null;
        boolean flag = true;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return flag;
    }

    public static byte[] readFileByte(String absPath) throws IOException {
        File file = new File(absPath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fileIS = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];

        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fileIS.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        fileIS.close();
        return buffer;
    }


    public static boolean writeFileByte(String absPath, byte[] data) {
        File file = new File(absPath);
        BufferedOutputStream bufferOS = null;
        boolean flag = true;
        try {
            bufferOS = new BufferedOutputStream(new FileOutputStream(file));
            bufferOS.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        } finally {
            if (bufferOS != null) {
                try {
                    bufferOS.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return flag;
    }

    public static boolean copyFile(String absPathSrc, String absPathTarget) {
        File fileSrc = new File(absPathSrc);
        File fileTarget = new File(absPathTarget);
        BufferedInputStream bufferIS = null;
        BufferedOutputStream bufferOS = null;
        boolean flag = true;

        byte[] buffer = new byte[1024];
        try {
            bufferIS = new BufferedInputStream(new FileInputStream(fileSrc));
            bufferOS = new BufferedOutputStream(new FileOutputStream(fileTarget));

            int len;
            while ((len = bufferIS.read(buffer)) != -1) {
                bufferOS.write(buffer, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        } finally {
            if (bufferIS != null) {
                try {
                    bufferIS.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            if (bufferOS != null) {
                try {
                    bufferOS.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return flag;
    }


    public static boolean copy(String absPathSrc, String absPathTarget) {

        boolean flag = true;
        if (LzFileDirTool.isFile(absPathSrc)) {
            flag = LzFileDirTool.copyFile(absPathSrc, absPathTarget);
        } else {
            for (String filePath : LzFileDirTool.dirListDeep(absPathSrc)) {
                String filePathTarget = filePath.replace(LzFileDirTool.parentDir(absPathSrc), absPathTarget);
                if (LzFileDirTool.isFile(filePath)) {
                    LzFileDirTool.makeDir(LzFileDirTool.parentDir(filePathTarget));
                    LzFileDirTool.copyFile(filePath, filePathTarget);
                } else {
                    LzFileDirTool.makeDir(filePathTarget);
                }
            }
        }
        return flag;
    }

    public static boolean remove(String absPath) {
        if (!LzFileDirTool.exists(absPath)) {
            return true;
        }

        boolean delete;
        if (LzFileDirTool.isFile(absPath)) {
            delete = new File(absPath).delete();
        } else if (LzFileDirTool.dirIsEmpty(absPath)) {
            delete = new File(absPath).delete();
        } else {

            // 递归删除目录下的文件
            for (String file : LzFileDirTool.dirListDeep(absPath)) {
                if (!LzFileDirTool.exists(file)) {
                    continue;
                }

                if (LzFileDirTool.isDir(file)) {
                    delete = LzFileDirTool.remove(file);
                } else {
                    delete = new File(file).delete();
                }
            }
            // 删除目录
            LzFileDirTool.remove(absPath);
        }
        return !LzFileDirTool.exists(absPath);
    }

    public static boolean move(String absPathSrc, String absPathTarget) {
        if (!LzFileDirTool.exists(absPathSrc)) {
            return false;
        }
        LzFileDirTool.copy(absPathSrc, absPathTarget);
        LzFileDirTool.remove(absPathSrc);
        boolean flag = true;

        if (LzFileDirTool.exists(absPathSrc) || !LzFileDirTool.exists(absPathTarget)) {
            flag = false;
        }
        return flag;
    }

    public static long fileSize(String absPath) {
        File file = new File(absPath);
        // 字节数
        return file.length();
    }

    public static long FileCreateTime(String absPath) throws IOException {
        BasicFileAttributes attrs = Files.readAttributes(Paths.get(absPath), BasicFileAttributes.class);
        FileTime time = attrs.creationTime();
        return time.toMillis();
    }

    public static long FileLastModifiedTime(String absPath) {
        File file = new File(absPath);
        return file.lastModified();
    }

    public static boolean setFileLastModifiedTime(String absPath, long time) throws IOException {
        File file = new File(absPath);
        return file.setLastModified(time);
    }


    public static long fileLastVisitTime(String absPath) throws IOException {
        BasicFileAttributes attrs = Files.readAttributes(Paths.get(absPath), BasicFileAttributes.class);
        FileTime time = attrs.lastAccessTime();
        return time.toMillis();
    }

    public static String filenameAndPath(String absPath) {
        return absPath.substring(0, absPath.lastIndexOf("."));
    }

    public static String filenameAndSuffix(String absPath) {
        File file = new File(absPath);
        return file.getName();
    }

    public static String filename(String absPath) {
        String filenameAndSuffix = LzFileDirTool.filenameAndSuffix(absPath);
        return filenameAndSuffix.substring(0, filenameAndSuffix.lastIndexOf("."));
    }

    public static String suffix(String absPath) {
        String filenameAndSuffix = LzFileDirTool.filenameAndSuffix(absPath);
        return filenameAndSuffix.substring(filenameAndSuffix.lastIndexOf("."));
    }

    public static String parentDir(String absPath) {
        return LzFileDirTool.parentDir(absPath, 1);
    }

    public static String parentDir(String absPath, int upLevel) {
        String path = absPath;
        for (int i = upLevel; i > 0; i--) {
            path = new File(path).getParent();
        }
        return path;
    }


}
