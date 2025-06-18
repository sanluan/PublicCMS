package com.publiccms.common.tools;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.github.bgalek.security.svg.SvgSecurityValidator;
import com.github.bgalek.security.svg.ValidationResult;
import com.luciad.imageio.webp.WebPReadParam;
import com.publiccms.common.constants.Constants;

import net.ifok.image.image4j.codec.ico.ICOEncoder;

/**
 *
 * ImageUtils
 * 
 */
public class ImageUtils {
    private ImageUtils() {
    }

    private static final Log log = LogFactory.getLog(ImageUtils.class);
    /**
     * 
     */
    public static final String FORMAT_NAME_PNG = "png";
    /**
     * 
     */
    public static final String FORMAT_NAME_JPG = "jpg";
    /**
     * 
     */
    public static final String FORMAT_NAME_WEBP = "webp";
    /**
     * 
     */
    public static final String FORMAT_NAME_SVG = "svg";
    /**
     * 
     */
    public static final String DEFAULT_FORMAT_NAME = FORMAT_NAME_JPG;

    /**
     * @param width
     * @param height
     * @param text
     * @return base64 encoded picture
     * @throws IOException
     */
    public static String generateImageData(int width, int height, String text) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        drawImage(width, height, text, byteArrayOutputStream);
        byteArrayOutputStream.close();
        return CommonUtils.joinString("data:image/png;base64,", VerificationUtils.base64Encode(byteArrayOutputStream.toByteArray()));
    }

    /**
     * <pre>
     * &#64;RequestMapping(value = "getCaptchaImage")
     * public ResponseEntity&lt;StreamingResponseBody&gt; getCaptchaImage(HttpSession session) {
     *     String captcha = VerificationUtils.getRandomString("ABCDEFGHJKMNPQRSTUVWXYZ23456789", 4);
     *     session.setAttribute("captcha", captcha);
     *     StreamingResponseBody body = new StreamingResponseBody() {
     *         &#64;Override
     *         public void writeTo(OutputStream outputStream) throws IOException {
     *             ImageUtils.drawImage(120, 30, captcha, outputStream);
     *         }
     *     };
     *     return ResponseEntity.ok().body(body);
     * }
     * </pre>
     * 
     * <pre>
     * &#64;PostMapping("doLogin")
     * public String login(@RequestAttribute SysSite site, HttpSession session, String username, String password, String captcha, String returnUrl, Long clientId, String uuid,
     *         HttpServletRequest request, ModelMap model) {
     *     String sessionCaptcha = (String) session.getAttribute("captcha");
     *     session.removeAttribute("captcha");
     *     if (null != sessionCaptcha &amp;&amp; sessionCaptcha.equalsIgnoreCase(captcha)) {
     *         // login code
     *     } else {
     *         return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, "login.html");
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
        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(getRandColor(210, 255));
        g.fillRect(0, 0, width, height);
        if (CommonUtils.notEmpty(text)) {
            int fontWidth = width / text.length();
            int fontSize = fontWidth >= height ? height - height / 6 : fontWidth;
            Font font1 = getFont(fontSize);
            Font font2 = getFont(fontSize / 3);
            for (int i = 0; i < text.length(); i++) {
                AffineTransform saveAT = g.getTransform();
                AffineTransform affine = new AffineTransform();
                affine.setToRotation(Math.PI / 4 * Constants.random.nextDouble() * (Constants.random.nextBoolean() ? 1 : -1), i * fontWidth + 3, height / 2);
                g.setTransform(affine);
                g.setFont(font1);
                g.setColor(getRandColor(0, 200));
                g.drawString(String.valueOf(text.charAt(i)), i * fontWidth + 3, height / 2 + fontSize / 2);
                g.setTransform(saveAT);
                g.setFont(font2);
                for (int j = 0; j < 4; j++) {
                    g.setColor(getRandColor(100, 250));
                    g.drawString(String.valueOf(text.charAt(i)), Constants.random.nextInt(width), Constants.random.nextInt(height));
                }
            }
            g.setColor(getRandColor(160, 250));
            for (int i = 0; i < 10; i++) {
                g.drawLine(Constants.random.nextInt(width), Constants.random.nextInt(height), Constants.random.nextInt(width), Constants.random.nextInt(height));
            }
            shearX(g, width, height, Color.white);
        }
        g.dispose();
        ImageIO.write(bufferedImage, FORMAT_NAME_PNG, outputStream);
    }

    private static void shearX(Graphics g, int w1, int h1, Color color) {
        int period = Constants.random.nextInt(2);
        int frames = 1;
        int phase = Constants.random.nextInt(2);
        for (int i = 0; i < h1; i++) {
            double d = (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * phase) / frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            g.setColor(color);
            g.drawLine((int) d, i, 0, i);
            g.drawLine((int) d + w1, i, w1, i);
        }
    }

    private static Color getRandColor(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        int rc = (bc > 255 ? 255 : bc) - fc;
        return new Color(fc + Constants.random.nextInt(rc), fc + Constants.random.nextInt(rc), fc + Constants.random.nextInt(rc));
    }

    private static Font getFont(int size) {
        Font[] font = new Font[4];
        font[0] = new Font(null, Font.PLAIN, size);
        font[1] = new Font("Antique Olive Compact", Font.PLAIN, size);
        font[2] = new Font("Fixedsys", Font.PLAIN, size);
        font[3] = new Font("Gill Sans Ultra", Font.PLAIN, size);
        return font[Constants.random.nextInt(4)];
    }

    public static void webp2Image(InputStream webpInputStream, boolean png, String imageFilepath) throws IOException {
        ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();
        WebPReadParam readParam = new WebPReadParam();
        readParam.setBypassFiltering(true);
        reader.setInput(webpInputStream);
        BufferedImage image = reader.read(0, readParam);
        ImageIO.write(image, png ? FORMAT_NAME_PNG : FORMAT_NAME_JPG, new File(imageFilepath));
    }

    public static void webp2Image(String webpFilepath, boolean png, String imageFilepath) throws IOException {
        ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();
        WebPReadParam readParam = new WebPReadParam();
        readParam.setBypassFiltering(true);
        reader.setInput(new FileImageInputStream(new File(webpFilepath)));
        BufferedImage image = reader.read(0, readParam);
        ImageIO.write(image, png ? FORMAT_NAME_PNG : FORMAT_NAME_JPG, new File(imageFilepath));
    }

    public static void image2Webp(String imageFilepath, String webpFilepath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imageFilepath));
        ImageIO.write(image, FORMAT_NAME_WEBP, new File(webpFilepath));
    }

    public static void image2Ico(InputStream input, String suffix, int size, String icoFilepath) throws IOException {
        BufferedImage sourceImage = ImageIO.read(input);
        BufferedImage resultImage = thumb(sourceImage, size, size, 0, ".png".equalsIgnoreCase(suffix));
        try (FileOutputStream outputStream = new FileOutputStream(icoFilepath)) {
            ICOEncoder.write(resultImage, outputStream);
        }
    }

    public static boolean svgSafe(File imageFile) throws IOException {
        List<String> safeElementsList = new ArrayList<>();
        safeElementsList.add("missing-glyph");
        safeElementsList.add("font-face");
        List<String> safeAttributesList = new ArrayList<>();
        safeAttributesList.add("horiz-adv-x");
        safeAttributesList.add("t");
        safeAttributesList.add("p-id");
        SvgSecurityValidator svgSecurityValidator = SvgSecurityValidator.builder().withAdditionalElements(safeElementsList).withAdditionalAttributes(safeAttributesList).build();
        ValidationResult validation = svgSecurityValidator.validate(FileUtils.readFileToString(imageFile, StandardCharsets.UTF_8));
        if (validation.hasViolations()) {
            log.error("unsafe svg file:");
            log.error(imageFile.getAbsolutePath());
            log.error(validation.getOffendingElements());
            return false;
        }
        return true;
    }

    public static BufferedImage thumb(BufferedImage sourceImage, int width, int height, int angle, boolean png) {
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resultImage.createGraphics();
        if (png) {
            resultImage = g.getDeviceConfiguration().createCompatibleImage(resultImage.getWidth(), resultImage.getHeight(), Transparency.TRANSLUCENT);
            g = resultImage.createGraphics();
        }
        Image scaledImage;
        if (90 == angle || 270 == angle) {
            scaledImage = sourceImage.getScaledInstance(height, width, Image.SCALE_SMOOTH);
        } else {
            scaledImage = sourceImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        }
        if (90 == angle || 180 == angle || 270 == angle) {
            AffineTransform at = new AffineTransform();
            if (180 == angle) {
                at.rotate(Math.toRadians(angle), width / 2, height / 2);
            } else {
                at.rotate(Math.toRadians(angle), height / 2, width / 2);
                if (angle == 270) {
                    at.translate((width - height) / 2, (width - height) / 2);
                } else {
                    at.translate((height - width) / 2, (height - width) / 2);
                }
            }
            g.setTransform(at);
        }
        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();
        return resultImage;
    }

    public static void thumb(String sourceFilePath, String thumbFilePath, int width, int height, String suffix) throws IOException, ImageProcessingException {
        File file = new File(sourceFilePath);
        BufferedImage sourceImage = ImageIO.read(file);
        if (width > sourceImage.getWidth()) {
            width = sourceImage.getWidth();
        }
        if (height > sourceImage.getHeight()) {
            height = sourceImage.getHeight();
        }
        BufferedImage resultImage = thumb(sourceImage, width, height, getAngle(file), ".png".equalsIgnoreCase(suffix));
        try (FileOutputStream outputStream = new FileOutputStream(thumbFilePath)) {
            if (null != suffix && suffix.length() > 1) {
                ImageIO.write(resultImage, suffix.substring(1), outputStream);
            } else {
                ImageIO.write(resultImage, DEFAULT_FORMAT_NAME, outputStream);
            }
        }
    }

    public static int getAngle(File file) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        for (Directory d : metadata.getDirectoriesOfType(ExifIFD0Directory.class)) {
            for (Tag tag : d.getTags()) {
                if ("Orientation".equals(tag.getTagName())) {
                    String desc = tag.getDescription();
                    if (desc.contains("90")) {
                        return 90;
                    } else if (desc.contains("270")) {
                        return 270;
                    } else if (desc.contains("180")) {
                        return 180;
                    }
                }
            }
        }
        return 0;
    }

    public static void watermark(String sourceFilePath, String watermarkFilePath, String watermarkText, String color, String font, int fontsize, float alpha, String position, String suffix)
            throws IOException {
        if (CommonUtils.notEmpty(watermarkFilePath) || CommonUtils.notEmpty(watermarkText)) {
            BufferedImage sourceImage = ImageIO.read(new File(sourceFilePath));
            Graphics2D g = sourceImage.createGraphics();
            int fontWidth = 0;
            int fontHeight = 0;
            int watermarkWidth = 0;
            int watermarkHeight = 0;

            BufferedImage watermarkImage = null;
            if (CommonUtils.notEmpty(watermarkFilePath)) {
                watermarkImage = ImageIO.read(new File(watermarkFilePath));
                watermarkWidth = watermarkImage.getWidth();
                watermarkHeight = watermarkImage.getHeight();
            }
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            if (CommonUtils.notEmpty(watermarkText)) {
                if (CommonUtils.notEmpty(color)) {
                    g.setColor(Color.decode(color));
                }
                if (CommonUtils.notEmpty(font)) {
                    g.setFont(new Font(font, Font.PLAIN, fontsize));
                }
                fontWidth = g.getFontMetrics().charsWidth(watermarkText.toCharArray(), 0, watermarkText.length());
                fontHeight = g.getFontMetrics().getHeight();
                if (fontWidth < sourceImage.getWidth() - 10 && fontHeight < sourceImage.getHeight() - 10) {
                    g.drawString(watermarkText, matermarkPosition(position, sourceImage.getWidth(), sourceImage.getHeight(), watermarkWidth, watermarkHeight, fontWidth, fontHeight, true, true),
                            matermarkPosition(position, sourceImage.getWidth(), sourceImage.getHeight(), watermarkWidth, watermarkHeight, fontWidth, fontHeight, false, true));
                }
            }
            if (null != watermarkImage && (watermarkWidth < sourceImage.getWidth() - 10 && watermarkHeight < sourceImage.getHeight() - 10)) {
                g.drawImage(watermarkImage, matermarkPosition(position, sourceImage.getWidth(), sourceImage.getHeight(), watermarkWidth, watermarkHeight, fontWidth, fontHeight, true, false),
                        matermarkPosition(position, sourceImage.getWidth(), sourceImage.getHeight(), watermarkWidth, watermarkHeight, fontWidth, fontHeight, false, false), watermarkWidth,
                        watermarkHeight, null);

            }
            try (FileOutputStream outputStream = new FileOutputStream(sourceFilePath)) {
                g.dispose();
                if (null != suffix && suffix.length() > 1) {
                    ImageIO.write(sourceImage, suffix.substring(1), outputStream);
                } else {
                    ImageIO.write(sourceImage, DEFAULT_FORMAT_NAME, outputStream);
                }
            }
        }
    }

    private static int matermarkPosition(String position, int width, int height, int watermarkWidth, int watermarkHeight, int textWidth, int textHeight, boolean x, boolean text) {
        int result = 0;
        int between = (0 == watermarkWidth || 0 == textWidth) ? 0 : 10;
        int boxHeight = watermarkHeight > textHeight ? watermarkHeight : textHeight;
        switch (position) {
        case "center":
            if (x) {
                result = (width - watermarkWidth - textWidth - between) / 2;
            } else {
                result = (height - boxHeight) / 2;
            }
            break;
        case "leftTop":
            result = 10;
            break;
        case "leftBottom":
            if (x) {
                result = 10;
            } else {
                result = height - boxHeight - 10;
            }
            break;
        case "rightTop":
            if (x) {
                result = width - watermarkWidth - textWidth - between - 10;
            } else {
                result = 10;
            }
            break;
        case "rightBottom":
        default:
            if (x) {
                result = width - watermarkWidth - textWidth - between - 10;
            } else {
                result = height - boxHeight - 10;
            }
        }

        if (text) {
            if (x) {
                result += watermarkWidth + between;
            } else {
                if (watermarkHeight > textHeight) {
                    result += (watermarkHeight - textHeight) / 2;
                }
                result += textHeight;
            }
        }
        return result;

    }
}
