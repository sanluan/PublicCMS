package com.publiccms.views.method.tools;

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
 * <li>日期类型【yesterday:昨天,tomorrow:明天,thisSunday:本周日,thisMonday:本周一,lastMonday:上周一,lastSunday:上周日,lastWeek:上一周,lastMonth:上个月,lastSeason:上季度,lastHalfYear:半年前,lastYear:去年,nextMonday:下周一,nextSunday:下周日,nextWeek:下一周,nextMonth:下个月,nextSeason:下季度,nextHalfYear:半年后,nextYear:明年】,默认当前时间
 * <li>日期,【2020-01-01 23:59:59】,【2020-01-01】,为空则取当前日期
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>date</code>:日期
 * </ul>
 * 使用示例
 * <p>
 * ${getDate('thisSunday','2020-01-01')}
 * <p>
 *
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getDate?parameters=thisSunday&amp;parameters=2020-01-01', function(data){
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
            date = getDate(1, arguments);
        } else {
            date = CommonUtils.getDate();
        }
        if (!arguments.isEmpty()) {
            String type = getString(0, arguments);
            switch (type) {
            case "yesterday":
                date = DateUtils.addDays(date, -1);
                break;
            case "tomorrow":
                date = DateUtils.addDays(date, 1);
                break;
            case "thisSunday":
                date = getSunday(date);
                break;
            case "thisMonday":
                date = getMonday(date);
                break;
            case "lastMonday":
                date = getMonday(DateUtils.addWeeks(date, -1));
                break;
            case "lastSunday":
                date = getSunday(DateUtils.addWeeks(date, -1));
                break;
            case "lastWeek":
                date = DateUtils.addWeeks(date, -1);
                break;
            case "lastMonth":
                date = DateUtils.addMonths(date, -1);
                break;
            case "lastSeason":
                date = DateUtils.addMonths(date, -3);
                break;
            case "lastHalfYear":
                date = DateUtils.addMonths(date, -6);
                break;
            case "lastYear":
                date = DateUtils.addYears(date, -1);
                break;
            case "nextMonday":
                date = getMonday(DateUtils.addWeeks(date, 1));
                break;
            case "nextSunday":
                date = getSunday(DateUtils.addWeeks(date, 1));
                break;
            case "nextWeek":
                date = DateUtils.addWeeks(date, 1);
                break;
            case "nextMonth":
                date = DateUtils.addMonths(date, 1);
                break;
            case "nextSeason":
                date = DateUtils.addMonths(date, 3);
                break;
            case "nextHalfYear":
                date = DateUtils.addMonths(date, 6);
                break;
            case "nextYear":
                date = DateUtils.addYears(date, 1);
                break;
            default:
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

    static Date getMonday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }

    static Date getSunday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calendar.getTime();
    }
}
