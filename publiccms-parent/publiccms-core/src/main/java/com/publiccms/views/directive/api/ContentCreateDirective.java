package com.publiccms.views.directive.api;

import static org.springframework.util.StringUtils.collectionToDelimitedString;

//Generated 2015-5-10 17:54:56 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.HtmlUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.controller.admin.cms.CmsContentAdminController;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsCategoryModelId;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.cms.CmsContentFile;
import com.publiccms.entities.cms.CmsTag;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentFileService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsTagService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysExtendFieldService;
import com.publiccms.logic.service.sys.SysExtendService;
import com.publiccms.views.pojo.entities.CmsModel;

import freemarker.template.TemplateException;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidatorFactory;

/**
 *
 * contentCreate 内容创建接口
 *
 * <p>
 * 参数列表
 * <ul>
 * <li><code>id</code>:内容id,为空时新建内容
 * <li><code>categoryId</code>:分类id
 * <li><code>modelId</code>:模型id
 * <li><code>title</code>:标题
 * <li><code>description</code>:标题
 * <li><code>title</code>:标题
 * <li><code>author</code>:标题
 * <li><code>editor</code>:编辑
 * <li><code>copied</code>:转载,【true,false】,默认为<code>false</code>
 * <li><code>publishDate</code>:发布日期,默认为当前日期
 * <li><code>tagNames</code>:多个标签名
 * <li><code>tagIds</code>:多个标签id
 * <li><code>url</code>:url,当模型为外链时有效
 * <li><code>checked</code>:已审核,【true,false】,默认为<code>false</code>
 * <li><code>extendData</code>:扩展数据map
 * <li><code>source</code>:来源
 * <li><code>sourceUrl</code>:来源URL
 * <li><code>text</code>:正文html
 * <li><code>draft</code>:草稿,审核为<code>false</code>时有效,【true,false】,默认为<code>false</code>
 * <li><code>parentId</code>:父id,模型为子模型时有效
 * <li><code>filePaths</code>:多个文件路径
 * <li><code>fileDescriptions</code>:多个文件描述
 * <li><code>imagePaths</code>:多个图片路径
 * <li><code>imageDescriptions</code>:多个图片描述
 * 
 * </code>:标题
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>result</code>:结果【failed:失败,success:成功】
 * <li><code>contentId</code>:内容id,当result为success时有效
 * <li><code>error</code>:错误,当result为failed时有效
 * </ul>
 * 使用示例
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath!}api/contentCheck?categoryId=1&amp;modelId=article&amp;title=title&amp;text=%3Cdiv%3Econtent%3C/div%3E&amp;extendData.field1=value1&amp;extendData.field2=value2&amp;authToken=用户登录授权&amp;authUserId=1&amp;appToken=接口访问授权Token', function(data){
console.log(data.result);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class ContentCreateDirective extends AbstractAppDirective {
    @Resource
    private ValidatorFactory validatorFactory;
    @Resource
    private CmsContentService service;
    @Resource
    private CmsTagService tagService;
    @Resource
    private SysExtendService extendService;
    @Resource
    private SysExtendFieldService extendFieldService;
    @Resource
    private CmsContentAttributeService attributeService;
    @Resource
    private CmsCategoryModelService categoryModelService;
    @Resource
    private CmsCategoryService categoryService;
    @Resource
    private ModelComponent modelComponent;
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    private CmsContentFileService contentFileService;
    @Resource
    private TemplateComponent templateComponent;

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, TemplateException {
        SysSite site = getSite(handler);
        CmsContent entity = new CmsContent();
        entity.setCategoryId(handler.getInteger("categoryId"));
        entity.setModelId(handler.getString("modelId"));
        CmsCategoryModel categoryModel = categoryModelService
                .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));
        handler.put("result", "failed");
        if (null != categoryModel) {
            CmsCategory category = categoryService.getEntity(entity.getCategoryId());
            CmsModel cmsModel = modelComponent.getModel(site, entity.getModelId());
            if (null != category && null != cmsModel && site.getId() == category.getSiteId()) {
                if (category.isAllowContribute()) {
                    entity.setId(handler.getLong("id"));
                    entity.setOnlyUrl(cmsModel.isOnlyUrl());
                    entity.setHasImages(cmsModel.isHasImages());
                    entity.setHasFiles(cmsModel.isHasFiles());
                    entity.setTitle(handler.getString("title"));
                    entity.setDescription(handler.getString("description"));
                    entity.setAuthor(handler.getString("author"));
                    entity.setEditor(handler.getString("editor"));
                    entity.setCopied(handler.getBoolean("copied", false));
                    entity.setPublishDate(handler.getDate("publishDate"));
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
                        Set<Serializable> tagIdSet = tagService.update(site.getId(), tagList);
                        entity.setTagIds(collectionToDelimitedString(tagIdSet, Constants.BLANK_SPACE));
                    } else {
                        entity.setTagIds(null);
                    }
                    if (entity.isOnlyUrl()) {
                        entity.setUrl(handler.getString("url"));
                    }

                    Boolean checked = handler.getBoolean("checked");
                    List<SysExtendField> categoryExtendList = null;
                    if (null != category.getExtendId() && null != extendService.getEntity(category.getExtendId())) {
                        categoryExtendList = extendFieldService.getList(category.getExtendId(), null, null);
                    }

                    Map<String, String> extendData = handler.getMap("extendData");
                    CmsContentAttribute attribute = new CmsContentAttribute();
                    attribute.setSource(handler.getString("source"));
                    attribute.setSourceUrl(handler.getString("sourceUrl"));
                    attribute.setText(handler.getString("text"));
                    attribute.setData(ExtendUtils.getExtendString(extendData, site.getSitePath(), cmsModel.getExtendList(),
                            categoryExtendList));
                    CmsContentAdminController.initContent(entity, site, cmsModel, handler.getBoolean("draft"), checked, attribute,
                            false, CommonUtils.getDate());
                    String text = HtmlUtils.removeHtmlTag(attribute.getText());
                    attribute.setWordCount(text.length());
                    if (CommonUtils.empty(entity.getDescription())) {
                        entity.setDescription(CommonUtils.keep(text, 300));
                    }
                    if (cmsModel.isSearchable()) {
                        attribute.setSearchText(text);
                    }

                    Set<ConstraintViolation<CmsContent>> set = validatorFactory.getValidator().validate(entity);
                    if (!set.isEmpty()) {
                        handler.put(CommonConstants.ERROR, set.stream().map(cv -> cv.getPropertyPath().toString())
                                .collect(Collectors.joining(Constants.COMMA_DELIMITED)));
                    } else {
                        if (null != entity.getId()) {
                            CmsContent oldEntity = service.getEntity(entity.getId());
                            if (null != oldEntity && site.getId() == oldEntity.getSiteId()) {
                                entity = service.update(entity.getId(), entity,
                                        entity.isOnlyUrl() ? CmsContentAdminController.ignoreProperties
                                                : CmsContentAdminController.ignorePropertiesWithUrl);
                            }
                            if (null != entity.getId()) {
                                logOperateService.save(new LogOperate(site.getId(), user.getId(), user.getDeptId(),
                                        app.getChannel(), "update.content", RequestUtils.getIpAddress(handler.getRequest()),
                                        CommonUtils.getDate(), JsonUtils.getString(entity)));
                            }
                        } else {
                            if (CommonUtils.notEmpty(cmsModel.getParentId())) {
                                entity.setParentId(handler.getLong("parentId"));
                            }
                            entity.setSiteId(site.getId());
                            entity.setUserId(user.getId());
                            service.save(entity);
                            if (CommonUtils.notEmpty(entity.getParentId())) {
                                service.updateChilds(entity.getParentId(), 1);
                            }
                            logOperateService.save(new LogOperate(site.getId(), user.getId(), user.getDeptId(), app.getChannel(),
                                    "save.content", RequestUtils.getIpAddress(handler.getRequest()), CommonUtils.getDate(),
                                    JsonUtils.getString(entity)));
                        }

                        service.saveEditorHistory(attributeService.getEntity(entity.getId()), attribute, site.getId(),
                                entity.getId(), user.getId(), cmsModel.getExtendList(), categoryExtendList, extendData);

                        attributeService.updateAttribute(entity.getId(), attribute);
                        if (entity.isHasImages() || entity.isHasFiles()) {
                            List<CmsContentFile> files = null;
                            if (entity.isHasFiles()) {
                                files = new ArrayList<>();
                                String[] filepaths = handler.getStringArray("filePaths");
                                String[] fileDescriptions = handler.getStringArray("fileDescriptions");
                                if (null != filepaths) {
                                    int i = 0;
                                    for (String filepath : filepaths) {
                                        CmsContentFile e = new CmsContentFile();
                                        e.setFilePath(filepath);
                                        if (null != fileDescriptions && fileDescriptions.length > i) {
                                            e.setDescription(fileDescriptions[i]);
                                        }
                                        files.add(e);
                                        i++;
                                    }
                                }
                            }
                            List<CmsContentFile> images = null;
                            if (entity.isHasImages()) {
                                images = new ArrayList<>();
                                String[] imagePaths = handler.getStringArray("imagePaths");
                                String[] imageDescriptions = handler.getStringArray("imageDescriptions");
                                if (null != imagePaths) {
                                    int i = 0;
                                    for (String filepath : imagePaths) {
                                        CmsContentFile e = new CmsContentFile();
                                        e.setFilePath(filepath);
                                        if (null != imageDescriptions && imageDescriptions.length > i) {
                                            e.setDescription(imageDescriptions[i]);
                                        }
                                        files.add(e);
                                        i++;
                                    }
                                }
                            }
                            contentFileService.update(entity.getId(), user.getId(), files, images);// 更新保存图集,附件
                        }
                        if (null != checked && checked) {
                            service.check(site.getId(), user, new Long[] { entity.getId() });
                            templateComponent.createContentFile(site, entity, category, categoryModel);
                            templateComponent.createCategoryFile(site, category, null, null);
                        }
                        handler.put("contentId", entity.getId());
                        handler.put("result", "success");
                    }
                } else {
                    handler.put(CommonConstants.ERROR, LanguagesUtils.getMessage(CommonConstants.applicationContext,
                            RequestContextUtils.getLocale(handler.getRequest()), "verify.custom.contribute"));
                }
            } else {
                handler.put(CommonConstants.ERROR, LanguagesUtils.getMessage(CommonConstants.applicationContext,
                        RequestContextUtils.getLocale(handler.getRequest()), "verify.notEmpty.category"));
            }
        } else {
            handler.put(CommonConstants.ERROR, LanguagesUtils.getMessage(CommonConstants.applicationContext,
                    RequestContextUtils.getLocale(handler.getRequest()), "verify.notEmpty.categoryModel"));
        }
        handler.render();
    }

    @Override
    public boolean needUserToken() {
        return true;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }

}