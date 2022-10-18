package com.publiccms.logic.component.site;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ZipUtils;
import com.publiccms.entities.cms.CmsDictionary;
import com.publiccms.entities.cms.CmsDictionaryData;
import com.publiccms.entities.cms.CmsDictionaryExclude;
import com.publiccms.entities.cms.CmsDictionaryExcludeValue;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsDictionaryDataService;
import com.publiccms.logic.service.cms.CmsDictionaryExcludeService;
import com.publiccms.logic.service.cms.CmsDictionaryExcludeValueService;
import com.publiccms.logic.service.cms.CmsDictionaryService;

/**
 * ExportComponent 数据导出组件
 * 
 */
@Component
public class ExportComponent {
    @Autowired
    private CmsCategoryService categoryService;
    @Autowired
    private CmsContentService contentService;
    @Autowired
    private CmsDictionaryService dictionaryService;
    @Autowired
    private CmsDictionaryDataService dictionaryDataService;
    @Autowired
    private CmsDictionaryExcludeService dictionaryExcludeService;
    @Autowired
    private CmsDictionaryExcludeValueService dictionaryExcludeValueService;

    public void exportCategory(short siteId, ZipOutputStream zipOutputStream) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        categoryService.batchWork(siteId, (list, i) -> {
            try {
                out.reset();
                Constants.objectMapper.writeValue(out, list);
                ZipUtils.compressFile(new ByteArrayInputStream(out.toByteArray()), zipOutputStream, "category_" + i + ".json");
            } catch (IOException e) {
            }
        }, PageHandler.MAX_PAGE_SIZE);
    }

    public void exportDictionary(short siteId, ZipOutputStream zipOutputStream) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        dictionaryService.batchWork(siteId, (list, i) -> {
            try {
                out.reset();
                for (CmsDictionary dictionary : list) {
                    List<CmsDictionaryData> dataList = dictionaryDataService.getList(siteId, dictionary.getId().getId(), null);
                    if (CommonUtils.notEmpty(dataList)) {
                        Constants.objectMapper.writeValue(out, dataList);
                        ZipUtils.compressFile(new ByteArrayInputStream(out.toByteArray()), zipOutputStream,
                                "dictionary_data_" + dictionary.getId().getId() + ".json");
                    }
                    List<CmsDictionaryExclude> excludeList = dictionaryExcludeService.getList(siteId, dictionary.getId().getId(),
                            null);
                    if (CommonUtils.notEmpty(excludeList)) {
                        Constants.objectMapper.writeValue(out, excludeList);
                        ZipUtils.compressFile(new ByteArrayInputStream(out.toByteArray()), zipOutputStream,
                                "dictionary_exclude_" + dictionary.getId().getId() + ".json");
                    }
                    List<CmsDictionaryExcludeValue> excludeValueList = dictionaryExcludeValueService.getList(siteId,
                            dictionary.getId().getId(), null);
                    if (CommonUtils.notEmpty(excludeValueList)) {
                        Constants.objectMapper.writeValue(out, dataList);
                        ZipUtils.compressFile(new ByteArrayInputStream(out.toByteArray()), zipOutputStream,
                                "dictionary_exclude_value_" + dictionary.getId().getId() + ".json");
                    }
                }
                Constants.objectMapper.writeValue(out, list);
                ZipUtils.compressFile(new ByteArrayInputStream(out.toByteArray()), zipOutputStream, "dictionary_" + i + ".json");
            } catch (IOException e) {
            }
        }, PageHandler.MAX_PAGE_SIZE);
    }

    public void importCategory(short siteId) {

    }

    public void importContent(short siteId) {
        contentService.batchWork(siteId, null, null, (list, i) -> {

        }, PageHandler.MAX_PAGE_SIZE);
    }

    public void exportContent(short siteId) {

    }
}
