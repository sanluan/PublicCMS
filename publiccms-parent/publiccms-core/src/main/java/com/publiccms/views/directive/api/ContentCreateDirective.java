package com.publiccms.views.directive.api;

import static org.springframework.util.StringUtils.arrayToDelimitedString;

//Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.HtmlUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.controller.admin.cms.CmsContentAdminController;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsCategoryModelId;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.cms.CmsTag;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsTagService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.views.pojo.entities.CmsModel;

/**
 *
 * ContentClickDirective
 * 
 */
@Component
public class ContentCreateDirective extends AbstractAppDirective {
    @Autowired
    private CmsContentService service;
    @Autowired
    private CmsTagService tagService;
    @Autowired
    private CmsContentAttributeService attributeService;
    @Autowired
    private CmsCategoryModelService categoryModelService;
    @Autowired
    private CmsCategoryService categoryService;
    @Autowired
    private ModelComponent modelComponent;
    @Autowired
    protected LogOperateService logOperateService;

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        SysSite site = getSite(handler);
        CmsContent entity = new CmsContent();
        entity.setCategoryId(handler.getInteger("categoryId"));
        entity.setModelId(handler.getString("modelId"));
        CmsCategoryModel categoryModel = categoryModelService
                .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));
        handler.put("result", "failed");
        if (null != categoryModel) {
            CmsCategory category = categoryService.getEntity(entity.getCategoryId());
            CmsModel cmsModel = modelComponent.getMap(site).get(entity.getModelId());
            if (null != category && null != cmsModel && site.getId() == category.getSiteId() && category.isAllowContribute()) {
                entity.setOnlyUrl(cmsModel.isOnlyUrl());
                entity.setHasImages(cmsModel.isHasImages());
                entity.setHasFiles(cmsModel.isHasFiles());
                entity.setTitle(handler.getString("title"));
                entity.setDescription(handler.getString("description"));
                entity.setAuthor(handler.getString("author"));
                entity.setEditor(handler.getString("editor"));
                entity.setCopied(handler.getBoolean("copied", false));
                entity.setPublishDate(handler.getDate("publishDate"));
                entity.setDictionaryValues(
                        arrayToDelimitedString(handler.getStringArray("dictionaryValues"), CommonConstants.BLANK_SPACE));
                String[] tagNames = handler.getStringArray("tagNames");
                Long[] tagIds = handler.getLongArray("tagIds");
                if (CommonUtils.notEmpty(tagNames) || CommonUtils.notEmpty(tagIds)) {
                    List<CmsTag> tagList = new ArrayList<>();
                    if (null != tagNames) {
                        for (String name : tagNames) {
                            CmsTag tag = new CmsTag();
                            tag.setName(name);
                            tagList.add(tag);
                        }
                    }
                    if (null != tagIds) {
                        for (Long id : tagIds) {
                            CmsTag tag = new CmsTag();
                            tag.setId(id);
                            tagList.add(tag);
                        }
                    }
                    tagIds = tagService.update(site.getId(), tagList);
                    entity.setTagIds(arrayToDelimitedString(tagIds, CommonConstants.BLANK_SPACE));
                } else {
                    entity.setTagIds(null);
                }
                if (entity.isOnlyUrl()) {
                    entity.setUrl(handler.getString("url"));
                }

                CmsContentAttribute attribute = new CmsContentAttribute();
                attribute.setSource(handler.getString("source"));
                attribute.setSourceUrl(handler.getString("sourceUrl"));
                attribute.setText(handler.getString("text"));
                attribute.setData(handler.getString("data"));

                CmsContentAdminController.initContent(entity, cmsModel, handler.getBoolean("draft"),
                        handler.getBoolean("checked"), attribute, false, CommonUtils.getDate());
                if (null != entity.getId()) {
                    CmsContent oldEntity = service.getEntity(entity.getId());
                    if (null != oldEntity && site.getId() == oldEntity.getSiteId()) {
                        entity = service.update(entity.getId(), entity,
                                entity.isOnlyUrl() ? CmsContentAdminController.ignoreProperties
                                        : CmsContentAdminController.ignorePropertiesWithUrl);
                    }
                    if (null != entity.getId()) {
                        logOperateService.save(new LogOperate(site.getId(), user.getId(), app.getChannel(), "update.content",
                                RequestUtils.getIpAddress(handler.getRequest()), CommonUtils.getDate(),
                                JsonUtils.getString(entity)));
                    }
                } else {
                    entity.setParentId(handler.getLong("parentId"));
                    entity.setSiteId(site.getId());
                    entity.setUserId(user.getId());
                    service.save(entity);
                    if (CommonUtils.notEmpty(entity.getParentId())) {
                        service.updateChilds(entity.getParentId(), 1);
                    }
                    logOperateService.save(new LogOperate(site.getId(), user.getId(), app.getChannel(), "save.content",
                            RequestUtils.getIpAddress(handler.getRequest()), CommonUtils.getDate(), JsonUtils.getString(entity)));
                }
                String text = HtmlUtils.removeHtmlTag(attribute.getText());
                attribute.setWordCount(text.length());
                if (CommonUtils.empty(entity.getDescription())) {
                    entity.setDescription(StringUtils.substring(text, 0, 300));
                }
                if (cmsModel.isSearchable()) {
                    attribute.setSearchText(text);
                }
                attributeService.updateAttribute(entity.getId(), attribute);
                handler.put("result", "success");
            }
        }
    }

    @Override
    public boolean needUserToken() {
        return true;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

}