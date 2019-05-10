package com.publiccms.common.view;

import java.util.Map;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;

public class ExcelView extends AbstractXlsxStreamingView {
    private Consumer<Workbook> consumer;

    /**
     * @param consumer
     *            the consumer to set
     */
    public ExcelView(Consumer<Workbook> consumer) {
        this.consumer = consumer;
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if (null != consumer) {
            consumer.accept(workbook);
        }
    }

}
