package com.publiccms.controller.admin.cms;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CodeUtil;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.DateUtil;
import com.publiccms.common.tools.ExcelUtil;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.common.tools.ajaxresponse.JsonResp;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsCategoryModelId;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.cms.CmsTag;
import com.publiccms.entities.cms.CmsTagType;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsTagService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.views.pojo.entities.CmsModel;
import com.publiccms.views.pojo.model.CmsCategoryModelParameters;
import com.publiccms.views.pojo.model.CmsCategoryParameters;
import com.publiccms.views.pojo.model.CmsContentParameters;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * CmsContentController
 */
@Controller
@RequestMapping("cmsImport")
public class CmsImportAdminController {
    @Autowired
    private CmsContentService cmsContentService;
    @Autowired
    private CmsContentAttributeService cmsContentAttributeService;

    @Autowired
    private CmsTagService cmsTagService;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;
    @Autowired
    private CmsCategoryService categoryService;
    @Autowired
    private CmsCategoryModelService categoryModelService;
    @Autowired
    private ModelComponent modelComponent;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 忽视行数+1 (计算机从0开始)
     */
    private static final int IMPORT_DEFAULT_VALUE_IGNORE_ROW_COUNT = 3;

        private static final String IMPORT_DEFAULT_VALUE_CATEGORY_TEMPLATE_PATH = "/category/list.html";
        private static final String IMPORT_DEFAULT_VALUE_CATEGORY_PATH = "${category.code}/index.html";
        private static final String IMPORT_DEFAULT_VALUE_CATEGORY_CONTENT_PATH = "${category.code}/${content.publishDate?string('yyyy/MM-dd')}/${content.id}.html";
//    private static final String IMPORT_DEFAULT_VALUE_CATEGORY_TEMPLATE_PATH = "";
//    private static final String IMPORT_DEFAULT_VALUE_CATEGORY_PATH = "category/list-dynamic.html?id=${category.id}&pageIndex=1";
//    private static final String IMPORT_DEFAULT_VALUE_CATEGORY_CONTENT_PATH = "system/article-dynamic.html?id=${content.id}";
    private static final int IMPORT_DEFAULT_VALUE_CATEGORY_PAGE_SIZE = 20;
    private static final int IMPORT_DEFAULT_VALUE_CATEGORY_SORT = 0;

    private static final String IMPORT_DEFAULT_VALUE_CONTENT_MODELID = "1";
    private static final boolean IMPORT_DEFAULT_VALUE_CONTENT_DRAFT = false;
    private static final boolean IMPORT_DEFAULT_VALUE_CONTENT_CHECKED = false;
    private static final boolean IMPORT_DEFAULT_VALUE_CONTENT_TEXT_NEED_DECIPHER = false;

    private static final String IMPORT_FIELD_FLAG_TAG = "tag";
    private static final String IMPORT_FIELD_FLAG_CATEGORY = "category";
    private static final String IMPORT_FIELD_FLAG_CONTENT = "content";

    @RequestMapping("hello")
    @ResponseBody
    public String hello() {
        return "hellp";
    }

