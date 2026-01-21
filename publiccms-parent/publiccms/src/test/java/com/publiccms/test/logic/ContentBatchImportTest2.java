package com.publiccms.test.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.views.pojo.exchange.Content;

class ContentBatchImportTest2 {

    public static void main(String[] args) {
        int i = 1500;
        int f = 5;
        Map<String, List<Content>> titleMap = new HashMap<>();
        try (Workbook wb = WorkbookFactory.create(new FileInputStream(new File("D:/1.xlsx")))) {
            Sheet sheet = wb.getSheetAt(0);
            Row row = null;
            int lastRowNum = sheet.getLastRowNum();
            for (int rowNum = sheet.getFirstRowNum() + 1; rowNum <= lastRowNum; rowNum++) {
                row = sheet.getRow(rowNum);
                if (row == null || CommonUtils.empty(getCellValue(row.getCell(1)))) {
                    continue;
                }
                String[] codes = { "chiefsupervisor", "chiefplanner", "chiefeditor", "chiefeditor", "deputyeditor",
                        "deputyeditor", "author", "author", "author", "author", "author", "consultant", "supervision",
                        "supervision", "supervision", "supervisioninterpreter", "supervisioninterpreter", "translator",
                        "translator", "translator", "translator" };
                int start = 3;
                int n = 0;
                long contentId = Long.valueOf(getCellValue(row.getCell(1)));
                for (String code : codes) {
                    Content content = dealContent(row, start, code);
                    if (null != content) {
                        List<Content> contentList = titleMap.computeIfAbsent(content.getEntity().getTitle(),
                                k -> new ArrayList<>());
                        boolean flag = true;
                        for (Content old : contentList) {
                            if (null == content.getAttribute().getText() && null == old.getAttribute().getText()
                                            || content.getAttribute().getText().equalsIgnoreCase(old.getAttribute().getText())) {
                                flag = false;
                            }
                        }
                        File file = new File("D://book/" + content.getEntity().getTitle() + ".json");
                        if (flag) {
                            if (contentList.isEmpty()) {
                                contentList.add(content);
                            }
                            String updateSQL = "";
                            if (file.exists()) {
                                Content old = Constants.objectMapper.readValue(file, Content.class);
                                if (null != old) {
                                    content.getAttribute()
                                            .setText(old.getAttribute().getText() + content.getAttribute().getText());
                                    content.getEntity().setTitle(content.getEntity().getTitle() + "-重复人物");
                                    updateSQL = "update `cms_content_related` set related_content_id = " + i
                                            + " where related_content_id = " + old.getEntity().getId() + ";\n";
                                }
                            }
                            content.getEntity().setId((long) i);
                            content.getAttribute().setContentId((long) i);
                            Constants.objectMapper.writeValue(file, content);
                            FileUtils.write(new File("D://book/sql.text"), "INSERT INTO `cms_content_related` VALUES (" + f + ", "
                                    + contentId + ", 'relation', '" + code + "', " + i + ", 1, '', '"
                                    + content.getEntity().getTitle() + "', '"
                                    + (null != content.getEntity().getDescription() ? content.getEntity().getDescription() : "")
                                    + "', " + n + ");\n" + updateSQL, Charset.defaultCharset(), true);

                            i++;
                        } else {
                            contentList.add(content);
                            if (file.exists()) {
                                Content old = Constants.objectMapper.readValue(file, Content.class);
                                if (null != old) {
                                    FileUtils.write(new File("D://book/sql.text"), "INSERT INTO `cms_content_related` VALUES ("
                                            + f + ", " + contentId + ", 'relation', '" + code + "', " + old.getEntity().getId()
                                            + ", 1, '', '" + old.getEntity().getTitle() + "', '"
                                            + (null != old.getEntity().getDescription() ? old.getEntity().getDescription() : "")
                                            + "', " + n + ");\n", Charset.defaultCharset(), true);
                                } else {
                                    System.out.println(file.getAbsolutePath());
                                }
                            } else {
                                System.out.println(file.getAbsolutePath());
                            }
                        }
                        f++;
                        n++;
                    }
                    start += 3;
                }
                n = 0;
            }
        } catch (EncryptedDocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取表格单元格Cell内容
     */
    private static Content dealContent(Row row, int indexStart, String categoryCode) {
        String name = getCellValue(row.getCell(indexStart));
        if (CommonUtils.notEmpty(name)) {
            Content content = new Content();
            content.setCategoryCode(categoryCode);
            CmsContent entity = new CmsContent();
            entity.setTitle(name);
            entity.setEditor(getCellValue(row.getCell(indexStart + 1)));
            entity.setModelId("author");
            entity.setStatus(1);
            CmsContentAttribute attribute = new CmsContentAttribute();
            attribute.setText(getCellValue(row.getCell(indexStart + 2)));
            if (null != attribute.getText()) {
                int length = attribute.getText().length();
                entity.setDescription(CommonUtils.keep(attribute.getText(), 100));
                attribute.setText("<p>" + attribute.getText().replace("\r\n", "<br>") + "</p>");
                attribute.setWordCount(length);
            }
            content.setEntity(entity);
            content.setAttribute(attribute);
            return content;
        } else {
            return null;
        }
    }

    /**
     * 获取表格单元格Cell内容
     */
    private static String getCellValue(Cell cell) {
        String result = new String();
        if (null != cell) {
            switch (cell.getCellType()) {
            case NUMERIC:// 数字类型
                if (DateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                    SimpleDateFormat sdf = null;
                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else {// 日期
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    }
                    Date date = cell.getDateCellValue();
                    result = sdf.format(date);
                } else if (cell.getCellStyle().getDataFormat() == 58) {
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    double value = cell.getNumericCellValue();
                    Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                    result = sdf.format(date);
                } else {
                    double value = cell.getNumericCellValue();
                    CellStyle style = cell.getCellStyle();
                    DecimalFormat format = new DecimalFormat();
                    String temp = style.getDataFormatString();
                    // 单元格设置成常规
                    if (temp.equals("General")) {
                        format.applyPattern("#");
                    }
                    result = format.format(value);
                }
                break;
            case STRING:// String类型
                result = cell.getRichStringCellValue().toString();
                break;
            case BLANK:
                result = "";
                break;
            default:
                result = "";
                break;
            }
        }
        return result;
    }

}