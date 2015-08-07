package com.publiccms.views.method;

import static org.apache.commons.lang3.time.DateUtils.addDays;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sanluan.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

@Component
public class GetDateMethod extends BaseMethod {

	/* (non-Javadoc)
	 * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
		Date date;
		if (arguments.size() >= 2) {
			date = getDate(1, arguments);
		} else {
			date = new Date();
		}
		if (arguments.size() >= 1) {
			String type = getString(0, arguments);
			if ("thisSunday".equalsIgnoreCase(type))
				date = getThisSunday(date);
			else if ("thisMonday".equalsIgnoreCase(type))
				date = getThisMonday(date);
			else if ("lastMonday".equalsIgnoreCase(type))
				date = getLastMonday(date);
			else if ("lastSunday".equalsIgnoreCase(type))
				date = getLastSunday(date);
		}
		return date;
	}

	Date getThisMonday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(addDays(date, -1));
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return calendar.getTime();
	}

	Date getThisSunday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(addDays(date, 6));
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return calendar.getTime();
	}

	Date getLastMonday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(addDays(date, -8));
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return calendar.getTime();
	}

	Date getLastSunday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(addDays(date, -1));
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return calendar.getTime();
	}

}
