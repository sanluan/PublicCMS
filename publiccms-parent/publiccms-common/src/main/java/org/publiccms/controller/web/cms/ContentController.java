package org.publiccms.controller.web.cms;

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.ControllerUtils.redirectPermanently;
import static com.publiccms.common.tools.ControllerUtils.verifyCustom;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEmpty;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEquals;
import static com.publiccms.common.tools.HtmlUtils.removeHtmlTag;
import static com.publiccms.common.tools.JsonUtils.getString;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;
import static org.apache.commons.lang3.ArrayUtils.addAll;
import static org.publiccms.common.tools.ExtendUtils.getExtendString;
import static org.publiccms.common.tools.ExtendUtils.getExtentDataMap;
import static org.publiccms.common.tools.ExtendUtils.getSysExtentDataMap;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.cms.CmsCategory;
import org.publiccms.entities.cms.CmsCategoryModel;
import org.publiccms.entities.cms.CmsCategoryModelId;
import org.publiccms.entities.cms.CmsContent;
import org.publiccms.entities.cms.CmsContentAttribute;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysExtendField;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.entities.sys.SysUser;
import org.publiccms.logic.component.site.StatisticsComponent;
import org.publiccms.logic.component.template.ModelComponent;
import org.publiccms.logic.service.cms.CmsCategoryModelService;
import org.publiccms.logic.service.cms.CmsCategoryService;
import org.publiccms.logic.service.cms.CmsContentAttributeService;
import org.publiccms.logic.service.cms.CmsContentFileService;
import org.publiccms.logic.service.cms.CmsContentService;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.sys.SysExtendFieldService;
import org.publiccms.logic.service.sys.SysExtendService;
import org.publiccms.views.pojo.CmsContentParamters;
import org.publiccms.views.pojo.CmsContentRelatedStatistics;
import org.publiccms.views.pojo.CmsContentStatistics;
import org.publiccms.views.pojo.CmsModel;
import org.publiccms.views.pojo.ExtendField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * ContentController 内容
 *
 */
@Controller
@RequestMapping("content")
public class ContentController extends AbstractController {
    @Autowired
    private CmsContentService service;
    @Autowired
    private StatisticsComponent statisticsComponent;
    @Autowired
    private CmsCategoryModelService categoryModelService;
    @Autowired
    private CmsCategoryService categoryService;
    @Autowired
    private ModelComponent modelComponent;
    @Autowired
    private CmsContentAttributeService attributeService;
    @Autowired
    private SysExtendService extendService;
    @Autowired
    private SysExtendFieldService extendFieldService;
    @Autowired
    private CmsContentFileService contentFileService;

    private String[] ignoreProperties = new String[] { "siteId", "userId", "categoryId", "tagIds", "createDate", "clicks",
            "comments", "scores", "childs", "checkUserId" };

    private String[] ignorePropertiesWithUrl = addAll(ignoreProperties, new String[] { "url" });

