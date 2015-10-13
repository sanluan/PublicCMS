package com.publiccms.web.views.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.entities.cms.CmsContent;
import com.publiccms.logic.service.cms.CmsContentService;

/**
 * 
 * StatisticsController 统计
 *
 */
@Controller
public class StatisticsController {
    @Autowired
    private CmsContentService contentService;

    /**
     * 内容点击统计
     * 
     * @param id
     * @param callback
     * @return
     */
    @RequestMapping("content/clicks.json")
    @ResponseBody
    public MappingJacksonValue clicks(Integer id, String callback) {
        Map<String, Object> map = new HashMap<String, Object>();
        CmsContent content = contentService.updateClicks(id, 1);
        map.put("clicks", content.getClicks());
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(map);
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }

    /**
     * 内容评论统计
     * 
     * @param id
     * @param callback
     * @return
     */
    @RequestMapping("content/comments.json")
    @ResponseBody
    public MappingJacksonValue comments(Integer id, String callback) {
        Map<String, Object> map = new HashMap<String, Object>();
        CmsContent content = contentService.updateComments(id, 1);
        map.put("comments", content.getComments());
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(map);
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }
}
