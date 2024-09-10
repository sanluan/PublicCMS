package com.publiccms.views.directive.api;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysUser;

import freemarker.template.TemplateException;

/**
 *
 * upload 上传接口
 * <p>
 * 认证头
 * <ul>
 * <li><code>appToken</code>:应用授权Token
 * <li><code>authToken</code>:用户登录Token
 * <li><code>authUserId</code>:用户id
 * </ul>
 * <p>
 * 参数列表
 * <ul>
 * <li><code>file</code>:文件
 * <li><code>base64File</code>:文件的base64编码字符串,文件为空时生效
 * <li><code>originalFilename</code>:文件原名,文件以base64方式上传时需要
 * <li><code>privatefile</code>:是否私有文件
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>result</code>:上传结果,【true,false】
 * <li><code>error</code>:错误原因编码
 * <li><code>fileName</code>:文件路径
 * <li><code>fileType</code>:文件类型
 * <li><code>fileSize</code>:文件大小
 * </ul>
 * 使用示例
 * <p>
 * 
 * <pre>
&lt;script&gt;
$('input[type=file]').change(function() {
    var formData = new FormData();
    var file = this.files[0];
    formData.append('file', file);
    $.ajax({url:'${site.dynamicPath}api/upload',type:'post',headers: {'appToken': 'your app token','':'','':''},data: formData,, success:function(data){
        console.log(result+","+error+","+fileName);
    });
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class UploadDirective extends AbstractAppDirective {

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, TemplateException {
        // in ApiController upload function
    }

    @Override
    public boolean needUserToken() {
        return true;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }
}