package com.publiccms.logic.service.cms;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.HtmlUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentLang;
import com.publiccms.entities.cms.CmsEditorHistory;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.dao.cms.CmsContentLangDao;
import com.publiccms.logic.service.sys.SysExtendFieldService;
import com.publiccms.logic.service.sys.SysExtendService;
import com.publiccms.views.pojo.entities.CmsModel;
import com.publiccms.views.pojo.model.CmsContentLangListParameters;
import com.publiccms.views.pojo.model.CmsContentLangParameters;

import jakarta.annotation.Resource;

/**
 *
 * CmsContentLangService
 * 
 */
@Service
@Transactional
public class CmsContentLangService extends BaseService<CmsContentLang> {
    private String[] ignoreProperties = new String[] { "id" };
    @Resource
    private SysExtendService extendService;
    @Resource
    private SysExtendFieldService extendFieldService;
    @Resource
    private CmsContentFileService contentFileService;
    @Resource
    private CmsEditorHistoryService editorHistoryService;

    public List<CmsContentLang> getList(Long contentId) {
        return dao.getList(contentId);
    }

    public void save(SysSite site, Long userId, CmsContent content, CmsContentLangListParameters contentLangListParameters,
            CmsModel cmsModel, Integer extendId) {
        if (null != contentLangListParameters && null != contentLangListParameters.getContentLangList()) {
            for (CmsContentLangParameters langParameter : contentLangListParameters.getContentLangList()) {
                CmsContentLang entity = langParameter.getEntity();
                entity.getId().setContentId(content.getId());
                if (content.isHasImages() || content.isHasFiles()) {
                    contentFileService.update(entity.getId().getContentId(), entity.getId().getLang(), userId,
                            content.isHasFiles() ? langParameter.getFiles() : null,
                            content.isHasImages() ? langParameter.getImages() : null);// 更新保存图集，附件
                }

                entity.setText(HtmlUtils.cleanUnsafeHtml(
                        new String(VerificationUtils.base64Decode(entity.getText()), StandardCharsets.UTF_8),
                        site.getSitePath()));

                List<SysExtendField> modelExtendList = cmsModel.getExtendList();
                List<SysExtendField> categoryExtendList = null;
                if (null != extendId && null != extendService.getEntity(extendId)) {
                    categoryExtendList = extendFieldService.getList(extendId, null, null);
                }
                dealAttribute(entity, site, modelExtendList, categoryExtendList, langParameter.getExtendData());

                CmsContentLang oldEntity = getEntity(entity.getId());
                if (null != oldEntity) {
                    update(entity.getId(), entity, ignoreProperties);
                } else {
                    save(entity);
                }

                saveEditorHistory(getEntity(entity.getId()), entity, site.getId(), entity.getId().getContentId(),
                        entity.getId().getLang(), userId, modelExtendList, categoryExtendList, langParameter.getExtendData());// 保存编辑器字段历史记录
            }
        }
    }

    /**
     * @param id
     * @param url
     * @return result
     */
    public CmsContentLang updateUrl(Serializable id, String url) {
        CmsContentLang entity = getEntity(id);
        if (null != entity) {
            entity.setUrl(url);
        }
        return entity;
    }

    private void dealAttribute(CmsContentLang entity, SysSite site, List<SysExtendField> modelExtendList,
            List<SysExtendField> categoryExtendList, Map<String, String> map) {
        String text = HtmlUtils.removeHtmlTag(entity.getText());
        if (null != text) {
            if (CommonUtils.empty(entity.getDescription())) {
                entity.setDescription(CommonUtils.keep(text, 300));
            }
        }

        if (CommonUtils.notEmpty(map)) {
            entity.setData(ExtendUtils.getExtendString(map, site.getSitePath(), modelExtendList, categoryExtendList));
        } else {
            entity.setData(null);
        }
    }

    private void saveEditorHistory(CmsContentLang oldAttribute, CmsContentLang attribute, short siteId, long contentId,
            String lang, long userId, List<SysExtendField> modelExtendList, List<SysExtendField> categoryExtendList,
            Map<String, String> map) {
        if (null != oldAttribute) {
            if (CommonUtils.notEmpty(oldAttribute.getText()) && !oldAttribute.getText().equals(attribute.getText())) {
                CmsEditorHistory history = new CmsEditorHistory(siteId, CmsEditorHistoryService.ITEM_TYPE_CONTENT,
                        String.valueOf(contentId), "text", lang, CommonUtils.now(), userId, oldAttribute.getText());
                editorHistoryService.save(history);
            }
            if (CommonUtils.notEmpty(oldAttribute.getData())) {
                Map<String, String> oldMap = ExtendUtils.getExtendMap(oldAttribute.getData());
                if (CommonUtils.notEmpty(modelExtendList)) {
                    editorHistoryService.saveHistory(siteId, userId, CmsEditorHistoryService.ITEM_TYPE_CONTENT_EXTEND,
                            String.valueOf(contentId), lang, oldMap, map, modelExtendList);
                }
                if (CommonUtils.notEmpty(categoryExtendList)) {
                    editorHistoryService.saveHistory(siteId, userId, CmsEditorHistoryService.ITEM_TYPE_CONTENT_EXTEND,
                            String.valueOf(contentId), lang, oldMap, map, categoryExtendList);
                }
            }
        }
    }

    @Resource
    private CmsContentLangDao dao;
}