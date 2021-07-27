package com.publiccms.common.tools;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

public class XSSFWorkbookUtils {
    public static String workbookToHtml(Workbook wb) {
        StringBuffer sb = new StringBuffer();
        int sheetCounts = wb.getNumberOfSheets();
        for (int i = 0; i < sheetCounts; i++) {
            Sheet sheet = wb.getSheetAt(i);
            int lastRowNum = sheet.getLastRowNum();
            Map<String, String> map0 = new HashMap<String, String>();
            Map<String, String> map1 = new HashMap<String, String>();
            int mergedNum = sheet.getNumMergedRegions();
            CellRangeAddress range = null;
            for (int j = 0; j < mergedNum; j++) {
                range = sheet.getMergedRegion(j);
                int topRow = range.getFirstRow();
                int topCol = range.getFirstColumn();
                int bottomRow = range.getLastRow();
                int bottomCol = range.getLastColumn();
                map0.put(topRow + "," + topCol, bottomRow + "," + bottomCol);
                int tempRow = topRow;
                while (tempRow <= bottomRow) {
                    int tempCol = topCol;
                    while (tempCol <= bottomCol) {
                        map1.put(tempRow + "," + tempCol, "");
                        tempCol++;
                    }
                    tempRow++;
                }
                map1.remove(topRow + "," + topCol);
            }
            sb.append("<h3>").append(sheet.getSheetName()).append("</h3>");
            sb.append("<table style='border-collapse:collapse;' width='100%'>");
            Row row = null;
            Cell cell = null;
            for (int rowNum = sheet.getFirstRowNum(); rowNum <= lastRowNum; rowNum++) {
                row = sheet.getRow(rowNum);
                if (row == null) {
                    sb.append("<tr><td>&nbsp;</td></tr>");
                    continue;
                }
                sb.append("<tr>");
                int lastColNum = row.getLastCellNum();
                for (int colNum = 0; colNum < lastColNum; colNum++) {
                    cell = row.getCell(colNum);
                    if (cell == null) {
                        sb.append("<td>&nbsp;</td>");
                        continue;
                    }
                    String stringValue = getCellValue(cell);
                    if (map0.containsKey(rowNum + "," + colNum)) {
                        String pointString = map0.get(rowNum + "," + colNum);
                        map0.remove(rowNum + "," + colNum);
                        int bottomeRow = Integer.valueOf(pointString.split(",")[0]);
                        int bottomeCol = Integer.valueOf(pointString.split(",")[1]);
                        int rowSpan = bottomeRow - rowNum + 1;
                        int colSpan = bottomeCol - colNum + 1;
                        sb.append("<td rowspan= '").append(rowSpan).append("' colspan= '").append(colSpan).append("' ");
                    } else if (map1.containsKey(rowNum + "," + colNum)) {
                        map1.remove(rowNum + "," + colNum);
                        continue;
                    } else {
                        sb.append("<td ");
                    }
                    dealExcelStyle(sheet, cell, sb);
                    sb.append(">");
                    if (stringValue == null || "".equals(stringValue.trim())) {
                        sb.append("&nbsp;");
                    } else {
                        sb.append(stringValue.replace(String.valueOf((char) 160), "&nbsp;"));
                    }
                    sb.append("</td>");
                }
                sb.append("</tr>");
            }
            sb.append("</table>");
        }
        return sb.toString();
    }

    /**
     * 获取表格单元格Cell内容
     */
    private static String getCellValue(Cell cell) {
        String result = new String();
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
        return result;
    }

    /**
     * 处理表格样式
     */
    private static void dealExcelStyle(Sheet sheet, Cell cell, StringBuffer sb) {
        XSSFCellStyle cellStyle = (XSSFCellStyle) cell.getCellStyle();
        if (cellStyle != null) {
            HorizontalAlignment alignment = cellStyle.getAlignment();
            sb.append("align='" + convertAlignToHtml(alignment) + "' ");// 单元格内容的水平对齐方式
            VerticalAlignment verticalAlignment = cellStyle.getVerticalAlignment();
            sb.append("valign='" + convertVerticalAlignToHtml(verticalAlignment) + "' ");// 单元格中内容的垂直排列方式
            XSSFFont font = cellStyle.getFont();
            sb.append("style='");
            if (font.getBold()) {
                sb.append("font-weight:bold;");
            }
            if (font.getItalic()) {
                sb.append("font-style: italic;");
            }
            sb.append("font-size: ").append(font.getFontHeightInPoints()).append("pt;");
            int columnWidth = sheet.getColumnWidth(cell.getColumnIndex());
            sb.append("width:").append(columnWidth).append("px;");
            XSSFColor xc = font.getXSSFColor();
            if (xc != null && xc.isRGB()) {
                sb.append("color:#").append(xc.getARGBHex().substring(2)).append(";");
            }
            XSSFColor bgColor = (XSSFColor) cellStyle.getFillForegroundColorColor();
            if (bgColor != null && bgColor.isAuto()) {
                sb.append("background-color:#").append(bgColor.getARGBHex().substring(2)).append(";");
            }
            sb.append(getBorderStyle(0, cellStyle.getBorderTop().getCode(), cellStyle.getTopBorderXSSFColor()));
            sb.append(getBorderStyle(1, cellStyle.getBorderRight().getCode(), cellStyle.getRightBorderXSSFColor()));
            sb.append(getBorderStyle(2, cellStyle.getBorderBottom().getCode(), cellStyle.getBottomBorderXSSFColor()));
            sb.append(getBorderStyle(3, cellStyle.getBorderLeft().getCode(), cellStyle.getLeftBorderXSSFColor()));
            sb.append("' ");
        }
    }

    /**
     * 单元格内容的水平对齐方式 331
     */
    private static String convertAlignToHtml(HorizontalAlignment alignment) {
        String align = "left";
        switch (alignment) {
        case LEFT:
            align = "left";
            break;
        case CENTER:
            align = "center";
            break;
        case RIGHT:
            align = "right";
            break;
        default:
            break;
        }
        return align;
    }

    private static String convertVerticalAlignToHtml(VerticalAlignment verticalAlignment) {
        String valign = "middle";
        switch (verticalAlignment) {
        case BOTTOM:
            valign = "bottom";
            break;
        case CENTER:
            valign = "center";
            break;
        case TOP:
            valign = "top";
            break;
        default:
            break;
        }
        return valign;
    }

    static String[] bordesr = { "border-top:", "border-right:", "border-bottom:", "border-left:" };
    static String[] borderStyles = { "none ", "solid ", "solid ", "dashed ", "dotted ", "solid ", "double ", "solid ", "dashed ",
            "dashed", "dashed", "dashed", "dashed", "dashed" };

    private static String getBorderStyle(int b, short s, XSSFColor xc) {
        if (s == 0) {
            return bordesr[b] + borderStyles[s] + "#d0d7e5 1px;";
        }
        if (xc != null && xc.isRGB()) {
            String borderColorStr = xc.getARGBHex();
            borderColorStr = borderColorStr == null || borderColorStr.length() < 1 ? "#000000" : borderColorStr.substring(2);
            return bordesr[b] + borderStyles[s] + borderColorStr + " 1px;";
        }
        return "";
    }
}
