package com.trip.noting.controller;

import cn.idev.excel.EasyExcel;
import cn.idev.excel.ExcelWriter;
import cn.idev.excel.enums.WriteDirectionEnum;
import cn.idev.excel.util.ListUtils;
import cn.idev.excel.write.metadata.WriteSheet;
import cn.idev.excel.write.metadata.fill.FillConfig;
import cn.idev.excel.write.metadata.fill.FillWrapper;
import cn.idev.excel.write.style.HorizontalCellStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Controller
@Slf4j
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
            data.setName("字符串" + i);
            data.setTime(new Date());
            data.setNumber(0.88);
            list.add(data);
        }
        return list;
    }

    public void writeFile() {
        String filename = "";
        // 首先创建任意一个OutPutStream流但最好是用Byte
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // 使用这个方法把Excel数据write到流中
            EasyExcel.write(outputStream).head(DownloadData.class).sheet("filename").doWrite(data());
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


    @GetMapping("downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) throws IOException, URISyntaxException {
        URL res = getClass().getClassLoader().getResource("templates/excel_test.xlsx");
        if (res == null) {
            return;
        }
        File file = Paths.get(res.toURI()).toFile();
        // 根据模板构建数据写入新的文件对象并制定sheet
        EasyExcel.write(file, DownloadData.class).file("excel_test3.xlsx").sheet(0, "sheet1").doWrite(data());
    }

    // 测试成功（多sheet多列数据）：模板文件适用的灵魂是占位符
    @GetMapping("writeMultiSheet")
    public void writeMultiSheet(HttpServletResponse response) throws IOException {
        // 读取模板
        InputStream templateStream = getClass().getClassLoader().getResourceAsStream("templates/excel_template.xlsx");
        if (templateStream == null) {
            return;
        }
        // 指定输出文件（新文件）
        String outputFileName = "excel_test6.xlsx";
        try (ExcelWriter excelWriter = EasyExcel.write(outputFileName).withTemplate(templateStream).build()) {
            WriteSheet sheet0 = EasyExcel.writerSheet(0).build();
            excelWriter.fill(data(), sheet0);
            WriteSheet sheet1 = EasyExcel.writerSheet(1).build();
            excelWriter.fill(getUsers(), sheet1);
        }
    }

    // 验证数据横向不规则扩展
    @GetMapping("writeMultiSheetExt")
    public void writeMultiSheetExt(HttpServletResponse response) throws IOException {
        // 读取模板
        InputStream templateStream = getClass().getClassLoader().getResourceAsStream("templates/excel_template.xlsx");
        if (templateStream == null) {
            return;
        }
        // 指定输出文件（新文件）
        String outputFileName = "excel_test12.xlsx";
        FillConfig fillConfig = FillConfig.builder()
                .direction(WriteDirectionEnum.HORIZONTAL) // 设置横向填充
                .build();
        try (ExcelWriter excelWriter = EasyExcel.write(outputFileName).withTemplate(templateStream).build()) {
            // WriteSheet sheet2 = EasyExcel.writerSheet(2).registerWriteHandler(new HorizontalCellStyleStrategy()).build();
            WriteSheet sheet2 = EasyExcel.writerSheet(2).build();
            excelWriter.fill(new FillWrapper("数据类型数据列表", getNameList()), fillConfig, sheet2);
            // excelWriter.fill(getNameList(), sheet2);
        }

        // FillConfig fillConfig = FillConfig.builder()
        //         .direction(WriteDirectionEnum.HORIZONTAL) // 设置横向填充
        //         .build();
        // //
        // //
        // WriteSheet writeSheet = EasyExcel.writerSheet().build();
        // excelWriter.fill(new FillWrapper("nameList", getNameList()), fillConfig, writeSheet);


    }

    private List<SheetExtData> getNameList() {
        List<SheetExtData> sheetExtData = new ArrayList<>();
        sheetExtData.add(new SheetExtData() {{
            this.setName("测试1");
            List<String> nameList = new ArrayList<>();
            nameList.add("名称一");
            nameList.add("名称二");
            this.setNameList(nameList);
        }});
        sheetExtData.add(new SheetExtData() {{
            this.setName("测试2");
            List<String> nameList = new ArrayList<>();
            nameList.add("名称二二");
            nameList.add("名称三三");
            this.setNameList(nameList);
        }});
        return sheetExtData;
    }

    // 代码有问题
    @GetMapping("downloadMultiSheet")
    public void downloadMultiSheet(HttpServletResponse response) throws URISyntaxException, IOException {
        // 读取模板
        InputStream templateStream = getClass().getClassLoader().getResourceAsStream("templates/excel_template.xlsx");
        if (templateStream == null) {
            return;
        }
        // 指定输出文件（新文件）
        String outputFileName = "excel_test3.xlsx";
        // 写入 Sheet0（假设模板中已有 Sheet0）
        EasyExcel.write(outputFileName).withTemplate(templateStream).sheet(0).doFill(data());
        // 此处代码有问题
        EasyExcel.write(outputFileName).withTemplate(templateStream).sheet(1).doFill(getUsers());
    }

    private List<Sheet2Data> getUsers() {
        System.out.println("------------getUsers--------------");
        List<Sheet2Data> sheet2Data = new ArrayList<>();
        sheet2Data.add(new Sheet2Data() {{
            this.setId(1L);
            this.setName("用户1");
            this.setAge(11);
        }});
        sheet2Data.add(new Sheet2Data() {{
            this.setId(2L);
            this.setName("用户2");
            this.setAge(22);
        }});
        sheet2Data.add(new Sheet2Data() {{
            this.setId(3L);
            this.setName("用户3");
            this.setAge(33);
        }});
        return sheet2Data;
    }


    // cruiseRP5049Export.xlsx
    @GetMapping("exportReal")
    public void export(HttpServletResponse response) throws URISyntaxException {
        // 读取模板
        InputStream templateStream = getClass().getClassLoader().getResourceAsStream("templates/cruiseRP5049Export.xlsx");
        if (templateStream == null) {
            return;
        }
        // 指定输出文件（新文件）
        String outputFileName = "export3.xlsx";
        File outputFile = new File(outputFileName);
        try (ExcelWriter excelWriter = EasyExcel.write(outputFile).withTemplate(templateStream).build()) {
            // 写入 Sheet0（假设模板中已有 Sheet0）
            WriteSheet sheet1 = EasyExcel.writerSheet(1, "标准房型").head(RoomType.class).build();
            excelWriter.write(getRoomType(), sheet1);
            // 写入 Sheet1（新增 Sheet）
            WriteSheet sheet2 = EasyExcel.writerSheet(2, "航线列表").head(Voyage.class).build();
            excelWriter.write(getVoyages(), sheet2);
        } catch (Exception e) {
            log.error("exportReal={}", e.getMessage());
        }
    }

    private List<Voyage> getVoyages() {
        List<Voyage> voyages = new ArrayList<>();
        voyages.add(new Voyage() {{
            this.setName("211492/纽约+百慕大+菲利普斯堡+圣胡安+拉巴地+纽约");
        }});
        voyages.add(new Voyage() {{
            this.setName("211555/纽约+哈里法克斯+纽约");
        }});
        return voyages;

    }

    private List<RoomType> getRoomType() {
        List<RoomType> roomTypes = new ArrayList<>();
        roomTypes.add(new RoomType() {{
            this.setName("内舱房");
            this.setCode("1V");
        }});
        roomTypes.add(new RoomType() {{
            this.setName("阳台房");
            this.setCode("3D");
        }});
        return roomTypes;
    }
}
