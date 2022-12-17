package com.publiccms.logic.component.interaction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import com.publiccms.views.pojo.interaction.Category;
import com.publiccms.views.pojo.query.CmsCategoryQuery;

import freemarker.template.TemplateException;

/**
 * CategoryInteractionComponent 分类数据导出组件
 * 
 */
@Component
public class CategoryInteractionComponent extends InteractionComponent<CmsCategory, Category> {
    @Autowired
    private CmsCategoryService service;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private SiteComponent siteComponent;
    @Autowired
    private CmsCategoryAttributeService attributeService;
    @Autowired
    private CmsCategoryModelService categoryModelService;
    @Autowired
    private SysExtendService extendService;
    @Autowired
    private SysExtendFieldService extendFieldService;
    @Autowired
    private CmsTagTypeService tagTypeService;

    @Override
    public void exportAll(short siteId, String directory, ZipOutputStream zipOutputStream) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CmsCategoryQuery query = new CmsCategoryQuery();
        query.setSiteId(siteId);
        query.setDisabled(false);
        PageHandler page = service.getPage(query, null, null);
        if (null != page.getList()) {
            @SuppressWarnings("unchecked")
            List<CmsCategory> list = (List<CmsCategory>) page.getList();
            for (CmsCategory category : list) {
                exportEntity(siteId, directory, category, out, zipOutputStream);
            }
        }
    }

    @Override
    public void exportEntity(short siteId, String directory, CmsCategory entity, ByteArrayOutputStream out,
            ZipOutputStream zipOutputStream) {
        exportEntity(siteId, directory, null, entity, out, zipOutputStream);
    }

    public void exportEntity(short siteId, String directory, String parentCode, CmsCategory entity, ByteArrayOutputStream out,
            ZipOutputStream zipOutputStream) {
        Category data = new Category();
        data.setParentCode(parentCode);
        entity.setId(null);
        data.setEntity(entity);
        data.setAttribute(attributeService.getEntity(entity.getId()));
        data.setModelList(categoryModelService.getList(siteId, null, entity.getId()));
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
        export(directory, out, zipOutputStream, data, entity.getCode() + ".json");
        if (CommonUtils.notEmpty(entity.getChildIds())) {
            CmsCategoryQuery query = new CmsCategoryQuery();
            query.setSiteId(siteId);
            query.setParentId(entity.getId());
            query.setDisabled(false);
            PageHandler page = service.getPage(query, null, null);
            if (null != page.getList()) {
                @SuppressWarnings("unchecked")
                List<CmsCategory> list = (List<CmsCategory>) page.getList();
                for (CmsCategory category : list) {
                    exportEntity(siteId, directory, entity.getCode(), category, out, zipOutputStream);
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
            service.save(entity);
            if (null != data.getAttribute()) {
                data.getAttribute().setCategoryId(entity.getId());
                attributeService.save(data.getAttribute());
            }
            if (null != data.getModelList()) {
                for (CmsCategoryModel temp : data.getModelList()) {
                    temp.setSiteId(siteId);
                    temp.getId().setCategoryId(entity.getId());
                }
                categoryModelService.save(data.getModelList());
            }
            if (null != data.getExtendList()) {
                SysExtend extend = new SysExtend("category", entity.getId());
                extendService.save(extend);

                entity.setExtendId(extend.getId());
                service.update(entity, entity);

                for (SysExtendField temp : data.getExtendList()) {
                    temp.getId().setExtendId(extend.getId());
                }
                extendFieldService.save(data.getExtendList());
            }
            for (Category child : data.getChildList()) {
                save(siteId, userId, overwrite, child);
            }
            try {
                templateComponent.createCategoryFile(siteComponent.getSiteById(siteId), entity, null, null);
            } catch (IOException | TemplateException e) {
            }
        }
    }
}
