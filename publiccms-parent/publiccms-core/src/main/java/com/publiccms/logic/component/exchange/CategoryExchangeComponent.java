package com.publiccms.logic.component.exchange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractExchange;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.sys.SysExtend;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryAttributeService;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsTagTypeService;
import com.publiccms.logic.service.sys.SysExtendFieldService;
import com.publiccms.logic.service.sys.SysExtendService;
import com.publiccms.views.pojo.exchange.Category;
import com.publiccms.views.pojo.query.CmsCategoryQuery;

import freemarker.template.TemplateException;
import jakarta.annotation.Resource;

/**
 * CategoryExchangeComponent 分类数据导入导出组件
 * 
 */
@Component
public class CategoryExchangeComponent extends AbstractExchange<CmsCategory, Category> {
    @Resource
    private CmsCategoryService service;
    @Resource
    private TemplateComponent templateComponent;
    @Resource
    private SiteComponent siteComponent;
    @Resource
    private CmsCategoryAttributeService attributeService;
    @Resource
    private CmsCategoryModelService categoryModelService;
    @Resource
    private SysExtendService extendService;
    @Resource
    private SysExtendFieldService extendFieldService;
    @Resource
    private CmsTagTypeService tagTypeService;

    @Override
    public void exportAll(short siteId, String directory, ByteArrayOutputStream outputStream, ZipOutputStream zipOutputStream) {
        CmsCategoryQuery query = new CmsCategoryQuery();
        query.setSiteId(siteId);
        query.setDisabled(false);
        PageHandler page = service.getPage(query, null, null);
        if (null != page.getList()) {
            @SuppressWarnings("unchecked")
            List<CmsCategory> list = (List<CmsCategory>) page.getList();
            for (CmsCategory category : list) {
                exportEntity(siteId, directory, category, outputStream, zipOutputStream);
            }
        }
    }

    @Override
    public void exportEntity(short siteId, String directory, CmsCategory entity, ByteArrayOutputStream outputStream,
            ZipOutputStream zipOutputStream) {
        exportEntity(siteId, directory, null, entity, outputStream, zipOutputStream);
    }

    public void exportEntity(short siteId, String directory, String parentCode, CmsCategory entity,
            ByteArrayOutputStream outputStream, ZipOutputStream zipOutputStream) {
        Integer categoryId = entity.getId();
        Category data = new Category();
        data.setParentCode(parentCode);
        entity.setId(null);
        data.setEntity(entity);
        data.setAttribute(attributeService.getEntity(categoryId));
        data.setModelList(categoryModelService.getList(siteId, null, categoryId));
        if (null != entity.getExtendId()) {
            data.setExtendList(extendFieldService.getList(entity.getExtendId(), null, null));
        }
        if (CommonUtils.notEmpty(entity.getTagTypeIds())) {
            String[] tagIds = StringUtils.split(entity.getTagTypeIds(), CommonConstants.COMMA);
            Set<Serializable> set = new TreeSet<>();
            for (String s : tagIds) {
                try {
                    set.add(Integer.valueOf(s));
                } catch (NumberFormatException e) {
                }
            }
            tagTypeService.getEntitys(set);
        }
        export(directory, outputStream, zipOutputStream, data, entity.getCode() + ".json");
        if (CommonUtils.notEmpty(entity.getChildIds())) {
            CmsCategoryQuery query = new CmsCategoryQuery();
            query.setSiteId(siteId);
            query.setParentId(categoryId);
            query.setDisabled(false);
            PageHandler page = service.getPage(query, null, null);
            if (null != page.getList()) {
                @SuppressWarnings("unchecked")
                List<CmsCategory> list = (List<CmsCategory>) page.getList();
                for (CmsCategory category : list) {
                    exportEntity(siteId, directory, entity.getCode(), category, outputStream, zipOutputStream);
                }
            }
        }
    }

    public void save(short siteId, long userId, boolean overwrite, Category data) {
        save(siteId, userId, overwrite, null, data);
    }

    public void save(short siteId, long userId, boolean overwrite, Integer parentId, Category data) {
        CmsCategory entity = data.getEntity();
        CmsCategory oldentity = service.getEntityByCode(siteId, entity.getCode());
        if (null == oldentity || overwrite) {
            entity.setSiteId(siteId);
            if (null != parentId) {
                entity.setParentId(parentId);
            } else if (CommonUtils.notEmpty(data.getParentCode())) {
                CmsCategory parent = service.getEntityByCode(siteId, data.getParentCode());
                if (null != parent) {
                    entity.setParentId(parent.getId());
                }
            }
            if (null == oldentity) {
                service.save(entity);
                if (null != data.getModelList()) {
                    for (CmsCategoryModel temp : data.getModelList()) {
                        temp.setSiteId(siteId);
                        temp.getId().setCategoryId(entity.getId());
                    }
                    categoryModelService.save(data.getModelList());
                }
                if (null != data.getExtendList()) {
                    SysExtend extend = new SysExtend("category", entity.getId());
                    extendService.saveOrUpdate(extend);

                    entity.setExtendId(extend.getId());
                    service.update(entity, entity);

                    for (SysExtendField temp : data.getExtendList()) {
                        temp.getId().setExtendId(extend.getId());
                    }
                    extendFieldService.save(data.getExtendList());
                }
            } else {
                entity.setId(oldentity.getId());
                service.update(oldentity.getId(), entity);
                if (null != data.getModelList()) {
                    for (CmsCategoryModel temp : data.getModelList()) {
                        temp.setSiteId(siteId);
                        temp.getId().setCategoryId(entity.getId());
                    }
                    categoryModelService.saveOrUpdate(data.getModelList());
                }
                if (null != data.getExtendList()) {
                    Integer extendId;
                    if (null == oldentity.getExtendId()) {
                        SysExtend extend = new SysExtend("category", entity.getId());
                        extendService.saveOrUpdate(extend);
                        extendId = extend.getId();
                    } else {
                        extendId = oldentity.getExtendId();
                    }
                    entity.setExtendId(extendId);
                    service.update(entity, entity);
                    for (SysExtendField temp : data.getExtendList()) {
                        temp.getId().setExtendId(extendId);
                    }
                    extendFieldService.update(parentId, data.getExtendList());
                }
            }
            if (null != data.getAttribute()) {
                data.getAttribute().setCategoryId(entity.getId());
                attributeService.saveOrUpdate(data.getAttribute());
            }
            if (null != data.getChildList()) {
                for (Category child : data.getChildList()) {
                    save(siteId, userId, overwrite, child);
                }
            }
            try {
                templateComponent.createCategoryFile(siteComponent.getSiteById(siteId), entity, null, null);
            } catch (IOException | TemplateException e) {
            }
        }
    }

    @Override
    public String getDirectory() {
        return "category";
    }
}
