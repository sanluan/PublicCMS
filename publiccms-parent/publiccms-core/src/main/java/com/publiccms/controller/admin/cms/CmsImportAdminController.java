package com.publiccms.controller.admin.cms;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExcelUtil;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsCategoryModelId;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.cms.CmsTag;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.views.pojo.entities.CmsModel;
import com.publiccms.views.pojo.model.CmsContentParameters;

/**
 * CmsContentController
 */
@Controller
@RequestMapping("cmsImport")
public class CmsImportAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Autowired
    private CmsContentService cmsContentService;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    private CmsCategoryService categoryService;
    @Autowired
    private CmsCategoryModelService categoryModelService;
    @Autowired
    private ModelComponent modelComponent;
    @Autowired
    private TemplateComponent templateComponent;

    /**
     * 忽视行数+1 (计算机从0开始)
     */
    private static final int IMPORT_DEFAULT_VALUE_IGNORE_ROW_COUNT = 3;

    private static final String IMPORT_DEFAULT_VALUE_CONTENT_MODELID = "1";
    private static final boolean IMPORT_DEFAULT_VALUE_CONTENT_DRAFT = false;
    private static final boolean IMPORT_DEFAULT_VALUE_CONTENT_CHECKED = false;
    private static final boolean IMPORT_DEFAULT_VALUE_CONTENT_TEXT_NEED_DECIPHER = false;

    @RequestMapping("upload")
    @ResponseBody
    public List<String> upload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile file,
            HttpServletRequest request) {
        List<String> resultList = importCmsContent(site, admin, file, request);
        return resultList;
    }

    /**
     * 导入分类
     *
     * @param site
     *            站点
     * @param admin
     *            用户
     * @param file
     *            导入文件
     * @param request
     *            请求
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
                Integer categoryId = Integer.valueOf((String) rowObj.get(0));
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
                CmsContent cmsContent = createContent(categoryId, IMPORT_DEFAULT_VALUE_CONTENT_MODELID, title,
                        author, editor, description, cover, site, admin);
                CmsContentAttribute cmsContentAttribute = createCmsContentAttribute(text, copied, source, sourceUrl);
                CmsContentParameters cmsContentParameters = createCmsContentParameters(tags);

                saveContent(site, admin, cmsContent, cmsContentAttribute, cmsContentParameters);
            } catch (Exception e) {
                resultList.add(MessageFormat.format("第{0}行插入时出现错误，错误原因：{1}<br />", i + IMPORT_DEFAULT_VALUE_IGNORE_ROW_COUNT,
                        e.getMessage()));
            }
        }

        // 保存日志
        logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "import.content",
                RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(importDataList)));
        IOUtils.closeQuietly(fileInputStream);

        return resultList;
    }

    /***************************************************
     * 导入内容
     ************************************************************/

    /**
     * 获取CmsContentParameters
     *
     * @param tags
     *            tags字符串
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
     * @param text
     *            正文
     * @param copied
     *            是否转载，on/off
     * @param source
     *            来源
     * @param sourceUrl
     *            来源地址
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
     * @param categoryId
     *            分类id
     * @param modelId
     *            文章类型
     * @param title
     *            标题
     * @param author
     *            作者
     * @param description
     *            描述
     * @param cover
     *            封面
     * @param sysSite
     *            站点
     * @param admin
     *            用户
     * @return 文章实体
     */
    private CmsContent createContent(int categoryId, String modelId, String title, String author, String editor,
            String description, String cover, SysSite sysSite, SysUser admin) {
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
        return cmsContent;
    }

    /**
     * 保存文章
     *
     * @param site
     *            站点
     * @param admin
     *            用户
     * @param cmsContent
     *            文章
     * @param cmsContentAttribute
     *            正文
     * @param cmsContentParameters
     *            标签等信息
     * @return 是否成功插入
     */
    private boolean saveContent(SysSite site, SysUser admin, CmsContent cmsContent, CmsContentAttribute cmsContentAttribute,
            CmsContentParameters cmsContentParameters) {
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
        cmsContentService.saveTagAndAttribute(site.getId(), admin.getId(), cmsContent.getId(), cmsContentParameters, cmsModel,
                category, cmsContentAttribute);
        templateComponent.createContentFile(site, cmsContent, category, categoryModel);// 静态化
        if (null == cmsContent.getParentId() && !cmsContent.isOnlyUrl()) {
            cmsContentService.quote(site.getId(), cmsContent, cmsContentParameters.getContentIds(), cmsContentParameters,
                    cmsModel, category, cmsContentAttribute);
            Set<Integer> categoryIdsList = cmsContentParameters.getCategoryIds();
            if (CommonUtils.notEmpty(categoryIdsList)) {
                categoryIdsList.remove(cmsContent.getCategoryId());
                cmsContent = cmsContentService.getEntity(cmsContent.getId());
                for (Integer tmpCategoryId : categoryIdsList) {
                    CmsCategory newCategory = categoryService.getEntity(tmpCategoryId);
                    if (null != newCategory) {
                        CmsContent quote = new CmsContent(cmsContent.getSiteId(), cmsContent.getTitle(), cmsContent.getUserId(),
                                tmpCategoryId, cmsContent.getModelId(), cmsContent.isCopied(), true, cmsContent.isHasImages(),
                                cmsContent.isHasFiles(), false, 0, 0, 0, 0, cmsContent.getPublishDate(),
                                cmsContent.getCreateDate(), 0, cmsContent.getStatus(), false);
                        quote.setQuoteContentId(cmsContent.getId());
                        quote.setDescription(cmsContent.getDescription());
                        quote.setAuthor(cmsContent.getAuthor());
                        quote.setCover(cmsContent.getCover());
                        quote.setEditor(cmsContent.getEditor());
                        quote.setExpiryDate(cmsContent.getExpiryDate());
                        cmsContentService.save(quote);
                        cmsContentService.saveTagAndAttribute(site.getId(), admin.getId(), quote.getId(), cmsContentParameters,
                                cmsModel, category, cmsContentAttribute);
                    }
                }
            }
        }
        return true;
    }

    /**
     * 文章--初始化文章内容
     *
     * @param entity
     *            文章
     * @param cmsModel
     *            cmsModel
     * @param draft
     *            草稿
     * @param checked
     *            审核
     * @param attribute
     *            包含正文
     * @param now
     *            时间
     * @param textNeedDecipher
     *            正文是否需要解密
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
            attribute.setText(new String(VerificationUtils.base64Decode(attribute.getText()), CommonConstants.DEFAULT_CHARSET));
        }
    }

    /**
     * 获取tag数组，通过逗号分割的字符串
     *
     * @param tags
     *            逗号分割的字符串
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

}