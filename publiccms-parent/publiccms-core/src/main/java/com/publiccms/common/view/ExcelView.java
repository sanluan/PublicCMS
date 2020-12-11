package com.publiccms.common.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;

import com.publiccms.common.constants.CommonConstants;

public class ExcelView extends AbstractXlsxStreamingView {
    private Consumer<Workbook> consumer;
    private String filename;

    /**
     * <code>
       &#64;RequestMapping("export")
       public ExcelView export() {
           ExcelView view = new ExcelView(workbook -> {
               Sheet sheet = workbook.createSheet("sheetname");
               int i = 0, j = 0;
               Row row = sheet.createRow(i++);
               row.createCell(j++).setCellValue("id");
               row.createCell(j++).setCellValue("title");
               row = sheet.createRow(i++);
               row.createCell(j++).setCellValue("id");
               row.createCell(j++).setCellValue("title");
           });
           view.setFilename("filename");
           return view;
       }
     * </code>
     * 
     * @param filename
     * @param consumer
     *            the consumer to set
     */
    public ExcelView(String filename, Consumer<Workbook> consumer) {
        this.filename = filename;
        this.consumer = consumer;
    }

    public ExcelView(Consumer<Workbook> consumer) {
        this.consumer = consumer;
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if (null != consumer) {
            consumer.accept(workbook);
        }
        if (null != filename) {
            try {
                if (-1 < filename.indexOf(CommonConstants.DOT)) {
                    response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(filename, "utf-8"));
                } else {
                    response.setHeader("content-disposition",
                            "attachment;fileName=" + URLEncoder.encode(filename, "utf-8") + ".xlsx");
                }
            } catch (UnsupportedEncodingException e) {
            }
        }
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename
     *            the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

}
