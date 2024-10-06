package com.trip.noting.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

@Controller
public class FileController {

    @GetMapping("download")
    public void download(HttpServletResponse response) throws IOException {
        // 文件流生成使用ByteArrayOutputStream
        // ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // byte[] byteArray = outputStream.toByteArray();
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), DownloadData.class).sheet("模板").doWrite(data());
    }

    private List<DownloadData> data() {
        List<DownloadData> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            DownloadData data = new DownloadData();
            data.setString("字符串" + 0);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }

    public void writeFile() {
        String filename="";
        // 首先创建任意一个OutPutStream流但最好是用Byte
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // 使用这个方法把Excel数据write到流中
            EasyExcel.write(outputStream)
                    .head(DownloadData.class)
                    .sheet("filename")
                    .doWrite(data());
            // 此时这个流中已经有数据 因为业务需求我要转成MultipartFile 所以会有这一步
            InputStream inputStream = streamTran(outputStream);
            // MultipartFile multipartFile = new MultipartFile(filename + ".xlsx", filename + ".xlsx", "application/vnd.ms-excel", inputStream);
            // 我的业务处理比较繁琐了 但是也是没办法其实有了字节流就已经可以为所欲为了
            BASE64Encoder base64Encoder = new BASE64Encoder();
            // String encode = base64Encoder.encode(multipartFile.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private InputStream streamTran(ByteArrayOutputStream in) {
        return new ByteArrayInputStream(in.toByteArray());
    }
}
