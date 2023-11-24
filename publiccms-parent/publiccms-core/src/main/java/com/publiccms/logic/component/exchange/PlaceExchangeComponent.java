package com.publiccms.logic.component.exchange;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractDataExchange;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CmsFileUtils.FileInfo;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsPlaceAttributeService;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.views.pojo.exchange.Place;
import com.publiccms.views.pojo.exchange.PlaceData;

/**
 * PlaceExchangeComponent 页面片段数据导入导出组件
 * 
 */
@Component
public class PlaceExchangeComponent extends AbstractDataExchange<String, Place> {
    @Resource
    private CmsPlaceService service;
    @Resource
    private CmsPlaceAttributeService attributeService;
    @Resource
    private CmsCategoryService categoryService;
    @Resource
    private SiteComponent siteComponent;

    @Override
    public void exportAll(SysSite site, String directory, ByteArrayOutputStream outputStream, ArchiveOutputStream<ZipArchiveEntry> archiveOutputStream) {
        dealDir(site, directory, "", outputStream, archiveOutputStream);
    }

    private void dealDir(SysSite site, String directory, String path, ByteArrayOutputStream outputStream,
            ArchiveOutputStream<ZipArchiveEntry> archiveOutputStream) {
        String realPath = siteComponent.getTemplateFilePath(site.getId(),
                CommonUtils.joinString(TemplateComponent.INCLUDE_DIRECTORY, Constants.SEPARATOR, path));
        List<FileInfo> list = CmsFileUtils.getFileList(realPath, null);
        for (FileInfo fileInfo : list) {
            String filepath = CommonUtils.joinString(path, fileInfo.getFileName());
            if (fileInfo.isDirectory()) {
                dealDir(site, directory, CommonUtils.joinString(filepath, Constants.SEPARATOR), outputStream, archiveOutputStream);
            } else {
                exportEntity(site, directory, filepath, outputStream, archiveOutputStream);
            }
        }
    }

    @Override
    public void exportEntity(SysSite site, String directory, String path, ByteArrayOutputStream outputStream,
            ArchiveOutputStream<ZipArchiveEntry> archiveOutputStream) {
        PageHandler page = service.getPage(site.getId(), null, CommonUtils.joinString(Constants.SEPARATOR, path), null, null,
                null, null, null, null, false, null, null, null, PageHandler.MAX_PAGE_SIZE);
        @SuppressWarnings("unchecked")
        List<CmsPlace> list = (List<CmsPlace>) page.getList();
        if (0 < page.getTotalCount()) {
            Place data = new Place();
            data.setPath(path);
            List<PlaceData> datalist = new ArrayList<>();
            for (CmsPlace entity : list) {
                long placeId = entity.getId();
                PlaceData placeData = new PlaceData();
                entity.setId(null);
                if (CmsPlaceService.ITEM_TYPE_CATEGORY.equals(entity.getItemType()) && null != entity.getItemId()) {
                    CmsCategory category = categoryService.getEntity(entity.getItemId().intValue());
                    if (null != category) {
                        placeData.setCategoryCode(category.getCode());
                    }
                }
                placeData.setEntity(entity);
                placeData.setAttribute(attributeService.getEntity(placeId));
                if (null != placeData.getAttribute()) {
                    if (needReplace(placeData.getAttribute().getData(), site.getDynamicPath())) {
                        placeData.getAttribute().setData(
                                StringUtils.replace(placeData.getAttribute().getData(), site.getDynamicPath(), "#DYNAMICPATH#"));
                    }
                    if (needReplace(placeData.getAttribute().getData(), site.getSitePath())) {
                        placeData.getAttribute().setData(
                                StringUtils.replace(placeData.getAttribute().getData(), site.getSitePath(), "#SITEPATH#"));
                    }
                }
                datalist.add(placeData);
            }
            data.setDatalist(datalist);
            export(directory, outputStream, archiveOutputStream, data, CommonUtils.joinString(data.getPath(), ".json"));
        }
    }

    @Override
    public void save(SysSite site, long userId, boolean overwrite, Place data) {
        if (null != data.getDatalist()) {
            PageHandler page = service.getPage(site.getId(), null, data.getPath(), null, null, null, null, null, null, false,
                    null, null, null, 0);
            if (0 == page.getTotalCount() || overwrite) {
                for (PlaceData placeData : data.getDatalist()) {
                    CmsPlace entity = placeData.getEntity();
                    entity.setSiteId(site.getId());
                    if (CmsPlaceService.ITEM_TYPE_CATEGORY.equals(entity.getItemType())) {
                        if (CommonUtils.notEmpty(placeData.getCategoryCode())) {
                            CmsCategory category = categoryService.getEntityByCode(site.getId(), placeData.getCategoryCode());
                            if (null != category) {
                                entity.setItemId(entity.getId());
                                service.save(entity);
                            }
                        }
                    } else {
                        service.save(entity);
                    }
                    if (null != entity.getId() && null != placeData.getAttribute()) {
                        placeData.getAttribute().setPlaceId(entity.getId());
                        if (CommonUtils.notEmpty(placeData.getAttribute().getData())) {
                            placeData.getAttribute().setData(StringUtils.replace(placeData.getAttribute().getData(),
                                    "#DYNAMICPATH#", site.getDynamicPath()));
                        }
                        if (CommonUtils.notEmpty(placeData.getAttribute().getData())) {
                            placeData.getAttribute().setData(
                                    StringUtils.replace(placeData.getAttribute().getData(), "#SITEPATH#", site.getSitePath()));
                        }
                        attributeService.save(placeData.getAttribute());
                    }
                }
            }
        }
    }

    @Override
    public String getDirectory() {
        return "place";
    }
}
