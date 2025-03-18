package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getSleep 睡眠一段时间
 * <p lang="zh">参数列表
 * <p lang="en">parameter list
 * <p lang="ja">パラメータリスト
 * <ol>
 * <li><code>number</code>:睡眠时间0-60秒
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>boolean</code>:是否睡眠
 * </ul>
 * <p lang="zh">使用示例
 * <p lang="en">usage example
 * <p lang="ja">使用例
 * <p>
 * ${getSleep(5)}
 * <p>
 *
 */
@Component
public class GetSleepMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        Integer time = getInteger(0, arguments);
        if (CommonUtils.notEmpty(time) && time <= 60) {
            try {
                Thread.sleep(time * 1000);
            } catch (InterruptedException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Override
    public boolean httpEnabled() {
        return false;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }
}
