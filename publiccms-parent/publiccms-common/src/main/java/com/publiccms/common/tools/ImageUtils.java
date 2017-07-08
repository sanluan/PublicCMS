package com.publiccms.common.tools;

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import com.publiccms.common.base.Base;

/**
 *
 * ImageUtils
 * 
 */
public class ImageUtils implements Base {

    /**
     * @param width
     * @param height
     * @param text
     * @return
     * @throws IOException
     */
    public static String getImageData(int width, int height, String text) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        drawImage(width, height, text, byteArrayOutputStream);
        byteArrayOutputStream.close();
        StringBuilder sb = new StringBuilder();
        sb.append("data:image/png;base64,");
        sb.append(Base64.encodeBase64String(byteArrayOutputStream.toByteArray()));
        return sb.toString();
    }

    /**
     * @param width
     * @param height
     * @param text
     * @param outputStream
     * @throws IOException
     */
    public static void drawImage(int width, int height, String text, OutputStream outputStream) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();
        g.setColor(getRandColor(210, 255));
        g.fillRect(0, 0, width, height);
        if (notEmpty(text)) {
            int fontWidth = width / text.length();
            int fontSize = fontWidth >= height ? height - 1 : fontWidth;
            Font font1 = getFont(fontSize);
            Font font2 = getFont(fontSize / 2);
            for (int i = 0; i < text.length(); i++) {
                g.setFont(font1);
                g.setColor(getRandColor(0, 200));
                g.drawString(String.valueOf(text.charAt(i)), i * fontWidth + 3, height - r.nextInt(height - fontSize));
                g.setFont(font2);
                for (int j = 0; j < 4; j++) {
                    g.setColor(getRandColor(100, 250));
                    g.drawString(String.valueOf(text.charAt(i)), r.nextInt(width), r.nextInt(height));
                }
            }
        }
        ImageIO.write(bufferedImage, "png", outputStream);
    }

    private static Color getRandColor(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        int rc = (bc > 255 ? 255 : bc) - fc;
        return new Color(fc + r.nextInt(rc), fc + r.nextInt(rc), fc + r.nextInt(rc));
    }

    private static Font getFont(int size) {
        Font font[] = new Font[4];
        font[0] = new Font(null, Font.PLAIN, size);
        font[1] = new Font("Antique Olive Compact", Font.PLAIN, size);
        font[2] = new Font("Fixedsys", Font.PLAIN, size);
        font[3] = new Font("Gill Sans Ultra", Font.PLAIN, size);
        return font[r.nextInt(4)];
    }
}