    @RequestMapping("upload")
    @ResponseBody
    public JsonResp upload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String field,
            MultipartFile file, HttpServletRequest request, ModelMap model) {
        List<String> resultList;
        switch (field) {
            case IMPORT_FIELD_FLAG_TAG:
                resultList = importCmsTag(site, admin, file, request);
                break;
            case IMPORT_FIELD_FLAG_CATEGORY:
                resultList = importCmsCategory(site, admin, file, request);
                break;
            case IMPORT_FIELD_FLAG_CONTENT:
                resultList = importCmsContent(site, admin, file, request);
                break;
            default:
                resultList = Collections.emptyList();
                resultList.add("找不到要导入的信息");
                break;
        }
        if (resultList.size() != 0) {
            return JsonResp.fail("导入过程中以下行数出现了一些问题").addAttr("results", resultList);
        }
        return JsonResp.success("导入成功").addAttr("results", "success");

    }

    @RequestMapping(value = "reptile")
    @ResponseBody
    public JsonResp reptile(@RequestAttribute SysSite site, @RequestBody Map<String, String> map) {
        // 2019年4月17日想要把所有数据变成base64
        //        , consumes = MediaType.TEXT_PLAIN_VALUE
        //            @RequestBody String content

        //        String mapJson = new String(VerificationUtils.base64Decode(content), CommonConstants.DEFAULT_CHARSET);
        //        Gson gson = new Gson();
        //        HashMap<String, String> map = gson.fromJson(mapJson, new TypeToken<HashMap<String, String>>() {
        //        }.getType());
        SysUser admin = sysUserService.findByName(site.getId(), "admin");
        String categoryName = map.get("categoryName");
        CmsCategory cmsCategory;
        // 插入分类http://localhost:8080/publiccms/admin/cmsImport/reptile
        try {
            cmsCategory = createCategoryOnlyName(site, admin, categoryName);
        } catch (Exception e) {
            return JsonResp.fail(e.getMessage());
        }

        String sourceUrl = map.get("sourceUrl");
        if (!CommonUtils.empty(sourceUrl)) {
            CmsContentAttribute cmsContentAttribute = new CmsContentAttribute();
            cmsContentAttribute.setSourceUrl(sourceUrl);
            CmsContentAttribute cmsContentAttributeDaoByEntity = cmsContentAttributeService
                    .findByEntity(cmsContentAttribute);
            if (cmsContentAttributeDaoByEntity != null) {
                return JsonResp.fail("文章已导入");
            }
        }

        String title = map.get("title");
        String author = map.get("author");
        String editor = map.get("editor");
        String description = map.get("description");
        String text = new String(VerificationUtils.base64Decode(map.get("text").replace("b'", "")),
                CommonConstants.DEFAULT_CHARSET);
        String cover = map.get("cover");
        String tags = map.get("tags");
        //        String copied = map.get("copied");
        String copied = "on";
        String source = map.get("source");
        String importDateStr = map.get("importDateStr");

        if (CommonUtils.empty(title) || CommonUtils.empty(description) || CommonUtils.empty(text) || CommonUtils
                .empty(importDateStr)) {
            return JsonResp.fail("缺少部分内容title|author|description|text|importDateStr");
        }

        if (CommonUtils.empty(author)) {
            author = source;
        }
        if (CommonUtils.empty(editor)) {
            editor = source;
        }

        Date date = DateUtil.strToDateLong(importDateStr);
        // 插入文章
        try {
            CmsContent cmsContent = createContent(cmsCategory.getId(), IMPORT_DEFAULT_VALUE_CONTENT_MODELID, title,
                    author, editor, description, cover, site, admin, date);
            CmsContentAttribute cmsContentAttribute = createCmsContentAttribute(text, copied, source, sourceUrl);
            CmsContentParameters cmsContentParameters = createCmsContentParameters(tags);
            saveContent(site, admin, cmsContent, cmsContentAttribute, cmsContentParameters);
        } catch (Exception e) {
            return JsonResp.fail(MessageFormat.format("插入文章出现错误，原因是：{0}", e.getMessage()));
        }
        return JsonResp.success();
    }

    /**
     * 导入标签
     *
     * @param site    站点
     * @param admin   用户
     * @param file    导入文件
     * @param request 请求
     * @return 错误列表
     */
    private List<String> importCmsTag(SysSite site, SysUser admin, MultipartFile file, HttpServletRequest request) {
        List<String> resultList = new ArrayList<>();
        InputStream fileInputStream;
        List<List<Object>> importDataList;
        try {
            fileInputStream = file.getInputStream();
            importDataList = ExcelUtil.mGetBankListByExcel(fileInputStream, file.getOriginalFilename());
        } catch (Exception e) {
            resultList.add(MessageFormat.format("载入文件时出现错误，错误原因：{0}", e.getMessage()));
            return resultList;
        }
        // 循环插入
        for (int i = 0; i < importDataList.size(); i++) {
            List<Object> rowObj = importDataList.get(i);
            try {
                String name = String.valueOf(rowObj.get(0));
                if (!saveTagByName(site, name)) {
                    resultList.add(MessageFormat
                            .format("第{0}行插入时发现数据已存在，不进行操作", i + IMPORT_DEFAULT_VALUE_IGNORE_ROW_COUNT));
                }
            } catch (Exception e) {
                resultList.add(MessageFormat
                        .format("第{0}行插入时出现错误，错误原因：{1}", i + IMPORT_DEFAULT_VALUE_IGNORE_ROW_COUNT, e.getMessage()));
            }
        }
        // 保存日志
        logOperateService
                .save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "import.tag",
                        RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                        JsonUtils.getString(importDataList)));
        IOUtils.closeQuietly(fileInputStream);
        return resultList;
    }

    /**
     * 导入分类
     *
     * @param site    站点
     * @param admin   用户
     * @param file    导入文件
     * @param request 请求
     * @return 错误列表
     */
    private List<String> importCmsCategory(SysSite site, SysUser admin, MultipartFile file,
            HttpServletRequest request) {
        List<String> resultList = new ArrayList<>();
        InputStream fileInputStream;
        List<List<Object>> importDataList;
        try {
            fileInputStream = file.getInputStream();
            importDataList = ExcelUtil.mGetBankListByExcel(fileInputStream, file.getOriginalFilename());
        } catch (Exception e) {
            resultList.add(MessageFormat.format("载入文件时出现错误，错误原因：{0}<br />", e.getMessage()));
            return resultList;
        }
        // 循环插入
        for (int i = 0; i < importDataList.size(); i++) {
            List<Object> rowObj = importDataList.get(i);
            try {
                String name = String.valueOf(rowObj.get(0));
                String code = String.valueOf(rowObj.get(1));
                String templatePath = String.valueOf(rowObj.get(2));
                String path = String.valueOf(rowObj.get(3));
                String contentPath = String.valueOf(rowObj.get(4));
                String title = String.valueOf(rowObj.get(5));
                String keywords = String.valueOf(rowObj.get(6));
                String description = String.valueOf(rowObj.get(7));
                String tagTypes = String.valueOf(rowObj.get(8));
                String parentCategory = String.valueOf(rowObj.get(9));

                CmsCategory cmsCategoryByName = getCmsCategoryByName(name, site);
                if (cmsCategoryByName != null) {
                    throw new Exception(MessageFormat.format("已存在名称为{0}的分类<br />", name));
                }

                cmsCategoryByName = getCmsCategoryByCode(code, site);
                if (cmsCategoryByName != null) {
                    throw new Exception(MessageFormat.format("已存在编码为{0}的分类<br />", code));
                }

                // 获取父分类
                cmsCategoryByName = getCmsCategoryByName(parentCategory, site);
                Integer parentCategoryId = cmsCategoryByName == null ? null : cmsCategoryByName.getId();

                CmsCategory cmsCategory = createCategory(name, code, templatePath, path, contentPath, parentCategoryId);
                CmsCategoryAttribute cmsCategoryAttribute = createCmsCategoryAttribute(title, keywords, description);
                CmsCategoryParameters cmsCategoryParameters = createCmsCategoryParameters(tagTypes);

                if (!saveCategory(site, admin, cmsCategory, cmsCategoryAttribute, cmsCategoryParameters, request)) {
                    resultList.add(MessageFormat
                            .format("第{0}行插入时发现数据已存在，不进行操作<br />", i + IMPORT_DEFAULT_VALUE_IGNORE_ROW_COUNT));
                }
            } catch (Exception e) {
                resultList.add(MessageFormat
                        .format("第{0}行插入时出现错误，错误原因：{1} <br />", i + IMPORT_DEFAULT_VALUE_IGNORE_ROW_COUNT,
                                e.getMessage()));
            }
        }

        // 保存日志
        logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                "import.category", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                JsonUtils.getString(importDataList)));
        IOUtils.closeQuietly(fileInputStream);

        return resultList;
    }

    /**
     * 导入分类
     *
     * @param site    站点
     * @param admin   用户
     * @param file    导入文件
     * @param request 请求
     * @return 错误列表
     */
    private List<String> importCmsContent(SysSite site, SysUser admin, MultipartFile file, HttpServletRequest request) {
        List<String> resultList = new ArrayList<>();
        InputStream fileInputStream;
        List<List<Object>> importDataList;
        try {
            fileInputStream = file.getInputStream();
            importDataList = ExcelUtil.mGetBankListByExcel(fileInputStream, file.getOriginalFilename());
        } catch (Exception e) {
            resultList.add(MessageFormat.format("载入文件时出现错误，错误原因：{0}<br />", e.getMessage()));
            return resultList;
        }
        // 循环插入
        for (int i = 0; i < importDataList.size(); i++) {
            List<Object> rowObj = importDataList.get(i);
            try {
                String categoryName = String.valueOf(rowObj.get(0));
                String title = String.valueOf(rowObj.get(1));
                String author = String.valueOf(rowObj.get(2));
                String editor = String.valueOf(rowObj.get(3));
                String description = String.valueOf(rowObj.get(4));
                String text = String.valueOf(rowObj.get(5));
                String cover = String.valueOf(rowObj.get(6));
                String tags = String.valueOf(rowObj.get(7));
                String copied = String.valueOf(rowObj.get(8));
                String source = String.valueOf(rowObj.get(9));
                String sourceUrl = String.valueOf(rowObj.get(10));

                CmsCategory cmsCategoryByName = getCmsCategoryByName(categoryName, site);
                if (cmsCategoryByName == null) {
                    throw new Exception("找不到该名称的分类<br />");
                }

                CmsContent cmsContent = createContent(cmsCategoryByName.getId(), IMPORT_DEFAULT_VALUE_CONTENT_MODELID,
                        title, author, editor, description, cover, site, admin, new Date());
                CmsContentAttribute cmsContentAttribute = createCmsContentAttribute(text, copied, source, sourceUrl);
                CmsContentParameters cmsContentParameters = createCmsContentParameters(tags);

                saveContent(site, admin, cmsContent, cmsContentAttribute, cmsContentParameters);
            } catch (Exception e) {
                resultList.add(MessageFormat
                        .format("第{0}行插入时出现错误，错误原因：{1}<br />", i + IMPORT_DEFAULT_VALUE_IGNORE_ROW_COUNT,
                                e.getMessage()));
            }
        }

        // 保存日志
        logOperateService
                .save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "import.content",
                        RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                        JsonUtils.getString(importDataList)));
        IOUtils.closeQuietly(fileInputStream);

        return resultList;
    }

    /***************************************************导入内容************************************************************/

    /**
     * 获取CmsContentParameters
     *
     * @param tags tags字符串
     * @return CmsContentParameters
     */
    private CmsContentParameters createCmsContentParameters(String tags) {
        List<CmsTag> tagsFromStrSplit = getTagsFromStrSplit(tags);
        CmsContentParameters contentParameters = new CmsContentParameters();
        contentParameters.setTags(tagsFromStrSplit);
        return contentParameters;
    }

    /**
     * 创建正文及相关字段
     *
     * @param text      正文
     * @param copied    是否转载，on/off
     * @param source    来源
     * @param sourceUrl 来源地址
     * @return CmsContentAttribute
     */
    private CmsContentAttribute createCmsContentAttribute(String text, String copied, String source, String sourceUrl) {
        CmsContentAttribute attribute = new CmsContentAttribute();
        attribute.setText(text);
        if ("on".equals(copied)) {
            attribute.setSource(source);
            attribute.setSourceUrl(sourceUrl);
        }
        return attribute;
    }

    /**
     * 创建文章实体
     *
     * @param categoryId  分类id
     * @param modelId     文章类型
     * @param title       标题
     * @param author      作者
     * @param description 描述
     * @param cover       封面
     * @param sysSite     站点
     * @param admin       用户
     * @return 文章实体
     */
    private CmsContent createContent(int categoryId, String modelId, String title, String author, String editor,
            String description, String cover, SysSite sysSite, SysUser admin, Date importDate) {
        CmsContent cmsContent = new CmsContent();
        cmsContent.setCategoryId(categoryId);
        cmsContent.setModelId(modelId);
        cmsContent.setTitle(title);
        cmsContent.setAuthor(author);
        cmsContent.setEditor(editor);
        cmsContent.setDescription(description);
        cmsContent.setCover(cover);
        cmsContent.setSiteId(sysSite.getId());
        cmsContent.setUserId(admin.getId());
        cmsContent.setCreateDate(new Date());
//        cmsContent.setImportDate(importDate);
        cmsContent.setUpdateDate(new Date());
        return cmsContent;
    }

    /**
     * 保存文章
     *
     * @param site                 站点
     * @param admin                用户
     * @param cmsContent           文章
     * @param cmsContentAttribute  正文
     * @param cmsContentParameters 标签等信息
     * @return 是否成功插入
     */
    private boolean saveContent(SysSite site, SysUser admin, CmsContent cmsContent,
            CmsContentAttribute cmsContentAttribute, CmsContentParameters cmsContentParameters) {
        CmsCategoryModel categoryModel = categoryModelService
                .getEntity(new CmsCategoryModelId(cmsContent.getCategoryId(), cmsContent.getModelId()));// 获取分类
        CmsCategory category = categoryService.getEntity(cmsContent.getCategoryId());// 获取分类完整信息
        CmsModel cmsModel = modelComponent.getMap(site).get(cmsContent.getModelId());
        Date now = CommonUtils.getDate();
        initContent(cmsContent, cmsModel, IMPORT_DEFAULT_VALUE_CONTENT_DRAFT, IMPORT_DEFAULT_VALUE_CONTENT_CHECKED,
                cmsContentAttribute, now, IMPORT_DEFAULT_VALUE_CONTENT_TEXT_NEED_DECIPHER);

        // 保存
        cmsContentService.save(cmsContent);
        if (CommonUtils.notEmpty(cmsContent.getParentId())) {
            cmsContentService.updateChilds(cmsContent.getParentId(), 1);
        }

        // 保存关联表
        cmsContentService
                .saveTagAndAttribute(site.getId(), admin.getId(), cmsContent.getId(), cmsContentParameters, cmsModel,
                        category, cmsContentAttribute);
        templateComponent.createContentFile(site, cmsContent, category, categoryModel);// 静态化
        if (null == cmsContent.getParentId() && !cmsContent.isOnlyUrl()) {
            cmsContentService
                    .quote(site.getId(), cmsContent, cmsContentParameters.getContentIds(), cmsContentParameters,
                            cmsModel, category, cmsContentAttribute);
            Set<Integer> categoryIdsList = cmsContentParameters.getCategoryIds();
            if (CommonUtils.notEmpty(categoryIdsList)) {
                categoryIdsList.remove(cmsContent.getCategoryId());
                cmsContent = cmsContentService.getEntity(cmsContent.getId());
                for (Integer tmpCategoryId : categoryIdsList) {
                    CmsCategory newCategory = categoryService.getEntity(tmpCategoryId);
                    if (null != newCategory) {
                        CmsContent quote = new CmsContent(cmsContent.getSiteId(), cmsContent.getTitle(),
                                cmsContent.getUserId(), tmpCategoryId, cmsContent.getModelId(), cmsContent.isCopied(),
                                true, cmsContent.isHasImages(), cmsContent.isHasFiles(), false, 0, 0, 0, 0,
                                cmsContent.getPublishDate(), cmsContent.getCreateDate(), 0,
                                cmsContent.getStatus(), false);
                        quote.setQuoteContentId(cmsContent.getId());
                        quote.setDescription(cmsContent.getDescription());
                        quote.setAuthor(cmsContent.getAuthor());
                        quote.setCover(cmsContent.getCover());
                        quote.setEditor(cmsContent.getEditor());
                        quote.setExpiryDate(cmsContent.getExpiryDate());
                        cmsContentService.save(quote);
                        cmsContentService
                                .saveTagAndAttribute(site.getId(), admin.getId(), quote.getId(), cmsContentParameters,
                                        cmsModel, category, cmsContentAttribute);
                    }
                }
            }
        }
        return true;
    }

    /**
     * 根据分类名称获取分类，
     *
     * @param categoryName 分类名
     * @param sysSite      站点
     * @return 找到的分类实体
     */
    private CmsCategory getCmsCategoryByName(String categoryName, SysSite sysSite) {
        CmsCategory cmsCategory = new CmsCategory();
        cmsCategory.setSiteId(sysSite.getId());
        cmsCategory.setName(categoryName);
        return categoryService.findByEntity(cmsCategory);
    }

    /**
     * 根据分类code获取分类，
     *
     * @param categoryCode 分类编码
     * @param sysSite      站点
     * @return 找到的分类实体
     */
    private CmsCategory getCmsCategoryByCode(String categoryCode, SysSite sysSite) {
        CmsCategory cmsCategory = new CmsCategory();
        cmsCategory.setSiteId(sysSite.getId());
        cmsCategory.setCode(categoryCode);
        return categoryService.findByEntity(cmsCategory);
    }

    /**
     * 文章--初始化文章内容
     *
     * @param entity           文章
     * @param cmsModel         cmsModel
     * @param draft            草稿
     * @param checked          审核
     * @param attribute        包含正文
     * @param now              时间
     * @param textNeedDecipher 正文是否需要解密
     */
    public static void initContent(CmsContent entity, CmsModel cmsModel, Boolean draft, Boolean checked,
            CmsContentAttribute attribute, Date now, boolean textNeedDecipher) {
        entity.setHasFiles(cmsModel.isHasFiles());
        entity.setHasImages(cmsModel.isHasImages());
        entity.setOnlyUrl(cmsModel.isOnlyUrl());
        if ((null == checked || !checked) && null != draft && draft) {
            entity.setStatus(CmsContentService.STATUS_DRAFT);
        } else {
            entity.setStatus(CmsContentService.STATUS_PEND);
        }
        if (null == entity.getPublishDate()) {
            entity.setPublishDate(now);
        }
        if (null != attribute.getText() && textNeedDecipher) {
            attribute.setText(
                    new String(VerificationUtils.base64Decode(attribute.getText()), CommonConstants.DEFAULT_CHARSET));
        }
    }

    /***************************************************导入分类************************************************************/

    private CmsCategory createCategoryOnlyName(SysSite site, SysUser admin, String categoryName) throws Exception {
        String name = categoryName;
        String code = CodeUtil.createRandomChar();
        String templatePath = IMPORT_DEFAULT_VALUE_CATEGORY_TEMPLATE_PATH;
        String path = IMPORT_DEFAULT_VALUE_CATEGORY_PATH;
        String contentPath = IMPORT_DEFAULT_VALUE_CATEGORY_CONTENT_PATH;
        String title = "标题" + name;
        String keywords = "关键词" + name;
        String description = "描述" + name;
        String tagTypes = "默认标签";
        Integer parentCategoryId = null;

        CmsCategory cmsCategoryByName = getCmsCategoryByName(name, site);
        if (cmsCategoryByName != null) {
            return cmsCategoryByName;
        }
        CmsCategory cmsCategory = createCategory(name, code, templatePath, path, contentPath, parentCategoryId);
        try {
            CmsCategoryAttribute cmsCategoryAttribute = createCmsCategoryAttribute(title, keywords, description);
            CmsCategoryParameters cmsCategoryParameters = createCmsCategoryParameters(tagTypes);
            saveCategory(site, admin, cmsCategory, cmsCategoryAttribute, cmsCategoryParameters, null);
        } catch (Exception e) {
            throw new Exception(MessageFormat.format("插入分类{0}时出错,原因是：{1}", name, e.getMessage()));
        }
        return cmsCategory;

    }

    /**
     * 创建分类对象
     *
     * @param name         名称
     * @param code         编码
     * @param templatePath 静态页面
     * @param path         动态地址
     * @param contentPath  文章地址
     * @param parentId     父分类id
     * @return 分类对象
     */
    private CmsCategory createCategory(String name, String code, String templatePath, String path, String contentPath,
            Integer parentId) {
        CmsCategory cmsCategory = new CmsCategory();
        cmsCategory.setName(name);
        cmsCategory.setCode(code);
        cmsCategory.setTemplatePath(templatePath);
        cmsCategory.setPath(path);
        cmsCategory.setContentPath(contentPath);

        // 下面使用默认内容
        cmsCategory.setPageSize(IMPORT_DEFAULT_VALUE_CATEGORY_PAGE_SIZE);
        cmsCategory.setSort(IMPORT_DEFAULT_VALUE_CATEGORY_SORT);
        cmsCategory.setContainChild(true);
        cmsCategory.setAllowContribute(true);
        cmsCategory.setHasStatic(true);
        cmsCategory.setParentId(parentId);

        return cmsCategory;
    }

    /**
     * 创建分类你的seo对象
     *
     * @param title       标题
     * @param keywords    关键词
     * @param description 描述
     * @return CmsCategoryAttribute
     */
    private CmsCategoryAttribute createCmsCategoryAttribute(String title, String keywords, String description) {
        // SEO
        CmsCategoryAttribute cmsCategoryAttribute = new CmsCategoryAttribute();
        cmsCategoryAttribute.setTitle(title);
        cmsCategoryAttribute.setKeywords(keywords);
        cmsCategoryAttribute.setDescription(description);
        return cmsCategoryAttribute;
    }

    /**
     * 创建发表内容的字段
     *
     * @param tagTypes 标签类型数组
     * @return CmsCategoryParameters
     */
    private CmsCategoryParameters createCmsCategoryParameters(String tagTypes) {
        // CmsCategoryParameters
        CmsCategoryParameters cmsCategoryParameters = new CmsCategoryParameters();
        if (CommonUtils.notEmpty(tagTypes)) {
            String[] parameters = StringUtils.split(tagTypes, CommonConstants.COMMA_DELIMITED);
            List<String> strings = Arrays.asList(parameters);
            ArrayList<CmsTagType> cmsTagTypes = new ArrayList<>();
            strings.forEach(string -> {
                CmsTagType cmsTagType = new CmsTagType();
                cmsTagType.setName(string);
                cmsTagTypes.add(cmsTagType);
            });
            cmsCategoryParameters.setTagTypes(cmsTagTypes);
        }
        ArrayList<CmsCategoryModelParameters> cmsCategoryModelParametersList = new ArrayList<>();
        CmsCategoryModelParameters cmsCategoryModelParameters = createCmsCategoryModelParameters(0, "1",
                "/system/article.html", true);
        CmsCategoryModelParameters cmsCategoryModelParameters2 = createCmsCategoryModelParameters(0, "2", "", true);
        CmsCategoryModelParameters cmsCategoryModelParameters3 = createCmsCategoryModelParameters(0, "3",
                "/system/picture.html", true);
        CmsCategoryModelParameters cmsCategoryModelParameters4 = createCmsCategoryModelParameters(0, "4",
                "/system/book.html", true);
        CmsCategoryModelParameters cmsCategoryModelParameters5 = createCmsCategoryModelParameters(0, "5", "", true);
        CmsCategoryModelParameters cmsCategoryModelParameters6 = createCmsCategoryModelParameters(0, "6",
                "/system/chapter.html", true);
        cmsCategoryModelParametersList.add(cmsCategoryModelParameters);
        cmsCategoryModelParametersList.add(cmsCategoryModelParameters2);
        cmsCategoryModelParametersList.add(cmsCategoryModelParameters3);
        cmsCategoryModelParametersList.add(cmsCategoryModelParameters4);
        cmsCategoryModelParametersList.add(cmsCategoryModelParameters5);
        cmsCategoryModelParametersList.add(cmsCategoryModelParameters6);

        cmsCategoryParameters.setCategoryModelList(cmsCategoryModelParametersList);
        return cmsCategoryParameters;
    }

    /**
     * 创建能发表的文章类型
     *
     * @param categoryId   分类id
     * @param modelId      模型id
     * @param templatePath 地址
     * @param use          是否使用
     * @return CmsCategoryModelParameters
     */
    private CmsCategoryModelParameters createCmsCategoryModelParameters(int categoryId, String modelId,
            String templatePath, boolean use) {
        CmsCategoryModelId cmsCategoryModelId = new CmsCategoryModelId();
        cmsCategoryModelId.setCategoryId(categoryId);
        cmsCategoryModelId.setModelId(modelId);

        CmsCategoryModel cmsCategoryModel = new CmsCategoryModel();
        cmsCategoryModel.setId(cmsCategoryModelId);
        cmsCategoryModel.setTemplatePath(templatePath);

        CmsCategoryModelParameters cmsCategoryModelParameters = new CmsCategoryModelParameters();
        cmsCategoryModelParameters.setUse(use);
        cmsCategoryModelParameters.setCategoryModel(cmsCategoryModel);
        return cmsCategoryModelParameters;
    }

    /**
     * 保存分类
     *
     * @param site               站点
     * @param admin              用户
     * @param cmsCategory        分类
     * @param attribute          seo信息
     * @param categoryParameters 标签和能发布类型
     * @param request            请求
     * @return 成功返回true
     */
    private boolean saveCategory(SysSite site, SysUser admin, CmsCategory cmsCategory, CmsCategoryAttribute attribute,
            CmsCategoryParameters categoryParameters, HttpServletRequest request) {
        categoryService.save(site.getId(), cmsCategory);
        categoryService.saveTagAndAttribute(site.getId(), cmsCategory.getId(), attribute, categoryParameters);
        return true;
    }

    /**
     * 获取tag数组，通过逗号分割的字符串
     *
     * @param tags 逗号分割的字符串
     * @return tag数组
     */
    private List<CmsTag> getTagsFromStrSplit(String tags) {

        String[] parameters = StringUtils.split(tags, CommonConstants.COMMA_DELIMITED);
        List<String> strings = Arrays.asList(parameters);
        ArrayList<CmsTag> cmsTags = new ArrayList<>();
        strings.forEach(string -> {
            CmsTag cmsTag = new CmsTag();
            cmsTag.setName(string);
            cmsTags.add(cmsTag);
        });
        return cmsTags;
    }

    /***************************************************导入标签************************************************************/
    /**
     * 保存标签
     *
     * @param site 站点
     * @param name 标签名称
     */
    private boolean saveTagByName(SysSite site, String name) {
        CmsTag cmsTag = new CmsTag();
        cmsTag.setSiteId(site.getId());
        cmsTag.setName(name);
        CmsTag byTag = cmsTagService.findByTag(cmsTag);
        if (byTag != null) {
            return false;
        }
        cmsTagService.save(cmsTag);
        return true;
    }

}