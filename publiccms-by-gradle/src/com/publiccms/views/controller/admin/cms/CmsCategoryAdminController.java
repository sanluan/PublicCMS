package com.publiccms.views.controller.admin.cms;

import static com.publiccms.common.tools.ExtendUtils.getExtendString;
import static com.publiccms.common.tools.ExtendUtils.getSysExtentDataMap;
import static com.sanluan.common.tools.RequestUtils.getIpAddress;
import static com.sanluan.common.tools.RequestUtils.getValue;
import static org.apache.commons.lang3.StringUtils.join;
import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.entities.cms.CmsCategoryType;
import com.publiccms.entities.cms.CmsModel;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysExtend;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryAttributeService;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsCategoryTypeService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsModelService;
import com.publiccms.logic.service.cms.CmsTagTypeService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysExtendFieldService;
import com.publiccms.logic.service.sys.SysExtendService;
import com.publiccms.views.pojo.CmsCategoryParamters;

import freemarker.template.TemplateException;

/**
 * 
 * CmsCategoryController
 *
 */
@Controller
@RequestMapping("cmsCategory")
public class CmsCategoryAdminController extends AbstractController {
	@Autowired
	private CmsCategoryService service;
	@Autowired
	private CmsTagTypeService tagTypeService;
	@Autowired
	private CmsContentService contentService;
	@Autowired
	private CmsCategoryAttributeService attributeService;
	@Autowired
	private CmsModelService modelService;
	@Autowired
	private CmsCategoryModelService categoryModelService;
	@Autowired
	private CmsCategoryTypeService categoryTypeService;
	@Autowired
	private SysExtendService extendService;
	@Autowired
	private SysExtendFieldService extendFieldService;
	@Autowired
	private TemplateComponent templateComponent;

