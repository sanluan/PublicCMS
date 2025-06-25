package com.publiccms.logic.component.exchange;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.DateFormatUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.XSSFWorkbookUtils;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.cms.CmsPlaceAttribute;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsPlaceAttributeService;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;

import jakarta.annotation.Resource;

/**
 * PlaceExportComponent 页面片段数据导出组件
 * 
 */
@Component
public class PlaceImportComponent {
    @Resource
    private CmsPlaceService service;
    @Resource
    private CmsPlaceAttributeService attributeService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private MetadataComponent metadataComponent;
    @Resource
    private SiteComponent siteComponent;

    /**
     * @param site
     * @param path
     * @param file
     * @param userId
     * @param model
     * @return
     */
    public String importExcel(SysSite site, String path, MultipartFile file, Long userId, ModelMap model) {
        String filepath = siteComponent.getTemplateFilePath(site.getId(),
                CommonUtils.joinString(TemplateComponent.INCLUDE_DIRECTORY, path));
        CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath);
        List<String> fieldList = metadata.getFieldList();
        try (Workbook wb = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = wb.getSheetAt(0);
            Row row = null;
            int lastRowNum = sheet.getLastRowNum();
            int j;
            DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.FULL_DATE_FORMAT_STRING);
            for (int rowNum = sheet.getFirstRowNum() + 1; rowNum <= lastRowNum; rowNum++) {
                j = 1;
                CmsPlace entity = new CmsPlace();
                entity.setPath(path);
                entity.setSiteId(site.getId());
                row = sheet.getRow(rowNum);
                entity.setTitle(XSSFWorkbookUtils.getCellValue(row.getCell(j++)));
                if (null != fieldList && fieldList.contains("url")) {
                    entity.setUrl(XSSFWorkbookUtils.getCellValue(row.getCell(j++)));
                }
                if (null != fieldList && fieldList.contains("description")) {
                    entity.setDescription(XSSFWorkbookUtils.getCellValue(row.getCell(j++)));
                }
                j++;
                entity.setUserId(userId);
                j++;
                entity.setClicks(0);
                Date now = CommonUtils.getDate();
                try {
                    entity.setPublishDate(dateFormat.parse(XSSFWorkbookUtils.getCellValue(row.getCell(j++))));
                } catch (ParseException e) {
                    entity.setPublishDate(now);
                }
                try {
                    entity.setCreateDate(dateFormat.parse(XSSFWorkbookUtils.getCellValue(row.getCell(j++))));
                } catch (ParseException e) {
                    entity.setCreateDate(now);
                }
                j++;
                entity.setStatus(CmsPlaceService.STATUS_PEND);
                j++;
                CmsPlaceAttribute attribute = null;
                if (CommonUtils.notEmpty(metadata.getExtendList())) {
                    attribute = new CmsPlaceAttribute();
                    Map<String, String> map = new HashMap<>();
                    for (SysExtendField extend : metadata.getExtendList()) {
                        map.put(extend.getId().getCode(), XSSFWorkbookUtils.getCellValue(row.getCell(j++)));
                    }
                    attribute.setData(ExtendUtils.getExtendString(map));
                }
                if (row.getLastCellNum() > j) {
                    entity.setItemType(XSSFWorkbookUtils.getCellValue(row.getCell(j++)));
                    if (!CmsPlaceService.ITEM_TYPE_CUSTOM.equalsIgnoreCase(entity.getItemType())) {
                        entity.setItemId(Long.valueOf(XSSFWorkbookUtils.getCellValue(row.getCell(j++))));
                    }
                }
                service.save(entity);
                if (null != attribute) {
                    attribute.setPlaceId(entity.getId());
                    attributeService.save(attribute);
                }
            }
            return CommonConstants.TEMPLATE_DONE;
        } catch (EncryptedDocumentException | IOException e) {
            model.addAttribute(CommonConstants.ERROR, e.getMessage());
            return CommonConstants.TEMPLATE_ERROR;
        }
    }
}
