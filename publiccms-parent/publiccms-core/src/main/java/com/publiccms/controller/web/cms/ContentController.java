package com.publiccms.controller.web.cms;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.HtmlUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsCategoryModelId;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentFileService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysExtendFieldService;
import com.publiccms.logic.service.sys.SysExtendService;
import com.publiccms.views.pojo.entities.CmsContentRelatedStatistics;
import com.publiccms.views.pojo.entities.CmsContentStatistics;
import com.publiccms.views.pojo.entities.CmsModel;
import com.publiccms.views.pojo.entities.ExtendField;
import com.publiccms.views.pojo.model.CmsContentParamters;

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

    private String[] ignorePropertiesWithUrl = ArrayUtils.addAll(ignoreProperties, new String[] { "url" });

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
     * @return view name
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(CmsContent entity, CmsContentAttribute attribute, @ModelAttribute CmsContentParamters contentParamters,
            String returnUrl, HttpServletRequest request, HttpSession session, HttpServletResponse response, ModelMap model) {
        SysSite site = getSite(request);
        if (CommonUtils.empty(returnUrl)) {
            returnUrl = site.getDynamicPath();
        }
        SysUser user = getUserFromSession(session);
        CmsCategoryModel categoryModel = categoryModelService
                .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));
        if (ControllerUtils.verifyNotEmpty("categoryModel", categoryModel, model)
                || ControllerUtils.verifyCustom("contribute", null == user, model)) {
            return REDIRECT + returnUrl;
        }
        CmsCategory category = categoryService.getEntity(entity.getCategoryId());
        if (null != category && (site.getId() != category.getSiteId() || !category.isAllowContribute())) {
            category = null;
        }
        CmsModel cmsModel = modelComponent.getMap(site).get(entity.getModelId());
        if (ControllerUtils.verifyNotEmpty("category", category, model)
                || ControllerUtils.verifyNotEmpty("model", cmsModel, model)) {
            return REDIRECT + returnUrl;
        }
        entity.setHasFiles(cmsModel.isHasFiles());
        entity.setHasImages(cmsModel.isHasImages());
        entity.setOnlyUrl(cmsModel.isOnlyUrl());
        entity.setStatus(CmsContentService.STATUS_PEND);
        if (null != entity.getId()) {
            CmsContent oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return REDIRECT + returnUrl;
            }
            entity = service.update(entity.getId(), entity, entity.isOnlyUrl() ? ignoreProperties : ignorePropertiesWithUrl);
            if (null != entity.getId()) {
                logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "update.content",
                        RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        } else {
            entity.setSiteId(site.getId());
            entity.setUserId(user.getId());
            service.save(entity);
            if (CommonUtils.notEmpty(entity.getParentId())) {
                service.updateChilds(entity.getParentId(), 1);
            }
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "save.content",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        if (entity.isHasImages() || entity.isHasFiles()) {
            contentFileService.update(entity.getId(), user.getId(), entity.isHasFiles() ? contentParamters.getFiles() : null,
                    entity.isHasImages() ? contentParamters.getImages() : null);// 更新保存图集，附件
        }

        if (null != attribute.getText()) {
            attribute.setWordCount(HtmlUtils.removeHtmlTag(attribute.getText()).length());
        }
        List<ExtendField> modelExtendList = cmsModel.getExtendList();
        Map<String, String> map = ExtendUtils.getExtentDataMap(contentParamters.getModelExtendDataList(), modelExtendList);
        if (null != category && null != extendService.getEntity(category.getExtendId())) {
            List<SysExtendField> categoryExtendList = extendFieldService.getList(category.getExtendId());
            Map<String, String> categoryMap = ExtendUtils.getSysExtentDataMap(contentParamters.getCategoryExtendDataList(),
                    categoryExtendList);
            if (CommonUtils.notEmpty(map)) {
                map.putAll(categoryMap);
            } else {
                map = categoryMap;
            }
        }

        if (CommonUtils.notEmpty(map)) {
            attribute.setData(ExtendUtils.getExtendString(map));
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
     * @return view name
     */
    @RequestMapping("related/redirect")
    public String relatedRedirect(Long id, HttpServletRequest request, HttpServletResponse response) {
        CmsContentRelatedStatistics contentRelatedStatistics = statisticsComponent.relatedClicks(id);
        SysSite site = getSite(request);
        if (null != contentRelatedStatistics && null != contentRelatedStatistics.getEntity()) {
            return REDIRECT + contentRelatedStatistics.getEntity().getUrl();
        } else {
            return REDIRECT + site.getDynamicPath();
        }
    }

    /**
     * 内容链接重定向并计数
     * 
     * @param id
     * @param request
     * @param response
     * @return view name
     */
    @RequestMapping("redirect")
    public String contentRedirect(Long id, HttpServletRequest request, HttpServletResponse response) {
        CmsContentStatistics contentStatistics = statisticsComponent.clicks(id);
        SysSite site = getSite(request);
        if (null != contentStatistics && null != contentStatistics.getEntity()
                && site.getId() == contentStatistics.getEntity().getSiteId()) {
            return REDIRECT + contentStatistics.getEntity().getUrl();
        } else {
            return REDIRECT + site.getDynamicPath();
        }
    }

    /**
     * 内容点击
     * 
     * @param id
     * @param response
     * @return click
     */
    @RequestMapping("click")
    @ResponseBody
    public int click(Long id, HttpServletResponse response) {
        CmsContentStatistics contentStatistics = statisticsComponent.clicks(id);
        if (null != contentStatistics && null != contentStatistics.getEntity()) {
            return contentStatistics.getEntity().getClicks() + contentStatistics.getClicks();
        }
        return 0;
    }

}
