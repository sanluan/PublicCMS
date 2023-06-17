package com.publiccms.logic.component.exchange;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentFile;
import com.publiccms.entities.cms.CmsContentProduct;
import com.publiccms.entities.cms.CmsContentRelated;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentFileService;
import com.publiccms.logic.service.cms.CmsContentProductService;
import com.publiccms.logic.service.cms.CmsContentRelatedService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.logic.service.sys.SysExtendFieldService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.views.pojo.entities.CmsModel;
import com.publiccms.views.pojo.exchange.Content;
import com.publiccms.views.pojo.query.CmsContentQuery;

import freemarker.template.TemplateException;
import jakarta.annotation.Resource;

/**
 * ContentExchangeComponent 内容数据导入导出组件
 * 
 */
@Component
public class ContentExchangeComponent extends AbstractDataExchange<CmsContent, Content> {
    @Resource
    private CmsContentService service;
    @Resource
    private TemplateComponent templateComponent;
    @Resource
    private SiteComponent siteComponent;
    @Resource
    private ModelComponent modelComponent;
    @Resource
    private CmsCategoryService categoryService;
    @Resource
    private CmsContentAttributeService attributeService;
    @Resource
    private CmsContentFileService fileService;
    @Resource
    private CmsContentProductService productService;
    @Resource
    private CmsContentRelatedService relatedService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private SysExtendFieldService extendFieldService;

    @Override
    public void exportAll(SysSite site, String directory, ByteArrayOutputStream outputStream, ZipOutputStream zipOutputStream) {
        CmsContentQuery queryEntity = new CmsContentQuery();
        queryEntity.setSiteId(site.getId());
        queryEntity.setDisabled(false);
        queryEntity.setEmptyParent(true);
        exportDataByQuery(site, directory, queryEntity, outputStream, zipOutputStream);
    }

    /**
     * @param site
     * @param directory
     * @param queryEntity
     * @param zipOutputStream
     */
    public void exportDataByQuery(SysSite site, String directory, CmsContentQuery queryEntity, ZipOutputStream zipOutputStream) {
        exportDataByQuery(site, directory, queryEntity, new ByteArrayOutputStream(), zipOutputStream);
    }

    /**
     * @param site
     * @param directory
     * @param queryEntity
     * @param outputStream
     * @param zipOutputStream
     */
    public void exportDataByQuery(SysSite site, String directory, CmsContentQuery queryEntity, ByteArrayOutputStream outputStream,
            ZipOutputStream zipOutputStream) {
        PageHandler page = service.getPage(queryEntity, true, null, null, null, null, PageHandler.MAX_PAGE_SIZE, null);
        int i = 1;
        do {
            @SuppressWarnings("unchecked")
            List<CmsContent> list = (List<CmsContent>) page.getList();
            for (CmsContent entity : list) {
                exportEntity(site, directory, entity, outputStream, zipOutputStream);
            }
            page = service.getPage(queryEntity, null, null, null, null, i++, PageHandler.MAX_PAGE_SIZE, null);
        } while (!page.isLastPage());
    }

