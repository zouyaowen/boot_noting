package com.trip.noting.image;

import com.alibaba.fastjson2.JSON;
import com.trip.noting.utils.SvgToJpgUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SvgTest {
    @Test
    public void svgConverter() throws IOException {
        // 示例：加载本地 SVG 文件为字节数组
        // File svgFile = new File("diceng.svg");
        ClassPathResource resource = new ClassPathResource("diceng.svg");
        try {
            InputStream inputStream = resource.getInputStream();
            byte[] bytes = readFileToByteArray((BufferedInputStream) inputStream);
            byte[] jpgBytes = convertSvgToJpg(bytes);
            Files.write(Paths.get("1-2.jpg"), jpgBytes);
        } catch (IOException e) {
            log.error("svgConverter", e);
        }


        // // 转换为 JPG 字节数组
        // byte[] jpgBytes = convertSvgToJpg(svgBytes);
        // // 将图像写入文件
        // Files.write(Paths.get("diceng_output.jpg"), jpgBytes);
        // System.out.println("------------ending-----------------");
    }

    /**
     * 将 SVG 字节数组转换为 JPG 字节数组
     *
     * @param svgBytes 输入的 SVG 字节数组
     * @return 转换后的 JPG 字节数组
     * @throws IOException 转换过程中可能抛出的异常
     */
    public static byte[] convertSvgToJpg(byte[] svgBytes) throws IOException {
        // 将字节数组转换为输入流
        InputStream svgInputStream = new ByteArrayInputStream(svgBytes);

        // 使用 Batik Transcoder 渲染 SVG 为 BufferedImage
        BufferedImage bufferedImage = renderSvgToBufferedImage(svgInputStream);

        // 将 BufferedImage 转换为 JPG 格式字节数组
        return bufferedImageToJpgBytes(bufferedImage);
    }

    /**
     * 将 BufferedImage 转换为 JPG 格式字节数组
     *
     * @param bufferedImage 输入的 BufferedImage
     * @return 转换后的 JPG 字节数组
     * @throws IOException 转换过程中可能抛出的异常
     */
    private static byte[] bufferedImageToJpgBytes(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // 使用 ImageIO 写出为 JPG 格式
            ImageIO.write(bufferedImage, "jpg", outputStream);
            outputStream.flush();
            return outputStream.toByteArray();
        } finally {
            outputStream.close();
        }
    }

    /**
     * 使用 Batik 渲染 SVG 为 BufferedImage
     *
     * @param svgInputStream 输入的 SVG 流
     * @return 渲染后的 BufferedImage
     * @throws IOException 转换过程中可能抛出的异常
     */
    private static BufferedImage renderSvgToBufferedImage(InputStream svgInputStream) throws IOException {
        final BufferedImage[] imagePointer = new BufferedImage[1];
        // 自定义 Transcoder，将渲染结果存入 BufferedImage
        ImageTranscoder transcoder = new ImageTranscoder() {
            @Override
            public BufferedImage createImage(int width, int height) {
                System.out.println("width:" + width * 15);
                System.out.println("height:" + height * 15);
                return new BufferedImage(width * 15, height * 15, BufferedImage.TYPE_INT_RGB);
            }

            @Override
            public void writeImage(BufferedImage img, TranscoderOutput output) {
                imagePointer[0] = img;
            }
        };
        transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, 1f);
        // 配置 Transcoder
        TranscoderInput input = new TranscoderInput(svgInputStream);
        try {
            transcoder.transcode(input, null);
        } catch (Exception e) {
            throw new IOException("Error while transcoding SVG to BufferedImage", e);
        }
        return imagePointer[0];
    }

    /**
     * 读取文件为字节数组
     *
     * @return 文件的字节数组
     * @throws IOException 读取文件时可能抛出的异常
     */
    private static byte[] readFileToByteArray(BufferedInputStream inputStream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
        return bos.toByteArray();
    }


    // 方式二
    @Test
    public void svgConverter2() throws IOException, TranscoderException {
        File svgFile = null;
        try {
            svgFile = ResourceUtils.getFile("classpath:diceng.svg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        float compressionQuality = 1f; // 压缩质量，范围0.0-1.0，值越小压缩越强
        JPEGTranscoder transcoder = new JPEGTranscoder();
        transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, compressionQuality); // 设置压缩质量
        // 设置输入和输出
        TranscoderInput input = new TranscoderInput(svgFile.toURI().toURL().toString());
        OutputStream outputStream = new FileOutputStream("diceng-output444.jpg");
        TranscoderOutput output = new TranscoderOutput(outputStream);
        // 执行转换
        transcoder.transcode(input, output);
        outputStream.flush();
        outputStream.close();
    }

    // 方式三
    @Test
    public void svgConverter3() throws IOException, TranscoderException {
        SvgToJpgUtil svgToJpgUtil = new SvgToJpgUtil();

        ClassPathResource resource = new ClassPathResource("diceng.svg");

        InputStream inputStream = resource.getInputStream();
        ByteArrayOutputStream byteArrayOutputStream = svgToJpgUtil.transferAlpha2Byte(inputStream);
        inputStream.close();
        Files.write(Paths.get("3_output.jpg"), byteArrayOutputStream.toByteArray());
    }


    @Test
    public void testNumber() throws IOException, TranscoderException {
        // 2/4=4/8   280/x=42.29488/242.57634
        BigDecimal bigDecimal = new BigDecimal("280");
        BigDecimal bigDecimal1 = new BigDecimal("42.29488");
        BigDecimal divide1 = bigDecimal1.divide(new BigDecimal("242.57634"), 2, RoundingMode.HALF_UP);
        System.out.println(bigDecimal.divide(divide1, 2, RoundingMode.HALF_UP));
    }

    // 方式四
    @Test
    public void svgConverter4() throws IOException, TranscoderException {
        SvgToJpgUtil svgToJpgUtil = new SvgToJpgUtil();

        ClassPathResource resource = new ClassPathResource("diceng.svg");
        InputStream inputStream = resource.getInputStream();


        BigDecimal height = new BigDecimal("280").divide(new BigDecimal("42.29488").divide(new BigDecimal("242.57634"), 2, RoundingMode.HALF_UP), 2, RoundingMode.HALF_UP);

        // Map<String, String> map = new HashMap<>();
        // map.put("name1", "width");
        // map.put("value1", new BigDecimal("280").toString());
        // map.put("name2", "height");
        // map.put("value2", height.toString());

        Map<String, String> map = new HashMap<>();
        map.put("name1", "width");
        map.put("value1", new BigDecimal("42.29488").multiply(new BigDecimal("10")).toString());
        map.put("name2", "height");
        map.put("value2", new BigDecimal("242.57634").multiply(new BigDecimal("10")).toString());

        ByteArrayOutputStream outputStream = svgToJpgUtil.convertToPngByFile(readFileToByteArray(inputStream), map);

        Files.write(Paths.get("4-5.jpg"), outputStream.toByteArray());
    }

    private static byte[] readFileToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
        return bos.toByteArray();
    }

    @Test
    public void testSub() throws IOException, TranscoderException {
        String str = "/3/fg/aa.svg";
        System.out.println(str.substring(1));
    }

    private final Semaphore semaphore = new Semaphore(5);

    public void limitedMethod() {
        try {
            semaphore.acquire(); // 获取许可，最多允许5个线程进入
            // 你的业务逻辑
            System.out.println(Thread.currentThread().getName() + " is running");
            Thread.sleep(100); // 模拟耗时操作
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release(); // 释放许可
            System.out.println(Thread.currentThread().getName() + " has finished");
        }
    }

    @Test
    public void testImageGet() throws IOException, TranscoderException {
        for (int i = 0; i < 10; i++) {
            new Thread(this::limitedMethod).start();
        }
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testNumber_() {
        DecimalFormat formatter = new DecimalFormat("00");
        List<Long> numList = new ArrayList<>();
        numList.add(1L);
        numList.add(8L);
        numList.add(15L);
        for (Long num : numList) {
            String format = formatter.format(num % 4);
            System.out.println(format);
        }
    }

    @Data
    public static class JSONEntity {
        private Boolean isNull;
        private Boolean isUpdated;
        private String name;
    }

    @Test
    public void testJSON() {
        String str = "{\n" +
                "            \"isNull\": false,\n" +
                "            \"name\": \"ModifyUser\",\n" +
                "            \"isKey\": false,\n" +
                "            \"isUpdated\": true,\n" +
                "            \"value\": \"M2250560238\"\n" +
                "        }";
        JSONEntity jsonEntity = JSON.parseObject(str, JSONEntity.class);
        System.out.println(jsonEntity);

    }

    @Test
    public void testImageHandle() throws InterruptedException {
        ImageProcessor processor = new ImageProcessor();

        // 基本使用
        try {
            processor.processImage("input.png", "output.png");
        } catch (IOException e) {
            log.error("testImageHandle", e);
        }

        // 带进度回调的使用
        processor.processImageWithProgress(
                "input_f.png",
                "output_blur_3.png",
                new ImageProcessor.ProgressCallback() {
                    @Override
                    public void onProgress(int percent, String message) {
                        System.out.println("Progress: " + percent + "% - " + message);
                    }

                    @Override
                    public void onError(String error) {
                        System.err.println("Error: " + error);
                    }
                }
        );
        TimeUnit.SECONDS.sleep(5);
    }
}
