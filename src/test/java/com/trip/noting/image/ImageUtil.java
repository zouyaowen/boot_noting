package com.trip.noting.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageUtil {

    public static void main(String[] args) throws Exception {
        // BufferedImage originalImage = ImageIO.read(new File("input_f.png"));
        // BufferedImage resultImage = processImage(originalImage);
        // ImageIO.write(resultImage, "PNG", new File("output_blur_4.png"));
        BufferedImage originalImage = ImageIO.read(new File("hangcheng_1.jpg"));
        BufferedImage resultImage = processImage(originalImage);
        // formatName 写死 PNG JPEG参数不行
        ImageIO.write(resultImage, "PNG", new File("hangcheng_out_9_750_594.jpg"));

    }

    public static BufferedImage processImage(BufferedImage original) {
        int w = original.getWidth();
        int h = original.getHeight();
        int s = Math.max(w, h);

        // 创建覆盖缩放图像作为模糊背景
        BufferedImage coverImage = createCoverImage(original, s);
        // 应用高斯模糊
        // radius=5, sigma=2.0	原图	轻度模糊
        // radius=10, sigma=3.5	同上	中度模糊
        // radius=15, sigma=5.0	同上	重度模糊
        // radius=30, sigma=10.0	同上	重度模糊
        BufferedImage blurredCover = applyGaussianBlur_(coverImage, 100, 33f);

        // 创建最终图像并绘制模糊背景
        BufferedImage finalImage = new BufferedImage(s, s, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = finalImage.createGraphics();
        g.drawImage(blurredCover, 0, 0, null);

        // 居中绘制原始图像（保持比例）
        BufferedImage containImage = createContainImage(original, s);
        int x = (s - containImage.getWidth()) / 2;
        int y = (s - containImage.getHeight()) / 2;
        g.drawImage(containImage, x, y, null);
        g.dispose();

        return finalImage;
    }

    // 创建覆盖缩放图像（填充整个区域）
    private static BufferedImage createCoverImage(BufferedImage original, int s) {
        int w = original.getWidth();
        int h = original.getHeight();
        float scale = Math.max((float) s / w, (float) s / h);
        int scaledWidth = (int) (w * scale);
        int scaledHeight = (int) (h * scale);

        BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaledImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();

        // 裁剪中间部分
        int x = (scaledWidth - s) / 2;
        int y = (scaledHeight - s) / 2;
        x = Math.max(x, 0);
        y = Math.max(y, 0);
        return scaledImage.getSubimage(x, y, s, s);
    }

    // 创建包含缩放图像（适应区域）
    private static BufferedImage createContainImage(BufferedImage original, int s) {
        int w = original.getWidth();
        int h = original.getHeight();
        float scale = Math.min((float) s / w, (float) s / h);
        int scaledWidth = (int) (w * scale);
        int scaledHeight = (int) (h * scale);

        BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaledImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledImage;
    }

    // 应用高斯模糊——祛除黑边
    private static BufferedImage applyGaussianBlur_(BufferedImage image, int radius, float sigma) {
        int w = image.getWidth();
        int h = image.getHeight();

        // 创建镜像边缘填充的图像
        BufferedImage paddedImage = new BufferedImage(
                w + 2 * radius,
                h + 2 * radius,
                BufferedImage.TYPE_INT_ARGB
        );

        // 绘制原图到中心
        Graphics2D g = paddedImage.createGraphics();
        g.drawImage(image, radius, radius, null);

        // 手动扩展边缘像素（镜像填充）避免黑色边缘
        extendEdges(image, paddedImage, radius);
        g.dispose();

        float[] kernel = createGaussianKernel(radius, sigma);

        // 水平模糊
        Kernel horizontalKernel = new Kernel(kernel.length, 1, kernel);
        ConvolveOp horizontalOp = new ConvolveOp(horizontalKernel, ConvolveOp.EDGE_NO_OP, null);
        BufferedImage horizontalBlur = horizontalOp.filter(paddedImage, null);

        // 垂直模糊
        Kernel verticalKernel = new Kernel(1, kernel.length, kernel);
        ConvolveOp verticalOp = new ConvolveOp(verticalKernel, ConvolveOp.EDGE_NO_OP, null);
        BufferedImage verticalBlur = verticalOp.filter(horizontalBlur, null);

        // 裁剪回原始尺寸
        return verticalBlur.getSubimage(radius, radius, image.getWidth(), image.getHeight());
    }

    // 镜像边缘填充方法
    private static void extendEdges(BufferedImage src, BufferedImage dest, int padding) {
        int w = src.getWidth();
        int h = src.getHeight();

        // 填充四边
        for (int y = 0; y < h; y++) {
            // 左边缘
            int leftPixel = src.getRGB(0, y);
            for (int x = 0; x < padding; x++) {
                dest.setRGB(x, y + padding, leftPixel);
            }
            // 右边缘
            int rightPixel = src.getRGB(w - 1, y);
            for (int x = 0; x < padding; x++) {
                dest.setRGB(w + padding + x, y + padding, rightPixel);
            }
        }

        for (int x = 0; x < w; x++) {
            // 上边缘
            int topPixel = src.getRGB(x, 0);
            for (int y = 0; y < padding; y++) {
                dest.setRGB(x + padding, y, topPixel);
            }
            // 下边缘
            int bottomPixel = src.getRGB(x, h - 1);
            for (int y = 0; y < padding; y++) {
                dest.setRGB(x + padding, h + padding + y, bottomPixel);
            }
        }

        // 填充四角（可选）
        fillCorners(dest, padding);
    }

    // 填充四个角落（进一步优化过渡）
    private static void fillCorners(BufferedImage dest, int padding) {
        int width = dest.getWidth();
        int height = dest.getHeight();

        // 左上角
        int topLeft = dest.getRGB(padding, padding);
        for (int x = 0; x < padding; x++) {
            for (int y = 0; y < padding; y++) {
                dest.setRGB(x, y, topLeft);
            }
        }

        // 右上角
        int topRight = dest.getRGB(width - padding - 1, padding);
        for (int x = width - padding; x < width; x++) {
            for (int y = 0; y < padding; y++) {
                dest.setRGB(x, y, topRight);
            }
        }

        // 左下角
        int bottomLeft = dest.getRGB(padding, height - padding - 1);
        for (int x = 0; x < padding; x++) {
            for (int y = height - padding; y < height; y++) {
                dest.setRGB(x, y, bottomLeft);
            }
        }

        // 右下角
        int bottomRight = dest.getRGB(width - padding - 1, height - padding - 1);
        for (int x = width - padding; x < width; x++) {
            for (int y = height - padding; y < height; y++) {
                dest.setRGB(x, y, bottomRight);
            }
        }
    }


    // 应用高斯模糊
    private static BufferedImage applyGaussianBlur(BufferedImage image, int radius, float sigma) {
        // 添加边缘填充
        int padding = radius;
        BufferedImage paddedImage = new BufferedImage(
                image.getWidth() + 2 * padding,
                image.getHeight() + 2 * padding,
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g = paddedImage.createGraphics();
        g.drawImage(image, padding, padding, null);
        g.dispose();

        float[] kernel = createGaussianKernel(radius, sigma);

        // 水平模糊
        Kernel horizontalKernel = new Kernel(kernel.length, 1, kernel);
        ConvolveOp horizontalOp = new ConvolveOp(horizontalKernel, ConvolveOp.EDGE_NO_OP, null);
        BufferedImage horizontalBlur = horizontalOp.filter(paddedImage, null);

        // 垂直模糊
        Kernel verticalKernel = new Kernel(1, kernel.length, kernel);
        ConvolveOp verticalOp = new ConvolveOp(verticalKernel, ConvolveOp.EDGE_NO_OP, null);
        BufferedImage verticalBlur = verticalOp.filter(horizontalBlur, null);

        // 裁剪回原始尺寸
        return verticalBlur.getSubimage(padding, padding, image.getWidth(), image.getHeight());
    }

    // 生成高斯核
    private static float[] createGaussianKernel(int radius, float sigma) {
        int size = 2 * radius + 1;
        float[] kernel = new float[size];
        float sum = 0.0f;

        for (int i = -radius; i <= radius; i++) {
            float value = (float) Math.exp(-(i * i) / (2 * sigma * sigma));
            kernel[i + radius] = value;
            sum += value;
        }

        // 归一化
        for (int i = 0; i < size; i++) {
            kernel[i] /= sum;
        }
        return kernel;
    }
}