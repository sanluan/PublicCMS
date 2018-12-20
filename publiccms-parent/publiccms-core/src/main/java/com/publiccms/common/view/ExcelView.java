package com.publiccms.common.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.LanguagesUtils;

public class ExcelView extends AbstractXlsxStreamingView {
    private List<List<String>> dataList = new ArrayList<>();

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Sheet sheet = workbook
                .createSheet(LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(), "page.content"));
        if (CommonUtils.notEmpty(dataList)) {
            int i = 0, j;
            Row row;
            for (List<String> data : dataList) {
                row = sheet.createRow(i++);
                j = 0;
                for (String value : data) {
                    row.createCell(j++).setCellValue(value);
                }
            }
        }
    }

    /**
     * @return the dataList
     */
    public List<List<String>> getDataList() {
        return dataList;
    }

}