    /**
     * 保存内容
     * 
     * @param entity
     * @param attribute
     * @param contentParamters
     * @param returnUrl
     * @param request
     * @param session
     * @param response
     * @param model
     * @return 
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(CmsContent entity, CmsContentAttribute attribute, @ModelAttribute CmsContentParamters contentParamters,
            String returnUrl, HttpServletRequest request, HttpSession session, HttpServletResponse response, ModelMap model) {
        SysSite site = getSite(request);
        if (empty(returnUrl)) {
            returnUrl = site.getDynamicPath();
        }
        SysUser user = getUserFromSession(session);
        CmsCategoryModel categoryModel = categoryModelService
                .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));
        if (verifyNotEmpty("categoryModel", categoryModel, model) || verifyCustom("contribute", null == user, model)) {
            return REDIRECT + returnUrl;
        }
        CmsCategory category = categoryService.getEntity(entity.getCategoryId());
        if (null != category && (site.getId() != category.getSiteId() || !category.isAllowContribute())) {
            category = null;
        }
        CmsModel cmsModel = modelComponent.getMap(site).get(entity.getModelId());
        if (verifyNotEmpty("category", category, model) || verifyNotEmpty("model", cmsModel, model)) {
            return REDIRECT + returnUrl;
        }
        entity.setHasFiles(cmsModel.isHasFiles());
        entity.setHasImages(cmsModel.isHasImages());
        entity.setOnlyUrl(cmsModel.isOnlyUrl());
        entity.setStatus(CmsContentService.STATUS_PEND);
        if (null != entity.getId()) {
            CmsContent oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return REDIRECT + returnUrl;
            }
            entity = service.update(entity.getId(), entity, entity.isOnlyUrl() ? ignoreProperties : ignorePropertiesWithUrl);
            if (null != entity.getId()) {
                logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "update.content",
                        getIpAddress(request), getDate(), getString(entity)));
            }
        } else {
            entity.setSiteId(site.getId());
            entity.setUserId(user.getId());
            service.save(entity);
            if (notEmpty(entity.getParentId())) {
                service.updateChilds(entity.getParentId(), 1);
            } else {
                categoryService.updateContents(entity.getCategoryId(), 1);
            }
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "save.content",
                    getIpAddress(request), getDate(), getString(entity)));
        }
        if (entity.isHasImages() || entity.isHasFiles()) {
            contentFileService.update(entity.getId(), user.getId(), entity.isHasFiles() ? contentParamters.getFiles() : null,
                    entity.isHasImages() ? contentParamters.getImages() : null);// 更新保存图集，附件
        }

        if (null != attribute.getText()) {
            attribute.setWordCount(removeHtmlTag(attribute.getText()).length());
        }
        List<ExtendField> modelExtendList = cmsModel.getExtendList();
        Map<String, String> map = getExtentDataMap(contentParamters.getModelExtendDataList(), modelExtendList);
        if (null != category && null != extendService.getEntity(category.getExtendId())) {
            List<SysExtendField> categoryExtendList = extendFieldService.getList(category.getExtendId());
            Map<String, String> categoryMap = getSysExtentDataMap(contentParamters.getCategoryExtendDataList(),
                    categoryExtendList);
            if (notEmpty(map)) {
                map.putAll(categoryMap);
            } else {
                map = categoryMap;
            }
        }

        if (notEmpty(map)) {
            attribute.setData(getExtendString(map));
        } else {
            attribute.setData(null);
        }
        attributeService.updateAttribute(entity.getId(), attribute);// 更新保存扩展字段，文本字段
        return REDIRECT + returnUrl;
    }

    /**
     * 内容推荐重定向并计数
     * 
     * @param id
     * @param request
     * @param response
     */
    @RequestMapping("related/redirect")
    public void relatedRedirect(Long id, HttpServletRequest request, HttpServletResponse response) {
        CmsContentRelatedStatistics contentRelatedStatistics = statisticsComponent.relatedClicks(id);
        SysSite site = getSite(request);
        if (null != contentRelatedStatistics && null != contentRelatedStatistics.getEntity()) {
            redirectPermanently(response, contentRelatedStatistics.getEntity().getUrl());
        } else {
            redirectPermanently(response, site.getDynamicPath());
        }
    }

    /**
     * 内容链接重定向并计数
     * 
     * @param id
     * @param request
     * @param response
     */
    @RequestMapping("redirect")
    public void contentRedirect(Long id, HttpServletRequest request, HttpServletResponse response) {
        CmsContentStatistics contentStatistics = statisticsComponent.clicks(id);
        SysSite site = getSite(request);
        if (null != contentStatistics && null != contentStatistics.getEntity()
                && site.getId() == contentStatistics.getEntity().getSiteId()) {
            redirectPermanently(response, contentStatistics.getEntity().getUrl());
        } else {
            redirectPermanently(response, site.getDynamicPath());
        }
    }

}