    @Override
    public void exportEntity(SysSite site, String directory, CmsContent entity, ByteArrayOutputStream out,
            ZipOutputStream zipOutputStream) {
        CmsCategory category = categoryService.getEntity(entity.getCategoryId());
        if (null != category) {
            Set<String> webfileList = null;
            if (null == directory) {
                webfileList = new HashSet<>();
            }
            Content data = exportEntity(site, category.getCode(), entity, webfileList);
            CmsModel model = modelComponent.getModel(site, entity.getModelId());
            if (null != model && model.isHasChild()) {
                List<CmsContent> list = service.getListByTopId(site.getId(), entity.getId());
                if (null != list) {
                    List<Content> childList = new ArrayList<>();
                    for (CmsContent content : list) {
                        childList.add(exportEntity(site, category.getCode(), content, webfileList));
                    }
                    data.setChildList(childList);
                }
            }
            export(directory, out, zipOutputStream, data, CommonUtils.joinString(entity.getId(), ".json"));
            if (null != webfileList && !webfileList.isEmpty()) {
                for (String file : webfileList) {
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
    }

    @Override
    public void importData(SysSite site, long userId, String directory, boolean overwrite, ZipFile zipFile) {
        super.importData(site, userId, directory, overwrite, zipFile);
        if (null == directory) {
            String filepath = siteComponent.getWebFilePath(site.getId(), Constants.SEPARATOR);
            ZipUtils.unzip(zipFile, ATTACHMENT_DIR, filepath, overwrite, null);
        }
    }

    @Override
    public void save(SysSite site, long userId, boolean overwrite, Content data) {
        CmsCategory category = categoryService.getEntityByCode(site.getId(), data.getCategoryCode());
        SysUser user = sysUserService.getEntity(userId);
        if (null != category) {
            save(site, userId, overwrite, category, user, data);
        }
    }

    public void save(SysSite site, long userId, boolean overwrite, CmsCategory category, SysUser user, Content data) {
        CmsContent entity = data.getEntity();
        CmsContent oldentity = service.getEntity(entity.getId());
        if (null != category
                && (null == oldentity || oldentity.isDisabled() || oldentity.getSiteId() != site.getId() || overwrite)) {
            if (null != oldentity && oldentity.getSiteId() != site.getId()) {
                entity.setId(null);
            }
            entity.setSiteId(site.getId());
            entity.setUserId(userId);
            entity.setDeptId(null != user ? user.getDeptId() : null);
            entity.setCategoryId(category.getId());
            if (null == entity.getId()) {
                service.save(entity);
            } else {
                service.update(entity.getId(), entity);
            }
            if (null != data.getAttribute()) {
                data.getAttribute().setContentId(entity.getId());
                if (needReplace(data.getAttribute().getText(), site.getDynamicPath())) {
                    data.getAttribute()
                            .setText(StringUtils.replace(data.getAttribute().getText(), "#DYNAMICPATH#", site.getDynamicPath()));
                }
                if (needReplace(data.getAttribute().getText(), site.getSitePath())) {
                    data.getAttribute()
                            .setText(StringUtils.replace(data.getAttribute().getText(), "#SITEPATH#", site.getSitePath()));
                }
                if (needReplace(data.getAttribute().getData(), site.getDynamicPath())) {
                    data.getAttribute()
                            .setData(StringUtils.replace(data.getAttribute().getData(), "#DYNAMICPATH#", site.getDynamicPath()));
                }
                if (needReplace(data.getAttribute().getData(), site.getSitePath())) {
                    data.getAttribute()
                            .setData(StringUtils.replace(data.getAttribute().getData(), "#SITEPATH#", site.getSitePath()));
                }
                if (null == attributeService.update(data.getAttribute().getContentId(), data.getAttribute())) {
                    attributeService.save(data.getAttribute());
                }
            }
            if (null != data.getChildList()) {
                for (Content child : data.getChildList()) {
                    child.getEntity().setParentId(entity.getId());
                    save(site, userId, overwrite, category, user, child);
                }
            }
            try {
                templateComponent.createContentFile(site, entity, category, null);
            } catch (IOException | TemplateException e) {
            }
        }
    }

    private Content exportEntity(SysSite site, String categoryCode, CmsContent entity, Set<String> webfileList) {
        Content data = new Content();
        data.setCategoryCode(categoryCode);
        data.setEntity(entity);
        if (null != webfileList && CommonUtils.notEmpty(entity.getCover()) && !entity.getCover().contains("://")
                && entity.getCover().startsWith("//")) {
            webfileList.add(entity.getCover());
        }
        data.setAttribute(attributeService.getEntity(entity.getId()));
        if (null != data.getAttribute()) {
            if (CommonUtils.notEmpty(data.getAttribute().getText())) {
                if (null != webfileList) {
                    HtmlUtils.getFileList(data.getAttribute().getText(), webfileList);
                }
                data.getAttribute().setText(StringUtils.replace(data.getAttribute().getText(), site.getSitePath(), "#SITEPATH#"));
                data.getAttribute()
                        .setText(StringUtils.replace(data.getAttribute().getText(), site.getDynamicPath(), "#DYNAMICPATH#"));
            }
            if (CommonUtils.notEmpty(data.getAttribute().getData())) {
                if (null != webfileList) {
                    Map<String, String> map = ExtendUtils.getExtendMap(data.getAttribute().getData());
                    for (String value : map.values()) {
                        if (null != value && value.contains("<")) {
                            HtmlUtils.getFileList(value, webfileList);
                        }
                    }
                }
                data.getAttribute().setData(StringUtils.replace(data.getAttribute().getData(), site.getSitePath(), "#SITEPATH#"));
                data.getAttribute()
                        .setData(StringUtils.replace(data.getAttribute().getData(), site.getDynamicPath(), "#DYNAMICPATH#"));
            }
        }
        if (entity.isHasFiles() || entity.isHasImages()) {
            @SuppressWarnings("unchecked")
            List<CmsContentFile> fileList = (List<CmsContentFile>) fileService
                    .getPage(entity.getId(), null, null, null, null, null, null).getList();
            data.setFileList(fileList);
            if (null != webfileList) {
                for (CmsContentFile file : fileList) {
                    if (!file.getFilePath().contains("://") && file.getFilePath().startsWith("//")) {
                        webfileList.add(file.getFilePath());
                    }
                }
            }
        }
        if (entity.isHasProducts()) {
            List<CmsContentProduct> productList = productService.getList(site.getId(), entity.getId());
            data.setProductList(productList);
            if (null != webfileList) {
                for (CmsContentProduct file : productList) {
                    if (CommonUtils.notEmpty(file.getCover()) && !file.getCover().contains("://")
                            && file.getCover().startsWith("//")) {
                        webfileList.add(file.getCover());
                    }
                }
            }
        }
        @SuppressWarnings("unchecked")
        List<CmsContentRelated> relatedList = (List<CmsContentRelated>) relatedService
                .getPage(entity.getId(), null, null, null, null, null, null, null).getList();
        data.setRelatedList(relatedList);
        return data;
    }

    @Override
    public String getDirectory() {
        return "content";
    }
}
