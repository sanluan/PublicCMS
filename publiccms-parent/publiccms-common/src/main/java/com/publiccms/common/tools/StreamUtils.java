package com.publiccms.common.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 流输出工具类
 * 
 * StreamUtils
 * 
 */
public class StreamUtils {

    private static final int BUFFER_SIZE = 1024 * 1024;

    /**
     * @param outputStream
     * @return
     */
    public static BufferedOutputStream getBufferedOutputStream(OutputStream outputStream) {
        return new BufferedOutputStream(outputStream, BUFFER_SIZE);
    }

    /**
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public static void write(InputStream inputStream, OutputStream outputStream) throws IOException {
        write(inputStream, true, outputStream, true);
    }

    /**
     * @param inputStream
     * @param closeInput
     * @param outputStream
     * @throws IOException
     */
    public static void write(InputStream inputStream, boolean closeInput, OutputStream outputStream) throws IOException {
        write(inputStream, closeInput, outputStream, true);
    }

    /**
     * @param inputStream
     * @param outputStream
     * @param clossOutput
     * @throws IOException
     */
    public static void write(InputStream inputStream, OutputStream outputStream, boolean clossOutput) throws IOException {
        write(inputStream, true, outputStream, clossOutput);
    }

    /**
     * @param inputStream
     * @param closeInput
     * @param outputStream
     * @param clossOutput
     * @throws IOException
     */
    public static void write(InputStream inputStream, boolean closeInput, OutputStream outputStream, boolean clossOutput)
            throws IOException {
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(inputStream, BUFFER_SIZE);
            bufferedOutputStream = new BufferedOutputStream(outputStream, BUFFER_SIZE);
            int i;
            while ((i = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(i);
            }
            bufferedOutputStream.flush();
        } finally {
            if (null != bufferedInputStream && closeInput) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                }
            }
            if (null != bufferedOutputStream && clossOutput) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
