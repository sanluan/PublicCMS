package com.publiccms.logic.component.interaction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentFile;
import com.publiccms.entities.cms.CmsContentProduct;
import com.publiccms.entities.cms.CmsContentRelated;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentFileService;
import com.publiccms.logic.service.cms.CmsContentProductService;
import com.publiccms.logic.service.cms.CmsContentRelatedService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.views.pojo.entities.CmsModel;
import com.publiccms.views.pojo.interaction.Content;
import com.publiccms.views.pojo.query.CmsContentQuery;

import freemarker.template.TemplateException;

/**
 * CategoryInteractionComponent 分类数据导出组件
 * 
 */
@Component
public class ContentInteractionComponent extends InteractionComponent<CmsContent, Content> {
    @Autowired
    private CmsContentService service;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private SiteComponent siteComponent;
    @Autowired
    private ModelComponent modelComponent;
    @Autowired
    private CmsCategoryService categoryService;
    @Autowired
    private CmsContentAttributeService attributeService;
    @Autowired
    private CmsContentFileService fileService;
    @Autowired
    private CmsContentProductService productService;
    @Autowired
    private CmsContentRelatedService relatedService;

    @Override
    public void exportAll(short siteId, String directory, ZipOutputStream zipOutputStream) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CmsContentQuery queryEntity = new CmsContentQuery();
        queryEntity.setSiteId(siteId);
        queryEntity.setDisabled(false);
        queryEntity.setEmptyParent(true);
        PageHandler page = service.getPage(queryEntity, null, null, null, null, null, PageHandler.MAX_PAGE_SIZE, null);
        int i = 1;
        while (page.isLastPage()) {
            @SuppressWarnings("unchecked")
            List<CmsContent> list = (List<CmsContent>) page.getList();
            for (CmsContent entity : list) {
                exportEntity(siteId, directory, entity, out, zipOutputStream);
            }
            page = service.getPage(queryEntity, null, null, null, null, i++, PageHandler.MAX_PAGE_SIZE, null);
        }

    }

    public Content exportEntity(short siteId, CmsContent entity) {
        Content data = new Content();
        data.setEntity(entity);
        data.setAttribute(attributeService.getEntity(entity.getId()));
        if (entity.isHasFiles() || entity.isHasImages()) {
            @SuppressWarnings("unchecked")
            List<CmsContentFile> fileList = (List<CmsContentFile>) fileService
                    .getPage(entity.getId(), null, null, null, null, null, null).getList();
            data.setFileList(fileList);
        }
        if (entity.isHasProducts()) {
            List<CmsContentProduct> productList = productService.getList(siteId, entity.getId());
            data.setProductList(productList);
        }
        @SuppressWarnings("unchecked")
        List<CmsContentRelated> relatedList = (List<CmsContentRelated>) relatedService
                .getPage(entity.getId(), null, null, null, null, null, null, null).getList();
        data.setRelatedList(relatedList);
        return data;
    }

    @Override
    public void exportEntity(short siteId, String directory, CmsContent entity, ByteArrayOutputStream out,
            ZipOutputStream zipOutputStream) {
        CmsCategory category = categoryService.getEntity(entity.getCategoryId());
        if (null != category) {
            Content data = exportEntity(siteId, entity);
            data.setCategoryCode(category.getCode());
            CmsModel model = modelComponent.getModelMap(siteId).get(entity.getModelId());
            if (null != model && model.isHasChild()) {
                List<CmsContent> list = service.getListByTopId(siteId, entity.getId());
                if (null != list) {
                    List<Content> childList = new ArrayList<>();
                    for (CmsContent content : list) {
                        childList.add(exportEntity(siteId, content));
                    }
                    data.setChildList(childList);
                }
            }
            export(directory, out, zipOutputStream, data, entity.getId() + ".json");
        }
    }

    public void save(short siteId, long userId, boolean overwrite, Content data) {
        CmsContent entity = data.getEntity();
        CmsContent oldentity = service.getEntity(entity.getId());
        CmsCategory category = categoryService.getEntityByCode(siteId, data.getCategoryCode());
        if (null != category && (null == oldentity || overwrite)) {
            entity.setSiteId(siteId);
            service.save(entity);
            if (null != data.getAttribute()) {
                attributeService.save(data.getAttribute());
            }
            for (Content child : data.getChildList()) {
                save(siteId, userId, overwrite, child);
            }
            try {
                templateComponent.createContentFile(siteComponent.getSiteById(siteId), entity, category, null);
            } catch (IOException | TemplateException e) {
            }
        }
    }
}
