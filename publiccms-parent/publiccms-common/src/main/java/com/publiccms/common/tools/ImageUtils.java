package com.publiccms.common.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;

import com.luciad.imageio.webp.WebPReadParam;
import com.publiccms.common.constants.Constants;

/**
 *
 * ImageUtils
 * 
 */
public class ImageUtils {
    /**
     * 
     */
    public static final String DEFAULT_FORMAT_NAME = "jpg";

    /**
     * @param width
     * @param height
     * @param text
     * @return base64 encoded picture
     * @throws IOException
     */
    public static String getImageData(int width, int height, String text) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        drawImage(width, height, text, byteArrayOutputStream);
        byteArrayOutputStream.close();
        StringBuilder sb = new StringBuilder();
        sb.append("data:image/png;base64,");
        sb.append(VerificationUtils.base64Encode(byteArrayOutputStream.toByteArray()));
        return sb.toString();
    }

    /**
     * <pre>
     * &#64;RequestMapping(value = "getCaptchaImage")
     * public void getCaptchaImage(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletResponse response) {
     *     try {
     *         String captcha = VerificationUtils.getRandomString("ABCDEFGHJKMNPQRSTUVWXYZ23456789", 4);
     *         session.setAttribute("captcha", captcha);
     *         ImageUtils.drawImage(100, 20, captcha, response.getOutputStream());
     *     } catch (IOException e) {
     *     }
     * }
     * </pre>
     * 
     * <pre>
     * &#64;RequestMapping(value = "doLogin", method = RequestMethod.POST)
     * public String login(@RequestAttribute SysSite site, javax.servlet.http.HttpSession session, String username,
     *         String password, String captcha, String returnUrl, Long clientId, String uuid,
     *         javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, ModelMap model) {
     *     String sessionCaptcha = (String) session.getAttribute("captcha");
     *     session.removeAttribute("captcha");
     *     if (null != sessionCaptcha &amp;&amp; sessionCaptcha.equals(captcha)) {
     *         // login code
     *     }
     * }
     * </pre>
     * 
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
        if (CommonUtils.notEmpty(text)) {
            int fontWidth = width / text.length();
            int fontSize = fontWidth >= height ? height - 1 : fontWidth;
            Font font1 = getFont(fontSize);
            Font font2 = getFont(fontSize / 2);
            for (int i = 0; i < text.length(); i++) {
                g.setFont(font1);
                g.setColor(getRandColor(0, 200));
                g.drawString(String.valueOf(text.charAt(i)), i * fontWidth + 3,
                        height - Constants.random.nextInt(height - fontSize));
                g.setFont(font2);
                for (int j = 0; j < 4; j++) {
                    g.setColor(getRandColor(100, 250));
                    g.drawString(String.valueOf(text.charAt(i)), Constants.random.nextInt(width),
                            Constants.random.nextInt(height));
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
        return new Color(fc + Constants.random.nextInt(rc), fc + Constants.random.nextInt(rc), fc + Constants.random.nextInt(rc));
    }

    private static Font getFont(int size) {
        Font font[] = new Font[4];
        font[0] = new Font(null, Font.PLAIN, size);
        font[1] = new Font("Antique Olive Compact", Font.PLAIN, size);
        font[2] = new Font("Fixedsys", Font.PLAIN, size);
        font[3] = new Font("Gill Sans Ultra", Font.PLAIN, size);
        return font[Constants.random.nextInt(4)];
    }

    public static void webp2Image(InputStream webpInputStream, boolean png, File imageFile)
            throws FileNotFoundException, IOException {
        ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();
        WebPReadParam readParam = new WebPReadParam();
        readParam.setBypassFiltering(true);
        reader.setInput(webpInputStream);
        BufferedImage image = reader.read(0, readParam);
        ImageIO.write(image, png ? "png" : "jpg", imageFile);
    }

    public static void webp2Image(File webpFile, boolean png, File imageFile) throws FileNotFoundException, IOException {
        ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();
        WebPReadParam readParam = new WebPReadParam();
        readParam.setBypassFiltering(true);
        reader.setInput(new FileImageInputStream(webpFile));
        BufferedImage image = reader.read(0, readParam);
        ImageIO.write(image, png ? "png" : "jpg", imageFile);
    }

    public static void image2Webp(File imageFile, File webpFile) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);
        ImageIO.write(image, "webp", webpFile);
    }

    public static void thumb(String sourceFilePath, String thumbFilePath, int width, int height, String suffix)
            throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(thumbFilePath);) {
            BufferedImage sourceImage = ImageIO.read(new File(sourceFilePath));
            if (width > sourceImage.getWidth()) {
                width = sourceImage.getWidth();
            }
            if (height > sourceImage.getHeight()) {
                height = sourceImage.getHeight();
            }
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Image scaledImage = sourceImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            Graphics2D g = img.createGraphics();
            if (".png".equalsIgnoreCase(suffix)) {
                img = g.getDeviceConfiguration().createCompatibleImage(img.getWidth(), img.getHeight(), Transparency.TRANSLUCENT);
                g = img.createGraphics();
            }
            g.drawImage(scaledImage, 0, 0, null);
            if (null != suffix && suffix.length() > 1) {
                ImageIO.write(img, suffix.substring(1), outputStream);
            } else {
                ImageIO.write(img, DEFAULT_FORMAT_NAME, outputStream);
            }
        }
    }
}
