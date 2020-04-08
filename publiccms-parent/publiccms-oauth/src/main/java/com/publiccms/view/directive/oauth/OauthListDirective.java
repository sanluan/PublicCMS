package com.publiccms.view.directive.oauth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.oauth.OauthGateway;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.sys.SysSite;

/**
 *
 * OauthListDirective
 * 
 */
@Component
public class OauthListDirective extends AbstractTemplateDirective {
    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        SysSite site = getSite(handler);
        List<String> list = new ArrayList<>();
        if (null != oauthList) {
            for (OauthGateway oauth : oauthList) {
                if (oauth.enabled(site.getId())) {
                    list.add(oauth.getChannel());
                }
            }
        }
        handler.put("list", list).render();
    }

    @Autowired(required = false)
    private List<OauthGateway> oauthList;
}
