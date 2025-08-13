package com.trip.noting.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SvgToJpgUtil {


    public ByteArrayOutputStream convertToPngByFile(byte[] svgCode, Map<String, String> map) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ByteArrayInputStream streams = new ByteArrayInputStream(svgCode);
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
            Document doc = f.createDocument(null, streams);
            Element e = doc.getDocumentElement();
            for (int i = 1; i <= map.size() / 2; i++) {
                e.setAttribute(map.get("name" + i), map.get("value" + i));
            }
            PNGTranscoder t = new PNGTranscoder();
            //t.addTranscodingHint(JPEGTranscoder., 0.8f);
            TranscoderInput input = new TranscoderInput(doc);
            TranscoderOutput output = new TranscoderOutput(stream);
            t.transcode(input, output);
//            ByteArrayInputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
//            stream=SvgToJpgUtil.transferAlpha2Byte(inputStream);
            stream.flush();
        } catch (Exception e) {
            log.warn("transcode Fail", e);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                Map<String, String> maps = new HashMap<>();
                maps.put("ApiName", "SvgToJpgUtil");
                log.warn("IO FAIL", e, maps);
            }
        }
        return stream;
    }

    public ByteArrayOutputStream transferAlpha2Byte(InputStream is) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = null;
        ByteArrayOutputStream result;
        try {
            BufferedImage bi = ImageIO.read(is);
            ImageIcon imageIcon = new ImageIcon(bi);
            BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
            int alpha = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                    int rgb = bufferedImage.getRGB(j2, j1);
                    int R = (rgb & 0xff0000) >> 16;
                    int G = (rgb & 0xff00) >> 8;
                    int B = (rgb & 0xff);
                    if (((255 - R) < 30) && ((255 - G) < 30) && ((255 - B) < 30)) {
                        rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                    }
                    bufferedImage.setRGB(j2, j1, rgb);
                }
            }
            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
            byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);//输出到 outputStream
            result = byteArrayOutputStream;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.warn("InputStream close fail");
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    log.warn("byteArrayOutputStream close fail");
                }
            }
        }
        return result;
    }
}
