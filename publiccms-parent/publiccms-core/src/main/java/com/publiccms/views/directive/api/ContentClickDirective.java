package com.publiccms.views.directive.api;

//Generated 2015-5-10 17:54:56 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.views.pojo.entities.ClickStatistics;

/**
*
* contentClick 内容点击接口
* <p>
* 参数列表
* <ul>
* <li><code>id</code> 内容id
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>clicks</code> 内容点击数
* </ul>
* 使用示例
* <p>
* <pre>
&lt;script&gt;
$.getJSON('//cms.publiccms.com/api/contentClick?id=1', function(data){
  console.log(data.clicks);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class ContentClickDirective extends AbstractAppDirective {

    @Autowired
    private StatisticsComponent statisticsComponent;

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        Long id = handler.getLong("id");
        ClickStatistics contentStatistics = statisticsComponent.contentClicks(getSite(handler), id);
        if (null != contentStatistics) {
            handler.put("clicks", contentStatistics.getOldClicks() + contentStatistics.getClicks());
        }
        handler.render();
    }

    @Override
    public boolean needUserToken() {
        return false;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }

}