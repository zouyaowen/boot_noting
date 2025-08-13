package com.trip.noting.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

public class ImageProcessor {

    /**
     * 主处理方法
     */
    public void processImage(String inputPath, String outputPath) throws IOException {
        // 1. 读取原始图片
        BufferedImage originalImage = ImageIO.read(new File(inputPath));

        // 2. 调整为1:1比例
        BufferedImage squareImage = makeImageSquare(originalImage);

        // 3. 应用高斯模糊
        // BufferedImage blurredImage = applyGaussianBlur(squareImage);
        BufferedImage blurredImage = applyGaussianBlur_(squareImage);

        // 4. 保存处理后的图片
        ImageIO.write(blurredImage, "PNG", new File(outputPath));
    }

    /**
     * 将图片调整为1:1比例
     */
    private BufferedImage makeImageSquare(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();
        int size = Math.max(width, height);

        // 创建正方形画布
        BufferedImage square = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = square.createGraphics();

        // 设置背景透明
        square.createGraphics().setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, size, size);

        // 设置渲染品质
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // 居中绘制原图
        int x = (size - width) / 2;
        int y = (size - height) / 2;
        g2d.drawImage(original, x, y, width, height, null);
        g2d.dispose();

        return square;
    }


    /**
     * 应用高斯模糊——测试显示图片不对
     */
    private BufferedImage applyGaussianBlur(BufferedImage input) {
        // 创建高斯模糊核
        float[] matrix = createGaussianKernel(5, 1.0f);
        Kernel kernel = new Kernel(5, 5, matrix);

        // 创建卷积操作
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

        // 应用模糊效果
        return op.filter(input, null);
    }

    private BufferedImage applyGaussianBlur_(BufferedImage originalImage) {
        // 创建高斯模糊核
        BufferedImage blurredImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = blurredImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.drawImage(originalImage, 0, 0, originalImage.getWidth(), originalImage.getHeight(), null);
        g2d.dispose();

        // 应用高斯模糊
        float[] matrix = {
                1f / 16f, 2f / 16f, 1f / 16f,
                2f / 16f, 4f / 16f, 2f / 16f,
                1f / 16f, 2f / 16f, 1f / 16f
        };
        BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, matrix), ConvolveOp.EDGE_NO_OP, null);
        blurredImage = op.filter(blurredImage, null);
        return blurredImage;
    }

    /**
     * 创建高斯核矩阵
     */
    private float[] createGaussianKernel(int radius, float sigma) {
        float[] matrix = new float[radius * radius];
        float sum = 0.0f;

        for (int i = 0; i < radius; i++) {
            for (int j = 0; j < radius; j++) {
                int x = i - (radius - 1) / 2;
                int y = j - (radius - 1) / 2;
                matrix[i * radius + j] = (float) Math.exp(-(x * x + y * y) / (2 * sigma * sigma));
                sum += matrix[i * radius + j];
            }
        }

        // 归一化
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] /= sum;
        }

        return matrix;
    }

    /**
     * 高级模糊处理（多次应用不同参数的高斯模糊）
     */
    private BufferedImage advancedBlur(BufferedImage input) {
        BufferedImage result = input;

        // 多层模糊，不同半径
        float[] radiusArray = {3f, 5f, 7f};
        float[] sigmaArray = {0.8f, 1.0f, 1.2f};

        for (int i = 0; i < radiusArray.length; i++) {
            float[] matrix = createGaussianKernel((int) radiusArray[i], sigmaArray[i]);
            Kernel kernel = new Kernel((int) radiusArray[i],
                    (int) radiusArray[i],
                    matrix);
            ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
            result = op.filter(result, null);
        }

        return result;
    }

    /**
     * 带进度回调的处理方法
     */
    public void processImageWithProgress(String inputPath,
                                         String outputPath,
                                         ProgressCallback callback) {
        try {
            callback.onProgress(0, "开始处理图片");

            BufferedImage original = ImageIO.read(new File(inputPath));
            callback.onProgress(25, "图片读取完成");

            BufferedImage square = makeImageSquare(original);
            callback.onProgress(50, "调整比例完成");

            BufferedImage blurred = applyGaussianBlur(square);
            callback.onProgress(75, "模糊处理完成");

            ImageIO.write(blurred, "PNG", new File(outputPath));
            callback.onProgress(100, "处理完成");

        } catch (IOException e) {
            callback.onError("处理失败: " + e.getMessage());
        }
    }

    /**
     * 进度回调接口
     */
    public interface ProgressCallback {
        void onProgress(int percent, String message);

        void onError(String error);
    }

    /**
     * 图片处理参数配置类
     */
    public static class ProcessingConfig {
        private float blurRadius = 5.0f;
        private float blurSigma = 1.0f;
        private boolean preserveTransparency = true;
        private Color backgroundColor = new Color(255, 255, 255, 0);

        // getter和setter方法
        // ...

        public static ProcessingConfig getDefaultConfig() {
            return new ProcessingConfig();
        }
    }
}
