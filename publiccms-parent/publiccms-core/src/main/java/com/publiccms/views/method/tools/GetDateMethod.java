package com.publiccms.views.method.tools;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getDate 获取特定日期
 * <p>
 * 参数列表
 * <ol>
 * <li>日期类型【thisSunday:本周日,thisMonday:本周一,lastMonday:上周一,lastSunday:上周日,nextMonday:下周一,nextSunday:下周日】,默认当前时间
 * <li>日期,【2020-01-01 23:59:59】,【2020-01-01】,为空则取当前日期
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>date</code> 日期
 * </ul>
 * 使用示例
 * <p>
 * ${getDate('thisSunday','2020-01-01')}
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('//cms.publiccms.com/api/method/getDate?parameters=thisSunday&amp;parameters=2020-01-01', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetDateMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        Date date;
        if (arguments.size() >= 2) {
            try {
                date = getDate(1, arguments);
            } catch (ParseException e) {
                date = CommonUtils.getDate();
            }
        } else {
            date = CommonUtils.getDate();
        }
        if (arguments.size() >= 1) {
            String type = getString(0, arguments);
            switch (type) {
            case "thisSunday":
                date = getThisSunday(date);
                break;
            case "thisMonday":
                date = getThisMonday(date);
                break;
            case "lastMonday":
                date = getLastMonday(date);
                break;
            case "lastSunday":
                date = getLastSunday(date);
                break;
            case "nextMonday":
                date = getLastMonday(date);
                break;
            case "nextSunday":
                date = getLastSunday(date);
                break;
            }
        }
        return date;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }

    @Override
    public int minParametersNumber() {
        return 0;
    }

    static Date getThisMonday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.addDays(date, -1));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }

    static Date getThisSunday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.addDays(date, 6));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calendar.getTime();
    }

    static Date getLastMonday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.addDays(date, -8));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }

    static Date getLastSunday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.addDays(date, -1));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calendar.getTime();
    }

    static Date getNextMonday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.addDays(date, 7));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }

    static Date getNextSunday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.addDays(date, 13));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calendar.getTime();
    }
}
