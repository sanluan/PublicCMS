package com.publiccms.common.view;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.view.AbstractView;

import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExcelView extends AbstractView {
    private Consumer<Workbook> consumer;
    private String filename;
    public static final String SUFFIX = ".xlsx";

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
        setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public ExcelView(Consumer<Workbook> consumer) {
        this.consumer = consumer;
        setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    /**
     * Renders the Excel view, given the specified model.
     */
    @Override
    protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        // Create a fresh workbook instance for this render step.
        Workbook workbook = createWorkbook(model, request);

        // Delegate to application-provided document code.
        buildExcelDocument(model, workbook, request, response);

        // Set the content type.
        response.setContentType(getContentType());

        // Flush byte array to servlet output stream.
        renderWorkbook(workbook, response);
    }

    /**
     * The actual render step: taking the POI {@link Workbook} and rendering it
     * to the given response.
     * 
     * @param workbook
     *            the POI Workbook to render
     * @param response
     *            current HTTP response
     * @throws IOException
     *             when thrown by I/O methods that we're delegating to
     */
    protected void renderWorkbook(Workbook workbook, HttpServletResponse response) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
    }

    /**
     * This implementation creates a {@link SXSSFWorkbook} for streaming the
     * XLSX format.
     */
    protected SXSSFWorkbook createWorkbook(Map<String, Object> model, HttpServletRequest request) {
        return new SXSSFWorkbook();
    }

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
