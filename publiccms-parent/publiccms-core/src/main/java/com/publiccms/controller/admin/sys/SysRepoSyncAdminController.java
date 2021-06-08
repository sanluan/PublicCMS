package com.publiccms.controller.admin.sys;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.views.pojo.entities.SysConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Map;

/**
 * SysRepoSyncAdminController
 *
 * @author Qicz
 * @since 2021/6/4 13:59
 */
@RestController
@RequestMapping("sysRepoSync")
public class SysRepoSyncAdminController {

	@Autowired
	protected LogOperateService logOperateService;

	@GetMapping(value = "sync", produces = "text/html;charset=utf-8")
	public String sync(@RequestAttribute SysSite site, @SessionAttribute SysUser admin,
					   HttpServletRequest request, ModelMap model) {
		if (ControllerUtils.verifyCustom("noright", null != site.getParentId(), model)) {
			return CommonConstants.TEMPLATE_ERROR;
		}
		String log = null;
		try {
			Short siteId = site.getId();
			String dir = CommonConstants.CMS_FILEPATH + String.format("/template/site_%d/repo", siteId);
			String shPath = String.format("%s/sync.sh %d", dir, siteId);
			File shFile = new File(shPath);
			if (!shFile.exists()) {
				throw new FileNotFoundException(String.format("template/site_%d/sync.sh Not found, create it using 'Repo Sync template(sync.sh)'", siteId));
			}
			Process ps = Runtime.getRuntime().exec("sh " + shPath, null, new File(dir));
			ps.waitFor();

			BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("<br/>");
			}
			log = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			log = e.toString();
		} finally {
			logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "repo.sync",
					RequestUtils.getIpAddress(request), CommonUtils.getDate(), log));
		}
		return log;
	}
}
