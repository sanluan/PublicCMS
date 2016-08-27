package com.sanluan.common.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.sanluan.common.base.Base;

/**
 * 流输出工具类
 * 
 * @author kerneler
 *
 */
public class StreamUtils extends Base {
    public static final int BUFFER_SIZE = 1024 * 1024;

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
        } finally {
            if (notEmpty(bufferedInputStream) && closeInput) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                }
            }
            if (notEmpty(bufferedOutputStream) && clossOutput) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
