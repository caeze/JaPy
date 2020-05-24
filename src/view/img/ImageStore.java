package view.img;

import console.Log;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.RescaleOp;

import javax.swing.ImageIcon;

/**
 * Image Store for the application.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class ImageStore {

    public static ImageIcon getImageIcon(String name) {
        ImageIcon iconToReturn = null;
        try {
            if (name != null && !name.isEmpty() && ImageStore.class.getResource(name) != null) {
                iconToReturn = new ImageIcon(ImageStore.class.getResource(name));
                return iconToReturn;
            }
        } catch (Exception e) {
            Log.error(ImageStore.class, e.getMessage());
        }

        return createResourceNotFoundImage();
    }

    public static ImageIcon getImageForButton(String imageFileName, int width, int height, int percentToLightUp, float newAlpha) {
        ImageIcon iconToReturn = null;
        try {
            iconToReturn = ImageStore.getImageIcon(imageFileName);
            iconToReturn = getScaledImage(iconToReturn, width, height);
            iconToReturn = lightImageUp(iconToReturn, percentToLightUp);
            iconToReturn = setImageAlphaValues(iconToReturn, newAlpha);
        } catch (Exception e) {
            Log.error(ImageStore.class, e.getMessage());
        }
        return iconToReturn;
    }

    public static ImageIcon getScaledImage(ImageIcon srcImg, int width, int height) {
        BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resizedImg.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(srcImg.getImage(), 0, 0, width, height, null);
        graphics2D.dispose();
        return new ImageIcon(resizedImg);
    }

    public static ImageIcon lightImageUp(ImageIcon srcImg, int percentToLightUp) {
        BufferedImage image = new BufferedImage(srcImg.getIconWidth(), srcImg.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageTarget = new BufferedImage(srcImg.getIconWidth(), srcImg.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.createGraphics();
        srcImg.paintIcon(null, graphics, 0, 0);
        graphics.dispose();
        Graphics graphics2 = imageTarget.createGraphics();
        srcImg.paintIcon(null, graphics2, 0, 0);
        graphics2.dispose();
        RescaleOp rop = new RescaleOp(1 + (percentToLightUp * 0.01f), 0, null);
        rop.filter(image, imageTarget);
        return new ImageIcon(imageTarget);
    }

    public static ImageIcon setImageAlphaValues(ImageIcon srcImg, float newAlpha) {
        BufferedImage image = new BufferedImage(srcImg.getIconWidth(), srcImg.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) image.createGraphics();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, newAlpha));
        srcImg.paintIcon(null, graphics, 0, 0);
        graphics.dispose();
        return new ImageIcon(image);
    }

    private static ImageIcon createResourceNotFoundImage() {
        byte[] imageBytes = {-1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1,
            28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, 20, 24, 0,
            0, 0, 53, 0, 0, 0, 0, 0, 0, 31, 16, 53, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, 0, 11, -87, 14, 0, 0, -1, 28, -87, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28,
            0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, 8, 16, -52, 8, 0, 0, -1, 24, -123, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, 25, 28, 0, 0, 0, 95, 20, 6, 0, -1, 28, -52, -1, 28, 0, -1, 28, 0,
            -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, 31, 28, 0, 0, 6, -123, 0, 0, 0, 14, 0, 0, -1, 28, -87, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, 25, 28, 0, 0, 0, 95, 31, 16, 53, -1, 28,
            0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1,
            28, 0, 8, 16, -52, 8, 0, 0, -1, 24, -123, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, 0, 11, -87, 14, 0, 0, -1, 28, -87, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28,
            0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1,
            28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0,
            -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0, -1, 28, 0};
        return new ImageIcon(createRGBImage(imageBytes, 16, 16));
    }

    private static BufferedImage createRGBImage(byte[] bytes, int width, int height) {
        DataBufferByte buffer = new DataBufferByte(bytes, bytes.length);
        ColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[]{8, 8, 8}, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        return new BufferedImage(cm, Raster.createInterleavedRaster(buffer, width, height, width * 3, 3, new int[]{0, 1, 2}, null), false, null);
    }

    public static ImageIcon createImageIconFromText(String text, int fontSize, Color foregroundColor) {
        return createImageIconFromText(text, fontSize, foregroundColor, null);
    }

    public static ImageIcon createImageIconFromText(String text, int fontSize, Color foregroundColor, Color backgroundColor) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("Arial", Font.PLAIN, fontSize);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        if (backgroundColor != null) {
            g2d.setColor(backgroundColor);
            g2d.fillRect(0, 0, width, height);
        }
        g2d.setColor(foregroundColor);
        g2d.drawString(text, 0, fm.getAscent());
        g2d.dispose();
        return new ImageIcon(img);
    }
}
