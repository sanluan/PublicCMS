package com.publiccms.common.view;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;

import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;

public class ExcelView extends AbstractXlsxStreamingView {
    private Consumer<Workbook> consumer;
    private String filename;
    public final static String SUFFIX = ".xlsx";

    /**
     * <pre>
     * &#64;RequestMapping("export")
     * public ExcelView export() {
     *     ExcelView view = new ExcelView(workbook -&gt; {
     *         Sheet sheet = workbook.createSheet("sheetname");
     *         int i = 0, j = 0;
     *         Row row = sheet.createRow(i++);
     *         row.createCell(j++).setCellValue("id");
     *         row.createCell(j++).setCellValue("title");
     *         row = sheet.createRow(i++);
     *         row.createCell(j++).setCellValue("id");
     *         row.createCell(j++).setCellValue("title");
     *     });
     *     view.setFilename("filename");
     *     return view;
     * }
     * </pre>
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
            if (-1 < filename.indexOf(Constants.DOT)) {
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(filename, StandardCharsets.UTF_8).build().toString());
            } else {
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(CommonUtils.joinString(filename, SUFFIX), StandardCharsets.UTF_8).build().toString());
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
