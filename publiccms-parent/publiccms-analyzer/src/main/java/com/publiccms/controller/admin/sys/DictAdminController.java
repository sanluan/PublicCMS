package com.publiccms.controller.admin.sys;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.cn.smart.hhmm.DictionaryReloader;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.AnalyzerDictUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;

/**
 *
 * DictAdminController
 * 
 */
@Controller
@RequestMapping("dict")
public class DictAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    protected LogOperateService logOperateService;

    /**
     * @param site
     * @param admin
     * @param dict
     * @param skipWord
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String dict, String skipWord,
            HttpServletRequest request, ModelMap model) {
        if (ControllerUtils.errorCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        try {
            String dictDir = siteComponent.getRootPath() + AnalyzerDictUtils.DIR_DICT;
            File dictFile = new File(dictDir + AnalyzerDictUtils.TXT_DICT);
            FileUtils.writeStringToFile(dictFile, dict, CommonConstants.DEFAULT_CHARSET);
            Map<String, Integer> wordMap = new HashMap<>();
            for (String word : FileUtils.readLines(dictFile, CommonConstants.DEFAULT_CHARSET)) {
                if (!word.startsWith("#")) {
                    wordMap.put(word, 10);
                }
            }
            File skipWordFile = new File(dictDir + AnalyzerDictUtils.TXT_SKIPWORD);
            FileUtils.writeStringToFile(skipWordFile, skipWord, CommonConstants.DEFAULT_CHARSET);
            List<String> skipWordList = new ArrayList<>();
            for (String word : FileUtils.readLines(skipWordFile, CommonConstants.DEFAULT_CHARSET)) {
                if (!word.startsWith("#")) {
                    skipWordList.add(word);
                }
            }
            AnalyzerDictUtils.generate(dictDir, wordMap, skipWordList);
            DictionaryReloader.reload(dictDir);
        } catch (IOException | ClassNotFoundException e1) {
        }
        try {
            logOperateService
                    .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "save.dict", RequestUtils.getIpAddress(request), CommonUtils.getDate(), dict));
            return CommonConstants.TEMPLATE_DONE;
        } catch (IllegalStateException e) {
            log.error(e.getMessage(), e);
        }
        return CommonConstants.TEMPLATE_ERROR;
    }
}