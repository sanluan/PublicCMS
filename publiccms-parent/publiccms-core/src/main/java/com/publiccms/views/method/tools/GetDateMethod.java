package com.publiccms.views.method.tools;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;

import freemarker.template.TemplateModelException;

/**
 *
 * GetDateMethod
 * 
 */
@Component
public class GetDateMethod extends BaseMethod {

    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
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

    Date getThisMonday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.addDays(date, -1));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }

    Date getThisSunday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.addDays(date, 6));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calendar.getTime();
    }

    Date getLastMonday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.addDays(date, -8));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }

    Date getLastSunday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.addDays(date, -1));
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calendar.getTime();
    }

}
