package com.publiccms.logic.component.interaction;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsPlaceAttributeService;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.views.pojo.interaction.Place;
import com.publiccms.views.pojo.interaction.PlaceData;

/**
 * PlaceInteractionComponent 分类数据导出组件
 * 
 */
@Component
public class PlaceInteractionComponent extends InteractionComponent<String, Place> {
    @Autowired
    private CmsPlaceService service;
    @Autowired
    private CmsPlaceAttributeService attributeService;
    @Autowired
    private CmsCategoryService categoryService;

    @Override
    public void exportAll(short siteId, String directory, ZipOutputStream zipOutputStream) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        exportEntity(siteId, directory, "", out, zipOutputStream);
    }

    @Override
    public void exportEntity(short siteId, String directory, String path, ByteArrayOutputStream out,
            ZipOutputStream zipOutputStream) {
        PageHandler page = service.getPage(siteId, null, path, null, null, null, null, null, null, false, null, null, null, 0);
        @SuppressWarnings("unchecked")
        List<CmsPlace> list = (List<CmsPlace>) page.getList();
        if (0 < page.getTotalCount()) {
            Place data = new Place();
            data.setPath(path);
            List<PlaceData> datalist = new ArrayList<>();
            for (CmsPlace entity : list) {
                PlaceData placeData = new PlaceData();
                entity.setId(null);
                if (CmsPlaceService.ITEM_TYPE_CATEGORY.equals(entity.getItemType()) && null != entity.getItemId()) {
                    CmsCategory category = categoryService.getEntity(entity.getItemId().intValue());
                    if (null != category) {
                        placeData.setCategoryCode(category.getCode());
                    }
                }
                placeData.setEntity(entity);
                placeData.setAttribute(attributeService.getEntity(entity.getId()));
                datalist.add(placeData);
            }
            data.setDatalist(datalist);
        }
    }

    public void save(short siteId, long userId, boolean overwrite, Place data) {
        if (null != data.getDatalist()) {
            PageHandler page = service.getPage(siteId, null, data.getPath(), null, null, null, null, null, null, false, null,
                    null, null, 0);
            if (0 == page.getTotalCount() || overwrite) {
                for (PlaceData placeData : data.getDatalist()) {
                    CmsPlace entity = placeData.getEntity();
                    if (CmsPlaceService.ITEM_TYPE_CATEGORY.equals(entity.getItemType())) {
                        if (CommonUtils.notEmpty(placeData.getCategoryCode())) {
                            CmsCategory category = categoryService.getEntityByCode(siteId, placeData.getCategoryCode());
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
                        attributeService.save(placeData.getAttribute());
                    }
                }
            }
        }
    }
}
