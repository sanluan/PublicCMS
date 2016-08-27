package com.publiccms.views.api;

import static com.publiccms.common.tools.ExtendUtils.getExtendMap;

//Generated 2015-5-10 17:54:56 by com.sanluan.common.source.SourceMaker

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppV1Directive;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.sanluan.common.handler.RenderHandler;

@Component
public class GetContentAttributeDirective extends AbstractAppV1Directive {

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        Long id = handler.getLong("id");
        CmsContentAttribute entity = service.getEntity(id);
        if (notEmpty(entity)) {
            Map<String, String> map = getExtendMap(entity.getData());
            map.put("text", entity.getText());
            map.put("source", entity.getSource());
            map.put("sourceUrl", entity.getSourceUrl());
            map.put("wordCount", String.valueOf(entity.getWordCount()));
            handler.put("attribute", map).render();
        }

    }

    @Autowired
    private CmsContentAttributeService service;

    @Override
    public boolean needUserToken() {
        return false;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

}