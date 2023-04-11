package com.publiccms.logic.component.exchange;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractDataExchange;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.HtmlUtils;
import com.publiccms.common.tools.ZipUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.sys.SysExtend;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
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

/**
 * CategoryExchangeComponent 分类数据导入导出组件
 * 
 */
@Component
public class CategoryExchangeComponent extends AbstractDataExchange<CmsCategory, Category> {
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
    public void exportAll(SysSite site, String directory, ByteArrayOutputStream outputStream, ZipOutputStream zipOutputStream) {
        CmsCategoryQuery query = new CmsCategoryQuery();
        query.setSiteId(site.getId());
        query.setDisabled(false);
        PageHandler page = service.getPage(query, null, null);
        if (null != page.getList()) {
            @SuppressWarnings("unchecked")
            List<CmsCategory> list = (List<CmsCategory>) page.getList();
            for (CmsCategory category : list) {
                exportEntity(site, directory, category, outputStream, zipOutputStream);
            }
        }
    }

    @Override
    public void exportEntity(SysSite site, String directory, CmsCategory entity, ByteArrayOutputStream outputStream,
            ZipOutputStream zipOutputStream) {
        exportEntity(site, directory, null, entity, outputStream, zipOutputStream);
    }

    /**
     * @param site
     * @param directory
     * @param parentCode
     * @param entity
     * @param outputStream
     * @param zipOutputStream
     */
    public void exportEntity(SysSite site, String directory, String parentCode, CmsCategory entity,
            ByteArrayOutputStream outputStream, ZipOutputStream zipOutputStream) {
        Integer categoryId = entity.getId();
        Category data = new Category();
        data.setParentCode(parentCode);
        entity.setId(null);
        data.setEntity(entity);
        data.setAttribute(attributeService.getEntity(categoryId));
        if (null != data.getAttribute()) {
            if (CommonUtils.notEmpty(data.getAttribute().getData())) {
                if (null == directory) {
                    Map<String, String> map = ExtendUtils.getExtendMap(data.getAttribute().getData());
                    Set<String> filelist = new HashSet<>();
                    for (String value : map.values()) {
                        if (null != value && value.contains("<")) {
                            HtmlUtils.getFileList(value, filelist);
                        }
                    }
                    if (!filelist.isEmpty()) {
                        for (String file : filelist) {
                            if (file.startsWith(site.getSitePath())) {
                                String fullName = StringUtils.removeStart(file, site.getSitePath());
                                if (fullName.contains(Constants.DOT) && !fullName.contains(".htm")) {
                                    String filepath = siteComponent.getWebFilePath(site.getId(), fullName);
                                    try {
                                        ZipUtils.compressFile(new File(filepath), zipOutputStream,
                                                CommonUtils.joinString(ATTACHMENT_DIR, fullName));
                                    } catch (IOException e) {
                                    }
                                }
                            }
                        }
                    }
                }
                data.getAttribute().setData(StringUtils.replace(data.getAttribute().getData(), site.getSitePath(), "#SITEPATH#"));
                data.getAttribute()
                        .setData(StringUtils.replace(data.getAttribute().getData(), site.getDynamicPath(), "#DYNAMICPATH#"));
            }
        }
        data.setModelList(categoryModelService.getList(site.getId(), null, categoryId));
        if (null != entity.getExtendId()) {
            data.setExtendList(extendFieldService.getList(entity.getExtendId(), null, null));
        }
        if (CommonUtils.notEmpty(entity.getTagTypeIds())) {
            String[] tagIds = StringUtils.split(entity.getTagTypeIds(), Constants.COMMA);
            Set<Serializable> set = new TreeSet<>();
            for (String s : tagIds) {
                try {
                    set.add(Integer.valueOf(s));
                } catch (NumberFormatException e) {
                }
            }
            tagTypeService.getEntitys(set);
        }
        export(directory, outputStream, zipOutputStream, data, CommonUtils.joinString(entity.getCode(), ".json"));
        if (CommonUtils.notEmpty(entity.getChildIds())) {
            CmsCategoryQuery query = new CmsCategoryQuery();
            query.setSiteId(site.getId());
            query.setParentId(categoryId);
            query.setDisabled(false);
            PageHandler page = service.getPage(query, null, null);
            if (null != page.getList()) {
                @SuppressWarnings("unchecked")
                List<CmsCategory> list = (List<CmsCategory>) page.getList();
                for (CmsCategory category : list) {
                    exportEntity(site, directory, entity.getCode(), category, outputStream, zipOutputStream);
                }
            }
        }
    }

    @Override
    public void importData(SysSite site, long userId, String directory, boolean overwrite, ZipFile zipFile) {
        super.importData(site, userId, directory, overwrite, zipFile);
        if (null == directory) {
            String filepath = siteComponent.getWebFilePath(site.getId(), Constants.SEPARATOR);
            ZipUtils.unzip(zipFile, ATTACHMENT_DIR, filepath, overwrite, null);
        }
        service.generateChildIds(site.getId(), null);
    }

    @Override
    public void save(SysSite site, long userId, boolean overwrite, Category data) {
        save(site, userId, overwrite, null, data);
    }

    /**
     * @param site
     * @param userId
     * @param overwrite
     * @param parentId
     * @param data
     */
    public void save(SysSite site, long userId, boolean overwrite, Integer parentId, Category data) {
        CmsCategory entity = data.getEntity();
        CmsCategory oldentity = service.getEntityByCode(site.getId(), entity.getCode());
        if (null == oldentity || overwrite) {
            entity.setSiteId(site.getId());
            if (null != parentId) {
                entity.setParentId(parentId);
            } else if (CommonUtils.notEmpty(data.getParentCode())) {
                CmsCategory parent = service.getEntityByCode(site.getId(), data.getParentCode());
                if (null != parent) {
                    entity.setParentId(parent.getId());
                }
            }
            if (null == oldentity) {
                service.save(entity);
                if (null != data.getModelList()) {
                    for (CmsCategoryModel temp : data.getModelList()) {
                        temp.setSiteId(site.getId());
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
                        temp.setSiteId(site.getId());
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
                if (needReplace(data.getAttribute().getData(), site.getDynamicPath())) {
                    data.getAttribute()
                            .setData(StringUtils.replace(data.getAttribute().getData(), "#DYNAMICPATH#", site.getDynamicPath()));
                }
                if (needReplace(data.getAttribute().getData(), site.getSitePath())) {
                    data.getAttribute()
                            .setData(StringUtils.replace(data.getAttribute().getData(), "#SITEPATH#", site.getSitePath()));
                }
                attributeService.saveOrUpdate(data.getAttribute());
            }
            if (null != data.getChildList()) {
                for (Category child : data.getChildList()) {
                    save(site, userId, overwrite, entity.getId(), child);
                }
            }
            try {
                templateComponent.createCategoryFile(site, entity, null, null);
            } catch (IOException | TemplateException e) {
            }
        }
    }

    @Override
    public int importOrder() {
        return 1;
    }

    @Override
    public String getDirectory() {
        return "category";
    }
}