	/**
	 * @param entity
	 * @param attribute
	 * @param categoryParamters
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("save")
	public String save(CmsCategory entity, CmsCategoryAttribute attribute,
			@ModelAttribute CmsCategoryParamters categoryParamters, HttpServletRequest request, HttpSession session,
			ModelMap model) {
		SysSite site = getSite(request);
		if (notEmpty(entity.getId())) {
			CmsCategory oldEntity = service.getEntity(entity.getId());
			if (empty(oldEntity) || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
				return TEMPLATE_ERROR;
			}
			entity = service.update(entity.getId(), entity, new String[] { "siteId", "childIds", "tagTypeIds", "url",
					"disabled", "extendId", "contents", "typeId" });
			if (notEmpty(entity)) {
				if (notEmpty(oldEntity.getParentId()) && !oldEntity.getParentId().equals(entity.getParentId())) {
					service.generateChildIds(site.getId(), oldEntity.getParentId());
					service.generateChildIds(site.getId(), entity.getParentId());
				} else if (notEmpty(entity.getParentId()) && empty(oldEntity.getParentId())) {
					service.generateChildIds(site.getId(), entity.getParentId());
				}
				logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
						LogLoginService.CHANNEL_WEB_MANAGER, "update.category", getIpAddress(request), getDate(),
						entity.getId() + ":" + entity.getName()));
			}
		} else {
			if (entity.isOnlyUrl()) {
				entity.setUrl(entity.getPath());
			}
			entity.setSiteId(site.getId());
			service.save(entity);
			service.addChildIds(entity.getParentId(), entity.getId());
			logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
					LogLoginService.CHANNEL_WEB_MANAGER, "save.category", getIpAddress(request), getDate(),
					entity.getId() + ":" + entity.getName()));
		}
		if (empty(extendService.getEntity(entity.getExtendId()))) {
			entity = service.updateExtendId(entity.getId(),
					(Integer) extendService.save(new SysExtend("category", entity.getId())));
		}

		Integer[] tagTypeIds = tagTypeService.update(site.getId(), categoryParamters.getTagTypes());
		service.updateTagTypeIds(entity.getId(), arrayToCommaDelimitedString(tagTypeIds));// 更新保存标签分类

		Map<String, String[]> parameterMap = request.getParameterMap();
		@SuppressWarnings("unchecked")
		// 修改或增加分类与模型的映射
		List<CmsModel> modelList = (List<CmsModel>) modelService
				.getPage(site.getId(), null, null, null, null, null, false, null, null).getList();
		for (CmsModel cmsmodel : modelList) {
			categoryModelService.updateCategoryModel(notEmpty(getValue(parameterMap, "model_" + cmsmodel.getId())),
					entity.getId(), cmsmodel, parameterMap);
		}
		extendFieldService.update(entity.getExtendId(), categoryParamters.getContentExtends());// 修改或增加内容扩展字段

		CmsCategoryType categoryType = categoryTypeService.getEntity(entity.getTypeId());
		if (notEmpty(categoryType) && notEmpty(categoryType.getExtendId())) {
			@SuppressWarnings("unchecked")
			List<SysExtendField> categoryTypeExtendList = (List<SysExtendField>) extendFieldService
					.getPage(categoryType.getExtendId(), null, null).getList();
			Map<String, String> map = getSysExtentDataMap(categoryParamters.getExtendDataList(),
					categoryTypeExtendList);
			attribute.setData(getExtendString(map));
		} else {
			attribute.setData(null);
		}
		attributeService.updateAttribute(entity.getId(), attribute);
		try {
			publish(site, entity.getId(), null);
		} catch (IOException | TemplateException e) {
			verifyCustom("static", true, model);
		}
		return TEMPLATE_DONE;
	}

	/**
	 * @param ids
	 * @param parentId
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("move")
	public String move(Integer[] ids, Integer parentId, HttpServletRequest request, HttpSession session,
			ModelMap model) {
		SysSite site = getSite(request);
		CmsCategory parent = service.getEntity(parentId);
		if (notEmpty(ids) && (empty(parent) || notEmpty(parent) && site.getId() == parent.getSiteId())) {
			for (Integer id : ids) {
				move(site, id, parentId);
			}
			String parentName = empty(parent) ? "top" : parentId + ":" + parent.getName();
			logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), "move.category",
					getIpAddress(request), getDate(), join(ids, ',') + " to " + parentName));
		}
		return TEMPLATE_DONE;
	}

	/**
	 * @param siteId
	 * @param id
	 * @param parentId
	 */
	private void move(SysSite site, Integer id, Integer parentId) {
		CmsCategory entity = service.getEntity(id);
		if (notEmpty(entity) && site.getId() == entity.getSiteId()) {
			service.updateParentId(site.getId(), id, parentId);
			service.generateChildIds(site.getId(), entity.getParentId());
			if (notEmpty(parentId)) {
				service.generateChildIds(site.getId(), parentId);
			}
		}
	}

	/**
	 * @param ids
	 * @param max
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("publish")
	public String publish(Integer[] ids, Integer max, HttpServletRequest request, HttpSession session, ModelMap model) {
		SysSite site = getSite(request);
		if (notEmpty(ids)) {
			try {
				for (Integer id : ids) {
					publish(site, id, max);
				}
			} catch (IOException | TemplateException e) {
				verifyCustom("static", true, model);
				log.error(e.getMessage());
				return TEMPLATE_ERROR;
			}
			logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
					LogLoginService.CHANNEL_WEB_MANAGER, "static.category", getIpAddress(request), getDate(),
					join(ids, ',') + ",pageSize:" + (empty(max) ? 1 : max)));
		}
		return TEMPLATE_DONE;
	}

	/**
	 * @param siteId
	 * @param id
	 * @param max
	 * @throws IOException
	 * @throws TemplateException
	 */
	private void publish(SysSite site, Integer id, Integer max) throws IOException, TemplateException {
		CmsCategory entity = service.getEntity(id);
		if (notEmpty(site) && notEmpty(entity) && site.getId() == entity.getSiteId()) {
			templateComponent.createCategoryFile(site, entity, null, max);
		}
	}

	/**
	 * @param ids
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping("delete")
	public String delete(Integer[] ids, HttpServletRequest request, HttpSession session) {
		if (notEmpty(ids)) {
			SysSite site = getSite(request);
			service.delete(site.getId(), ids);
			contentService.deleteByCategoryIds(site.getId(), ids);
			logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
					LogLoginService.CHANNEL_WEB_MANAGER, "delete.category", getIpAddress(request), getDate(),
					join(ids, ',')));
		}
		return TEMPLATE_DONE;
	}
}