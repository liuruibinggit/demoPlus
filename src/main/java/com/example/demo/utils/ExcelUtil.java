package com.example.demo.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ExcelUtil {


    /**
     * 导出基本样式数据表格
     * @param worktableTitle
     * @param cellTitle
     * @param excelHeader
     * @param column
     * @param list
     * @return
     */
    public static HSSFWorkbook exportGeneralExcel(String worktableTitle, String cellTitle, String[] excelHeader, String[] column, List<Map> list) {
        HSSFWorkbook wb = new HSSFWorkbook();
        try {
            // 建立工作薄
            HSSFSheet sheet = wb.createSheet(worktableTitle);
            sheet.createFreezePane(0, 2, 0, 2);// 冻结第二行
            // 设置合并单元格字体
            HSSFFont font0 = wb.createFont();
            font0.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font0.setFontHeightInPoints((short) 15);
            // 设置列标题的单元格字体
            HSSFFont font = wb.createFont();
            font.setFontName("黑体");
            // 设置字体大小
            font.setFontHeightInPoints((short) 11);
            // 设置合并单元格的样式
            HSSFCellStyle style0 = wb.createCellStyle();
            style0.setAlignment(CellStyle.ALIGN_CENTER);
            style0.setFont(font0);
            // 如果后续想用\r\n强制换行, 必须先设置为自动换行
            style0.setWrapText(true);
            //下边框
            style0.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            //左边框
            style0.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            //上边框
            style0.setBorderTop(HSSFCellStyle.BORDER_THIN);
            //右边框
            style0.setBorderRight(HSSFCellStyle.BORDER_THIN);
            // 设置列标题样式
            HSSFCellStyle style = wb.createCellStyle();
            //下边框
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            //左边框
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            //上边框
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            //右边框
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style0.setAlignment(CellStyle.ALIGN_CENTER);
            style.setFont(font);
            // 创建第一行
            HSSFRow row0 = sheet.createRow(0);
            // 设定行高
            row0.setHeight((short) 450);
            // 合并第一行单元格
            // 参数1：行号, 参数2：起始列号, 参数3：行号, 参数4：终止列号
            org.apache.poi.ss.util.CellRangeAddress cellRangeAddress = new org.apache.poi.ss.util.CellRangeAddress(0, 0,
                    0, excelHeader.length - 1);
            sheet.addMergedRegion(cellRangeAddress);
            // 给第一行赋值
            HSSFCell cell0 = row0.createCell(0);
            cell0.setCellStyle(style0);
            cell0.setCellValue(new HSSFRichTextString(cellTitle));
            // 创建第二行
            HSSFRow row = sheet.createRow(1);
            // 给第二行赋值(列标题)
            for (int i = 0; i < excelHeader.length; i++) {
                HSSFCell cell = row.createCell(i);
                String value = excelHeader[i];
                cell.setCellValue(value);
                cell.setCellStyle(style);
                sheet.autoSizeColumn((short) i);
            }
            // 创建第三行--第四行, 并赋值
            for (int i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 2);
                Map<String, Object> obj = list.get(i);
                for (int j = 0; j < column.length; j++) {
                    Object o = obj.get(column[j]); // 列名
                    int col = 8 * 500;
                    sheet.setColumnWidth(j, col);
                    HSSFCell cell = row.createCell(j);
                    cell.setCellStyle(style);
                    if (o instanceof BigDecimal || o instanceof BigInteger || o instanceof Integer || o instanceof Long
                            || o instanceof Double || o instanceof Float) {
                        cell.setCellValue(Double.parseDouble(o.toString()));
                    } else {
                        cell.setCellValue(o != null ? o.toString() : "");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wb;
    }

    /**
     * 测试导出基本样式数据表格
     * @param examId
     * @param response
     */
    public void exportGeneralExcel(String examId, HttpServletResponse response) {
        try {
            String worktableTitle = "总分成绩榜";
            List<String> columnNamesList = new ArrayList<>();
            List<String> columnList = new ArrayList<>();
            Object[] columnNames = columnNamesList.toArray();
            String str[] = Arrays.copyOf(columnNames, columnNames.length, String[].class);
            Object[] column = columnList.toArray();
            String str1[] = Arrays.copyOf(column, column.length, String[].class);
            //获取数据
            List<Map> dataMap = new ArrayList<>();
            HSSFWorkbook wb = exportGeneralExcel(worktableTitle, worktableTitle, str, str1, dataMap);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            String fileName = new String(worktableTitle.getBytes(), "iso8859-1") + dateFormat.format(System.currentTimeMillis()) + ".xls";
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            response.setContentType("application/x-download");
            response.flushBuffer();
            response.setCharacterEncoding("UTF-8");
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 导出合并单元格样式数据表格
     * @param worktableTitle
     * @param worktableTitle1
     * @param subjectColumn
     * @param list
     * @return
     */
    private HSSFWorkbook exportZongFenXiangXiChengJiBangExcelUtils(String worktableTitle, String worktableTitle1, List<String> subjectColumn, List<Map> list) {
        HSSFWorkbook wb = new HSSFWorkbook();
        // 建立工作薄
        HSSFSheet sheet = wb.createSheet(worktableTitle);
        try {
            sheet.createFreezePane(0, 2, 0, 2);// 冻结第二行
            HSSFFont font0 = wb.createFont();
            font0.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font0.setFontHeightInPoints((short) 15);
            // 设置列标题的单元格字体
            HSSFFont font = wb.createFont();
            font.setFontName("黑体");
            // 设置字体大小
            font.setFontHeightInPoints((short) 11);
            // 设置合并单元格的样式
            HSSFCellStyle style0 = wb.createCellStyle();
            style0.setAlignment(CellStyle.ALIGN_CENTER);
            style0.setFont(font0);
            // 如果后续想用\r\n强制换行, 必须先设置为自动换行
            style0.setWrapText(true);
            //下边框
            style0.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            //左边框
            style0.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            //上边框
            style0.setBorderTop(HSSFCellStyle.BORDER_THIN);
            //右边框
            style0.setBorderRight(HSSFCellStyle.BORDER_THIN);
            // 设置列标题样式
            HSSFCellStyle style1 = wb.createCellStyle();
            //下边框
            style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            //左边框
            style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            //上边框
            style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
            //右边框
            style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style0.setAlignment(CellStyle.ALIGN_CENTER);
            style1.setFont(font);
            // 创建第一行
            HSSFRow row0 = sheet.createRow(0);
            // 设定行高
            row0.setHeight((short) 450);
            // 合并第一行单元格
            // 参数1：行号, 参数2：起始列号, 参数3：行号, 参数4：终止列号
            org.apache.poi.ss.util.CellRangeAddress cellRangeAddress = new org.apache.poi.ss.util.CellRangeAddress(0, 0,
                    0, subjectColumn.size()*3+6);
            sheet.addMergedRegion(cellRangeAddress);
            // 给第一行赋值
            HSSFCell cell0 = row0.createCell(0);
            cell0.setCellStyle(style0);
            cell0.setCellValue(new HSSFRichTextString(worktableTitle));
            //-------------------------------------------
            //文件初始化
            sheet.setDefaultColumnWidth((short) 15);
            //HSSFCellStyle contextstyle = wb.createCellStyle();
            //contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));//保留两位小数点

            //在第一行第一个单元格
            HSSFRow row = sheet.createRow(1);
            // 设定行高
            row.setHeight((short) 450);
            row.setHeightInPoints(20);

            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
            style.setWrapText(true);// 指定单元格自动换行
            HSSFFont font1 = wb.createFont();
            font.setFontName("黑体");
            style.setFont(font1);

            HSSFCell cell = row.createCell(0);
            cell.setCellValue("整体排名");
            CellRangeAddress region = new CellRangeAddress(1, 2, 0, 0);
            sheet.addMergedRegion(region);

            cell = row.createCell(1);
            cell.setCellValue("校内排名");
            region = new CellRangeAddress(1, 2, 1, 1);
            sheet.addMergedRegion(region);

            cell = row.createCell(2);
            cell.setCellValue("学籍号");
            region = new CellRangeAddress(1, 2, 2, 2);
            sheet.addMergedRegion(region);

            cell = row.createCell(3);
            cell.setCellValue("班级");
            region = new CellRangeAddress(1, 2, 3, 3);
            sheet.addMergedRegion(region);

            cell = row.createCell(4);
            cell.setCellValue("姓名");
            region = new CellRangeAddress(1, 2, 4, 4);
            sheet.addMergedRegion(region);
            int k = 4;
            if (subjectColumn != null && subjectColumn.size() > 0) {
                for (int i = 0; i < subjectColumn.size(); i++) {
                    String subjectClumn = subjectColumn.get(i);
                    k = k + 1;
                    cell = row.createCell(k);
                    cell.setCellValue(subjectClumn);
                    region = new CellRangeAddress(1, 1, k, k + 2);
                    sheet.addMergedRegion(region);
                    k = k + 2;
                }
            }

            int endClumn = k + 1;
            cell = row.createCell(endClumn);
            cell.setCellValue("总分");
            region = new CellRangeAddress(1, 2, endClumn, endClumn);
            sheet.addMergedRegion(region);
            //组装第二行，添加合并列
            row = sheet.createRow(2);
            int mm = 4;
            for (int i = 0; i < subjectColumn.size(); i++) {
                mm = mm + 1;
                cell = row.createCell(mm);
                cell.setCellStyle(style1);
                cell.setCellValue("得分");
                mm = mm + 1;
                cell = row.createCell(mm);
                cell.setCellValue("校内排名");
                cell.setCellStyle(style1);
                mm = mm + 1;
                cell = row.createCell(mm);
                cell.setCellStyle(style1);
                cell.setCellValue("整体排名");
            }

            // 创建第三行------, 并赋值
            int totalElements = list.size();
            if (totalElements > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> ob = list.get(i);
                    //第三行开始写入数据
                    HSSFRow row1 = sheet.createRow(i + 3);
                    row1.setHeightInPoints(20);
                    //第一列开始
                    HSSFCell cell1 = row1.createCell(0);
                    cell1.setCellStyle(style1);
                    cell1.setCellValue(ob.get("allRank").toString());
                    cell1 = row1.createCell(1);
                    cell1.setCellStyle(style1);
                    cell1.setCellValue(ob.get("rank").toString());
                    cell1 = row1.createCell(2);
                    cell1.setCellStyle(style1);
                    cell1.setCellValue(ob.get("studentNum").toString());
                    cell1 = row1.createCell(3);
                    cell1.setCellStyle(style1);
                    cell1.setCellValue(ob.get("className").toString());
                    cell1 = row1.createCell(4);
                    cell1.setCellStyle(style1);
                    cell1.setCellValue(ob.get("studentName").toString());
                    int dd = 0;
                    for (int wenshan = 0; wenshan < subjectColumn.size(); wenshan++) {
                        if (wenshan == 0) {
                            dd = wenshan + 5;
                        } else {
                            dd = dd + 1;
                        }
                        String subject = subjectColumn.get(wenshan);
                        cell1 = row1.createCell(dd);
                        cell1.setCellStyle(style1);
                        dd = dd + 1;
                        cell1.setCellStyle(style1);
                        cell1.setCellValue(ob.get(subject).toString());
                        cell1 = row1.createCell(dd);
                        dd = dd + 1;
                        cell1.setCellStyle(style1);
                        cell1.setCellValue(ob.get("singleAllRank"+subject).toString());
                        cell1 = row1.createCell(dd);
                        cell1.setCellStyle(style1);
                        cell1.setCellValue(ob.get("singRank"+subject).toString());
                    }
                    int columnNum = subjectColumn.size() * 3 + 5;
                    cell1 = row1.createCell(columnNum);
                    cell1.setCellStyle(style1);
                    cell1.setCellValue(ob.get("sumScore").toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wb;
    }


    /**
     * 导出单科成绩榜数据zip
     *
     * @param examId
     * @param response
     */
    @RequestMapping(value = "exportDankeChengjiBangZip", method = RequestMethod.GET)
    @ResponseBody
    public void exportDankeChengjiBangZip(String examId, HttpServletResponse response, HttpServletRequest request) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        OutputStream os = null;
        try {
            //导出文件名称
            String worktableTitle = "各科成绩榜汇总表";
            //压缩文件名称
            String zipName = worktableTitle + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".zip";
            //压缩文件完整路径
            File zip = new File(request.getSession().getServletContext().getRealPath("/files") + "/"
                    + zipName);
            // 用于存放生成的文件完整路径
            List<String> fileNames = new ArrayList();
            //响应输出流，向浏览器传输文件
            os = response.getOutputStream();
            //根据考试IDcahxun学科列表
            //定义响应头格式
            response.setContentType("application/x-download");
            //定义响应内容描述
            response.setHeader("Content-disposition", "attachment;filename=\"" + StringEncodeUtils.gbkToIso88591(zipName) + "\"");
            List<Map<String, Object>> subjectIdListByExamIdList = new ArrayList<>();
            for (Map<String, Object> map : subjectIdListByExamIdList) {
                List<Map> dataMap = new ArrayList<>();
                Map<String, Object> subjectMap = new HashMap<>();
                dataMap.add(subjectMap);
                //创建表格
                String[] columnNames = {"整体排名", "校内排名", "学校", "学籍号", "班级", "姓名", "分数"};
                String[] column = {"allRank", "rank", "schoolName", "studentNum", "className", "studentName", "singleScore"};
                HSSFWorkbook wb = exportGeneralExcel(worktableTitle, worktableTitle, columnNames, column, dataMap);
                String file = request.getSession().getServletContext().getRealPath("/files") + "/"
                        + worktableTitle + "-" + map.get("subjectName") + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xls";
                fileNames.add(file);
                FileOutputStream o = new FileOutputStream(file);
                //将表格保存至项目完整路径文件中
                wb.write(o);
                o.close();
            }
            //根据文件完整路径生成文件对象
            File srcfile[] = new File[fileNames.size()];
            for (int i = 0; i < fileNames.size(); i++) {
                srcfile[i] = new File(fileNames.get(i));
            }
            //压缩文件
            ZipFiles(srcfile, zip);
            //读取zip文件内容到响应流
            FileInputStream inStream = new FileInputStream(zip);
            byte[] buf = new byte[4096];
            int readLength;
            while (((readLength = inStream.read(buf)) != -1)) {
                os.write(buf, 0, readLength);
            }
            inStream.close();
            //删除zip原文件
            zip.delete();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void ZipFiles(java.io.File[] srcfile, java.io.File zipfile) {
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                    zipfile));
            for (int i = 0; i < srcfile.length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
                //压缩玩删除原文件
                if (srcfile[i].exists() && srcfile[i].isFile()) {
                    srcfile[i].delete();
                }
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
